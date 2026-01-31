package android.os;

/* loaded from: classes.dex */
public interface ISysPrivate extends IInterface {
    int getSysMode() throws RemoteException;

    String readFileProp(String str, String str2) throws RemoteException;

    String readLevelProp(int i, String str) throws RemoteException;

    String readProp(String str) throws RemoteException;

    int setSysMode(int i) throws RemoteException;

    void writeFileProp(String str, String str2, String str3) throws RemoteException;

    void writeLevelProp(int i, String str, String str2) throws RemoteException;

    void writeProp(String str, String str2) throws RemoteException;

    public static abstract class Stub extends Binder implements ISysPrivate {
        private static final String DESCRIPTOR = "android.os.ISysPrivate";
        static final int TRANSACTION_getSysMode = 2;
        static final int TRANSACTION_readFileProp = 8;
        static final int TRANSACTION_readLevelProp = 10;
        static final int TRANSACTION_readProp = 6;
        static final int TRANSACTION_setSysMode = 1;
        static final int TRANSACTION_writeFileProp = 7;
        static final int TRANSACTION_writeLevelProp = 9;
        static final int TRANSACTION_writeProp = 5;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISysPrivate asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISysPrivate)) {
                return (ISysPrivate) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    int _result = setSysMode(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _result2 = getSysMode();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    String _arg1 = data.readString();
                    writeProp(_arg02, _arg1);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    String _result3 = readProp(_arg03);
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    String _arg12 = data.readString();
                    String _arg2 = data.readString();
                    writeFileProp(_arg04, _arg12, _arg2);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    String _arg13 = data.readString();
                    String _result4 = readFileProp(_arg05, _arg13);
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    String _arg14 = data.readString();
                    String _arg22 = data.readString();
                    writeLevelProp(_arg06, _arg14, _arg22);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    String _arg15 = data.readString();
                    String _result5 = readLevelProp(_arg07, _arg15);
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements ISysPrivate {
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

            @Override // android.os.ISysPrivate
            public int setSysMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.ISysPrivate
            public int getSysMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.ISysPrivate
            public void writeProp(String key, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(value);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.ISysPrivate
            public String readProp(String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.ISysPrivate
            public void writeFileProp(String file, String key, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(file);
                    _data.writeString(key);
                    _data.writeString(value);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.ISysPrivate
            public String readFileProp(String file, String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(file);
                    _data.writeString(key);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.ISysPrivate
            public void writeLevelProp(int secLevel, String key, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(secLevel);
                    _data.writeString(key);
                    _data.writeString(value);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.ISysPrivate
            public String readLevelProp(int secLevel, String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(secLevel);
                    _data.writeString(key);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
