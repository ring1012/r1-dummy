package okhttp3.internal.platform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Protocol;

/* loaded from: classes.dex */
final class Jdk9Platform extends Platform {
    final Method getProtocolMethod;
    final Method setProtocolMethod;

    Jdk9Platform(Method setProtocolMethod, Method getProtocolMethod) {
        this.setProtocolMethod = setProtocolMethod;
        this.getProtocolMethod = getProtocolMethod;
    }

    @Override // okhttp3.internal.platform.Platform
    public void configureTlsExtensions(SSLSocket sslSocket, String hostname, List<Protocol> protocols) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            SSLParameters sslParameters = sslSocket.getSSLParameters();
            List<String> names = alpnProtocolNames(protocols);
            this.setProtocolMethod.invoke(sslParameters, names.toArray(new String[names.size()]));
            sslSocket.setSSLParameters(sslParameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AssertionError();
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public String getSelectedProtocol(SSLSocket socket) {
        try {
            String protocol = (String) this.getProtocolMethod.invoke(socket, new Object[0]);
            if (protocol != null) {
                if (!protocol.equals("")) {
                    return protocol;
                }
            }
            return null;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AssertionError();
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) {
        throw new UnsupportedOperationException("clientBuilder.sslSocketFactory(SSLSocketFactory) not supported on JDK 9+");
    }

    public static Jdk9Platform buildIfSupported() {
        try {
            Method setProtocolMethod = SSLParameters.class.getMethod("setApplicationProtocols", String[].class);
            Method getProtocolMethod = SSLSocket.class.getMethod("getApplicationProtocol", new Class[0]);
            return new Jdk9Platform(setProtocolMethod, getProtocolMethod);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
