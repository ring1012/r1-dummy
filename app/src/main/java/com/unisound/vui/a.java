package com.unisound.vui;

import android.content.Context;
import com.unisound.client.SpeechUnderstander;
import com.unisound.client.SpeechUnderstanderListener;
import com.unisound.vui.util.LogMgr;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    private SpeechUnderstanderListener f357a = new SpeechUnderstanderListener() { // from class: com.unisound.vui.a.1
        @Override // com.unisound.client.SpeechUnderstanderListener
        public void onError(int type, String errorMSG) {
            if (a.this.d != null) {
                a.this.d.onError(type, errorMSG);
            }
        }

        @Override // com.unisound.client.SpeechUnderstanderListener
        public void onEvent(int type, int timeMs) {
            if (a.this.d != null) {
                a.this.d.onEvent(type, timeMs);
            }
        }

        @Override // com.unisound.client.SpeechUnderstanderListener
        public void onResult(int type, String jsonResult) {
            if (a.this.d != null) {
                a.this.d.onResult(type, jsonResult);
            }
        }
    };
    private final SpeechUnderstander b;
    private final Context c;
    private b d;

    public a(Context context, String str, String str2) {
        LogMgr.d("SpeechUnderstanderManager", "appKey = %s", str);
        this.c = context;
        this.b = new SpeechUnderstander(context, str, str2);
        this.b.setListener(this.f357a);
    }

    public int a(int i, String str) {
        return this.b.release(i, str);
    }

    public int a(String str, String str2) {
        return this.b.loadCompiledJsgf(str, str2);
    }

    public int a(Map<String, List<String>> map, String str) {
        return this.b.insertVocab(map, str);
    }

    public Object a(int i) {
        return this.b.getOption(i);
    }

    public String a(List<String> list) {
        return this.b.setOnlineWakeupWord(list);
    }

    public void a() {
        this.b.stop();
    }

    public void a(int i, Object obj) {
        if (i == 1058) {
            LogMgr.e("SpeechUnderstanderManager", "value:" + obj);
        }
        this.b.setOption(i, obj);
    }

    public void a(b bVar) {
        this.d = bVar;
    }

    public void a(String str) throws JSONException {
        this.b.init(str);
    }

    public void a(Map<Integer, List<String>> map) {
        this.b.uploadUserData(map);
    }

    public int b(String str, String str2) {
        return this.b.loadGrammar(str, str2);
    }

    public int b(Map<String, List<String>> map) {
        return this.b.setWakeupWord(map);
    }

    public void b() {
        this.b.cancel();
    }

    public void b(String str) {
        this.b.start(str);
    }

    public int c(String str) {
        return this.b.unloadGrammar(str);
    }

    public String c() {
        return this.b.getVersion();
    }
}
