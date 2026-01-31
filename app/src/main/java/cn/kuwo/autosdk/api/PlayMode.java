package cn.kuwo.autosdk.api;

/* loaded from: classes.dex */
public enum PlayMode {
    MODE_SINGLE_CIRCLE { // from class: cn.kuwo.autosdk.api.PlayMode.1
        @Override // cn.kuwo.autosdk.api.PlayMode
        public String getPlayMode() {
            return PlayMode.MEDIA_ONE;
        }
    },
    MODE_SINGLE_PLAY { // from class: cn.kuwo.autosdk.api.PlayMode.2
        @Override // cn.kuwo.autosdk.api.PlayMode
        public String getPlayMode() {
            return PlayMode.MEDIA_SINGLE;
        }
    },
    MODE_ALL_ORDER { // from class: cn.kuwo.autosdk.api.PlayMode.3
        @Override // cn.kuwo.autosdk.api.PlayMode
        public String getPlayMode() {
            return PlayMode.MEDIA_ORDER;
        }
    },
    MODE_ALL_CIRCLE { // from class: cn.kuwo.autosdk.api.PlayMode.4
        @Override // cn.kuwo.autosdk.api.PlayMode
        public String getPlayMode() {
            return PlayMode.MEDIA_CIRCLE;
        }
    },
    MODE_ALL_RANDOM { // from class: cn.kuwo.autosdk.api.PlayMode.5
        @Override // cn.kuwo.autosdk.api.PlayMode
        public String getPlayMode() {
            return PlayMode.MEDIA_RANDOM;
        }
    };

    private static final String MEDIA_CIRCLE = "MEDIA_CIRCLE";
    private static final String MEDIA_ONE = "MEDIA_ONE";
    private static final String MEDIA_ORDER = "MEDIA_ORDER";
    private static final String MEDIA_RANDOM = "MEDIA_RANDOM";
    private static final String MEDIA_SINGLE = "MEDIA_SINGLE";

    /* synthetic */ PlayMode(PlayMode playMode) {
        this();
    }

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static PlayMode[] valuesCustom() {
        PlayMode[] playModeArrValuesCustom = values();
        int length = playModeArrValuesCustom.length;
        PlayMode[] playModeArr = new PlayMode[length];
        System.arraycopy(playModeArrValuesCustom, 0, playModeArr, 0, length);
        return playModeArr;
    }

    public abstract String getPlayMode();
}
