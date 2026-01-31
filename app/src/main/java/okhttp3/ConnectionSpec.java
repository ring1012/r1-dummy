package okhttp3;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.ssl.SSLSocket;
import okhttp3.internal.Util;

/* loaded from: classes.dex */
public final class ConnectionSpec {

    @Nullable
    final String[] cipherSuites;
    final boolean supportsTlsExtensions;
    final boolean tls;

    @Nullable
    final String[] tlsVersions;
    private static final CipherSuite[] APPROVED_CIPHER_SUITES = {CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384, CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA};
    public static final ConnectionSpec MODERN_TLS = new Builder(true).cipherSuites(APPROVED_CIPHER_SUITES).tlsVersions(TlsVersion.TLS_1_3, TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0).supportsTlsExtensions(true).build();
    public static final ConnectionSpec COMPATIBLE_TLS = new Builder(MODERN_TLS).tlsVersions(TlsVersion.TLS_1_0).supportsTlsExtensions(true).build();
    public static final ConnectionSpec CLEARTEXT = new Builder(false).build();

    ConnectionSpec(Builder builder) {
        this.tls = builder.tls;
        this.cipherSuites = builder.cipherSuites;
        this.tlsVersions = builder.tlsVersions;
        this.supportsTlsExtensions = builder.supportsTlsExtensions;
    }

    public boolean isTls() {
        return this.tls;
    }

    @Nullable
    public List<CipherSuite> cipherSuites() {
        if (this.cipherSuites != null) {
            return CipherSuite.forJavaNames(this.cipherSuites);
        }
        return null;
    }

    @Nullable
    public List<TlsVersion> tlsVersions() {
        if (this.tlsVersions != null) {
            return TlsVersion.forJavaNames(this.tlsVersions);
        }
        return null;
    }

    public boolean supportsTlsExtensions() {
        return this.supportsTlsExtensions;
    }

    void apply(SSLSocket sslSocket, boolean isFallback) {
        ConnectionSpec specToApply = supportedSpec(sslSocket, isFallback);
        if (specToApply.tlsVersions != null) {
            sslSocket.setEnabledProtocols(specToApply.tlsVersions);
        }
        if (specToApply.cipherSuites != null) {
            sslSocket.setEnabledCipherSuites(specToApply.cipherSuites);
        }
    }

    private ConnectionSpec supportedSpec(SSLSocket sslSocket, boolean isFallback) {
        String[] cipherSuitesIntersection;
        String[] tlsVersionsIntersection;
        if (this.cipherSuites != null) {
            cipherSuitesIntersection = Util.intersect(CipherSuite.ORDER_BY_NAME, sslSocket.getEnabledCipherSuites(), this.cipherSuites);
        } else {
            cipherSuitesIntersection = sslSocket.getEnabledCipherSuites();
        }
        if (this.tlsVersions != null) {
            tlsVersionsIntersection = Util.intersect(Util.NATURAL_ORDER, sslSocket.getEnabledProtocols(), this.tlsVersions);
        } else {
            tlsVersionsIntersection = sslSocket.getEnabledProtocols();
        }
        String[] supportedCipherSuites = sslSocket.getSupportedCipherSuites();
        int indexOfFallbackScsv = Util.indexOf(CipherSuite.ORDER_BY_NAME, supportedCipherSuites, "TLS_FALLBACK_SCSV");
        if (isFallback && indexOfFallbackScsv != -1) {
            cipherSuitesIntersection = Util.concat(cipherSuitesIntersection, supportedCipherSuites[indexOfFallbackScsv]);
        }
        return new Builder(this).cipherSuites(cipherSuitesIntersection).tlsVersions(tlsVersionsIntersection).build();
    }

    public boolean isCompatible(SSLSocket socket) {
        if (!this.tls) {
            return false;
        }
        if (this.tlsVersions == null || Util.nonEmptyIntersection(Util.NATURAL_ORDER, this.tlsVersions, socket.getEnabledProtocols())) {
            return this.cipherSuites == null || Util.nonEmptyIntersection(CipherSuite.ORDER_BY_NAME, this.cipherSuites, socket.getEnabledCipherSuites());
        }
        return false;
    }

    public boolean equals(@Nullable Object other) {
        if (!(other instanceof ConnectionSpec)) {
            return false;
        }
        if (other == this) {
            return true;
        }
        ConnectionSpec that = (ConnectionSpec) other;
        if (this.tls == that.tls) {
            return !this.tls || (Arrays.equals(this.cipherSuites, that.cipherSuites) && Arrays.equals(this.tlsVersions, that.tlsVersions) && this.supportsTlsExtensions == that.supportsTlsExtensions);
        }
        return false;
    }

    public int hashCode() {
        if (!this.tls) {
            return 17;
        }
        int result = Arrays.hashCode(this.cipherSuites) + 527;
        return (((result * 31) + Arrays.hashCode(this.tlsVersions)) * 31) + (this.supportsTlsExtensions ? 0 : 1);
    }

    public String toString() {
        if (!this.tls) {
            return "ConnectionSpec()";
        }
        String cipherSuitesString = this.cipherSuites != null ? cipherSuites().toString() : "[all enabled]";
        String tlsVersionsString = this.tlsVersions != null ? tlsVersions().toString() : "[all enabled]";
        return "ConnectionSpec(cipherSuites=" + cipherSuitesString + ", tlsVersions=" + tlsVersionsString + ", supportsTlsExtensions=" + this.supportsTlsExtensions + ")";
    }

    public static final class Builder {

        @Nullable
        String[] cipherSuites;
        boolean supportsTlsExtensions;
        boolean tls;

        @Nullable
        String[] tlsVersions;

        Builder(boolean tls) {
            this.tls = tls;
        }

        public Builder(ConnectionSpec connectionSpec) {
            this.tls = connectionSpec.tls;
            this.cipherSuites = connectionSpec.cipherSuites;
            this.tlsVersions = connectionSpec.tlsVersions;
            this.supportsTlsExtensions = connectionSpec.supportsTlsExtensions;
        }

        public Builder allEnabledCipherSuites() {
            if (!this.tls) {
                throw new IllegalStateException("no cipher suites for cleartext connections");
            }
            this.cipherSuites = null;
            return this;
        }

        public Builder cipherSuites(CipherSuite... cipherSuites) {
            if (!this.tls) {
                throw new IllegalStateException("no cipher suites for cleartext connections");
            }
            String[] strings = new String[cipherSuites.length];
            for (int i = 0; i < cipherSuites.length; i++) {
                strings[i] = cipherSuites[i].javaName;
            }
            return cipherSuites(strings);
        }

        public Builder cipherSuites(String... cipherSuites) {
            if (!this.tls) {
                throw new IllegalStateException("no cipher suites for cleartext connections");
            }
            if (cipherSuites.length == 0) {
                throw new IllegalArgumentException("At least one cipher suite is required");
            }
            this.cipherSuites = (String[]) cipherSuites.clone();
            return this;
        }

        public Builder allEnabledTlsVersions() {
            if (!this.tls) {
                throw new IllegalStateException("no TLS versions for cleartext connections");
            }
            this.tlsVersions = null;
            return this;
        }

        public Builder tlsVersions(TlsVersion... tlsVersions) {
            if (!this.tls) {
                throw new IllegalStateException("no TLS versions for cleartext connections");
            }
            String[] strings = new String[tlsVersions.length];
            for (int i = 0; i < tlsVersions.length; i++) {
                strings[i] = tlsVersions[i].javaName;
            }
            return tlsVersions(strings);
        }

        public Builder tlsVersions(String... tlsVersions) {
            if (!this.tls) {
                throw new IllegalStateException("no TLS versions for cleartext connections");
            }
            if (tlsVersions.length == 0) {
                throw new IllegalArgumentException("At least one TLS version is required");
            }
            this.tlsVersions = (String[]) tlsVersions.clone();
            return this;
        }

        public Builder supportsTlsExtensions(boolean supportsTlsExtensions) {
            if (!this.tls) {
                throw new IllegalStateException("no TLS extensions for cleartext connections");
            }
            this.supportsTlsExtensions = supportsTlsExtensions;
            return this;
        }

        public ConnectionSpec build() {
            return new ConnectionSpec(this);
        }
    }
}
