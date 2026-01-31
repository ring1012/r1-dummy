package com.kaolafm.sdk.client;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface PlayListener extends IInterface {
    void onCompleted() throws RemoteException;

    void onError(ErrorInfo errorInfo) throws RemoteException;

    void onPause() throws RemoteException;

    void onPlayMusic(Music music) throws RemoteException;

    void onPlaying() throws RemoteException;

    void onProgress(long j) throws RemoteException;

    void onStartPrepare() throws RemoteException;

    public static abstract class Stub extends Binder implements PlayListener {
        private static final String DESCRIPTOR = "com.kaolafm.sdk.client.PlayListener";
        static final int TRANSACTION_onCompleted = 6;
        static final int TRANSACTION_onError = 7;
        static final int TRANSACTION_onPause = 4;
        static final int TRANSACTION_onPlayMusic = 5;
        static final int TRANSACTION_onPlaying = 2;
        static final int TRANSACTION_onProgress = 3;
        static final int TRANSACTION_onStartPrepare = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static PlayListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof PlayListener)) {
                return (PlayListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ErrorInfo _arg0;
            Music _arg02;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onStartPrepare();
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onPlaying();
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg03 = data.readLong();
                    onProgress(_arg03);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    onPause();
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = Music.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    onPlayMusic(_arg02);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    onCompleted();
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = ErrorInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    onError(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements PlayListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.kaolafm.sdk.client.PlayListener
            public void onStartPrepare() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.PlayListener
            public void onPlaying() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.PlayListener
            public void onProgress(long progress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(progress);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.PlayListener
            public void onPause() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.PlayListener
            public void onPlayMusic(Music music) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (music != null) {
                        _data.writeInt(1);
                        music.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.PlayListener
            public void onCompleted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.PlayListener
            public void onError(ErrorInfo errInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (errInfo != null) {
                        _data.writeInt(1);
                        errInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
