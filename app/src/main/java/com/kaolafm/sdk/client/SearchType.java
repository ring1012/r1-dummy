package com.kaolafm.sdk.client;

import cn.yunzhisheng.asr.a;

/* loaded from: classes.dex */
public enum SearchType {
    ALL(0),
    PGC(10000),
    ALBUM(a.b),
    AUDIO(30000),
    BROADCAST(50000);

    private int code;

    SearchType(int code) {
        this.code = code;
    }

    public int value() {
        return this.code;
    }
}
