package okhttp3.internal.platform;

import android.util.Log;
import cn.yunzhisheng.asr.JniUscClient;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import okhttp3.internal.tls.CertificateChainCleaner;

/* loaded from: classes.dex */
class AndroidPlatform extends Platform {
    private static final int MAX_LOG_LENGTH = 4000;
    private final CloseGuard closeGuard = CloseGuard.get();
    private final OptionalMethod<Socket> getAlpnSelectedProtocol;
    private final OptionalMethod<Socket> setAlpnProtocols;
    private final OptionalMethod<Socket> setHostname;
    private final OptionalMethod<Socket> setUseSessionTickets;
    private final Class<?> sslParametersClass;

    AndroidPlatform(Class<?> sslParametersClass, OptionalMethod<Socket> setUseSessionTickets, OptionalMethod<Socket> setHostname, OptionalMethod<Socket> getAlpnSelectedProtocol, OptionalMethod<Socket> setAlpnProtocols) {
        this.sslParametersClass = sslParametersClass;
        this.setUseSessionTickets = setUseSessionTickets;
        this.setHostname = setHostname;
        this.getAlpnSelectedProtocol = getAlpnSelectedProtocol;
        this.setAlpnProtocols = setAlpnProtocols;
    }

    @Override // okhttp3.internal.platform.Platform
    public void connectSocket(Socket socket, InetSocketAddress address, int connectTimeout) throws IOException {
        try {
            socket.connect(address, connectTimeout);
        } catch (AssertionError e) {
            if (!Util.isAndroidGetsocknameError(e)) {
                throw e;
            }
            throw new IOException(e);
        } catch (SecurityException e2) {
            IOException ioException = new IOException("Exception in connect");
            ioException.initCause(e2);
            throw ioException;
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) throws ClassNotFoundException {
        Object context = readFieldOrNull(sslSocketFactory, this.sslParametersClass, "sslParameters");
        if (context == null) {
            try {
                Class<?> gmsSslParametersClass = Class.forName("com.google.android.gms.org.conscrypt.SSLParametersImpl", false, sslSocketFactory.getClass().getClassLoader());
                context = readFieldOrNull(sslSocketFactory, gmsSslParametersClass, "sslParameters");
            } catch (ClassNotFoundException e) {
                return super.trustManager(sslSocketFactory);
            }
        }
        X509TrustManager x509TrustManager = (X509TrustManager) readFieldOrNull(context, X509TrustManager.class, "x509TrustManager");
        if (x509TrustManager != null) {
            return x509TrustManager;
        }
        return (X509TrustManager) readFieldOrNull(context, X509TrustManager.class, "trustManager");
    }

    @Override // okhttp3.internal.platform.Platform
    public void configureTlsExtensions(SSLSocket sslSocket, String hostname, List<Protocol> protocols) throws Throwable {
        if (hostname != null) {
            this.setUseSessionTickets.invokeOptionalWithoutCheckedException(sslSocket, true);
            this.setHostname.invokeOptionalWithoutCheckedException(sslSocket, hostname);
        }
        if (this.setAlpnProtocols != null && this.setAlpnProtocols.isSupported(sslSocket)) {
            Object[] parameters = {concatLengthPrefixed(protocols)};
            this.setAlpnProtocols.invokeWithoutCheckedException(sslSocket, parameters);
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public String getSelectedProtocol(SSLSocket socket) {
        byte[] alpnResult;
        if (this.getAlpnSelectedProtocol == null || !this.getAlpnSelectedProtocol.isSupported(socket) || (alpnResult = (byte[]) this.getAlpnSelectedProtocol.invokeWithoutCheckedException(socket, new Object[0])) == null) {
            return null;
        }
        return new String(alpnResult, Util.UTF_8);
    }

    @Override // okhttp3.internal.platform.Platform
    public void log(int level, String message, Throwable t) {
        int logLevel = level != 5 ? 3 : 5;
        if (t != null) {
            message = message + '\n' + Log.getStackTraceString(t);
        }
        int i = 0;
        int length = message.length();
        while (i < length) {
            int newline = message.indexOf(10, i);
            if (newline == -1) {
                newline = length;
            }
            do {
                int end = Math.min(newline, i + MAX_LOG_LENGTH);
                Log.println(logLevel, "OkHttp", message.substring(i, end));
                i = end;
            } while (i < newline);
            i++;
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public Object getStackTraceForCloseable(String closer) {
        return this.closeGuard.createAndOpen(closer);
    }

    @Override // okhttp3.internal.platform.Platform
    public void logCloseableLeak(String message, Object stackTrace) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        boolean reported = this.closeGuard.warnIfOpen(stackTrace);
        if (!reported) {
            log(5, message, null);
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public boolean isCleartextTrafficPermitted(String hostname) throws IllegalAccessException, NoSuchMethodException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        try {
            Class<?> networkPolicyClass = Class.forName("android.security.NetworkSecurityPolicy");
            Method getInstanceMethod = networkPolicyClass.getMethod("getInstance", new Class[0]);
            Object networkSecurityPolicy = getInstanceMethod.invoke(null, new Object[0]);
            Method isCleartextTrafficPermittedMethod = networkPolicyClass.getMethod("isCleartextTrafficPermitted", String.class);
            return ((Boolean) isCleartextTrafficPermittedMethod.invoke(networkSecurityPolicy, hostname)).booleanValue();
        } catch (ClassNotFoundException e) {
            return super.isCleartextTrafficPermitted(hostname);
        } catch (IllegalAccessException e2) {
            throw new AssertionError();
        } catch (IllegalArgumentException e3) {
            throw new AssertionError();
        } catch (NoSuchMethodException e4) {
            return super.isCleartextTrafficPermitted(hostname);
        } catch (InvocationTargetException e5) {
            throw new AssertionError();
        }
    }

    @Override // okhttp3.internal.platform.Platform
    public CertificateChainCleaner buildCertificateChainCleaner(X509TrustManager trustManager) throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        try {
            Class<?> extensionsClass = Class.forName("android.net.http.X509TrustManagerExtensions");
            Constructor<?> constructor = extensionsClass.getConstructor(X509TrustManager.class);
            Object extensions = constructor.newInstance(trustManager);
            Method checkServerTrusted = extensionsClass.getMethod("checkServerTrusted", X509Certificate[].class, String.class, String.class);
            return new AndroidCertificateChainCleaner(extensions, checkServerTrusted);
        } catch (Exception e) {
            return super.buildCertificateChainCleaner(trustManager);
        }
    }

    public static Platform buildIfSupported() {
        Class<?> sslParametersClass;
        try {
            try {
                sslParametersClass = Class.forName("com.android.org.conscrypt.SSLParametersImpl");
            } catch (ClassNotFoundException e) {
                return null;
            }
        } catch (ClassNotFoundException e2) {
            sslParametersClass = Class.forName("org.apache.harmony.xnet.provider.jsse.SSLParametersImpl");
        }
        OptionalMethod<Socket> setUseSessionTickets = new OptionalMethod<>(null, "setUseSessionTickets", Boolean.TYPE);
        OptionalMethod<Socket> setHostname = new OptionalMethod<>(null, "setHostname", String.class);
        OptionalMethod<Socket> getAlpnSelectedProtocol = null;
        OptionalMethod<Socket> setAlpnProtocols = null;
        try {
            Class.forName("android.net.Network");
            OptionalMethod<Socket> getAlpnSelectedProtocol2 = new OptionalMethod<>(byte[].class, "getAlpnSelectedProtocol", new Class[0]);
            try {
                setAlpnProtocols = new OptionalMethod<>(null, "setAlpnProtocols", byte[].class);
                getAlpnSelectedProtocol = getAlpnSelectedProtocol2;
            } catch (ClassNotFoundException e3) {
                getAlpnSelectedProtocol = getAlpnSelectedProtocol2;
            }
        } catch (ClassNotFoundException e4) {
        }
        return new AndroidPlatform(sslParametersClass, setUseSessionTickets, setHostname, getAlpnSelectedProtocol, setAlpnProtocols);
    }

    static final class AndroidCertificateChainCleaner extends CertificateChainCleaner {
        private final Method checkServerTrusted;
        private final Object x509TrustManagerExtensions;

        AndroidCertificateChainCleaner(Object x509TrustManagerExtensions, Method checkServerTrusted) {
            this.x509TrustManagerExtensions = x509TrustManagerExtensions;
            this.checkServerTrusted = checkServerTrusted;
        }

        @Override // okhttp3.internal.tls.CertificateChainCleaner
        public List<Certificate> clean(List<Certificate> chain, String hostname) throws SSLPeerUnverifiedException {
            try {
                X509Certificate[] certificates = (X509Certificate[]) chain.toArray(new X509Certificate[chain.size()]);
                return (List) this.checkServerTrusted.invoke(this.x509TrustManagerExtensions, certificates, "RSA", hostname);
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            } catch (InvocationTargetException e2) {
                SSLPeerUnverifiedException exception = new SSLPeerUnverifiedException(e2.getMessage());
                exception.initCause(e2);
                throw exception;
            }
        }

        public boolean equals(Object other) {
            return other instanceof AndroidCertificateChainCleaner;
        }

        public int hashCode() {
            return 0;
        }
    }

    static final class CloseGuard {
        private final Method getMethod;
        private final Method openMethod;
        private final Method warnIfOpenMethod;

        CloseGuard(Method getMethod, Method openMethod, Method warnIfOpenMethod) {
            this.getMethod = getMethod;
            this.openMethod = openMethod;
            this.warnIfOpenMethod = warnIfOpenMethod;
        }

        Object createAndOpen(String closer) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (this.getMethod != null) {
                try {
                    Object closeGuardInstance = this.getMethod.invoke(null, new Object[0]);
                    this.openMethod.invoke(closeGuardInstance, closer);
                    return closeGuardInstance;
                } catch (Exception e) {
                }
            }
            return null;
        }

        boolean warnIfOpen(Object closeGuardInstance) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (closeGuardInstance == null) {
                return false;
            }
            try {
                this.warnIfOpenMethod.invoke(closeGuardInstance, new Object[0]);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        static CloseGuard get() throws NoSuchMethodException, ClassNotFoundException, SecurityException {
            Method getMethod;
            Method openMethod;
            Method warnIfOpenMethod;
            try {
                Class<?> closeGuardClass = Class.forName("dalvik.system.CloseGuard");
                getMethod = closeGuardClass.getMethod("get", new Class[0]);
                openMethod = closeGuardClass.getMethod(JniUscClient.r, String.class);
                warnIfOpenMethod = closeGuardClass.getMethod("warnIfOpen", new Class[0]);
            } catch (Exception e) {
                getMethod = null;
                openMethod = null;
                warnIfOpenMethod = null;
            }
            return new CloseGuard(getMethod, openMethod, warnIfOpenMethod);
        }
    }
}
