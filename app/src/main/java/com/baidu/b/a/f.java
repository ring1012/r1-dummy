package com.baidu.b.a;

import android.content.Context;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
class f {

    /* renamed from: a, reason: collision with root package name */
    private Context f41a;
    private HashMap<String, String> b = null;
    private a<String> c = null;

    interface a<Result> {
        void a(Result result);
    }

    protected f(Context context) {
        this.f41a = context;
    }

    private HashMap<String, String> a(HashMap<String, String> map) {
        HashMap<String, String> map2 = new HashMap<>();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String string = it.next().toString();
            map2.put(string, map.get(string));
        }
        return map2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) throws JSONException {
        JSONObject jSONObject;
        if (str == null) {
            str = "";
        }
        try {
            jSONObject = new JSONObject(str);
            if (!jSONObject.has("status")) {
                jSONObject.put("status", -1);
            }
        } catch (JSONException e) {
            jSONObject = new JSONObject();
            try {
                jSONObject.put("status", -1);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        if (this.c != null) {
            this.c.a(jSONObject != null ? jSONObject.toString() : new JSONObject().toString());
        }
    }

    protected void a(HashMap<String, String> map, a<String> aVar) {
        this.b = a(map);
        this.c = aVar;
        new Thread(new g(this)).start();
    }
}
