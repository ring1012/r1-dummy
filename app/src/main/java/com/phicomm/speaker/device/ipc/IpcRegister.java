package com.phicomm.speaker.device.ipc;

/* loaded from: classes.dex */
public interface IpcRegister {
    void registerReceiver(IpcReceiver ipcReceiver, int i);

    void unRegisterReceiver(IpcReceiver ipcReceiver);
}
