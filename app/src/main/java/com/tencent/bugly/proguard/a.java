package com.tencent.bugly.proguard;

import android.content.Context;
import com.tencent.bugly.crashreport.biz.UserInfoBean;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    protected HashMap<String, HashMap<String, byte[]>> f173a = new HashMap<>();
    protected String b;
    i c;
    private HashMap<String, Object> d;

    public static ag a(int i) {
        if (i == 1) {
            return new af();
        }
        if (i == 3) {
            return new ae();
        }
        return null;
    }

    a() {
        new HashMap();
        this.d = new HashMap<>();
        this.b = "GBK";
        this.c = new i();
    }

    public void a(String str) {
        this.b = str;
    }

    public static aq a(UserInfoBean userInfoBean) {
        if (userInfoBean == null) {
            return null;
        }
        aq aqVar = new aq();
        aqVar.f185a = userInfoBean.e;
        aqVar.e = userInfoBean.j;
        aqVar.d = userInfoBean.c;
        aqVar.c = userInfoBean.d;
        aqVar.g = com.tencent.bugly.crashreport.common.info.a.b().i();
        aqVar.h = userInfoBean.o == 1;
        switch (userInfoBean.b) {
            case 1:
                aqVar.b = (byte) 1;
                break;
            case 2:
                aqVar.b = (byte) 4;
                break;
            case 3:
                aqVar.b = (byte) 2;
                break;
            case 4:
                aqVar.b = (byte) 3;
                break;
            default:
                if (userInfoBean.b >= 10 && userInfoBean.b < 20) {
                    aqVar.b = (byte) userInfoBean.b;
                    break;
                } else {
                    x.e("unknown uinfo type %d ", Integer.valueOf(userInfoBean.b));
                    return null;
                }
                break;
        }
        aqVar.f = new HashMap();
        if (userInfoBean.p >= 0) {
            aqVar.f.put("C01", new StringBuilder().append(userInfoBean.p).toString());
        }
        if (userInfoBean.q >= 0) {
            aqVar.f.put("C02", new StringBuilder().append(userInfoBean.q).toString());
        }
        if (userInfoBean.r != null && userInfoBean.r.size() > 0) {
            for (Map.Entry<String, String> entry : userInfoBean.r.entrySet()) {
                aqVar.f.put("C03_" + entry.getKey(), entry.getValue());
            }
        }
        if (userInfoBean.s != null && userInfoBean.s.size() > 0) {
            for (Map.Entry<String, String> entry2 : userInfoBean.s.entrySet()) {
                aqVar.f.put("C04_" + entry2.getKey(), entry2.getValue());
            }
        }
        aqVar.f.put("A36", new StringBuilder().append(!userInfoBean.l).toString());
        aqVar.f.put("F02", new StringBuilder().append(userInfoBean.g).toString());
        aqVar.f.put("F03", new StringBuilder().append(userInfoBean.h).toString());
        aqVar.f.put("F04", userInfoBean.j);
        aqVar.f.put("F05", new StringBuilder().append(userInfoBean.i).toString());
        aqVar.f.put("F06", userInfoBean.m);
        aqVar.f.put("F10", new StringBuilder().append(userInfoBean.k).toString());
        x.c("summary type %d vm:%d", Byte.valueOf(aqVar.b), Integer.valueOf(aqVar.f.size()));
        return aqVar;
    }

    public static String a(ArrayList<String> arrayList) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayList.size(); i++) {
            String str = arrayList.get(i);
            if (str.equals("java.lang.Integer") || str.equals("int")) {
                str = "int32";
            } else if (str.equals("java.lang.Boolean") || str.equals("boolean")) {
                str = "bool";
            } else if (str.equals("java.lang.Byte") || str.equals("byte")) {
                str = "char";
            } else if (str.equals("java.lang.Double") || str.equals("double")) {
                str = "double";
            } else if (str.equals("java.lang.Float") || str.equals("float")) {
                str = "float";
            } else if (str.equals("java.lang.Long") || str.equals("long")) {
                str = "int64";
            } else if (str.equals("java.lang.Short") || str.equals("short")) {
                str = "short";
            } else {
                if (str.equals("java.lang.Character")) {
                    throw new IllegalArgumentException("can not support java.lang.Character");
                }
                if (str.equals("java.lang.String")) {
                    str = "string";
                } else if (str.equals("java.util.List")) {
                    str = "list";
                } else if (str.equals("java.util.Map")) {
                    str = "map";
                }
            }
            arrayList.set(i, str);
        }
        Collections.reverse(arrayList);
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            String str2 = arrayList.get(i2);
            if (str2.equals("list")) {
                arrayList.set(i2 - 1, "<" + arrayList.get(i2 - 1));
                arrayList.set(0, arrayList.get(0) + ">");
            } else if (str2.equals("map")) {
                arrayList.set(i2 - 1, "<" + arrayList.get(i2 - 1) + ",");
                arrayList.set(0, arrayList.get(0) + ">");
            } else if (str2.equals("Array")) {
                arrayList.set(i2 - 1, "<" + arrayList.get(i2 - 1));
                arrayList.set(0, arrayList.get(0) + ">");
            }
        }
        Collections.reverse(arrayList);
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            stringBuffer.append(it.next());
        }
        return stringBuffer.toString();
    }

    public <T> void a(String str, T t) throws UnsupportedEncodingException {
        if (str == null) {
            throw new IllegalArgumentException("put key can not is null");
        }
        if (t == null) {
            throw new IllegalArgumentException("put value can not is null");
        }
        if (t instanceof Set) {
            throw new IllegalArgumentException("can not support Set");
        }
        j jVar = new j();
        jVar.a(this.b);
        jVar.a(t, 0);
        byte[] bArrA = l.a(jVar.a());
        HashMap<String, byte[]> map = new HashMap<>(1);
        ArrayList<String> arrayList = new ArrayList<>(1);
        a(arrayList, t);
        map.put(a(arrayList), bArrA);
        this.d.remove(str);
        this.f173a.put(str, map);
    }

    public static ar a(List<UserInfoBean> list, int i) {
        if (list == null || list.size() == 0) {
            return null;
        }
        com.tencent.bugly.crashreport.common.info.a aVarB = com.tencent.bugly.crashreport.common.info.a.b();
        if (aVarB == null) {
            return null;
        }
        aVarB.t();
        ar arVar = new ar();
        arVar.b = aVarB.d;
        arVar.c = aVarB.h();
        ArrayList<aq> arrayList = new ArrayList<>();
        Iterator<UserInfoBean> it = list.iterator();
        while (it.hasNext()) {
            aq aqVarA = a(it.next());
            if (aqVarA != null) {
                arrayList.add(aqVarA);
            }
        }
        arVar.d = arrayList;
        arVar.e = new HashMap();
        arVar.e.put("A7", aVarB.f);
        arVar.e.put("A6", aVarB.s());
        arVar.e.put("A5", aVarB.r());
        arVar.e.put("A2", new StringBuilder().append(aVarB.p()).toString());
        arVar.e.put("A1", new StringBuilder().append(aVarB.p()).toString());
        arVar.e.put("A24", aVarB.h);
        arVar.e.put("A17", new StringBuilder().append(aVarB.q()).toString());
        arVar.e.put("A15", aVarB.w());
        arVar.e.put("A13", new StringBuilder().append(aVarB.x()).toString());
        arVar.e.put("F08", aVarB.v);
        arVar.e.put("F09", aVarB.w);
        Map<String, String> mapG = aVarB.G();
        if (mapG != null && mapG.size() > 0) {
            for (Map.Entry<String, String> entry : mapG.entrySet()) {
                arVar.e.put("C04_" + entry.getKey(), entry.getValue());
            }
        }
        switch (i) {
            case 1:
                arVar.f186a = (byte) 1;
                break;
            case 2:
                arVar.f186a = (byte) 2;
                break;
            default:
                x.e("unknown up type %d ", Integer.valueOf(i));
                return null;
        }
        return arVar;
    }

    public static <T extends k> T a(byte[] bArr, Class<T> cls) {
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        try {
            T tNewInstance = cls.newInstance();
            i iVar = new i(bArr);
            iVar.a(com.unisound.b.f.b);
            tNewInstance.a(iVar);
            return tNewInstance;
        } catch (Throwable th) {
            if (!x.b(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public static am a(Context context, int i, byte[] bArr) {
        com.tencent.bugly.crashreport.common.info.a aVarB = com.tencent.bugly.crashreport.common.info.a.b();
        StrategyBean strategyBeanC = com.tencent.bugly.crashreport.common.strategy.a.a().c();
        if (aVarB == null || strategyBeanC == null) {
            x.e("Can not create request pkg for parameters is invalid.", new Object[0]);
            return null;
        }
        try {
            am amVar = new am();
            synchronized (aVarB) {
                amVar.f181a = 1;
                amVar.b = aVarB.f();
                amVar.c = aVarB.c;
                amVar.d = aVarB.j;
                amVar.e = aVarB.l;
                aVarB.getClass();
                amVar.f = "2.6.6";
                amVar.g = i;
                amVar.h = bArr == null ? "".getBytes() : bArr;
                amVar.i = aVarB.g;
                amVar.j = aVarB.h;
                amVar.k = new HashMap();
                amVar.l = aVarB.e();
                amVar.m = strategyBeanC.p;
                amVar.o = aVarB.h();
                amVar.p = com.tencent.bugly.crashreport.common.info.b.f(context);
                amVar.q = System.currentTimeMillis();
                amVar.r = aVarB.k();
                amVar.s = aVarB.j();
                amVar.t = aVarB.m();
                amVar.u = aVarB.l();
                amVar.v = aVarB.n();
                amVar.w = amVar.p;
                aVarB.getClass();
                amVar.n = "com.tencent.bugly";
                amVar.k.put("A26", aVarB.y());
                amVar.k.put("A60", aVarB.z());
                amVar.k.put("A61", aVarB.A());
                amVar.k.put("F11", new StringBuilder().append(aVarB.z).toString());
                amVar.k.put("F12", new StringBuilder().append(aVarB.y).toString());
                amVar.k.put("G1", aVarB.u());
                if (aVarB.B) {
                    amVar.k.put("G2", aVarB.M());
                    amVar.k.put("G3", aVarB.N());
                    amVar.k.put("G4", aVarB.O());
                    amVar.k.put("G5", aVarB.P());
                    amVar.k.put("G6", aVarB.Q());
                    amVar.k.put("G7", Long.toString(aVarB.R()));
                }
                amVar.k.put("D3", aVarB.k);
                if (com.tencent.bugly.b.b != null) {
                    for (com.tencent.bugly.a aVar : com.tencent.bugly.b.b) {
                        if (aVar.versionKey != null && aVar.version != null) {
                            amVar.k.put(aVar.versionKey, aVar.version);
                        }
                    }
                }
                amVar.k.put("G15", z.b("G15", ""));
                amVar.k.put("D4", z.b("D4", "0"));
            }
            u uVarA = u.a();
            if (uVarA != null && !uVarA.f202a && bArr != null) {
                amVar.h = z.a(amVar.h, 2, 1, strategyBeanC.u);
                if (amVar.h == null) {
                    x.e("reqPkg sbuffer error!", new Object[0]);
                    return null;
                }
            }
            Map<String, String> mapF = aVarB.F();
            if (mapF != null) {
                for (Map.Entry<String, String> entry : mapF.entrySet()) {
                    amVar.k.put(entry.getKey(), entry.getValue());
                }
            }
            return amVar;
        } catch (Throwable th) {
            if (!x.b(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private void a(ArrayList<String> arrayList, Object obj) {
        if (obj.getClass().isArray()) {
            if (!obj.getClass().getComponentType().toString().equals("byte")) {
                throw new IllegalArgumentException("only byte[] is supported");
            }
            if (Array.getLength(obj) > 0) {
                arrayList.add("java.util.List");
                a(arrayList, Array.get(obj, 0));
                return;
            } else {
                arrayList.add("Array");
                arrayList.add("?");
                return;
            }
        }
        if (obj instanceof Array) {
            throw new IllegalArgumentException("can not support Array, please use List");
        }
        if (obj instanceof List) {
            arrayList.add("java.util.List");
            List list = (List) obj;
            if (list.size() > 0) {
                a(arrayList, list.get(0));
                return;
            } else {
                arrayList.add("?");
                return;
            }
        }
        if (obj instanceof Map) {
            arrayList.add("java.util.Map");
            Map map = (Map) obj;
            if (map.size() > 0) {
                Object next = map.keySet().iterator().next();
                Object obj2 = map.get(next);
                arrayList.add(next.getClass().getName());
                a(arrayList, obj2);
                return;
            }
            arrayList.add("?");
            arrayList.add("?");
            return;
        }
        arrayList.add(obj.getClass().getName());
    }

    public byte[] a() throws UnsupportedEncodingException {
        j jVar = new j(0);
        jVar.a(this.b);
        jVar.a((Map) this.f173a, 0);
        return l.a(jVar.a());
    }

    public void a(byte[] bArr) {
        this.c.a(bArr);
        this.c.a(this.b);
        HashMap map = new HashMap(1);
        HashMap map2 = new HashMap(1);
        map2.put("", new byte[0]);
        map.put("", map2);
        this.f173a = this.c.a((Map) map, 0, false);
    }

    public static byte[] a(Object obj) {
        try {
            d dVar = new d();
            dVar.b();
            dVar.a(com.unisound.b.f.b);
            dVar.b(1);
            dVar.b("RqdServer");
            dVar.c("sync");
            dVar.a("detail", (String) obj);
            return dVar.a();
        } catch (Throwable th) {
            if (!x.b(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public static an a(byte[] bArr, boolean z) {
        if (bArr != null) {
            try {
                d dVar = new d();
                dVar.b();
                dVar.a(com.unisound.b.f.b);
                dVar.a(bArr);
                Object objB = dVar.b("detail", new an());
                an anVar = an.class.isInstance(objB) ? (an) an.class.cast(objB) : null;
                if (!z && anVar != null && anVar.c != null && anVar.c.length > 0) {
                    x.c("resp buf %d", Integer.valueOf(anVar.c.length));
                    anVar.c = z.b(anVar.c, 2, 1, StrategyBean.d);
                    if (anVar.c == null) {
                        x.e("resp sbuffer error!", new Object[0]);
                        return null;
                    }
                    return anVar;
                }
                return anVar;
            } catch (Throwable th) {
                if (!x.b(th)) {
                    th.printStackTrace();
                }
            }
        }
        return null;
    }

    public static byte[] a(k kVar) {
        try {
            j jVar = new j();
            jVar.a(com.unisound.b.f.b);
            kVar.a(jVar);
            return jVar.b();
        } catch (Throwable th) {
            if (!x.b(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }
}
