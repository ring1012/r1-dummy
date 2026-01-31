package com.kaolafm.sdk.client;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.kaolafm.sdk.client.IClientAPI;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class KLClientAPI {
    public static final String ACTION_APP_EXIT = "com.edog.action.app.exit";
    public static final String ACTION_APP_LAUNCH = "com.edog.action.app.launch";
    public static final String ACTION_APP_SEARCH = "com.edog.action.app.search";
    public static final String ACTION_CLIENT = "com.kaolafm.sdk.client";
    public static final String ACTION_MUSIC_NEXT = "com.edog.action.music.next";
    public static final String ACTION_MUSIC_PAUSE = "com.edog.action.music.pause";
    public static final String ACTION_MUSIC_PLAY = "com.edog.action.music.play";
    public static final String ACTION_MUSIC_PRE = "com.edog.action.music.pre";
    public static final String EXTRA_IS_AUTOPLAY = "extra_is_autoplay";
    public static final String EXTRA_KEYWORD = "extra_keyword";
    public static final String EXTRA_KEY_EDOG_PAGE = "com.edog.car.start.page";
    public static final String EXTRA_MUSIC = "extra_music";
    public static final String EXTRA_PLAYLIST = "extra_playlist";
    public static final String KEY_AUTO = "auto";
    public static final String KEY_PHONE = "phone";
    private static final String PACKAGE_NAME_EDOG = "com.edog.car";
    private static final String PACKAGE_NAME_KAOLAFM = "com.itings.myradio";
    private static KLClientAPI mInstance = new KLClientAPI();
    private BroadcastReceiver exitReceiver = new BroadcastReceiver() { // from class: com.kaolafm.sdk.client.KLClientAPI.22
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            KLClientAPI.this.unBindService();
        }
    };
    private Context mContext;
    private String mKey;
    private String mPackageName;
    private IClientAPI mPlayerBinder;
    private ServiceConnection mServiceConn;
    private IServiceConnection mServiceConnection;
    private PlayStateListener playStateListener;

    private interface OnKaolaServiceConnectedListener {
        void onConnected();
    }

    private KLClientAPI() {
    }

    public static KLClientAPI getInstance() {
        return mInstance;
    }

    public void init(Context context, String key) {
        init(context, key, null);
    }

    private void init(Context context, String key, final OnKaolaServiceConnectedListener listener) {
        this.mContext = context;
        this.mKey = key;
        if ("phone".equals(key)) {
            this.mPackageName = PACKAGE_NAME_KAOLAFM;
        } else {
            this.mPackageName = PACKAGE_NAME_EDOG;
        }
        this.mServiceConn = new ServiceConnection() { // from class: com.kaolafm.sdk.client.KLClientAPI.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName name, IBinder service) {
                KLClientAPI.this.mPlayerBinder = IClientAPI.Stub.asInterface(service);
                KLClientAPI.this.registerExitReceiver();
                if (listener != null) {
                    listener.onConnected();
                }
                if (KLClientAPI.this.mServiceConnection != null) {
                    KLClientAPI.this.mServiceConnection.onServiceConnected(name);
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName name) {
                KLClientAPI.this.mPlayerBinder = null;
                KLClientAPI.this.unregisterExitReceiver();
                if (KLClientAPI.this.mServiceConnection != null) {
                    KLClientAPI.this.mServiceConnection.onServiceDisconnected(name);
                }
            }
        };
        bindService(key);
    }

    public void setServiceConnection(IServiceConnection s) {
        this.mServiceConnection = s;
    }

    private void bindService(String key) {
        Intent intent = new Intent("com.kaolafm.sdk.client");
        intent.setPackage(this.mPackageName);
        this.mContext.bindService(intent, this.mServiceConn, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unBindService() {
        if (this.mServiceConn != null) {
            this.mContext.unbindService(this.mServiceConn);
            this.mServiceConn = null;
            this.mPlayerBinder = null;
        }
    }

    public void launchApp(final boolean autoPlay) {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.2
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    KLClientAPI.this.mPlayerBinder.launchApp(autoPlay);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void exitApp() {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.3
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    KLClientAPI.this.mPlayerBinder.exitApp();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void play() {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.4
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    KLClientAPI.this.mPlayerBinder.play();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void pause() {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.5
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    KLClientAPI.this.mPlayerBinder.pause();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean hasNext() {
        try {
            if (this.mPlayerBinder != null) {
                return this.mPlayerBinder.hasNext();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void playNext() {
        playNext(null);
    }

    public void playNext(final PlayResult playResult) {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.6
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    if (KLClientAPI.this.mPlayerBinder.hasNext()) {
                        KLClientAPI.this.mPlayerBinder.playNext();
                        if (playResult != null) {
                            playResult.onSuccuss();
                        }
                    } else if (playResult != null) {
                        ErrorInfo info = new ErrorInfo(404);
                        info.info = "no next";
                        playResult.onFailure(info);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean hasPre() {
        try {
            if (this.mPlayerBinder != null) {
                return this.mPlayerBinder.hasPre();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void playPre() {
        playPre(null);
    }

    public void playPre(final PlayResult playResult) {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.7
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    if (KLClientAPI.this.mPlayerBinder.hasPre()) {
                        KLClientAPI.this.mPlayerBinder.playPre();
                        if (playResult != null) {
                            playResult.onSuccuss();
                        }
                    } else if (playResult != null) {
                        ErrorInfo info = new ErrorInfo(404);
                        info.info = "no pre";
                        playResult.onFailure(info);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public PlayState getPlayState() {
        if (this.mPlayerBinder != null) {
            try {
                int code = this.mPlayerBinder.getPlayState();
                if (code == PlayState.PLAYING.getCode()) {
                    return PlayState.PLAYING;
                }
                return PlayState.PAUSED;
            } catch (RemoteException e) {
                e.printStackTrace();
                return PlayState.PAUSED;
            }
        }
        return PlayState.PAUSED;
    }

    public long getProgress() {
        if (this.mPlayerBinder == null) {
            return 0L;
        }
        try {
            long result = this.mPlayerBinder.getProgress();
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public Music getCurrentMusicInfo() {
        if (this.mPlayerBinder == null) {
            return null;
        }
        try {
            Music result = this.mPlayerBinder.getCurrentMusicInfo();
            return result;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPlayListener(final PlayStateListener listener) {
        this.playStateListener = listener;
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.8
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    KLClientAPI.this.mPlayerBinder.setPlayListener(listener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (NullPointerException e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    public void search(String keyword) {
        search(keyword, null);
    }

    public void search(final String keyword, final SearchResult listener) {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.9
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    KLClientAPI.this.mPlayerBinder.search(keyword, listener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void searchAll(String keywords, SearchResultV2 listener) {
        searchByType(SearchType.ALL, keywords, 0, 0, listener);
    }

    public void searchByType(SearchType type, String keywords, SearchResultV2 listener) {
        searchByType(type, keywords, 1, 10, listener);
    }

    public void searchByType(final SearchType type, final String keywords, final int page, final int count, final SearchResultV2 listener) {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.10
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() throws PackageManager.NameNotFoundException {
                KLClientAPI.this.searchByType(type.value(), keywords, page, count, listener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void searchByType(int type, String keywords, int page, int count, final SearchResultV2 listener) throws PackageManager.NameNotFoundException {
        try {
            PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo(PACKAGE_NAME_EDOG, 0);
            if (packageInfo.versionCode >= 20301) {
                this.mPlayerBinder.searchByType(type, keywords, page, count, listener);
            } else {
                this.mPlayerBinder.search(keywords, new SearchResult() { // from class: com.kaolafm.sdk.client.KLClientAPI.11
                    @Override // com.kaolafm.sdk.client.ISearchResult
                    public void onSuccess(List<Music> musicList) throws RemoteException {
                        List<SearchData> searchDatas = new ArrayList<>();
                        if (musicList != null) {
                            for (Music music : musicList) {
                                SearchData searchData = new SearchData();
                                searchData.setId(music.audioId);
                                searchData.setName(music.audioName);
                                searchData.setImg(music.picUrl);
                                searchData.setType(SearchType.AUDIO.value());
                                searchDatas.add(searchData);
                            }
                            listener.onSuccess(searchDatas);
                        }
                    }

                    @Override // com.kaolafm.sdk.client.ISearchResult
                    public void onFailure(ErrorInfo errCode) throws RemoteException {
                        listener.onFailure(errCode);
                    }
                });
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (RemoteException e2) {
            e2.printStackTrace();
        }
    }

    public void play(@Nullable final Music music) {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.12
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    KLClientAPI.this.mPlayerBinder.playMusic(music);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void play(final List<Music> playList, final int index) {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.13
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    KLClientAPI.this.mPlayerBinder.playMusicList(playList, index);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void play(final SearchData searchData) {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.14
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() throws PackageManager.NameNotFoundException {
                if (searchData != null) {
                    try {
                        PackageInfo packageInfo = KLClientAPI.this.mContext.getPackageManager().getPackageInfo(KLClientAPI.PACKAGE_NAME_EDOG, 0);
                        if (packageInfo.versionCode >= 20301) {
                            KLClientAPI.this.mPlayerBinder.playSearchData(searchData);
                        } else {
                            Music music = new Music();
                            music.audioId = searchData.getId();
                            music.audioName = searchData.getName();
                            music.picUrl = searchData.getImg();
                            KLClientAPI.this.mPlayerBinder.playMusic(music);
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    } catch (RemoteException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
    }

    public void play(final String keywords, final PlayResult playResult) {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.15
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    KLClientAPI.this.mPlayerBinder.playByKeywords(keywords, playResult);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void download() {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.16
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    KLClientAPI.this.mPlayerBinder.download();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void forward() {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.17
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    KLClientAPI.this.mPlayerBinder.forward();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void backward() {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.18
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    KLClientAPI.this.mPlayerBinder.backward();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getRadioList(final RadioResult result) {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.19
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    KLClientAPI.this.mPlayerBinder.getRadioList(result);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void switchChannel() {
        switchChannel(null);
    }

    public void switchChannel(final Radio radio) {
        isConnectedWithRetry(new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.20
            @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
            public void onConnected() {
                try {
                    KLClientAPI.this.mPlayerBinder.switchChannel(radio);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onDestroy() {
        unBindService();
    }

    private void isConnectedWithRetry(final OnKaolaServiceConnectedListener listener) {
        if (this.mPlayerBinder == null) {
            if (this.mContext != null && !TextUtils.isEmpty(this.mKey)) {
                init(this.mContext, this.mKey, new OnKaolaServiceConnectedListener() { // from class: com.kaolafm.sdk.client.KLClientAPI.21
                    @Override // com.kaolafm.sdk.client.KLClientAPI.OnKaolaServiceConnectedListener
                    public void onConnected() {
                        if (KLClientAPI.this.playStateListener != null) {
                            try {
                                KLClientAPI.this.mPlayerBinder.setPlayListener(KLClientAPI.this.playStateListener);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e2) {
                                e2.printStackTrace();
                            }
                        }
                        listener.onConnected();
                    }
                });
                return;
            }
            return;
        }
        listener.onConnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerExitReceiver() {
        if (this.mContext != null) {
            this.mContext.registerReceiver(this.exitReceiver, new IntentFilter("com.kaolafm.auto.home.appExit.action"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterExitReceiver() {
        if (this.mContext != null) {
            try {
                this.mContext.unregisterReceiver(this.exitReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
