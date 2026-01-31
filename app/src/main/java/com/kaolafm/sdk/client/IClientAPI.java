package com.kaolafm.sdk.client;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.kaolafm.sdk.client.IPlayResult;
import com.kaolafm.sdk.client.IRadioResult;
import com.kaolafm.sdk.client.ISearchResult;
import com.kaolafm.sdk.client.ISearchResultV2;
import com.kaolafm.sdk.client.PlayListener;
import java.util.List;

/* loaded from: classes.dex */
public interface IClientAPI extends IInterface {
    void back() throws RemoteException;

    void backward() throws RemoteException;

    void download() throws RemoteException;

    void exitApp() throws RemoteException;

    void forward() throws RemoteException;

    Music getCurrentMusicInfo() throws RemoteException;

    int getPlayState() throws RemoteException;

    long getProgress() throws RemoteException;

    void getRadioList(IRadioResult iRadioResult) throws RemoteException;

    boolean hasNext() throws RemoteException;

    boolean hasPre() throws RemoteException;

    void launchApp(boolean z) throws RemoteException;

    void pause() throws RemoteException;

    void play() throws RemoteException;

    void playByKeywords(String str, IPlayResult iPlayResult) throws RemoteException;

    void playMusic(Music music) throws RemoteException;

    void playMusicList(List<Music> list, int i) throws RemoteException;

    void playNext() throws RemoteException;

    void playPre() throws RemoteException;

    void playSearchData(SearchData searchData) throws RemoteException;

    void search(String str, ISearchResult iSearchResult) throws RemoteException;

    void searchByType(int i, String str, int i2, int i3, ISearchResultV2 iSearchResultV2) throws RemoteException;

    void setPlayListener(PlayListener playListener) throws RemoteException;

    void showSearchResult(List<Music> list, String str) throws RemoteException;

    void switchChannel(Radio radio) throws RemoteException;

    public static abstract class Stub extends Binder implements IClientAPI {
        private static final String DESCRIPTOR = "com.kaolafm.sdk.client.IClientAPI";
        static final int TRANSACTION_back = 17;
        static final int TRANSACTION_backward = 14;
        static final int TRANSACTION_download = 18;
        static final int TRANSACTION_exitApp = 2;
        static final int TRANSACTION_forward = 13;
        static final int TRANSACTION_getCurrentMusicInfo = 12;
        static final int TRANSACTION_getPlayState = 10;
        static final int TRANSACTION_getProgress = 11;
        static final int TRANSACTION_getRadioList = 19;
        static final int TRANSACTION_hasNext = 24;
        static final int TRANSACTION_hasPre = 23;
        static final int TRANSACTION_launchApp = 1;
        static final int TRANSACTION_pause = 6;
        static final int TRANSACTION_play = 3;
        static final int TRANSACTION_playByKeywords = 25;
        static final int TRANSACTION_playMusic = 4;
        static final int TRANSACTION_playMusicList = 5;
        static final int TRANSACTION_playNext = 7;
        static final int TRANSACTION_playPre = 8;
        static final int TRANSACTION_playSearchData = 21;
        static final int TRANSACTION_search = 15;
        static final int TRANSACTION_searchByType = 22;
        static final int TRANSACTION_setPlayListener = 9;
        static final int TRANSACTION_showSearchResult = 16;
        static final int TRANSACTION_switchChannel = 20;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IClientAPI asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IClientAPI)) {
                return (IClientAPI) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            SearchData searchDataCreateFromParcel;
            Radio radioCreateFromParcel;
            Music musicCreateFromParcel;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    launchApp(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    exitApp();
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    play();
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        musicCreateFromParcel = Music.CREATOR.createFromParcel(parcel);
                    } else {
                        musicCreateFromParcel = null;
                    }
                    playMusic(musicCreateFromParcel);
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    playMusicList(parcel.createTypedArrayList(Music.CREATOR), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    pause();
                    parcel2.writeNoException();
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    playNext();
                    parcel2.writeNoException();
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    playPre();
                    parcel2.writeNoException();
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    setPlayListener(PlayListener.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    int playState = getPlayState();
                    parcel2.writeNoException();
                    parcel2.writeInt(playState);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    long progress = getProgress();
                    parcel2.writeNoException();
                    parcel2.writeLong(progress);
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    Music currentMusicInfo = getCurrentMusicInfo();
                    parcel2.writeNoException();
                    if (currentMusicInfo != null) {
                        parcel2.writeInt(1);
                        currentMusicInfo.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    forward();
                    parcel2.writeNoException();
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    backward();
                    parcel2.writeNoException();
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    search(parcel.readString(), ISearchResult.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    showSearchResult(parcel.createTypedArrayList(Music.CREATOR), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    back();
                    parcel2.writeNoException();
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    download();
                    parcel2.writeNoException();
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    getRadioList(IRadioResult.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 20:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        radioCreateFromParcel = Radio.CREATOR.createFromParcel(parcel);
                    } else {
                        radioCreateFromParcel = null;
                    }
                    switchChannel(radioCreateFromParcel);
                    parcel2.writeNoException();
                    return true;
                case 21:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        searchDataCreateFromParcel = SearchData.CREATOR.createFromParcel(parcel);
                    } else {
                        searchDataCreateFromParcel = null;
                    }
                    playSearchData(searchDataCreateFromParcel);
                    parcel2.writeNoException();
                    return true;
                case 22:
                    parcel.enforceInterface(DESCRIPTOR);
                    searchByType(parcel.readInt(), parcel.readString(), parcel.readInt(), parcel.readInt(), ISearchResultV2.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 23:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zHasPre = hasPre();
                    parcel2.writeNoException();
                    parcel2.writeInt(zHasPre ? 1 : 0);
                    return true;
                case 24:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zHasNext = hasNext();
                    parcel2.writeNoException();
                    parcel2.writeInt(zHasNext ? 1 : 0);
                    return true;
                case 25:
                    parcel.enforceInterface(DESCRIPTOR);
                    playByKeywords(parcel.readString(), IPlayResult.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        private static class Proxy implements IClientAPI {
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

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void launchApp(boolean autoPlay) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(autoPlay ? 1 : 0);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void exitApp() throws RemoteException {
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

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void play() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void playMusic(Music music) throws RemoteException {
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
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void playMusicList(List<Music> playList, int index) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(playList);
                    _data.writeInt(index);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void pause() throws RemoteException {
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

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void playNext() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void playPre() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void setPlayListener(PlayListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public int getPlayState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public long getProgress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public Music getCurrentMusicInfo() throws RemoteException {
                Music _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Music.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void forward() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void backward() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void search(String keyword, ISearchResult result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(keyword);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void showSearchResult(List<Music> music, String keyword) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(music);
                    _data.writeString(keyword);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void back() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void download() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void getRadioList(IRadioResult result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void switchChannel(Radio radio) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (radio != null) {
                        _data.writeInt(1);
                        radio.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void playSearchData(SearchData searchData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (searchData != null) {
                        _data.writeInt(1);
                        searchData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void searchByType(int type, String keywords, int page, int count, ISearchResultV2 result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(keywords);
                    _data.writeInt(page);
                    _data.writeInt(count);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public boolean hasPre() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public boolean hasNext() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.kaolafm.sdk.client.IClientAPI
            public void playByKeywords(String keywords, IPlayResult result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(keywords);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
