package cn.kuwo.autosdk.bean;

import android.text.TextUtils;
import com.google.android.exoplayer2.text.ttml.TtmlNode;

/* loaded from: classes.dex */
public enum MusicQuality {
    FLUENT { // from class: cn.kuwo.autosdk.bean.MusicQuality.1
        @Override // cn.kuwo.autosdk.bean.MusicQuality
        public String getDiscribe() {
            return "流畅";
        }
    },
    HIGHQUALITY { // from class: cn.kuwo.autosdk.bean.MusicQuality.2
        @Override // cn.kuwo.autosdk.bean.MusicQuality
        public String getDiscribe() {
            return "高品质";
        }
    },
    PERFECT { // from class: cn.kuwo.autosdk.bean.MusicQuality.3
        @Override // cn.kuwo.autosdk.bean.MusicQuality
        public String getDiscribe() {
            return "完美";
        }
    },
    LOSSLESS { // from class: cn.kuwo.autosdk.bean.MusicQuality.4
        @Override // cn.kuwo.autosdk.bean.MusicQuality
        public String getDiscribe() {
            return "无损";
        }
    };

    /* synthetic */ MusicQuality(MusicQuality musicQuality) {
        this();
    }

    public static MusicQuality getQualityFromBitrate(int i) {
        return i <= 48 ? FLUENT : i <= 128 ? HIGHQUALITY : i <= 320 ? PERFECT : LOSSLESS;
    }

    public static MusicQuality getQualityFromDiscribe(String str) {
        if (str == null) {
            return FLUENT;
        }
        for (MusicQuality musicQuality : valuesCustom()) {
            if (musicQuality.getDiscribe().equals(str)) {
                return musicQuality;
            }
        }
        return FLUENT;
    }

    public static MusicQuality getQualityFromDiscribe4Quku(String str) {
        if (!TextUtils.isEmpty(str) && !"s".equals(str)) {
            if ("h".equals(str)) {
                return HIGHQUALITY;
            }
            if (TtmlNode.TAG_P.equals(str)) {
                return PERFECT;
            }
            if (!"pp".equals(str) && !"ff".equals(str)) {
                return FLUENT;
            }
            return LOSSLESS;
        }
        return FLUENT;
    }

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static MusicQuality[] valuesCustom() {
        MusicQuality[] musicQualityArrValuesCustom = values();
        int length = musicQualityArrValuesCustom.length;
        MusicQuality[] musicQualityArr = new MusicQuality[length];
        System.arraycopy(musicQualityArrValuesCustom, 0, musicQualityArr, 0, length);
        return musicQualityArr;
    }

    abstract String getDiscribe();

    public boolean isEQ() {
        return this == LOSSLESS || this == PERFECT;
    }

    public boolean isFLAC() {
        return this == LOSSLESS;
    }
}
