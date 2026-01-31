package com.unisound.vui.common.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import com.unisound.vui.util.LogMgr;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class UniMediaPlayer {
    private static final String TAG = "UniMediaPlayer";
    public static UniMediaPlayer uniMediaPlayer;
    private Context appContent;
    private AudioManager mAudioManager;
    private String playTag;
    private int mMediaPlayerState = 0;
    private List<IMediaPlayerStateListener> mediaPlayerStateListenerList = new CopyOnWriteArrayList();
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() { // from class: com.unisound.vui.common.media.UniMediaPlayer.15
        @Override // android.media.AudioManager.OnAudioFocusChangeListener
        public void onAudioFocusChange(int focusChange) throws IllegalStateException {
            LogMgr.d(UniMediaPlayer.TAG, "onAudioFocusChange focusChange = " + focusChange);
            if (focusChange == -2) {
                UniMediaPlayer.this.stop();
            } else {
                if (focusChange == 1 || focusChange != -1) {
                    return;
                }
                UniMediaPlayer.this.mAudioManager.abandonAudioFocus(UniMediaPlayer.this.mAudioFocusChangeListener);
                UniMediaPlayer.this.stop();
            }
        }
    };
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    private UniMediaPlayer(Context context) {
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        this.appContent = context;
        LogMgr.d(TAG, "new MediaPlayer()");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callBackPlayerState(int state) {
        LogMgr.d(TAG, "callBackPlayerState State : " + state);
        this.mMediaPlayerState = state;
        if (this.mediaPlayerStateListenerList != null) {
            Iterator<IMediaPlayerStateListener> it = this.mediaPlayerStateListenerList.iterator();
            while (it.hasNext()) {
                it.next().onPlayerState(state, this.playTag);
            }
            if (state == -1001 || state == 3 || state == 4) {
                this.playTag = null;
            }
        }
    }

    public static UniMediaPlayer getInstance() {
        if (uniMediaPlayer == null) {
            throw new RuntimeException("UniMediaPlayer need init first");
        }
        return uniMediaPlayer;
    }

    public static void init(Context context) {
        uniMediaPlayer = new UniMediaPlayer(context);
    }

    private boolean requestFocus() {
        return this.mAudioManager.requestAudioFocus(this.mAudioFocusChangeListener, 3, 1) == 1;
    }

    public void addIMediaPlayerStateListener(IMediaPlayerStateListener listener) {
        if (listener == null || this.mediaPlayerStateListenerList.contains(listener)) {
            return;
        }
        this.mediaPlayerStateListenerList.add(listener);
    }

    public String getPlayTag() {
        return this.playTag;
    }

    public boolean isPlayFile(String file) {
        return this.playTag != null && this.playTag.equals(file) && this.mMediaPlayerState == 1;
    }

    public boolean isPlaying() {
        if (this.mMediaPlayer != null) {
            return this.mMediaPlayer.isPlaying();
        }
        return false;
    }

    public void pause() throws IllegalStateException {
        LogMgr.d(TAG, "puase mMediaPlayerState : 2");
        if (this.mMediaPlayer == null || this.mMediaPlayerState != 1) {
            return;
        }
        this.mMediaPlayer.pause();
        callBackPlayerState(2);
    }

    public void pausePushMusic() throws IllegalStateException {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.pause();
        }
    }

    public void play(String path, IMediaPlayerStateListener listener) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        callBackPlayerState(3);
        this.playTag = new String(path);
        addIMediaPlayerStateListener(listener);
        this.mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.unisound.vui.common.media.UniMediaPlayer.1
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mp) {
                LogMgr.d(UniMediaPlayer.TAG, "onCompletion");
                UniMediaPlayer.this.callBackPlayerState(4);
                UniMediaPlayer.this.mAudioManager.abandonAudioFocus(UniMediaPlayer.this.mAudioFocusChangeListener);
            }
        });
        this.mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.unisound.vui.common.media.UniMediaPlayer.2
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogMgr.d(UniMediaPlayer.TAG, "onError");
                UniMediaPlayer.this.callBackPlayerState(PlayerState.MPS_ERROR);
                return false;
            }
        });
        File file = new File(path);
        if (!requestFocus() || !file.exists() || file.length() <= 0 || this.mMediaPlayer == null) {
            return;
        }
        if (this.mMediaPlayerState == 2) {
            LogMgr.d(TAG, "mMediaPlayerState : 2");
            this.mMediaPlayer.start();
            callBackPlayerState(1);
            return;
        }
        this.mMediaPlayer.stop();
        try {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.setDataSource(path);
            this.mMediaPlayer.setAudioStreamType(3);
            this.mMediaPlayer.prepareAsync();
            this.mMediaPlayer.setVolume(1.0f, 1.0f);
            this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.unisound.vui.common.media.UniMediaPlayer.3
                @Override // android.media.MediaPlayer.OnPreparedListener
                public void onPrepared(MediaPlayer mp) throws IllegalStateException {
                    LogMgr.d(UniMediaPlayer.TAG, "onPrepared");
                    UniMediaPlayer.this.mMediaPlayer.start();
                    UniMediaPlayer.this.callBackPlayerState(1);
                }
            });
        } catch (Exception e) {
            LogMgr.e(TAG, "paly error = " + e.toString());
            callBackPlayerState(4);
        }
    }

    public void playBeepSound(int rawId) throws IllegalStateException, Resources.NotFoundException, IOException {
        playBeepSound("", rawId, null, false);
    }

    public void playBeepSound(String tag, int rawId, final IMediaPlayerStateListener stateListener, boolean isLooping) throws IllegalStateException, Resources.NotFoundException, IOException {
        this.playTag = new String(tag);
        LogMgr.d(TAG, "---->>playBeepSound isLooping = " + isLooping);
        stop();
        if (stateListener != null) {
            addIMediaPlayerStateListener(stateListener);
        } else {
            LogMgr.d(TAG, "---->>playBeepSound stateListener is null");
        }
        AssetFileDescriptor assetFileDescriptorOpenRawResourceFd = this.appContent.getResources().openRawResourceFd(rawId);
        if (this.mMediaPlayer == null) {
            throw new RuntimeException("MediaPlayer has been released.");
        }
        this.mMediaPlayer.setOnCompletionListener(null);
        if (this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.stop();
        }
        this.mMediaPlayer.reset();
        this.mMediaPlayer.setLooping(isLooping);
        try {
            try {
                this.mMediaPlayer.setDataSource(assetFileDescriptorOpenRawResourceFd.getFileDescriptor(), assetFileDescriptorOpenRawResourceFd.getStartOffset(), assetFileDescriptorOpenRawResourceFd.getLength());
                this.mMediaPlayer.setAudioStreamType(3);
                this.mMediaPlayer.prepareAsync();
                this.mMediaPlayer.setVolume(1.0f, 1.0f);
                this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.unisound.vui.common.media.UniMediaPlayer.13
                    @Override // android.media.MediaPlayer.OnPreparedListener
                    public void onPrepared(MediaPlayer mp) throws IllegalStateException {
                        LogMgr.d(UniMediaPlayer.TAG, "---->>playBeepSound onPrepared mMediaPlayer.isPlaying()=" + UniMediaPlayer.this.mMediaPlayer.isPlaying());
                        if (stateListener != null) {
                            UniMediaPlayer.this.callBackPlayerState(1);
                        }
                        if (UniMediaPlayer.this.mMediaPlayer.isPlaying()) {
                            return;
                        }
                        UniMediaPlayer.this.mMediaPlayer.start();
                    }
                });
                this.mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.unisound.vui.common.media.UniMediaPlayer.14
                    @Override // android.media.MediaPlayer.OnCompletionListener
                    public void onCompletion(MediaPlayer mp) {
                        LogMgr.d(UniMediaPlayer.TAG, "---->>playBeepSound onCompletion");
                        if (stateListener != null) {
                            UniMediaPlayer.this.callBackPlayerState(4);
                        }
                    }
                });
            } finally {
                try {
                    assetFileDescriptorOpenRawResourceFd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            LogMgr.e(TAG, "--->>playBeepSound has a exception");
            try {
                assetFileDescriptorOpenRawResourceFd.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        LogMgr.d(TAG, "---->>playBeepSound end");
    }

    public void playBeepSoundLooping(String tag, int rawId, IMediaPlayerStateListener stateListener) throws IllegalStateException, Resources.NotFoundException, IOException {
        playBeepSound(tag, rawId, stateListener, true);
    }

    public void playUrl(String videoUrl, String tag) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        callBackPlayerState(3);
        this.playTag = new String(tag);
        this.mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.unisound.vui.common.media.UniMediaPlayer.4
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mp) {
                LogMgr.d(UniMediaPlayer.TAG, "onCompletion");
                UniMediaPlayer.this.callBackPlayerState(4);
                UniMediaPlayer.this.mAudioManager.abandonAudioFocus(UniMediaPlayer.this.mAudioFocusChangeListener);
            }
        });
        this.mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.unisound.vui.common.media.UniMediaPlayer.5
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogMgr.d(UniMediaPlayer.TAG, "onError");
                UniMediaPlayer.this.callBackPlayerState(PlayerState.MPS_ERROR);
                return false;
            }
        });
        if (!requestFocus() || this.mMediaPlayer == null) {
            return;
        }
        LogMgr.d(TAG, "111 playUrl mMediaPlayerState : " + this.mMediaPlayerState);
        if (this.mMediaPlayerState == 2) {
            this.mMediaPlayer.start();
            callBackPlayerState(1);
            return;
        }
        this.mMediaPlayer.stop();
        try {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.setDataSource(videoUrl);
            this.mMediaPlayer.setAudioStreamType(3);
            this.mMediaPlayer.prepareAsync();
            this.mMediaPlayer.setVolume(1.0f, 1.0f);
            this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.unisound.vui.common.media.UniMediaPlayer.6
                @Override // android.media.MediaPlayer.OnPreparedListener
                public void onPrepared(MediaPlayer mp) throws IllegalStateException {
                    LogMgr.d(UniMediaPlayer.TAG, "onPrepared");
                    UniMediaPlayer.this.mMediaPlayer.start();
                    UniMediaPlayer.this.callBackPlayerState(1);
                }
            });
        } catch (Exception e) {
            LogMgr.e(TAG, "paly error = " + e.toString());
            callBackPlayerState(4);
        }
    }

    public void playUrl(String videoUrl, String tag, IMediaPlayerStateListener listener) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        this.playTag = new String(tag);
        addIMediaPlayerStateListener(listener);
        this.mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.unisound.vui.common.media.UniMediaPlayer.7
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mp) {
                LogMgr.d(UniMediaPlayer.TAG, "onCompletion");
                UniMediaPlayer.this.callBackPlayerState(4);
                UniMediaPlayer.this.mAudioManager.abandonAudioFocus(UniMediaPlayer.this.mAudioFocusChangeListener);
            }
        });
        this.mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.unisound.vui.common.media.UniMediaPlayer.8
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogMgr.d(UniMediaPlayer.TAG, "onError");
                UniMediaPlayer.this.callBackPlayerState(PlayerState.MPS_ERROR);
                return false;
            }
        });
        if (!requestFocus() || this.mMediaPlayer == null) {
            return;
        }
        LogMgr.d(TAG, "222 playUrl mMediaPlayerState : " + this.mMediaPlayerState);
        if (this.mMediaPlayerState == 2) {
            this.mMediaPlayer.start();
            callBackPlayerState(1);
            return;
        }
        this.mMediaPlayer.stop();
        try {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.setDataSource(videoUrl);
            this.mMediaPlayer.setAudioStreamType(3);
            this.mMediaPlayer.prepareAsync();
            this.mMediaPlayer.setVolume(1.0f, 1.0f);
            this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.unisound.vui.common.media.UniMediaPlayer.9
                @Override // android.media.MediaPlayer.OnPreparedListener
                public void onPrepared(MediaPlayer mp) throws IllegalStateException {
                    LogMgr.d(UniMediaPlayer.TAG, "onPrepared");
                    UniMediaPlayer.this.mMediaPlayer.start();
                    UniMediaPlayer.this.callBackPlayerState(1);
                }
            });
        } catch (Exception e) {
            LogMgr.e(TAG, "paly error = " + e.toString());
            callBackPlayerState(4);
        }
    }

    public void playUrl(String videoUrl, String tag, IMediaPlayerStateListener listener, final boolean isChatSession) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        LogMgr.d(TAG, "playUrl videoUrl : " + videoUrl + " tag=" + tag + "  isChatSession=" + isChatSession);
        callBackPlayerState(3);
        this.playTag = "";
        this.playTag = new String(tag);
        addIMediaPlayerStateListener(listener);
        this.mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.unisound.vui.common.media.UniMediaPlayer.10
            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mp) {
                LogMgr.d(UniMediaPlayer.TAG, "onCompletion");
                UniMediaPlayer.this.callBackPlayerState(4);
                UniMediaPlayer.this.mAudioManager.abandonAudioFocus(UniMediaPlayer.this.mAudioFocusChangeListener);
            }
        });
        this.mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.unisound.vui.common.media.UniMediaPlayer.11
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogMgr.d(UniMediaPlayer.TAG, "onError what:" + what + ",extra:" + extra);
                UniMediaPlayer.this.callBackPlayerState(PlayerState.MPS_ERROR);
                return false;
            }
        });
        LogMgr.d(TAG, "mMediaPlayerState : " + this.mMediaPlayerState);
        if (!requestFocus() || this.mMediaPlayer == null) {
            return;
        }
        if (this.mMediaPlayerState == 2) {
            this.mMediaPlayer.start();
            callBackPlayerState(1);
            return;
        }
        this.mMediaPlayer.stop();
        try {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.setDataSource(videoUrl);
            this.mMediaPlayer.setAudioStreamType(3);
            this.mMediaPlayer.prepareAsync();
            this.mMediaPlayer.setVolume(1.0f, 1.0f);
            this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.unisound.vui.common.media.UniMediaPlayer.12
                @Override // android.media.MediaPlayer.OnPreparedListener
                public void onPrepared(MediaPlayer mp) throws IllegalStateException {
                    LogMgr.d(UniMediaPlayer.TAG, "onPrepared");
                    if (isChatSession) {
                        UniMediaPlayer.this.callBackPlayerState(5);
                    } else {
                        UniMediaPlayer.this.mMediaPlayer.start();
                        UniMediaPlayer.this.callBackPlayerState(1);
                    }
                }
            });
        } catch (Exception e) {
            LogMgr.e(TAG, "paly error = " + e.toString());
            callBackPlayerState(4);
        }
    }

    public void release() {
        LogMgr.d(TAG, "release mMediaPlayerState : " + this.mMediaPlayerState);
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            callBackPlayerState(0);
            this.mAudioManager.abandonAudioFocus(this.mAudioFocusChangeListener);
            this.mMediaPlayer = null;
        }
    }

    public void removeIMediaPlayerStateListener(IMediaPlayerStateListener listener) {
        if (listener == null || !this.mediaPlayerStateListenerList.contains(listener)) {
            return;
        }
        this.mediaPlayerStateListenerList.remove(listener);
    }

    public void resume() throws IllegalStateException {
        if (this.mMediaPlayer == null || this.mMediaPlayerState != 2) {
            return;
        }
        this.mMediaPlayer.start();
        callBackPlayerState(1);
    }

    public void resumePushMusic() throws IllegalStateException {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.start();
        }
    }

    public void start() throws IllegalStateException {
        LogMgr.d(TAG, "mMediaPlayer start  mMediaPlayerState: " + this.mMediaPlayerState);
        if (this.mMediaPlayer == null || this.mMediaPlayerState != 5) {
            return;
        }
        this.mMediaPlayer.start();
        callBackPlayerState(1);
    }

    public void stop() throws IllegalStateException {
        LogMgr.d(TAG, "stop mMediaPlayerState : " + this.mMediaPlayerState);
        if (this.mMediaPlayer == null || !this.mMediaPlayer.isPlaying()) {
            return;
        }
        this.mMediaPlayer.stop();
        callBackPlayerState(3);
    }
}
