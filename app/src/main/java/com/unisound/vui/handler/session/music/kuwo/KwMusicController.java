package com.unisound.vui.handler.session.music.kuwo;

import android.content.Context;
import cn.kuwo.autosdk.api.KWAPI;
import cn.kuwo.autosdk.api.PlayMode;
import cn.kuwo.autosdk.api.PlayState;
import com.kaolafm.sdk.client.KLClientAPI;
import com.unisound.vui.handler.session.music.impl.IMusicController;
import java.util.List;
import nluparser.scheme.MusicResult;

/* loaded from: classes.dex */
public class KwMusicController implements IMusicController {
    private Context context;
    private KWAPI kwApi;

    public KwMusicController(Context context) {
        this.context = context;
        this.kwApi = KWAPI.createKWAPI(context, KLClientAPI.KEY_AUTO);
    }

    @Override // com.unisound.vui.handler.session.music.impl.IMusicController
    public void playRandomMusic() {
        this.kwApi.startAPP(this.context, true);
    }

    @Override // com.unisound.vui.handler.session.music.impl.IMusicController
    public void playSpecifiedMusic(String song, String singer, String album) {
        this.kwApi.playClientMusics(this.context, song, singer, album);
    }

    @Override // com.unisound.vui.handler.session.music.impl.IMusicController
    public void playSpecifiedMusic(List<MusicResult.Music> musicList) {
    }

    @Override // com.unisound.vui.handler.session.music.impl.IMusicController
    public void playNext() {
        this.kwApi.setPlayState(this.context, PlayState.STATE_NEXT);
    }

    @Override // com.unisound.vui.handler.session.music.impl.IMusicController
    public void playPrev() {
        this.kwApi.setPlayState(this.context, PlayState.STATE_PRE);
    }

    @Override // com.unisound.vui.handler.session.music.impl.IMusicController
    public void setPlayMode(MusicPlayMode playMode) {
        switch (playMode) {
            case MODE_ALL_CIRCLE:
                this.kwApi.setPlayMode(this.context, PlayMode.MODE_ALL_CIRCLE);
                break;
            case MODE_SINGLE_CIRCLE:
                this.kwApi.setPlayMode(this.context, PlayMode.MODE_SINGLE_CIRCLE);
                break;
            case MODE_SINGLE_PLAY:
                this.kwApi.setPlayMode(this.context, PlayMode.MODE_SINGLE_PLAY);
                break;
            case MODE_ALL_ORDER:
                this.kwApi.setPlayMode(this.context, PlayMode.MODE_ALL_ORDER);
                break;
            case MODE_ALL_RANDOM:
                this.kwApi.setPlayMode(this.context, PlayMode.MODE_ALL_RANDOM);
                break;
        }
    }

    @Override // com.unisound.vui.handler.session.music.impl.IMusicController
    public void play() {
        playRandomMusic();
    }

    @Override // com.unisound.vui.handler.session.music.impl.IMusicController
    public void pause() {
        this.kwApi.setPlayState(this.context, PlayState.STATE_PAUSE);
    }

    @Override // com.unisound.vui.handler.session.music.impl.IMusicController
    public void exit() {
        this.kwApi.exitAPP(this.context);
    }
}
