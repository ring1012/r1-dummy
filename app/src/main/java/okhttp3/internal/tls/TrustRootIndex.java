package okhttp3.internal.tls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

/* loaded from: classes.dex */
public abstract class TrustRootIndex {
    public abstract X509Certificate findByIssuerAndSignature(X509Certificate x509Certificate);

    public static TrustRootIndex get(X509TrustManager trustManager) throws NoSuchMethodException, SecurityException {
        try {
            Method method = trustManager.getClass().getDeclaredMethod("findTrustAnchorByIssuerAndSignature", X509Certificate.class);
            method.setAccessible(true);
            return new AndroidTrustRootIndex(trustManager, method);
        } catch (NoSuchMethodException e) {
            return get(trustManager.getAcceptedIssuers());
        }
    }

    public static TrustRootIndex get(X509Certificate... caCerts) {
        return new BasicTrustRootIndex(caCerts);
    }

    static final class AndroidTrustRootIndex extends TrustRootIndex {
        private final Method findByIssuerAndSignatureMethod;
        private final X509TrustManager trustManager;

        AndroidTrustRootIndex(X509TrustManager trustManager, Method findByIssuerAndSignatureMethod) {
            this.findByIssuerAndSignatureMethod = findByIssuerAndSignatureMethod;
            this.trustManager = trustManager;
        }

        @Override // okhttp3.internal.tls.TrustRootIndex
        public X509Certificate findByIssuerAndSignature(X509Certificate cert) {
            try {
                TrustAnchor trustAnchor = (TrustAnchor) this.findByIssuerAndSignatureMethod.invoke(this.trustManager, cert);
                if (trustAnchor != null) {
                    return trustAnchor.getTrustedCert();
                }
                return null;
            } catch (IllegalAccessException e) {
                throw new AssertionError();
            } catch (InvocationTargetException e2) {
                return null;
            }
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof AndroidTrustRootIndex)) {
                return false;
            }
            AndroidTrustRootIndex that = (AndroidTrustRootIndex) obj;
            return this.trustManager.equals(that.trustManager) && this.findByIssuerAndSignatureMethod.equals(that.findByIssuerAndSignatureMethod);
        }

        public int hashCode() {
            return this.trustManager.hashCode() + (this.findByIssuerAndSignatureMethod.hashCode() * 31);
        }
    }

    static final class BasicTrustRootIndex extends TrustRootIndex {
        private final Map<X500Principal, Set<X509Certificate>> subjectToCaCerts = new LinkedHashMap();

        BasicTrustRootIndex(X509Certificate... caCerts) {
            for (X509Certificate caCert : caCerts) {
                X500Principal subject = caCert.getSubjectX500Principal();
                Set<X509Certificate> subjectCaCerts = this.subjectToCaCerts.get(subject);
                if (subjectCaCerts == null) {
                    subjectCaCerts = new LinkedHashSet<>(1);
                    this.subjectToCaCerts.put(subject, subjectCaCerts);
                }
                subjectCaCerts.add(caCert);
            }
        }

        @Override // okhttp3.internal.tls.TrustRootIndex
        public X509Certificate findByIssuerAndSignature(X509Certificate cert) {
            X500Principal issuer = cert.getIssuerX500Principal();
            Set<X509Certificate> subjectCaCerts = this.subjectToCaCerts.get(issuer);
            if (subjectCaCerts == null) {
                return null;
            }
            for (X509Certificate caCert : subjectCaCerts) {
                PublicKey publicKey = caCert.getPublicKey();
                try {
                    cert.verify(publicKey);
                    return caCert;
                } catch (Exception e) {
                }
            }
            return null;
        }

        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            return (other instanceof BasicTrustRootIndex) && ((BasicTrustRootIndex) other).subjectToCaCerts.equals(this.subjectToCaCerts);
        }

        public int hashCode() {
            return this.subjectToCaCerts.hashCode();
        }
    }
}
