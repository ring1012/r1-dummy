package com.unisound.vui.util.entity;

/* loaded from: classes.dex */
public enum ExoAsrTag {
    TAG_UNIDRIVE_MAIN,
    TAG_PHONE,
    TAG_CONFIRM;

    private static final String CONFIRM = "confirm";
    private static final String PHONE = "phone";
    private static final String UNIDRIVE_MAIN = "unidrive_main";

    public static String getMainTag(ExoAsrTag asrTag) {
        switch (asrTag) {
        }
        return UNIDRIVE_MAIN;
    }
}
