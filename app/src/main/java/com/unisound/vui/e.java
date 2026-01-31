package com.unisound.vui;

import android.content.Context;
import com.unisound.client.TextUnderstander;
import com.unisound.client.TextUnderstanderListener;

/* loaded from: classes.dex */
public class e {

    /* renamed from: a, reason: collision with root package name */
    private TextUnderstanderListener f408a = new TextUnderstanderListener() { // from class: com.unisound.vui.e.1
        @Override // com.unisound.client.TextUnderstanderListener
        public void onError(int type, String errorMSG) {
            if (e.this.c != null) {
                e.this.c.onError(type, errorMSG);
            }
        }

        @Override // com.unisound.client.TextUnderstanderListener
        public void onEvent(int type) {
            if (e.this.c != null) {
                e.this.c.onEvent(type);
            }
        }

        @Override // com.unisound.client.TextUnderstanderListener
        public void onResult(int type, String jsonResult) {
            if (e.this.c != null) {
                e.this.c.onResult(type, jsonResult);
            }
        }
    };
    private TextUnderstander b;
    private f c;

    public e(Context context, String str, String str2) {
        this.b = new TextUnderstander(context, str, str2);
        this.b.setListener(this.f408a);
    }

    public int a(String str) {
        if (this.b == null) {
            return -1;
        }
        return this.b.init(str);
    }

    public void a(int i, Object obj) throws NumberFormatException {
        if (this.b == null) {
            return;
        }
        this.b.setOption(i, obj);
    }
}
