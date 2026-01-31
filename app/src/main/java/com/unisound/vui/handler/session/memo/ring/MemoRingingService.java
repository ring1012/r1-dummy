package com.unisound.vui.handler.session.memo.ring;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import com.unisound.vui.handler.session.memo.utils.MemoConstants;
import com.unisound.vui.handler.session.memo.utils.RingingUtils;
import com.unisound.vui.util.LogMgr;
import java.io.File;
import java.io.IOException;

/* loaded from: classes.dex */
public class MemoRingingService extends Service {
    public final String TAG = MemoConstants.MEMO_TAG + getClass().getSimpleName();
    private final IBinder mBinder = new LocalBinder();
    private RingingPlayer mRingPlayer;

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        LogMgr.d(this.TAG, "MemoRingingService created!");
        this.mRingPlayer = new RingingPlayer(this);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        LogMgr.d(this.TAG, "MemoRingingService onBind");
        return this.mBinder;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        LogMgr.d(this.TAG, "MemoRingingService onUnbind");
        return super.onUnbind(intent);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        LogMgr.d(this.TAG, "onCreate destroyed!");
    }

    public void stopPlayRing() throws IllegalStateException {
        if (this.mRingPlayer != null) {
            this.mRingPlayer.stop();
        }
    }

    public void startPlayingRing(boolean isLooping, String ringName) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        LogMgr.d(this.TAG, "startPlayingRing, ringName:" + ringName);
        File ringing = RingingUtils.getRingingFile(ringName, RingingUtils.RINGING_DEFAULT);
        Uri uri = Uri.fromFile(ringing);
        this.mRingPlayer.play(uri, isLooping);
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public MemoRingingService getService() {
            return MemoRingingService.this;
        }
    }
}
