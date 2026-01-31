package cn.kuwo.autosdk.bean;

import android.text.TextUtils;

/* loaded from: classes.dex */
public enum MusicFormat {
    NONE { // from class: cn.kuwo.autosdk.bean.MusicFormat.1
        @Override // cn.kuwo.autosdk.bean.MusicFormat
        public String getDiscribe() {
            return "NONE";
        }
    },
    AAC { // from class: cn.kuwo.autosdk.bean.MusicFormat.2
        @Override // cn.kuwo.autosdk.bean.MusicFormat
        public String getDiscribe() {
            return "aac";
        }
    },
    MP3 { // from class: cn.kuwo.autosdk.bean.MusicFormat.3
        @Override // cn.kuwo.autosdk.bean.MusicFormat
        public String getDiscribe() {
            return "mp3";
        }
    },
    MP4 { // from class: cn.kuwo.autosdk.bean.MusicFormat.4
        @Override // cn.kuwo.autosdk.bean.MusicFormat
        public String getDiscribe() {
            return "mp4";
        }
    },
    WMA { // from class: cn.kuwo.autosdk.bean.MusicFormat.5
        @Override // cn.kuwo.autosdk.bean.MusicFormat
        public String getDiscribe() {
            return "wma";
        }
    },
    APE { // from class: cn.kuwo.autosdk.bean.MusicFormat.6
        @Override // cn.kuwo.autosdk.bean.MusicFormat
        public String getDiscribe() {
            return "ape";
        }
    },
    FLAC { // from class: cn.kuwo.autosdk.bean.MusicFormat.7
        @Override // cn.kuwo.autosdk.bean.MusicFormat
        public String getDiscribe() {
            return "flac";
        }
    };

    /* synthetic */ MusicFormat(MusicFormat musicFormat) {
        this();
    }

    public static MusicFormat getFormatFromDiscribe(String str) {
        if (str == null) {
            return NONE;
        }
        for (MusicFormat musicFormat : valuesCustom()) {
            if (musicFormat.getDiscribe().equals(str)) {
                return musicFormat;
            }
        }
        return NONE;
    }

    public static MusicFormat getFormatFromDiscribe4Quku(String str) {
        if (TextUtils.isEmpty(str)) {
            return NONE;
        }
        for (MusicFormat musicFormat : valuesCustom()) {
            if (musicFormat.getDiscribe().equals(str)) {
                return musicFormat;
            }
        }
        return NONE;
    }

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static MusicFormat[] valuesCustom() {
        MusicFormat[] musicFormatArrValuesCustom = values();
        int length = musicFormatArrValuesCustom.length;
        MusicFormat[] musicFormatArr = new MusicFormat[length];
        System.arraycopy(musicFormatArrValuesCustom, 0, musicFormatArr, 0, length);
        return musicFormatArr;
    }

    public abstract String getDiscribe();
}
