package okhttp3.internal.tls;

import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.auth.x500.X500Principal;
import okhttp3.internal.Util;
import org.apache.commons.io.FilenameUtils;

/* loaded from: classes.dex */
public final class OkHostnameVerifier implements HostnameVerifier {
    private static final int ALT_DNS_NAME = 2;
    private static final int ALT_IPA_NAME = 7;
    public static final OkHostnameVerifier INSTANCE = new OkHostnameVerifier();

    private OkHostnameVerifier() {
    }

    @Override // javax.net.ssl.HostnameVerifier
    public boolean verify(String host, SSLSession session) throws SSLPeerUnverifiedException {
        try {
            Certificate[] certificates = session.getPeerCertificates();
            return verify(host, (X509Certificate) certificates[0]);
        } catch (SSLException e) {
            return false;
        }
    }

    public boolean verify(String host, X509Certificate certificate) {
        if (Util.verifyAsIpAddress(host)) {
            return verifyIpAddress(host, certificate);
        }
        return verifyHostname(host, certificate);
    }

    private boolean verifyIpAddress(String ipAddress, X509Certificate certificate) throws CertificateParsingException {
        List<String> altNames = getSubjectAltNames(certificate, 7);
        int size = altNames.size();
        for (int i = 0; i < size; i++) {
            if (ipAddress.equalsIgnoreCase(altNames.get(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean verifyHostname(String hostname, X509Certificate certificate) throws CertificateParsingException {
        String hostname2 = hostname.toLowerCase(Locale.US);
        boolean hasDns = false;
        List<String> altNames = getSubjectAltNames(certificate, 2);
        int size = altNames.size();
        for (int i = 0; i < size; i++) {
            hasDns = true;
            if (verifyHostname(hostname2, altNames.get(i))) {
                return true;
            }
        }
        if (!hasDns) {
            X500Principal principal = certificate.getSubjectX500Principal();
            String cn2 = new DistinguishedNameParser(principal).findMostSpecific("cn");
            if (cn2 != null) {
                return verifyHostname(hostname2, cn2);
            }
        }
        return false;
    }

    public static List<String> allSubjectAltNames(X509Certificate certificate) throws CertificateParsingException {
        List<String> altIpaNames = getSubjectAltNames(certificate, 7);
        List<String> altDnsNames = getSubjectAltNames(certificate, 2);
        List<String> result = new ArrayList<>(altIpaNames.size() + altDnsNames.size());
        result.addAll(altIpaNames);
        result.addAll(altDnsNames);
        return result;
    }

    private static List<String> getSubjectAltNames(X509Certificate certificate, int type) throws CertificateParsingException {
        Integer altNameType;
        String altName;
        List<String> result = new ArrayList<>();
        try {
            Collection<?> subjectAltNames = certificate.getSubjectAlternativeNames();
            if (subjectAltNames == null) {
                return Collections.emptyList();
            }
            for (Object subjectAltName : subjectAltNames) {
                List<?> entry = (List) subjectAltName;
                if (entry != null && entry.size() >= 2 && (altNameType = (Integer) entry.get(0)) != null && altNameType.intValue() == type && (altName = (String) entry.get(1)) != null) {
                    result.add(altName);
                }
            }
            return result;
        } catch (CertificateParsingException e) {
            return Collections.emptyList();
        }
    }

    public boolean verifyHostname(String hostname, String pattern) {
        if (hostname == null || hostname.length() == 0 || hostname.startsWith(".") || hostname.endsWith("..") || pattern == null || pattern.length() == 0 || pattern.startsWith(".") || pattern.endsWith("..")) {
            return false;
        }
        if (!hostname.endsWith(".")) {
            hostname = hostname + FilenameUtils.EXTENSION_SEPARATOR;
        }
        if (!pattern.endsWith(".")) {
            pattern = pattern + FilenameUtils.EXTENSION_SEPARATOR;
        }
        String pattern2 = pattern.toLowerCase(Locale.US);
        if (!pattern2.contains("*")) {
            return hostname.equals(pattern2);
        }
        if (!pattern2.startsWith("*.") || pattern2.indexOf(42, 1) != -1 || hostname.length() < pattern2.length() || "*.".equals(pattern2)) {
            return false;
        }
        String suffix = pattern2.substring(1);
        if (!hostname.endsWith(suffix)) {
            return false;
        }
        int suffixStartIndexInHostname = hostname.length() - suffix.length();
        return suffixStartIndexInHostname <= 0 || hostname.lastIndexOf(46, suffixStartIndexInHostname + (-1)) == -1;
    }
}
