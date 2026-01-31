package com.unisound.vui.transport;

import android.os.IInterface;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IDataStreamAIDL extends IInterface {
    DataStream getDataStream() throws RemoteException;

    void startDataTransport() throws RemoteException;

    void stopDataTransport() throws RemoteException;

    void useStereo(boolean z) throws RemoteException;
}
