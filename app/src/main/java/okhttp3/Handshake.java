package okhttp3;

import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import okhttp3.internal.Util;

/* loaded from: classes.dex */
public final class Handshake {
    private final CipherSuite cipherSuite;
    private final List<Certificate> localCertificates;
    private final List<Certificate> peerCertificates;
    private final TlsVersion tlsVersion;

    private Handshake(TlsVersion tlsVersion, CipherSuite cipherSuite, List<Certificate> peerCertificates, List<Certificate> localCertificates) {
        this.tlsVersion = tlsVersion;
        this.cipherSuite = cipherSuite;
        this.peerCertificates = peerCertificates;
        this.localCertificates = localCertificates;
    }

    public static Handshake get(SSLSession session) throws SSLPeerUnverifiedException {
        Certificate[] peerCertificates;
        List<Certificate> peerCertificatesList;
        List<Certificate> localCertificatesList;
        String cipherSuiteString = session.getCipherSuite();
        if (cipherSuiteString == null) {
            throw new IllegalStateException("cipherSuite == null");
        }
        CipherSuite cipherSuite = CipherSuite.forJavaName(cipherSuiteString);
        String tlsVersionString = session.getProtocol();
        if (tlsVersionString == null) {
            throw new IllegalStateException("tlsVersion == null");
        }
        TlsVersion tlsVersion = TlsVersion.forJavaName(tlsVersionString);
        try {
            peerCertificates = session.getPeerCertificates();
        } catch (SSLPeerUnverifiedException e) {
            peerCertificates = null;
        }
        if (peerCertificates != null) {
            peerCertificatesList = Util.immutableList(peerCertificates);
        } else {
            peerCertificatesList = Collections.emptyList();
        }
        Certificate[] localCertificates = session.getLocalCertificates();
        if (localCertificates != null) {
            localCertificatesList = Util.immutableList(localCertificates);
        } else {
            localCertificatesList = Collections.emptyList();
        }
        return new Handshake(tlsVersion, cipherSuite, peerCertificatesList, localCertificatesList);
    }

    public static Handshake get(TlsVersion tlsVersion, CipherSuite cipherSuite, List<Certificate> peerCertificates, List<Certificate> localCertificates) {
        if (tlsVersion == null) {
            throw new NullPointerException("tlsVersion == null");
        }
        if (cipherSuite == null) {
            throw new NullPointerException("cipherSuite == null");
        }
        return new Handshake(tlsVersion, cipherSuite, Util.immutableList(peerCertificates), Util.immutableList(localCertificates));
    }

    public TlsVersion tlsVersion() {
        return this.tlsVersion;
    }

    public CipherSuite cipherSuite() {
        return this.cipherSuite;
    }

    public List<Certificate> peerCertificates() {
        return this.peerCertificates;
    }

    @Nullable
    public Principal peerPrincipal() {
        if (!this.peerCertificates.isEmpty()) {
            return ((X509Certificate) this.peerCertificates.get(0)).getSubjectX500Principal();
        }
        return null;
    }

    public List<Certificate> localCertificates() {
        return this.localCertificates;
    }

    @Nullable
    public Principal localPrincipal() {
        if (!this.localCertificates.isEmpty()) {
            return ((X509Certificate) this.localCertificates.get(0)).getSubjectX500Principal();
        }
        return null;
    }

    public boolean equals(@Nullable Object other) {
        if (!(other instanceof Handshake)) {
            return false;
        }
        Handshake that = (Handshake) other;
        return this.tlsVersion.equals(that.tlsVersion) && this.cipherSuite.equals(that.cipherSuite) && this.peerCertificates.equals(that.peerCertificates) && this.localCertificates.equals(that.localCertificates);
    }

    public int hashCode() {
        int result = this.tlsVersion.hashCode() + 527;
        return (((((result * 31) + this.cipherSuite.hashCode()) * 31) + this.peerCertificates.hashCode()) * 31) + this.localCertificates.hashCode();
    }
}
