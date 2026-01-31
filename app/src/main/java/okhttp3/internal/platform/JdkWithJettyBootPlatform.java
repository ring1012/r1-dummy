package okhttp3.internal.platform;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import javax.net.ssl.SSLSocket;
import okhttp3.Protocol;
import okhttp3.internal.Util;

/* loaded from: classes.dex */
class JdkWithJettyBootPlatform extends Platform {
    private final Class<?> clientProviderClass;
    private final Method getMethod;
    private final Method putMethod;
    private final Method removeMethod;
    private final Class<?> serverProviderClass;

    JdkWithJettyBootPlatform(Method putMethod, Method getMethod, Method removeMethod, Class<?> clientProviderClass, Class<?> serverProviderClass) {
        this.putMethod = putMethod;
        this.getMethod = getMethod;
        this.removeMethod = removeMethod;
        this.clientProviderClass = clientProviderClass;
        this.serverProviderClass = serverProviderClass;
    }

    @Override // okhttp3.internal.platform.Platform
    public void configureTlsExtensions(SSLSocket sslSocket, String hostname, List<Protocol> protocols) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<String> names = alpnProtocolNames(protocols);
        try {
            Object provider = Proxy.newProxyInstance(Platform.class.getClassLoader(), new Class[]{this.clientProviderClass, this.serverProviderClass}, new JettyNegoProvider(names));
            this.putMethod.invoke(null, sslSocket, provider);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public void afterHandshake(SSLSocket sslSocket) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.removeMethod.invoke(null, sslSocket);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AssertionError();
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public String getSelectedProtocol(SSLSocket socket) {
        try {
            JettyNegoProvider provider = (JettyNegoProvider) Proxy.getInvocationHandler(this.getMethod.invoke(null, socket));
            if (!provider.unsupported && provider.selected == null) {
                Platform.get().log(4, "ALPN callback dropped: HTTP/2 is disabled. Is alpn-boot on the boot class path?", null);
                return null;
            }
            if (provider.unsupported) {
                return null;
            }
            return provider.selected;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AssertionError();
        }
    }

    public static Platform buildIfSupported() {
        try {
            Class<?> negoClass = Class.forName("org.eclipse.jetty.alpn.ALPN");
            Class<?> providerClass = Class.forName("org.eclipse.jetty.alpn.ALPN$Provider");
            Class<?> clientProviderClass = Class.forName("org.eclipse.jetty.alpn.ALPN$ClientProvider");
            Class<?> serverProviderClass = Class.forName("org.eclipse.jetty.alpn.ALPN$ServerProvider");
            Method putMethod = negoClass.getMethod("put", SSLSocket.class, providerClass);
            Method getMethod = negoClass.getMethod("get", SSLSocket.class);
            Method removeMethod = negoClass.getMethod("remove", SSLSocket.class);
            return new JdkWithJettyBootPlatform(putMethod, getMethod, removeMethod, clientProviderClass, serverProviderClass);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            return null;
        }
    }

    private static class JettyNegoProvider implements InvocationHandler {
        private final List<String> protocols;
        String selected;
        boolean unsupported;

        JettyNegoProvider(List<String> protocols) {
            this.protocols = protocols;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            Class<?> returnType = method.getReturnType();
            if (args == null) {
                args = Util.EMPTY_STRING_ARRAY;
            }
            if (methodName.equals("supports") && Boolean.TYPE == returnType) {
                return true;
            }
            if (methodName.equals("unsupported") && Void.TYPE == returnType) {
                this.unsupported = true;
                return null;
            }
            if (methodName.equals("protocols") && args.length == 0) {
                return this.protocols;
            }
            if ((methodName.equals("selectProtocol") || methodName.equals("select")) && String.class == returnType && args.length == 1 && (args[0] instanceof List)) {
                List<String> peerProtocols = (List) args[0];
                int size = peerProtocols.size();
                for (int i = 0; i < size; i++) {
                    if (this.protocols.contains(peerProtocols.get(i))) {
                        String str = peerProtocols.get(i);
                        this.selected = str;
                        return str;
                    }
                }
                String str2 = this.protocols.get(0);
                this.selected = str2;
                return str2;
            }
            if ((methodName.equals("protocolSelected") || methodName.equals("selected")) && args.length == 1) {
                this.selected = (String) args[0];
                return null;
            }
            return method.invoke(this, args);
        }
    }
}
