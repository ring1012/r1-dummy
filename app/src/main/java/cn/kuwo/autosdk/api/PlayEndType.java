package cn.kuwo.autosdk.api;

/* loaded from: classes.dex */
public enum PlayEndType {
    END_COMPLETE,
    END_USER,
    END_ERROR;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static PlayEndType[] valuesCustom() {
        PlayEndType[] playEndTypeArrValuesCustom = values();
        int length = playEndTypeArrValuesCustom.length;
        PlayEndType[] playEndTypeArr = new PlayEndType[length];
        System.arraycopy(playEndTypeArrValuesCustom, 0, playEndTypeArr, 0, length);
        return playEndTypeArr;
    }
}
