package com.google.android.exoplayer2.drm;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.DeniedByServerException;
import android.media.NotProvisionedException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@TargetApi(18)
/* loaded from: classes.dex */
public class DefaultDrmSessionManager<T extends ExoMediaCrypto> implements DrmSessionManager<T>, DrmSession<T> {
    private static final String CENC_SCHEME_MIME_TYPE = "cenc";
    private static final int MAX_LICENSE_DURATION_TO_RENEW = 60;
    public static final int MODE_DOWNLOAD = 2;
    public static final int MODE_PLAYBACK = 0;
    public static final int MODE_QUERY = 1;
    public static final int MODE_RELEASE = 3;
    private static final int MSG_KEYS = 1;
    private static final int MSG_PROVISION = 0;
    public static final String PLAYREADY_CUSTOM_DATA_KEY = "PRCustomData";
    private static final String TAG = "OfflineDrmSessionMngr";
    final MediaDrmCallback callback;
    private final Handler eventHandler;
    private final EventListener eventListener;
    private DrmSession.DrmSessionException lastException;
    private T mediaCrypto;
    private final ExoMediaDrm<T> mediaDrm;
    DefaultDrmSessionManager<T>.MediaDrmHandler mediaDrmHandler;
    private int mode;
    private byte[] offlineLicenseKeySetId;
    private int openCount;
    private final HashMap<String, String> optionalKeyRequestParameters;
    private Looper playbackLooper;
    private Handler postRequestHandler;
    DefaultDrmSessionManager<T>.PostResponseHandler postResponseHandler;
    private boolean provisioningInProgress;
    private HandlerThread requestHandlerThread;
    private byte[] schemeInitData;
    private String schemeMimeType;
    private byte[] sessionId;
    private int state;
    final UUID uuid;

    public interface EventListener {
        void onDrmKeysLoaded();

        void onDrmKeysRemoved();

        void onDrmKeysRestored();

        void onDrmSessionManagerError(Exception exc);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    public static DefaultDrmSessionManager<FrameworkMediaCrypto> newWidevineInstance(MediaDrmCallback callback, HashMap<String, String> optionalKeyRequestParameters, Handler eventHandler, EventListener eventListener) throws UnsupportedDrmException {
        return newFrameworkInstance(C.WIDEVINE_UUID, callback, optionalKeyRequestParameters, eventHandler, eventListener);
    }

    public static DefaultDrmSessionManager<FrameworkMediaCrypto> newPlayReadyInstance(MediaDrmCallback callback, String customData, Handler eventHandler, EventListener eventListener) throws UnsupportedDrmException {
        HashMap<String, String> optionalKeyRequestParameters;
        if (!TextUtils.isEmpty(customData)) {
            optionalKeyRequestParameters = new HashMap<>();
            optionalKeyRequestParameters.put(PLAYREADY_CUSTOM_DATA_KEY, customData);
        } else {
            optionalKeyRequestParameters = null;
        }
        return newFrameworkInstance(C.PLAYREADY_UUID, callback, optionalKeyRequestParameters, eventHandler, eventListener);
    }

    public static DefaultDrmSessionManager<FrameworkMediaCrypto> newFrameworkInstance(UUID uuid, MediaDrmCallback callback, HashMap<String, String> optionalKeyRequestParameters, Handler eventHandler, EventListener eventListener) throws UnsupportedDrmException {
        return new DefaultDrmSessionManager<>(uuid, FrameworkMediaDrm.newInstance(uuid), callback, optionalKeyRequestParameters, eventHandler, eventListener);
    }

    public DefaultDrmSessionManager(UUID uuid, ExoMediaDrm<T> mediaDrm, MediaDrmCallback callback, HashMap<String, String> optionalKeyRequestParameters, Handler eventHandler, EventListener eventListener) {
        this.uuid = uuid;
        this.mediaDrm = mediaDrm;
        this.callback = callback;
        this.optionalKeyRequestParameters = optionalKeyRequestParameters;
        this.eventHandler = eventHandler;
        this.eventListener = eventListener;
        mediaDrm.setOnEventListener(new MediaDrmEventListener());
        this.state = 1;
        this.mode = 0;
    }

    public final String getPropertyString(String key) {
        return this.mediaDrm.getPropertyString(key);
    }

    public final void setPropertyString(String key, String value) {
        this.mediaDrm.setPropertyString(key, value);
    }

    public final byte[] getPropertyByteArray(String key) {
        return this.mediaDrm.getPropertyByteArray(key);
    }

    public final void setPropertyByteArray(String key, byte[] value) {
        this.mediaDrm.setPropertyByteArray(key, value);
    }

    public void setMode(int mode, byte[] offlineLicenseKeySetId) {
        Assertions.checkState(this.openCount == 0);
        if (mode == 1 || mode == 3) {
            Assertions.checkNotNull(offlineLicenseKeySetId);
        }
        this.mode = mode;
        this.offlineLicenseKeySetId = offlineLicenseKeySetId;
    }

    @Override // com.google.android.exoplayer2.drm.DrmSessionManager
    public DrmSession<T> acquireSession(Looper playbackLooper, DrmInitData drmInitData) {
        byte[] psshData;
        Assertions.checkState(this.playbackLooper == null || this.playbackLooper == playbackLooper);
        int i = this.openCount + 1;
        this.openCount = i;
        if (i == 1) {
            if (this.playbackLooper == null) {
                this.playbackLooper = playbackLooper;
                this.mediaDrmHandler = new MediaDrmHandler(playbackLooper);
                this.postResponseHandler = new PostResponseHandler(playbackLooper);
            }
            this.requestHandlerThread = new HandlerThread("DrmRequestHandler");
            this.requestHandlerThread.start();
            this.postRequestHandler = new PostRequestHandler(this.requestHandlerThread.getLooper());
            if (this.offlineLicenseKeySetId == null) {
                DrmInitData.SchemeData schemeData = drmInitData.get(this.uuid);
                if (schemeData == null) {
                    onError(new IllegalStateException("Media does not support uuid: " + this.uuid));
                } else {
                    this.schemeInitData = schemeData.data;
                    this.schemeMimeType = schemeData.mimeType;
                    if (Util.SDK_INT < 21 && (psshData = PsshAtomUtil.parseSchemeSpecificData(this.schemeInitData, C.WIDEVINE_UUID)) != null) {
                        this.schemeInitData = psshData;
                    }
                    if (Util.SDK_INT < 26 && C.CLEARKEY_UUID.equals(this.uuid) && (MimeTypes.VIDEO_MP4.equals(this.schemeMimeType) || MimeTypes.AUDIO_MP4.equals(this.schemeMimeType))) {
                        this.schemeMimeType = CENC_SCHEME_MIME_TYPE;
                    }
                    this.state = 2;
                    openInternal(true);
                }
            } else {
                this.state = 2;
                openInternal(true);
            }
        }
        return this;
    }

    @Override // com.google.android.exoplayer2.drm.DrmSessionManager
    public void releaseSession(DrmSession<T> session) {
        int i = this.openCount - 1;
        this.openCount = i;
        if (i == 0) {
            this.state = 1;
            this.provisioningInProgress = false;
            this.mediaDrmHandler.removeCallbacksAndMessages(null);
            this.postResponseHandler.removeCallbacksAndMessages(null);
            this.postRequestHandler.removeCallbacksAndMessages(null);
            this.postRequestHandler = null;
            this.requestHandlerThread.quit();
            this.requestHandlerThread = null;
            this.schemeInitData = null;
            this.schemeMimeType = null;
            this.mediaCrypto = null;
            this.lastException = null;
            if (this.sessionId != null) {
                this.mediaDrm.closeSession(this.sessionId);
                this.sessionId = null;
            }
        }
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public final int getState() {
        return this.state;
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public final T getMediaCrypto() {
        if (this.state != 3 && this.state != 4) {
            throw new IllegalStateException();
        }
        return this.mediaCrypto;
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public boolean requiresSecureDecoderComponent(String mimeType) {
        if (this.state != 3 && this.state != 4) {
            throw new IllegalStateException();
        }
        return this.mediaCrypto.requiresSecureDecoderComponent(mimeType);
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public final DrmSession.DrmSessionException getError() {
        if (this.state == 0) {
            return this.lastException;
        }
        return null;
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public Map<String, String> queryKeyStatus() {
        if (this.sessionId == null) {
            throw new IllegalStateException();
        }
        return this.mediaDrm.queryKeyStatus(this.sessionId);
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public byte[] getOfflineLicenseKeySetId() {
        return this.offlineLicenseKeySetId;
    }

    private void openInternal(boolean z) {
        try {
            this.sessionId = this.mediaDrm.openSession();
            this.mediaCrypto = (T) this.mediaDrm.createMediaCrypto(this.uuid, this.sessionId);
            this.state = 3;
            doLicense();
        } catch (NotProvisionedException e) {
            if (z) {
                postProvisionRequest();
            } else {
                onError(e);
            }
        } catch (Exception e2) {
            onError(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postProvisionRequest() {
        if (!this.provisioningInProgress) {
            this.provisioningInProgress = true;
            ExoMediaDrm.ProvisionRequest request = this.mediaDrm.getProvisionRequest();
            this.postRequestHandler.obtainMessage(0, request).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onProvisionResponse(Object response) {
        this.provisioningInProgress = false;
        if (this.state == 2 || this.state == 3 || this.state == 4) {
            if (response instanceof Exception) {
                onError((Exception) response);
                return;
            }
            try {
                this.mediaDrm.provideProvisionResponse((byte[]) response);
                if (this.state == 2) {
                    openInternal(false);
                } else {
                    doLicense();
                }
            } catch (DeniedByServerException e) {
                onError(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doLicense() {
        switch (this.mode) {
            case 0:
            case 1:
                if (this.offlineLicenseKeySetId == null) {
                    postKeyRequest(this.sessionId, 1);
                    break;
                } else if (restoreKeys()) {
                    long licenseDurationRemainingSec = getLicenseDurationRemainingSec();
                    if (this.mode == 0 && licenseDurationRemainingSec <= 60) {
                        Log.d(TAG, "Offline license has expired or will expire soon. Remaining seconds: " + licenseDurationRemainingSec);
                        postKeyRequest(this.sessionId, 2);
                        break;
                    } else if (licenseDurationRemainingSec <= 0) {
                        onError(new KeysExpiredException());
                        break;
                    } else {
                        this.state = 4;
                        if (this.eventHandler != null && this.eventListener != null) {
                            this.eventHandler.post(new Runnable() { // from class: com.google.android.exoplayer2.drm.DefaultDrmSessionManager.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    DefaultDrmSessionManager.this.eventListener.onDrmKeysRestored();
                                }
                            });
                            break;
                        }
                    }
                }
                break;
            case 2:
                if (this.offlineLicenseKeySetId == null) {
                    postKeyRequest(this.sessionId, 2);
                    break;
                } else if (restoreKeys()) {
                    postKeyRequest(this.sessionId, 2);
                    break;
                }
                break;
            case 3:
                if (restoreKeys()) {
                    postKeyRequest(this.offlineLicenseKeySetId, 3);
                    break;
                }
                break;
        }
    }

    private boolean restoreKeys() {
        try {
            this.mediaDrm.restoreKeys(this.sessionId, this.offlineLicenseKeySetId);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error trying to restore Widevine keys.", e);
            onError(e);
            return false;
        }
    }

    private long getLicenseDurationRemainingSec() {
        if (!C.WIDEVINE_UUID.equals(this.uuid)) {
            return Long.MAX_VALUE;
        }
        Pair<Long, Long> pair = WidevineUtil.getLicenseDurationRemainingSec(this);
        return Math.min(((Long) pair.first).longValue(), ((Long) pair.second).longValue());
    }

    private void postKeyRequest(byte[] scope, int keyType) {
        try {
            ExoMediaDrm.KeyRequest keyRequest = this.mediaDrm.getKeyRequest(scope, this.schemeInitData, this.schemeMimeType, keyType, this.optionalKeyRequestParameters);
            this.postRequestHandler.obtainMessage(1, keyRequest).sendToTarget();
        } catch (Exception e) {
            onKeysError(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onKeyResponse(Object response) {
        if (this.state == 3 || this.state == 4) {
            if (response instanceof Exception) {
                onKeysError((Exception) response);
                return;
            }
            try {
                if (this.mode == 3) {
                    this.mediaDrm.provideKeyResponse(this.offlineLicenseKeySetId, (byte[]) response);
                    if (this.eventHandler != null && this.eventListener != null) {
                        this.eventHandler.post(new Runnable() { // from class: com.google.android.exoplayer2.drm.DefaultDrmSessionManager.2
                            @Override // java.lang.Runnable
                            public void run() {
                                DefaultDrmSessionManager.this.eventListener.onDrmKeysRemoved();
                            }
                        });
                        return;
                    }
                    return;
                }
                byte[] keySetId = this.mediaDrm.provideKeyResponse(this.sessionId, (byte[]) response);
                if ((this.mode == 2 || (this.mode == 0 && this.offlineLicenseKeySetId != null)) && keySetId != null && keySetId.length != 0) {
                    this.offlineLicenseKeySetId = keySetId;
                }
                this.state = 4;
                if (this.eventHandler != null && this.eventListener != null) {
                    this.eventHandler.post(new Runnable() { // from class: com.google.android.exoplayer2.drm.DefaultDrmSessionManager.3
                        @Override // java.lang.Runnable
                        public void run() {
                            DefaultDrmSessionManager.this.eventListener.onDrmKeysLoaded();
                        }
                    });
                }
            } catch (Exception e) {
                onKeysError(e);
            }
        }
    }

    private void onKeysError(Exception e) {
        if (e instanceof NotProvisionedException) {
            postProvisionRequest();
        } else {
            onError(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onError(final Exception e) {
        this.lastException = new DrmSession.DrmSessionException(e);
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() { // from class: com.google.android.exoplayer2.drm.DefaultDrmSessionManager.4
                @Override // java.lang.Runnable
                public void run() {
                    DefaultDrmSessionManager.this.eventListener.onDrmSessionManagerError(e);
                }
            });
        }
        if (this.state != 4) {
            this.state = 0;
        }
    }

    @SuppressLint({"HandlerLeak"})
    private class MediaDrmHandler extends Handler {
        public MediaDrmHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (DefaultDrmSessionManager.this.openCount != 0) {
                if (DefaultDrmSessionManager.this.state == 3 || DefaultDrmSessionManager.this.state == 4) {
                    switch (msg.what) {
                        case 1:
                            DefaultDrmSessionManager.this.state = 3;
                            DefaultDrmSessionManager.this.postProvisionRequest();
                            break;
                        case 2:
                            DefaultDrmSessionManager.this.doLicense();
                            break;
                        case 3:
                            if (DefaultDrmSessionManager.this.state == 4) {
                                DefaultDrmSessionManager.this.state = 3;
                                DefaultDrmSessionManager.this.onError(new KeysExpiredException());
                                break;
                            }
                            break;
                    }
                }
            }
        }
    }

    private class MediaDrmEventListener implements ExoMediaDrm.OnEventListener<T> {
        private MediaDrmEventListener() {
        }

        @Override // com.google.android.exoplayer2.drm.ExoMediaDrm.OnEventListener
        public void onEvent(ExoMediaDrm<? extends T> md, byte[] sessionId, int event, int extra, byte[] data) {
            if (DefaultDrmSessionManager.this.mode == 0) {
                DefaultDrmSessionManager.this.mediaDrmHandler.sendEmptyMessage(event);
            }
        }
    }

    @SuppressLint({"HandlerLeak"})
    private class PostResponseHandler extends Handler {
        public PostResponseHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DefaultDrmSessionManager.this.onProvisionResponse(msg.obj);
                    break;
                case 1:
                    DefaultDrmSessionManager.this.onKeyResponse(msg.obj);
                    break;
            }
        }
    }

    @SuppressLint({"HandlerLeak"})
    private class PostRequestHandler extends Handler {
        public PostRequestHandler(Looper backgroundLooper) {
            super(backgroundLooper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Object objExecuteKeyRequest;
            try {
                switch (msg.what) {
                    case 0:
                        objExecuteKeyRequest = DefaultDrmSessionManager.this.callback.executeProvisionRequest(DefaultDrmSessionManager.this.uuid, (ExoMediaDrm.ProvisionRequest) msg.obj);
                        break;
                    case 1:
                        objExecuteKeyRequest = DefaultDrmSessionManager.this.callback.executeKeyRequest(DefaultDrmSessionManager.this.uuid, (ExoMediaDrm.KeyRequest) msg.obj);
                        break;
                    default:
                        throw new RuntimeException();
                }
            } catch (Exception e) {
                objExecuteKeyRequest = e;
            }
            DefaultDrmSessionManager.this.postResponseHandler.obtainMessage(msg.what, objExecuteKeyRequest).sendToTarget();
        }
    }
}
