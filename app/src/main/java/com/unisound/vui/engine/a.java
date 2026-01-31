package com.unisound.vui.engine;

import android.content.Context;
import com.unisound.vui.c;
import com.unisound.vui.e;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public interface a {

    /* renamed from: com.unisound.vui.engine.a$a, reason: collision with other inner class name */
    public interface InterfaceC0011a {
        Context context();

        a getANTBuilder();
    }

    com.unisound.vui.a createASRManager();

    e createNluManager();

    c createTTSManager();
}
