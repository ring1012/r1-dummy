package com.unisound.vui.auth;

/* loaded from: classes.dex */
public interface UNIOSCredentials {
    boolean equals(Object obj);

    String getAccessKey();

    String getSecretKey();

    int hashCode();
}
