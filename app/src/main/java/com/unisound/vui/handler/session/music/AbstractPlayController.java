package com.unisound.vui.handler.session.music;

import android.content.Context;
import android.media.AudioManager;
import com.unisound.vui.util.LogMgr;

/* loaded from: classes.dex */
public abstract class AbstractPlayController implements PlayController, AudioManager.OnAudioFocusChangeListener {
    static final int AUDIO_FOCUSED = 2;
    static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    private static final String TAG = AbstractPlayController.class.getSimpleName();
    int audioFocus = 0;
    private AudioManager audioManager;
    boolean playOnFocusGain;

    public AbstractPlayController(Context context) {
        this.audioManager = (AudioManager) context.getSystemService("audio");
    }

    @Override // android.media.AudioManager.OnAudioFocusChangeListener
    public void onAudioFocusChange(int focusChange) {
        LogMgr.d(TAG, "onAudioFocusChange. focusChange=" + focusChange);
        if (focusChange == 1) {
            this.audioFocus = 2;
        } else if (focusChange == -1 || focusChange == -2 || focusChange == -3) {
            boolean canDuck = focusChange == -3;
            this.audioFocus = canDuck ? 1 : 0;
            if (isPlaying()) {
                this.playOnFocusGain = true;
                LogMgr.d(TAG, "onAudioFocusChange playOnFocusGain is true");
            } else {
                this.playOnFocusGain = false;
                LogMgr.d(TAG, "onAudioFocusChange playOnFocusGain is false");
            }
        } else {
            LogMgr.d(TAG, "onAudioFocusChange: Ignoring unsupported focusChange: ", Integer.valueOf(focusChange));
        }
        if (isPrepared()) {
            configMediaPlayerState();
        }
    }

    public void giveUpAudioFocus() {
        LogMgr.d(TAG, "giveUpAudioFocus");
        if (this.audioFocus == 2 && this.audioManager.abandonAudioFocus(this) == 1) {
            this.audioFocus = 0;
        }
    }

    public void tryToGetAudioFocus() {
        LogMgr.d(TAG, "tryToGetAudioFocus");
        if (this.audioFocus != 2) {
            int result = this.audioManager.requestAudioFocus(this, 4, 1);
            if (result == 1) {
                this.audioFocus = 2;
            }
        }
    }
}
