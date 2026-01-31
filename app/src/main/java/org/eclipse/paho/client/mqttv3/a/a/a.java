package org.eclipse.paho.client.mqttv3.a.a;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.logging.Logger;

/* loaded from: classes.dex */
public class a {
    private static final String C = "{xor}";
    public static final String o = "javax.net.ssl.keyStore";
    public static final String p = "javax.net.ssl.keyStoreType";
    public static final String q = "javax.net.ssl.keyStorePassword";
    public static final String r = "javax.net.ssl.trustStore";
    public static final String s = "javax.net.ssl.trustStoreType";
    public static final String t = "javax.net.ssl.trustStorePassword";
    public static final String u = "ssl.KeyManagerFactory.algorithm";
    public static final String v = "ssl.TrustManagerFactory.algorithm";
    public static final String w = "TLS";
    private static final String x = "org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory";
    private Properties A;
    private Logger D;
    private Hashtable z;

    /* renamed from: a, reason: collision with root package name */
    public static final String f469a = "com.ibm.ssl.protocol";
    public static final String b = "com.ibm.ssl.contextProvider";
    public static final String c = "com.ibm.ssl.keyStore";
    public static final String d = "com.ibm.ssl.keyStorePassword";
    public static final String e = "com.ibm.ssl.keyStoreType";
    public static final String f = "com.ibm.ssl.keyStoreProvider";
    public static final String g = "com.ibm.ssl.keyManager";
    public static final String h = "com.ibm.ssl.trustStore";
    public static final String i = "com.ibm.ssl.trustStorePassword";
    public static final String j = "com.ibm.ssl.trustStoreType";
    public static final String k = "com.ibm.ssl.trustStoreProvider";
    public static final String l = "com.ibm.ssl.trustManager";
    public static final String m = "com.ibm.ssl.enabledCipherSuites";
    public static final String n = "com.ibm.ssl.clientAuthentication";
    private static final String[] y = {f469a, b, c, d, e, f, g, h, i, j, k, l, m, n};
    private static final byte[] B = {-99, -89, -39, -128, 5, -72, -119, -100};

    public a() {
        this.D = null;
        this.z = new Hashtable();
    }

    public a(Logger logger) {
        this();
        this.D = logger;
    }

    private String a(String str, String str2) {
        String property;
        Properties properties = str != null ? (Properties) this.z.get(str) : null;
        if (properties != null) {
            property = properties.getProperty(str2);
            if (property == null) {
            }
            return property;
        }
        property = null;
        Properties properties2 = this.A;
        if (properties2 == null || (property = properties2.getProperty(str2)) != null) {
        }
        return property;
    }

    private String a(String str, String str2, String str3) {
        String strA = a(str, str2);
        return (strA == null && str3 != null) ? System.getProperty(str3) : strA;
    }

    public static String a(String[] strArr) {
        if (strArr == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < strArr.length; i2++) {
            stringBuffer.append(strArr[i2]);
            if (i2 < strArr.length - 1) {
                stringBuffer.append(',');
            }
        }
        return stringBuffer.toString();
    }

    private void a(Properties properties) {
        for (String str : properties.keySet()) {
            if (!t(str)) {
                throw new IllegalArgumentException(new StringBuffer(String.valueOf(str)).append(" is not a valid IBM SSL property key.").toString());
            }
        }
    }

    public static boolean a() throws ClassNotFoundException {
        try {
            Class.forName("javax.net.ssl.SSLServerSocketFactory");
            return true;
        } catch (ClassNotFoundException e2) {
            return false;
        }
    }

    public static byte[] a(char[] cArr) {
        int i2 = 0;
        if (cArr == null) {
            return null;
        }
        byte[] bArr = new byte[cArr.length * 2];
        int i3 = 0;
        while (i2 < cArr.length) {
            int i4 = i3 + 1;
            bArr[i3] = (byte) (cArr[i2] & 255);
            bArr[i4] = (byte) ((cArr[i2] >> '\b') & 255);
            i2++;
            i3 = i4 + 1;
        }
        return bArr;
    }

    public static char[] a(String str) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bArrA = b.a(str.substring(C.length()));
            for (int i2 = 0; i2 < bArrA.length; i2++) {
                bArrA[i2] = (byte) ((bArrA[i2] ^ B[i2 % B.length]) & 255);
            }
            return a(bArrA);
        } catch (Exception e2) {
            return null;
        }
    }

    public static char[] a(byte[] bArr) {
        int i2 = 0;
        if (bArr == null) {
            return null;
        }
        char[] cArr = new char[bArr.length / 2];
        int i3 = 0;
        while (i3 < bArr.length) {
            int i4 = i3 + 1;
            int i5 = bArr[i3] & 255;
            i3 = i4 + 1;
            cArr[i2] = (char) (((bArr[i4] & 255) << 8) + i5);
            i2++;
        }
        return cArr;
    }

    public static String b(char[] cArr) {
        if (cArr == null) {
            return null;
        }
        byte[] bArrA = a(cArr);
        for (int i2 = 0; i2 < bArrA.length; i2++) {
            bArrA[i2] = (byte) ((bArrA[i2] ^ B[i2 % B.length]) & 255);
        }
        return new StringBuffer(C).append(new String(b.a(bArrA))).toString();
    }

    private void b(Properties properties) {
        String property = properties.getProperty(d);
        if (property != null && !property.startsWith(C)) {
            properties.put(d, b(property.toCharArray()));
        }
        String property2 = properties.getProperty(i);
        if (property2 == null || property2.startsWith(C)) {
            return;
        }
        properties.put(i, b(property2.toCharArray()));
    }

    public static String[] b(String str) {
        if (str == null) {
            return null;
        }
        Vector vector = new Vector();
        int iIndexOf = str.indexOf(44);
        int i2 = 0;
        while (iIndexOf > -1) {
            vector.add(str.substring(i2, iIndexOf));
            i2 = iIndexOf + 1;
            iIndexOf = str.indexOf(44, i2);
        }
        vector.add(str.substring(i2));
        String[] strArr = new String[vector.size()];
        vector.toArray(strArr);
        return strArr;
    }

    private boolean t(String str) {
        int i2 = 0;
        while (i2 < y.length && !y[i2].equals(str)) {
            i2++;
        }
        return i2 < y.length;
    }

    /* JADX WARN: Removed duplicated region for block: B:192:0x02d7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private javax.net.ssl.SSLContext u(java.lang.String r14) throws java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException, java.io.IOException, java.security.KeyStoreException, java.security.cert.CertificateException, java.security.KeyManagementException, org.eclipse.paho.client.mqttv3.MqttSecurityException {
        /*
            Method dump skipped, instructions count: 733
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.eclipse.paho.client.mqttv3.a.a.a.u(java.lang.String):javax.net.ssl.SSLContext");
    }

    public void a(Properties properties, String str) {
        a(properties);
        Properties properties2 = new Properties();
        properties2.putAll(properties);
        b(properties2);
        if (str != null) {
            this.z.put(str, properties2);
        } else {
            this.A = properties2;
        }
    }

    public void b(Properties properties, String str) {
        a(properties);
        Properties properties2 = this.A;
        if (str != null) {
            properties2 = (Properties) this.z.get(str);
        }
        if (properties2 == null) {
            properties2 = new Properties();
        }
        b(properties);
        properties2.putAll(properties);
        if (str != null) {
            this.z.put(str, properties2);
        } else {
            this.A = properties2;
        }
    }

    public boolean c(String str) {
        if (str != null) {
            return this.z.remove(str) != null;
        }
        if (this.A == null) {
            return false;
        }
        this.A = null;
        return true;
    }

    public Properties d(String str) {
        return (Properties) (str == null ? this.A : this.z.get(str));
    }

    public String e(String str) {
        return a(str, f469a, null);
    }

    public String f(String str) {
        return a(str, b, null);
    }

    public String g(String str) {
        String strA = a(str, c);
        return (strA == null && o != 0) ? System.getProperty(o) : strA;
    }

    public char[] h(String str) {
        String strA = a(str, d, q);
        if (strA != null) {
            return strA.startsWith(C) ? a(strA) : strA.toCharArray();
        }
        return null;
    }

    public String i(String str) {
        return a(str, e, p);
    }

    public String j(String str) {
        return a(str, f, null);
    }

    public String k(String str) {
        return a(str, g, u);
    }

    public String l(String str) {
        return a(str, h, r);
    }

    public char[] m(String str) {
        String strA = a(str, i, t);
        if (strA != null) {
            return strA.startsWith(C) ? a(strA) : strA.toCharArray();
        }
        return null;
    }

    public String n(String str) {
        return a(str, j, null);
    }

    public String o(String str) {
        return a(str, k, null);
    }

    public String p(String str) {
        return a(str, l, v);
    }

    public String[] q(String str) {
        return b(a(str, m, null));
    }

    public boolean r(String str) {
        String strA = a(str, n, null);
        if (strA != null) {
            return Boolean.valueOf(strA).booleanValue();
        }
        return false;
    }

    public SSLSocketFactory s(String str) throws NoSuchAlgorithmException, UnrecoverableKeyException, IOException, KeyStoreException, CertificateException, KeyManagementException, MqttSecurityException {
        SSLContext sSLContextU = u(str);
        if (this.D != null) {
            Logger logger = this.D;
            Object[] objArr = new Object[2];
            objArr[0] = str != null ? str : "null (broker defaults)";
            objArr[1] = q(str) != null ? a(str, m, null) : "null (using platform-enabled cipher suites)";
            logger.fine(x, "createSocketFactory", "12020", objArr);
        }
        return sSLContextU.getSocketFactory();
    }
}
