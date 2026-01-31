package cn.kuwo.autosdk.api;

/* loaded from: classes.dex */
public enum PlayState {
    STATE_PRE { // from class: cn.kuwo.autosdk.api.PlayState.1
        @Override // cn.kuwo.autosdk.api.PlayState
        public String getPlayState() {
            return PlayState.MEDIA_PRE;
        }
    },
    STATE_NEXT { // from class: cn.kuwo.autosdk.api.PlayState.2
        @Override // cn.kuwo.autosdk.api.PlayState
        public String getPlayState() {
            return PlayState.MEDIA_NEXT;
        }
    },
    STATE_PAUSE { // from class: cn.kuwo.autosdk.api.PlayState.3
        @Override // cn.kuwo.autosdk.api.PlayState
        public String getPlayState() {
            return PlayState.MEDIA_PAUSE;
        }
    },
    STATE_PLAY { // from class: cn.kuwo.autosdk.api.PlayState.4
        @Override // cn.kuwo.autosdk.api.PlayState
        public String getPlayState() {
            return PlayState.MEDIA_PLAY;
        }
    };

    public static final String CHANGE_SONGLIST = "CHANGE_SONGLIST";
    private static final String MEDIA_NEXT = "MEDIA_NEXT";
    private static final String MEDIA_PAUSE = "MEDIA_PAUSE";
    private static final String MEDIA_PLAY = "MEDIA_PLAY";
    private static final String MEDIA_PRE = "MEDIA_PRE";

    /* synthetic */ PlayState(PlayState playState) {
        this();
    }

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static PlayState[] valuesCustom() {
        PlayState[] playStateArrValuesCustom = values();
        int length = playStateArrValuesCustom.length;
        PlayState[] playStateArr = new PlayState[length];
        System.arraycopy(playStateArrValuesCustom, 0, playStateArr, 0, length);
        return playStateArr;
    }

    public abstract String getPlayState();
}
