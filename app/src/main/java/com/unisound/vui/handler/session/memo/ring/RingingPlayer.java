package com.unisound.vui.handler.session.memo.ring;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import com.unisound.vui.handler.session.memo.utils.MemoConstants;
import com.unisound.vui.util.LogMgr;
import java.io.IOException;

/* loaded from: classes.dex */
public class RingingPlayer {
    private static final String TAG = MemoConstants.MEMO_TAG + RingingPlayer.class.getSimpleName();
    private Context mContext;
    private MediaPlayer mPlayer = new MediaPlayer();
    private MediaPlayer.OnCompletionListener onCompletionListener;

    public RingingPlayer(Context context) {
        this.mContext = context;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        this.onCompletionListener = listener;
    }

    public void play(Uri toneUri, boolean isLooping) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        try {
            if (this.mPlayer != null && !this.mPlayer.isPlaying()) {
                this.mPlayer.reset();
                this.mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.unisound.vui.handler.session.memo.ring.RingingPlayer.1
                    @Override // android.media.MediaPlayer.OnPreparedListener
                    public void onPrepared(MediaPlayer mp) throws IllegalStateException {
                        LogMgr.d(RingingPlayer.TAG, "play start");
                        mp.start();
                    }
                });
                this.mPlayer.setOnCompletionListener(this.onCompletionListener);
                LogMgr.d(TAG, " memoRinging play uri:" + toneUri);
                this.mPlayer.setDataSource(this.mContext, toneUri);
                this.mPlayer.setAudioStreamType(4);
                this.mPlayer.setLooping(isLooping);
                this.mPlayer.prepareAsync();
            }
        } catch (Exception e) {
            LogMgr.e(TAG, TAG, e);
        }
    }

    public void play(int rawId, boolean looping) throws Resources.NotFoundException, IOException {
        play(rawId, looping, null);
    }

    public void play(int rawId, boolean looping, MediaPlayer.OnCompletionListener completeCallback) throws Resources.NotFoundException, IOException {
        this.onCompletionListener = completeCallback;
        AssetFileDescriptor file = this.mContext.getResources().openRawResourceFd(rawId);
        if (this.mPlayer == null) {
            throw new RuntimeException("MediaPlayer has been released.");
        }
        if (this.onCompletionListener != null) {
            this.mPlayer.setOnCompletionListener(this.onCompletionListener);
        }
        this.mPlayer.reset();
        try {
            try {
                this.mPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                this.mPlayer.setAudioStreamType(4);
                this.mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.unisound.vui.handler.session.memo.ring.RingingPlayer.2
                    @Override // android.media.MediaPlayer.OnPreparedListener
                    public void onPrepared(MediaPlayer mp) throws IllegalStateException {
                        LogMgr.d(RingingPlayer.TAG, "play start");
                        mp.start();
                    }
                });
                this.mPlayer.setLooping(looping);
                this.mPlayer.setVolume(0.5f, 0.5f);
                this.mPlayer.prepareAsync();
            } catch (Exception e) {
                LogMgr.e(TAG, e.toString());
                try {
                    file.close();
                } catch (IOException e2) {
                    LogMgr.e(TAG, e2.toString());
                }
            }
        } finally {
            try {
                file.close();
            } catch (IOException e3) {
                LogMgr.e(TAG, e3.toString());
            }
        }
    }

    public void stop() throws IllegalStateException {
        if (this.mPlayer != null) {
            if (this.mPlayer.isPlaying()) {
                this.mPlayer.stop();
            }
            this.mPlayer.reset();
        }
    }
}
