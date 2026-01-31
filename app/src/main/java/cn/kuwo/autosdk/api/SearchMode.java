package cn.kuwo.autosdk.api;

/* loaded from: classes.dex */
public enum SearchMode {
    ALL,
    ARTIST,
    ALBUM,
    MV;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static SearchMode[] valuesCustom() {
        SearchMode[] searchModeArrValuesCustom = values();
        int length = searchModeArrValuesCustom.length;
        SearchMode[] searchModeArr = new SearchMode[length];
        System.arraycopy(searchModeArrValuesCustom, 0, searchModeArr, 0, length);
        return searchModeArr;
    }
}
