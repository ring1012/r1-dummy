package com.google.android.exoplayer2.drm;

import android.annotation.TargetApi;
import android.media.DeniedByServerException;
import android.media.MediaCrypto;
import android.media.MediaCryptoException;
import android.media.MediaDrm;
import android.media.NotProvisionedException;
import android.media.ResourceBusyException;
import android.media.UnsupportedSchemeException;
import android.support.annotation.NonNull;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.util.Assertions;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@TargetApi(18)
/* loaded from: classes.dex */
public final class FrameworkMediaDrm implements ExoMediaDrm<FrameworkMediaCrypto> {
    private final MediaDrm mediaDrm;

    public static FrameworkMediaDrm newInstance(UUID uuid) throws UnsupportedDrmException {
        try {
            return new FrameworkMediaDrm(uuid);
        } catch (UnsupportedSchemeException e) {
            throw new UnsupportedDrmException(1, e);
        } catch (Exception e2) {
            throw new UnsupportedDrmException(2, e2);
        }
    }

    private FrameworkMediaDrm(UUID uuid) throws UnsupportedSchemeException {
        this.mediaDrm = new MediaDrm((UUID) Assertions.checkNotNull(uuid));
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public void setOnEventListener(final ExoMediaDrm.OnEventListener<? super FrameworkMediaCrypto> listener) {
        this.mediaDrm.setOnEventListener(listener == null ? null : new MediaDrm.OnEventListener() { // from class: com.google.android.exoplayer2.drm.FrameworkMediaDrm.1
            @Override // android.media.MediaDrm.OnEventListener
            public void onEvent(@NonNull MediaDrm md, byte[] sessionId, int event, int extra, byte[] data) {
                listener.onEvent(FrameworkMediaDrm.this, sessionId, event, extra, data);
            }
        });
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public byte[] openSession() throws ResourceBusyException, NotProvisionedException {
        return this.mediaDrm.openSession();
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public void closeSession(byte[] sessionId) {
        this.mediaDrm.closeSession(sessionId);
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public ExoMediaDrm.KeyRequest getKeyRequest(byte[] scope, byte[] init, String mimeType, int keyType, HashMap<String, String> optionalParameters) throws NotProvisionedException {
        final MediaDrm.KeyRequest request = this.mediaDrm.getKeyRequest(scope, init, mimeType, keyType, optionalParameters);
        return new ExoMediaDrm.KeyRequest() { // from class: com.google.android.exoplayer2.drm.FrameworkMediaDrm.2
            @Override // com.google.android.exoplayer2.drm.ExoMediaDrm.KeyRequest
            public byte[] getData() {
                return request.getData();
            }

            @Override // com.google.android.exoplayer2.drm.ExoMediaDrm.KeyRequest
            public String getDefaultUrl() {
                return request.getDefaultUrl();
            }
        };
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public byte[] provideKeyResponse(byte[] scope, byte[] response) throws DeniedByServerException, NotProvisionedException {
        return this.mediaDrm.provideKeyResponse(scope, response);
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public ExoMediaDrm.ProvisionRequest getProvisionRequest() {
        final MediaDrm.ProvisionRequest provisionRequest = this.mediaDrm.getProvisionRequest();
        return new ExoMediaDrm.ProvisionRequest() { // from class: com.google.android.exoplayer2.drm.FrameworkMediaDrm.3
            @Override // com.google.android.exoplayer2.drm.ExoMediaDrm.ProvisionRequest
            public byte[] getData() {
                return provisionRequest.getData();
            }

            @Override // com.google.android.exoplayer2.drm.ExoMediaDrm.ProvisionRequest
            public String getDefaultUrl() {
                return provisionRequest.getDefaultUrl();
            }
        };
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public void provideProvisionResponse(byte[] response) throws DeniedByServerException {
        this.mediaDrm.provideProvisionResponse(response);
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public Map<String, String> queryKeyStatus(byte[] sessionId) {
        return this.mediaDrm.queryKeyStatus(sessionId);
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public void release() {
        this.mediaDrm.release();
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public void restoreKeys(byte[] sessionId, byte[] keySetId) {
        this.mediaDrm.restoreKeys(sessionId, keySetId);
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public String getPropertyString(String propertyName) {
        return this.mediaDrm.getPropertyString(propertyName);
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public byte[] getPropertyByteArray(String propertyName) {
        return this.mediaDrm.getPropertyByteArray(propertyName);
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public void setPropertyString(String propertyName, String value) {
        this.mediaDrm.setPropertyString(propertyName, value);
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public void setPropertyByteArray(String propertyName, byte[] value) {
        this.mediaDrm.setPropertyByteArray(propertyName, value);
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm
    public FrameworkMediaCrypto createMediaCrypto(UUID uuid, byte[] initData) throws MediaCryptoException {
        return new FrameworkMediaCrypto(new MediaCrypto(uuid, initData));
    }
}
