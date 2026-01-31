package com.unisound.ant.device.listener;

/* loaded from: classes.dex */
public interface VoiceConnectStateListener {
    void onStartVoiceConnect();

    void onVoiceConnectFailEnd();

    void onVoiceConnectFailStart();

    void onVoiceConnectSuccess();
}
