package android.support.v4.hardware.fingerprint;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23;
import android.support.v4.os.CancellationSignal;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

/* loaded from: classes.dex */
public final class FingerprintManagerCompat {
    static final FingerprintManagerCompatImpl IMPL;
    private Context mContext;

    private interface FingerprintManagerCompatImpl {
        void authenticate(Context context, CryptoObject cryptoObject, int i, CancellationSignal cancellationSignal, AuthenticationCallback authenticationCallback, Handler handler);

        boolean hasEnrolledFingerprints(Context context);

        boolean isHardwareDetected(Context context);
    }

    public static FingerprintManagerCompat from(Context context) {
        return new FingerprintManagerCompat(context);
    }

    private FingerprintManagerCompat(Context context) {
        this.mContext = context;
    }

    static {
        int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            IMPL = new Api23FingerprintManagerCompatImpl();
        } else {
            IMPL = new LegacyFingerprintManagerCompatImpl();
        }
    }

    public boolean hasEnrolledFingerprints() {
        return IMPL.hasEnrolledFingerprints(this.mContext);
    }

    public boolean isHardwareDetected() {
        return IMPL.isHardwareDetected(this.mContext);
    }

    public void authenticate(@Nullable CryptoObject crypto, int flags, @Nullable CancellationSignal cancel, @NonNull AuthenticationCallback callback, @Nullable Handler handler) {
        IMPL.authenticate(this.mContext, crypto, flags, cancel, callback, handler);
    }

    public static class CryptoObject {
        private final Cipher mCipher;
        private final Mac mMac;
        private final Signature mSignature;

        public CryptoObject(Signature signature) {
            this.mSignature = signature;
            this.mCipher = null;
            this.mMac = null;
        }

        public CryptoObject(Cipher cipher) {
            this.mCipher = cipher;
            this.mSignature = null;
            this.mMac = null;
        }

        public CryptoObject(Mac mac) {
            this.mMac = mac;
            this.mCipher = null;
            this.mSignature = null;
        }

        public Signature getSignature() {
            return this.mSignature;
        }

        public Cipher getCipher() {
            return this.mCipher;
        }

        public Mac getMac() {
            return this.mMac;
        }
    }

    public static final class AuthenticationResult {
        private CryptoObject mCryptoObject;

        public AuthenticationResult(CryptoObject crypto) {
            this.mCryptoObject = crypto;
        }

        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }
    }

    public static abstract class AuthenticationCallback {
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
        }

        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult result) {
        }

        public void onAuthenticationFailed() {
        }
    }

    private static class LegacyFingerprintManagerCompatImpl implements FingerprintManagerCompatImpl {
        @Override // android.support.v4.hardware.fingerprint.FingerprintManagerCompat.FingerprintManagerCompatImpl
        public boolean hasEnrolledFingerprints(Context context) {
            return false;
        }

        @Override // android.support.v4.hardware.fingerprint.FingerprintManagerCompat.FingerprintManagerCompatImpl
        public boolean isHardwareDetected(Context context) {
            return false;
        }

        @Override // android.support.v4.hardware.fingerprint.FingerprintManagerCompat.FingerprintManagerCompatImpl
        public void authenticate(Context context, CryptoObject crypto, int flags, CancellationSignal cancel, AuthenticationCallback callback, Handler handler) {
        }
    }

    private static class Api23FingerprintManagerCompatImpl implements FingerprintManagerCompatImpl {
        @Override // android.support.v4.hardware.fingerprint.FingerprintManagerCompat.FingerprintManagerCompatImpl
        public boolean hasEnrolledFingerprints(Context context) {
            return FingerprintManagerCompatApi23.hasEnrolledFingerprints(context);
        }

        @Override // android.support.v4.hardware.fingerprint.FingerprintManagerCompat.FingerprintManagerCompatImpl
        public boolean isHardwareDetected(Context context) {
            return FingerprintManagerCompatApi23.isHardwareDetected(context);
        }

        @Override // android.support.v4.hardware.fingerprint.FingerprintManagerCompat.FingerprintManagerCompatImpl
        public void authenticate(Context context, CryptoObject crypto, int flags, CancellationSignal cancel, AuthenticationCallback callback, Handler handler) {
            FingerprintManagerCompatApi23.authenticate(context, wrapCryptoObject(crypto), flags, cancel != null ? cancel.getCancellationSignalObject() : null, wrapCallback(callback), handler);
        }

        private static FingerprintManagerCompatApi23.CryptoObject wrapCryptoObject(CryptoObject cryptoObject) {
            if (cryptoObject == null) {
                return null;
            }
            if (cryptoObject.getCipher() != null) {
                return new FingerprintManagerCompatApi23.CryptoObject(cryptoObject.getCipher());
            }
            if (cryptoObject.getSignature() != null) {
                return new FingerprintManagerCompatApi23.CryptoObject(cryptoObject.getSignature());
            }
            if (cryptoObject.getMac() != null) {
                return new FingerprintManagerCompatApi23.CryptoObject(cryptoObject.getMac());
            }
            return null;
        }

        static CryptoObject unwrapCryptoObject(FingerprintManagerCompatApi23.CryptoObject cryptoObject) {
            if (cryptoObject == null) {
                return null;
            }
            if (cryptoObject.getCipher() != null) {
                return new CryptoObject(cryptoObject.getCipher());
            }
            if (cryptoObject.getSignature() != null) {
                return new CryptoObject(cryptoObject.getSignature());
            }
            if (cryptoObject.getMac() != null) {
                return new CryptoObject(cryptoObject.getMac());
            }
            return null;
        }

        private static FingerprintManagerCompatApi23.AuthenticationCallback wrapCallback(final AuthenticationCallback callback) {
            return new FingerprintManagerCompatApi23.AuthenticationCallback() { // from class: android.support.v4.hardware.fingerprint.FingerprintManagerCompat.Api23FingerprintManagerCompatImpl.1
                @Override // android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.AuthenticationCallback
                public void onAuthenticationError(int errMsgId, CharSequence errString) {
                    callback.onAuthenticationError(errMsgId, errString);
                }

                @Override // android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.AuthenticationCallback
                public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                    callback.onAuthenticationHelp(helpMsgId, helpString);
                }

                @Override // android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.AuthenticationCallback
                public void onAuthenticationSucceeded(FingerprintManagerCompatApi23.AuthenticationResultInternal result) {
                    callback.onAuthenticationSucceeded(new AuthenticationResult(Api23FingerprintManagerCompatImpl.unwrapCryptoObject(result.getCryptoObject())));
                }

                @Override // android.support.v4.hardware.fingerprint.FingerprintManagerCompatApi23.AuthenticationCallback
                public void onAuthenticationFailed() {
                    callback.onAuthenticationFailed();
                }
            };
        }
    }
}
