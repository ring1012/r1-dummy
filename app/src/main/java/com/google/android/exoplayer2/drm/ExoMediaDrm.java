package com.google.android.exoplayer2.drm;

import android.media.DeniedByServerException;
import android.media.MediaCryptoException;
import android.media.NotProvisionedException;
import android.media.ResourceBusyException;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/* loaded from: classes.dex */
public interface ExoMediaDrm<T extends ExoMediaCrypto> {

    public interface KeyRequest {
        byte[] getData();

        String getDefaultUrl();
    }

    public interface OnEventListener<T extends ExoMediaCrypto> {
        void onEvent(ExoMediaDrm<? extends T> exoMediaDrm, byte[] bArr, int i, int i2, byte[] bArr2);
    }

    public interface ProvisionRequest {
        byte[] getData();

        String getDefaultUrl();
    }

    void closeSession(byte[] bArr);

    T createMediaCrypto(UUID uuid, byte[] bArr) throws MediaCryptoException;

    KeyRequest getKeyRequest(byte[] bArr, byte[] bArr2, String str, int i, HashMap<String, String> map) throws NotProvisionedException;

    byte[] getPropertyByteArray(String str);

    String getPropertyString(String str);

    ProvisionRequest getProvisionRequest();

    byte[] openSession() throws ResourceBusyException, NotProvisionedException;

    byte[] provideKeyResponse(byte[] bArr, byte[] bArr2) throws DeniedByServerException, NotProvisionedException;

    void provideProvisionResponse(byte[] bArr) throws DeniedByServerException;

    Map<String, String> queryKeyStatus(byte[] bArr);

    void release();

    void restoreKeys(byte[] bArr, byte[] bArr2);

    void setOnEventListener(OnEventListener<? super T> onEventListener);

    void setPropertyByteArray(String str, byte[] bArr);

    void setPropertyString(String str, String str2);
}
