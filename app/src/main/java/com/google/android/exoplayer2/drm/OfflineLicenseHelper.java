package com.google.android.exoplayer2.drm;

import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Pair;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.HashMap;

/* loaded from: classes.dex */
public final class OfflineLicenseHelper<T extends ExoMediaCrypto> {
    private final ConditionVariable conditionVariable;
    private final DefaultDrmSessionManager<T> drmSessionManager;
    private final HandlerThread handlerThread = new HandlerThread("OfflineLicenseHelper");

    public static OfflineLicenseHelper<FrameworkMediaCrypto> newWidevineInstance(String licenseUrl, HttpDataSource.Factory httpDataSourceFactory) throws UnsupportedDrmException {
        return newWidevineInstance(new HttpMediaDrmCallback(licenseUrl, httpDataSourceFactory), (HashMap<String, String>) null);
    }

    public static OfflineLicenseHelper<FrameworkMediaCrypto> newWidevineInstance(MediaDrmCallback callback, HashMap<String, String> optionalKeyRequestParameters) throws UnsupportedDrmException {
        return new OfflineLicenseHelper<>(FrameworkMediaDrm.newInstance(C.WIDEVINE_UUID), callback, optionalKeyRequestParameters);
    }

    public OfflineLicenseHelper(ExoMediaDrm<T> mediaDrm, MediaDrmCallback callback, HashMap<String, String> optionalKeyRequestParameters) {
        this.handlerThread.start();
        this.conditionVariable = new ConditionVariable();
        DefaultDrmSessionManager.EventListener eventListener = new DefaultDrmSessionManager.EventListener() { // from class: com.google.android.exoplayer2.drm.OfflineLicenseHelper.1
            @Override // com.google.android.exoplayer2.drm.DefaultDrmSessionManager.EventListener
            public void onDrmKeysLoaded() {
                OfflineLicenseHelper.this.conditionVariable.open();
            }

            @Override // com.google.android.exoplayer2.drm.DefaultDrmSessionManager.EventListener
            public void onDrmSessionManagerError(Exception e) {
                OfflineLicenseHelper.this.conditionVariable.open();
            }

            @Override // com.google.android.exoplayer2.drm.DefaultDrmSessionManager.EventListener
            public void onDrmKeysRestored() {
                OfflineLicenseHelper.this.conditionVariable.open();
            }

            @Override // com.google.android.exoplayer2.drm.DefaultDrmSessionManager.EventListener
            public void onDrmKeysRemoved() {
                OfflineLicenseHelper.this.conditionVariable.open();
            }
        };
        this.drmSessionManager = new DefaultDrmSessionManager<>(C.WIDEVINE_UUID, mediaDrm, callback, optionalKeyRequestParameters, new Handler(this.handlerThread.getLooper()), eventListener);
    }

    public void release() {
        this.handlerThread.quit();
    }

    public synchronized byte[] downloadLicense(DrmInitData drmInitData) throws InterruptedException, IOException, DrmSession.DrmSessionException {
        Assertions.checkArgument(drmInitData != null);
        return blockingKeyRequest(2, null, drmInitData);
    }

    public synchronized byte[] renewLicense(byte[] offlineLicenseKeySetId) throws DrmSession.DrmSessionException {
        Assertions.checkNotNull(offlineLicenseKeySetId);
        return blockingKeyRequest(2, offlineLicenseKeySetId, null);
    }

    public synchronized void releaseLicense(byte[] offlineLicenseKeySetId) throws DrmSession.DrmSessionException {
        Assertions.checkNotNull(offlineLicenseKeySetId);
        blockingKeyRequest(3, offlineLicenseKeySetId, null);
    }

    public synchronized Pair<Long, Long> getLicenseDurationRemainingSec(byte[] offlineLicenseKeySetId) throws DrmSession.DrmSessionException {
        Pair<Long, Long> licenseDurationRemainingSec;
        Assertions.checkNotNull(offlineLicenseKeySetId);
        DrmSession<T> drmSession = openBlockingKeyRequest(1, offlineLicenseKeySetId, null);
        DrmSession.DrmSessionException error = drmSession.getError();
        licenseDurationRemainingSec = WidevineUtil.getLicenseDurationRemainingSec(drmSession);
        this.drmSessionManager.releaseSession(drmSession);
        if (error != null) {
            if (error.getCause() instanceof KeysExpiredException) {
                licenseDurationRemainingSec = Pair.create(0L, 0L);
            } else {
                throw error;
            }
        }
        return licenseDurationRemainingSec;
    }

    private byte[] blockingKeyRequest(int licenseMode, byte[] offlineLicenseKeySetId, DrmInitData drmInitData) throws DrmSession.DrmSessionException {
        DrmSession<T> drmSession = openBlockingKeyRequest(licenseMode, offlineLicenseKeySetId, drmInitData);
        DrmSession.DrmSessionException error = drmSession.getError();
        byte[] keySetId = drmSession.getOfflineLicenseKeySetId();
        this.drmSessionManager.releaseSession(drmSession);
        if (error != null) {
            throw error;
        }
        return keySetId;
    }

    private DrmSession<T> openBlockingKeyRequest(int licenseMode, byte[] offlineLicenseKeySetId, DrmInitData drmInitData) {
        this.drmSessionManager.setMode(licenseMode, offlineLicenseKeySetId);
        this.conditionVariable.close();
        DrmSession<T> drmSession = this.drmSessionManager.acquireSession(this.handlerThread.getLooper(), drmInitData);
        this.conditionVariable.block();
        return drmSession;
    }
}
