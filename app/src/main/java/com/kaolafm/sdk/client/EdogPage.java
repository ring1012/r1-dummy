package com.kaolafm.sdk.client;

/* loaded from: classes.dex */
public enum EdogPage {
    EDOG("EDog"),
    LIB("Discovery"),
    PLAYER("Fm"),
    ME("UserCenter");

    private String tag;

    EdogPage(String tag) {
        this.tag = tag;
    }

    public String tag() {
        return this.tag;
    }
}
