package cn.kuwo.autosdk.api;

/* loaded from: classes.dex */
public enum PlayerStatus {
    INIT,
    PLAYING,
    BUFFERING,
    PAUSE,
    STOP;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static PlayerStatus[] valuesCustom() {
        PlayerStatus[] playerStatusArrValuesCustom = values();
        int length = playerStatusArrValuesCustom.length;
        PlayerStatus[] playerStatusArr = new PlayerStatus[length];
        System.arraycopy(playerStatusArrValuesCustom, 0, playerStatusArr, 0, length);
        return playerStatusArr;
    }
}
