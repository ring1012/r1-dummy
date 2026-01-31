package cn.kuwo.autosdk.api;

/* loaded from: classes.dex */
public enum SearchStatus {
    SUCCESS,
    FAILED,
    CANCLE,
    NONE;

    /* renamed from: values, reason: to resolve conflict with enum method */
    public static SearchStatus[] valuesCustom() {
        SearchStatus[] searchStatusArrValuesCustom = values();
        int length = searchStatusArrValuesCustom.length;
        SearchStatus[] searchStatusArr = new SearchStatus[length];
        System.arraycopy(searchStatusArrValuesCustom, 0, searchStatusArr, 0, length);
        return searchStatusArr;
    }
}
