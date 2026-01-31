package com.baidu.b.a;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
class h {

    /* renamed from: a, reason: collision with root package name */
    private Context f43a;
    private List<HashMap<String, String>> b = null;
    private a<String> c = null;

    interface a<Result> {
        void a(Result result);
    }

    protected h(Context context) {
        this.f43a = context;
    }

    private List<HashMap<String, String>> a(HashMap<String, String> map, String[] strArr) {
        ArrayList arrayList = new ArrayList();
        if (strArr != null && strArr.length > 0) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= strArr.length) {
                    break;
                }
                HashMap map2 = new HashMap();
                Iterator<String> it = map.keySet().iterator();
                while (it.hasNext()) {
                    String string = it.next().toString();
                    map2.put(string, map.get(string));
                }
                map2.put("mcode", strArr[i2]);
                arrayList.add(map2);
                i = i2 + 1;
            }
        } else {
            HashMap map3 = new HashMap();
            Iterator<String> it2 = map.keySet().iterator();
            while (it2.hasNext()) {
                String string2 = it2.next().toString();
                map3.put(string2, map.get(string2));
            }
            arrayList.add(map3);
        }
        return arrayList;
    }

    private void a(String str) throws JSONException {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void a(List<HashMap<String, String>> list) throws Throwable {
        int i = 0;
        d.a("syncConnect start Thread id = " + String.valueOf(Thread.currentThread().getId()));
        if (list == null || list.size() == 0) {
            d.c("syncConnect failed,params list is null or size is 0");
            return;
        }
        ArrayList arrayList = new ArrayList();
        while (true) {
            int i2 = i;
            if (i2 >= list.size()) {
                d.a("--iiiiii:" + i2 + "<><>paramList.size():" + list.size() + "<><>authResults.size():" + arrayList.size());
                if (list.size() <= 0 || i2 != list.size() || arrayList.size() <= 0 || i2 != arrayList.size() || i2 - 1 <= 0) {
                    return;
                }
                try {
                    JSONObject jSONObject = new JSONObject((String) arrayList.get(i2 - 1));
                    if (!jSONObject.has("status") || jSONObject.getInt("status") == 0) {
                        return;
                    }
                    d.a("i-1 result is not 0,return first result");
                    a((String) arrayList.get(0));
                    return;
                } catch (JSONException e) {
                    a(com.baidu.b.a.a.a("JSONException:" + e.getMessage()));
                    return;
                }
            }
            d.a("syncConnect resuest " + i2 + "  start!!!");
            HashMap<String, String> map = list.get(i2);
            j jVar = new j(this.f43a);
            if (jVar.a()) {
                String strA = jVar.a(map);
                if (strA == null) {
                    strA = "";
                }
                d.a("syncConnect resuest " + i2 + "  result:" + strA);
                arrayList.add(strA);
                try {
                    JSONObject jSONObject2 = new JSONObject(strA);
                    if (jSONObject2.has("status") && jSONObject2.getInt("status") == 0) {
                        d.a("auth end and break");
                        a(strA);
                        return;
                    }
                } catch (JSONException e2) {
                    d.a("continue-------------------------------");
                }
            } else {
                d.a("Current network is not available.");
                arrayList.add(com.baidu.b.a.a.a("Current network is not available."));
            }
            d.a("syncConnect end");
            i = i2 + 1;
        }
    }

    protected void a(HashMap<String, String> map, String[] strArr, a<String> aVar) {
        this.b = a(map, strArr);
        this.c = aVar;
        new Thread(new i(this)).start();
    }
}
