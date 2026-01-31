package okhttp3;

import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.HttpUrl;
import okhttp3.internal.Util;

/* loaded from: classes.dex */
public final class Address {

    @Nullable
    final CertificatePinner certificatePinner;
    final List<ConnectionSpec> connectionSpecs;
    final Dns dns;

    @Nullable
    final HostnameVerifier hostnameVerifier;
    final List<Protocol> protocols;

    @Nullable
    final Proxy proxy;
    final Authenticator proxyAuthenticator;
    final ProxySelector proxySelector;
    final SocketFactory socketFactory;

    @Nullable
    final SSLSocketFactory sslSocketFactory;
    final HttpUrl url;

    public Address(String uriHost, int uriPort, Dns dns, SocketFactory socketFactory, @Nullable SSLSocketFactory sslSocketFactory, @Nullable HostnameVerifier hostnameVerifier, @Nullable CertificatePinner certificatePinner, Authenticator proxyAuthenticator, @Nullable Proxy proxy, List<Protocol> protocols, List<ConnectionSpec> connectionSpecs, ProxySelector proxySelector) {
        this.url = new HttpUrl.Builder().scheme(sslSocketFactory != null ? "https" : "http").host(uriHost).port(uriPort).build();
        if (dns == null) {
            throw new NullPointerException("dns == null");
        }
        this.dns = dns;
        if (socketFactory == null) {
            throw new NullPointerException("socketFactory == null");
        }
        this.socketFactory = socketFactory;
        if (proxyAuthenticator == null) {
            throw new NullPointerException("proxyAuthenticator == null");
        }
        this.proxyAuthenticator = proxyAuthenticator;
        if (protocols == null) {
            throw new NullPointerException("protocols == null");
        }
        this.protocols = Util.immutableList(protocols);
        if (connectionSpecs == null) {
            throw new NullPointerException("connectionSpecs == null");
        }
        this.connectionSpecs = Util.immutableList(connectionSpecs);
        if (proxySelector == null) {
            throw new NullPointerException("proxySelector == null");
        }
        this.proxySelector = proxySelector;
        this.proxy = proxy;
        this.sslSocketFactory = sslSocketFactory;
        this.hostnameVerifier = hostnameVerifier;
        this.certificatePinner = certificatePinner;
    }

    public HttpUrl url() {
        return this.url;
    }

    public Dns dns() {
        return this.dns;
    }

    public SocketFactory socketFactory() {
        return this.socketFactory;
    }

    public Authenticator proxyAuthenticator() {
        return this.proxyAuthenticator;
    }

    public List<Protocol> protocols() {
        return this.protocols;
    }

    public List<ConnectionSpec> connectionSpecs() {
        return this.connectionSpecs;
    }

    public ProxySelector proxySelector() {
        return this.proxySelector;
    }

    @Nullable
    public Proxy proxy() {
        return this.proxy;
    }

    @Nullable
    public SSLSocketFactory sslSocketFactory() {
        return this.sslSocketFactory;
    }

    @Nullable
    public HostnameVerifier hostnameVerifier() {
        return this.hostnameVerifier;
    }

    @Nullable
    public CertificatePinner certificatePinner() {
        return this.certificatePinner;
    }

    public boolean equals(@Nullable Object other) {
        return (other instanceof Address) && this.url.equals(((Address) other).url) && equalsNonHost((Address) other);
    }

    public int hashCode() {
        int result = this.url.hashCode() + 527;
        return (((((((((((((((((result * 31) + this.dns.hashCode()) * 31) + this.proxyAuthenticator.hashCode()) * 31) + this.protocols.hashCode()) * 31) + this.connectionSpecs.hashCode()) * 31) + this.proxySelector.hashCode()) * 31) + (this.proxy != null ? this.proxy.hashCode() : 0)) * 31) + (this.sslSocketFactory != null ? this.sslSocketFactory.hashCode() : 0)) * 31) + (this.hostnameVerifier != null ? this.hostnameVerifier.hashCode() : 0)) * 31) + (this.certificatePinner != null ? this.certificatePinner.hashCode() : 0);
    }

    boolean equalsNonHost(Address that) {
        return this.dns.equals(that.dns) && this.proxyAuthenticator.equals(that.proxyAuthenticator) && this.protocols.equals(that.protocols) && this.connectionSpecs.equals(that.connectionSpecs) && this.proxySelector.equals(that.proxySelector) && Util.equal(this.proxy, that.proxy) && Util.equal(this.sslSocketFactory, that.sslSocketFactory) && Util.equal(this.hostnameVerifier, that.hostnameVerifier) && Util.equal(this.certificatePinner, that.certificatePinner) && url().port() == that.url().port();
    }

    public String toString() {
        StringBuilder result = new StringBuilder().append("Address{").append(this.url.host()).append(":").append(this.url.port());
        if (this.proxy != null) {
            result.append(", proxy=").append(this.proxy);
        } else {
            result.append(", proxySelector=").append(this.proxySelector);
        }
        result.append("}");
        return result.toString();
    }
}
