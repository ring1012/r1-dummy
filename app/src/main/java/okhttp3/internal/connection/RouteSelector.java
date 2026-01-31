package okhttp3.internal.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import okhttp3.Address;
import okhttp3.HttpUrl;
import okhttp3.Route;
import okhttp3.internal.Util;

/* loaded from: classes.dex */
public final class RouteSelector {
    private final Address address;
    private InetSocketAddress lastInetSocketAddress;
    private Proxy lastProxy;
    private int nextInetSocketAddressIndex;
    private int nextProxyIndex;
    private final RouteDatabase routeDatabase;
    private List<Proxy> proxies = Collections.emptyList();
    private List<InetSocketAddress> inetSocketAddresses = Collections.emptyList();
    private final List<Route> postponedRoutes = new ArrayList();

    public RouteSelector(Address address, RouteDatabase routeDatabase) {
        this.address = address;
        this.routeDatabase = routeDatabase;
        resetNextProxy(address.url(), address.proxy());
    }

    public boolean hasNext() {
        return hasNextInetSocketAddress() || hasNextProxy() || hasNextPostponed();
    }

    public Route next() throws IOException {
        if (!hasNextInetSocketAddress()) {
            if (!hasNextProxy()) {
                if (!hasNextPostponed()) {
                    throw new NoSuchElementException();
                }
                return nextPostponed();
            }
            this.lastProxy = nextProxy();
        }
        this.lastInetSocketAddress = nextInetSocketAddress();
        Route route = new Route(this.address, this.lastProxy, this.lastInetSocketAddress);
        if (this.routeDatabase.shouldPostpone(route)) {
            this.postponedRoutes.add(route);
            return next();
        }
        return route;
    }

    public void connectFailed(Route failedRoute, IOException failure) {
        if (failedRoute.proxy().type() != Proxy.Type.DIRECT && this.address.proxySelector() != null) {
            this.address.proxySelector().connectFailed(this.address.url().uri(), failedRoute.proxy().address(), failure);
        }
        this.routeDatabase.failed(failedRoute);
    }

    private void resetNextProxy(HttpUrl url, Proxy proxy) {
        if (proxy != null) {
            this.proxies = Collections.singletonList(proxy);
        } else {
            List<Proxy> proxiesOrNull = this.address.proxySelector().select(url.uri());
            this.proxies = (proxiesOrNull == null || proxiesOrNull.isEmpty()) ? Util.immutableList(Proxy.NO_PROXY) : Util.immutableList(proxiesOrNull);
        }
        this.nextProxyIndex = 0;
    }

    private boolean hasNextProxy() {
        return this.nextProxyIndex < this.proxies.size();
    }

    private Proxy nextProxy() throws IOException {
        if (!hasNextProxy()) {
            throw new SocketException("No route to " + this.address.url().host() + "; exhausted proxy configurations: " + this.proxies);
        }
        List<Proxy> list = this.proxies;
        int i = this.nextProxyIndex;
        this.nextProxyIndex = i + 1;
        Proxy result = list.get(i);
        resetNextInetSocketAddress(result);
        return result;
    }

    private void resetNextInetSocketAddress(Proxy proxy) throws IOException {
        String socketHost;
        int socketPort;
        this.inetSocketAddresses = new ArrayList();
        if (proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.SOCKS) {
            socketHost = this.address.url().host();
            socketPort = this.address.url().port();
        } else {
            SocketAddress proxyAddress = proxy.address();
            if (!(proxyAddress instanceof InetSocketAddress)) {
                throw new IllegalArgumentException("Proxy.address() is not an InetSocketAddress: " + proxyAddress.getClass());
            }
            InetSocketAddress proxySocketAddress = (InetSocketAddress) proxyAddress;
            socketHost = getHostString(proxySocketAddress);
            socketPort = proxySocketAddress.getPort();
        }
        if (socketPort < 1 || socketPort > 65535) {
            throw new SocketException("No route to " + socketHost + ":" + socketPort + "; port is out of range");
        }
        if (proxy.type() == Proxy.Type.SOCKS) {
            this.inetSocketAddresses.add(InetSocketAddress.createUnresolved(socketHost, socketPort));
        } else {
            List<InetAddress> addresses = this.address.dns().lookup(socketHost);
            if (addresses.isEmpty()) {
                throw new UnknownHostException(this.address.dns() + " returned no addresses for " + socketHost);
            }
            int size = addresses.size();
            for (int i = 0; i < size; i++) {
                InetAddress inetAddress = addresses.get(i);
                this.inetSocketAddresses.add(new InetSocketAddress(inetAddress, socketPort));
            }
        }
        this.nextInetSocketAddressIndex = 0;
    }

    static String getHostString(InetSocketAddress socketAddress) {
        InetAddress address = socketAddress.getAddress();
        return address == null ? socketAddress.getHostName() : address.getHostAddress();
    }

    private boolean hasNextInetSocketAddress() {
        return this.nextInetSocketAddressIndex < this.inetSocketAddresses.size();
    }

    private InetSocketAddress nextInetSocketAddress() throws IOException {
        if (!hasNextInetSocketAddress()) {
            throw new SocketException("No route to " + this.address.url().host() + "; exhausted inet socket addresses: " + this.inetSocketAddresses);
        }
        List<InetSocketAddress> list = this.inetSocketAddresses;
        int i = this.nextInetSocketAddressIndex;
        this.nextInetSocketAddressIndex = i + 1;
        return list.get(i);
    }

    private boolean hasNextPostponed() {
        return !this.postponedRoutes.isEmpty();
    }

    private Route nextPostponed() {
        return this.postponedRoutes.remove(0);
    }
}
