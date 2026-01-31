package cn.kuwo.autosdk;

/* loaded from: classes.dex */
enum e {
    NOTIFY_START,
    NOTIFY_FAILED,
    NOTIFY_FINISH;

    public static e[] a() {
        e[] eVarArrValues = values();
        int length = eVarArrValues.length;
        e[] eVarArr = new e[length];
        System.arraycopy(eVarArrValues, 0, eVarArr, 0, length);
        return eVarArr;
    }
}
