package com.unisound.ant.device.mqtt;

/* loaded from: classes.dex */
public interface ChannelListener {
    void onChannelClose();

    void onChannelConnected();

    void onChannelDisConnected();

    void onReceivedMsg(int i, String str);

    void onSendDataResult(int i, String str);
}
