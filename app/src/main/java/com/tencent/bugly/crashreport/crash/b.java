package com.tencent.bugly.crashreport.crash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cn.yunzhisheng.asr.JniUscClient;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.common.info.PlugInBean;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.proguard.ah;
import com.tencent.bugly.proguard.aj;
import com.tencent.bugly.proguard.ak;
import com.tencent.bugly.proguard.al;
import com.tencent.bugly.proguard.am;
import com.tencent.bugly.proguard.k;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.r;
import com.tencent.bugly.proguard.t;
import com.tencent.bugly.proguard.u;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import com.unisound.b.f;
import com.unisound.vui.handler.session.music.syncloud.MusicStateMgr;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class b {

    /* renamed from: a, reason: collision with root package name */
    private static int f157a = 0;
    private Context b;
    private u c;
    private p d;
    private com.tencent.bugly.crashreport.common.strategy.a e;
    private o f;
    private BuglyStrategy.a g;

    public b(int i, Context context, u uVar, p pVar, com.tencent.bugly.crashreport.common.strategy.a aVar, BuglyStrategy.a aVar2, o oVar) {
        f157a = i;
        this.b = context;
        this.c = uVar;
        this.d = pVar;
        this.e = aVar;
        this.g = aVar2;
        this.f = oVar;
    }

    private static List<a> a(List<a> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        ArrayList arrayList = new ArrayList();
        for (a aVar : list) {
            if (aVar.d && aVar.b <= jCurrentTimeMillis - 86400000) {
                arrayList.add(aVar);
            }
        }
        return arrayList;
    }

    private CrashDetailBean a(List<a> list, CrashDetailBean crashDetailBean) {
        CrashDetailBean crashDetailBean2;
        CrashDetailBean crashDetailBean3;
        List<CrashDetailBean> listB;
        String[] strArrSplit;
        if (list == null || list.size() == 0) {
            return crashDetailBean;
        }
        CrashDetailBean crashDetailBean4 = null;
        ArrayList arrayList = new ArrayList(10);
        for (a aVar : list) {
            if (aVar.e) {
                arrayList.add(aVar);
            }
        }
        if (arrayList.size() <= 0 || (listB = b(arrayList)) == null || listB.size() <= 0) {
            crashDetailBean2 = null;
        } else {
            Collections.sort(listB);
            int i = 0;
            while (i < listB.size()) {
                CrashDetailBean crashDetailBean5 = listB.get(i);
                if (i != 0) {
                    if (crashDetailBean5.s != null && (strArrSplit = crashDetailBean5.s.split("\n")) != null) {
                        for (String str : strArrSplit) {
                            if (!crashDetailBean4.s.contains(str)) {
                                crashDetailBean4.t++;
                                crashDetailBean4.s += str + "\n";
                            }
                        }
                    }
                    crashDetailBean5 = crashDetailBean4;
                }
                i++;
                crashDetailBean4 = crashDetailBean5;
            }
            crashDetailBean2 = crashDetailBean4;
        }
        if (crashDetailBean2 == null) {
            crashDetailBean.j = true;
            crashDetailBean.t = 0;
            crashDetailBean.s = "";
            crashDetailBean3 = crashDetailBean;
        } else {
            crashDetailBean3 = crashDetailBean2;
        }
        for (a aVar2 : list) {
            if (!aVar2.e && !aVar2.d && !crashDetailBean3.s.contains(new StringBuilder().append(aVar2.b).toString())) {
                crashDetailBean3.t++;
                crashDetailBean3.s += aVar2.b + "\n";
            }
        }
        if (crashDetailBean3.r != crashDetailBean.r && !crashDetailBean3.s.contains(new StringBuilder().append(crashDetailBean.r).toString())) {
            crashDetailBean3.t++;
            crashDetailBean3.s += crashDetailBean.r + "\n";
            return crashDetailBean3;
        }
        return crashDetailBean3;
    }

    public final boolean a(CrashDetailBean crashDetailBean) {
        return a(crashDetailBean, -123456789);
    }

    public final boolean a(CrashDetailBean crashDetailBean, int i) throws Throwable {
        if (crashDetailBean == null) {
            return true;
        }
        if (c.m != null && !c.m.isEmpty()) {
            x.c("Crash filter for crash stack is: %s", c.m);
            if (crashDetailBean.q.contains(c.m)) {
                x.d("This crash contains the filter string set. It will not be record and upload.", new Object[0]);
                return true;
            }
        }
        if (c.n != null && !c.n.isEmpty()) {
            x.c("Crash regular filter for crash stack is: %s", c.n);
            if (Pattern.compile(c.n).matcher(crashDetailBean.q).find()) {
                x.d("This crash matches the regular filter string set. It will not be record and upload.", new Object[0]);
                return true;
            }
        }
        int i2 = crashDetailBean.b;
        String str = crashDetailBean.n;
        String str2 = crashDetailBean.p;
        String str3 = crashDetailBean.q;
        long j = crashDetailBean.r;
        String str4 = crashDetailBean.m;
        String str5 = crashDetailBean.e;
        String str6 = crashDetailBean.c;
        if (this.f != null) {
            o oVar = this.f;
            String str7 = crashDetailBean.z;
            if (!oVar.c()) {
                x.d("Crash listener 'onCrashSaving' return 'false' thus will not handle this crash.", new Object[0]);
                return true;
            }
        }
        if (crashDetailBean.b != 2) {
            r rVar = new r();
            rVar.b = 1;
            rVar.c = crashDetailBean.z;
            rVar.d = crashDetailBean.A;
            rVar.e = crashDetailBean.r;
            this.d.b(1);
            this.d.a(rVar);
            x.b("[crash] a crash occur, handling...", new Object[0]);
        } else {
            x.b("[crash] a caught exception occur, handling...", new Object[0]);
        }
        List<a> listB = b();
        ArrayList arrayList = null;
        if (listB != null && listB.size() > 0) {
            ArrayList arrayList2 = new ArrayList(10);
            ArrayList arrayList3 = new ArrayList(10);
            arrayList2.addAll(a(listB));
            listB.removeAll(arrayList2);
            if (!com.tencent.bugly.b.c && c.d) {
                boolean z = false;
                for (a aVar : listB) {
                    if (crashDetailBean.u.equals(aVar.c)) {
                        if (aVar.e) {
                            z = true;
                        }
                        arrayList3.add(aVar);
                    }
                    z = z;
                }
                if (z || arrayList3.size() >= c.c) {
                    x.a("same crash occur too much do merged!", new Object[0]);
                    CrashDetailBean crashDetailBeanA = a(arrayList3, crashDetailBean);
                    for (a aVar2 : arrayList3) {
                        if (aVar2.f149a != crashDetailBeanA.f148a) {
                            arrayList2.add(aVar2);
                        }
                    }
                    d(crashDetailBeanA);
                    c(arrayList2);
                    x.b("[crash] save crash success. For this device crash many times, it will not upload crashes immediately", new Object[0]);
                    return true;
                }
            }
            arrayList = arrayList2;
        }
        d(crashDetailBean);
        if (arrayList != null && !arrayList.isEmpty()) {
            c(arrayList);
        }
        x.b("[crash] save crash success", new Object[0]);
        return false;
    }

    public final List<CrashDetailBean> a() throws Throwable {
        StrategyBean strategyBeanC = com.tencent.bugly.crashreport.common.strategy.a.a().c();
        if (strategyBeanC == null) {
            x.d("have not synced remote!", new Object[0]);
            return null;
        }
        if (!strategyBeanC.g) {
            x.d("Crashreport remote closed, please check your APP ID correct and Version available, then uninstall and reinstall your app.", new Object[0]);
            x.b("[init] WARNING! Crashreport closed by server, please check your APP ID correct and Version available, then uninstall and reinstall your app.", new Object[0]);
            return null;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        long jB = z.b();
        List<a> listB = b();
        if (listB == null || listB.size() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Iterator<a> it = listB.iterator();
        while (it.hasNext()) {
            a next = it.next();
            if (next.b < jB - c.g) {
                it.remove();
                arrayList.add(next);
            } else if (next.d) {
                if (next.b >= jCurrentTimeMillis - 86400000) {
                    it.remove();
                } else if (!next.e) {
                    it.remove();
                    arrayList.add(next);
                }
            } else if (next.f >= 3 && next.b < jCurrentTimeMillis - 86400000) {
                it.remove();
                arrayList.add(next);
            }
        }
        if (arrayList.size() > 0) {
            c(arrayList);
        }
        ArrayList arrayList2 = new ArrayList();
        List<CrashDetailBean> listB2 = b(listB);
        if (listB2 != null && listB2.size() > 0) {
            String str = com.tencent.bugly.crashreport.common.info.a.b().j;
            Iterator<CrashDetailBean> it2 = listB2.iterator();
            while (it2.hasNext()) {
                CrashDetailBean next2 = it2.next();
                if (!str.equals(next2.f)) {
                    it2.remove();
                    arrayList2.add(next2);
                }
            }
        }
        if (arrayList2.size() > 0) {
            d(arrayList2);
        }
        return listB2;
    }

    public final void b(CrashDetailBean crashDetailBean) {
        if (this.f != null) {
            o oVar = this.f;
            int i = crashDetailBean.b;
        }
    }

    public final void a(CrashDetailBean crashDetailBean, long j, boolean z) {
        if (c.l) {
            x.a("try to upload right now", new Object[0]);
            ArrayList arrayList = new ArrayList();
            arrayList.add(crashDetailBean);
            a(arrayList, 3000L, z, crashDetailBean.b == 7, z);
        }
    }

    public final void a(final List<CrashDetailBean> list, long j, boolean z, boolean z2, boolean z3) {
        al alVar;
        if (com.tencent.bugly.crashreport.common.info.a.a(this.b).e && this.c != null) {
            if (z3 || this.c.b(c.f159a)) {
                StrategyBean strategyBeanC = this.e.c();
                if (!strategyBeanC.g) {
                    x.d("remote report is disable!", new Object[0]);
                    x.b("[crash] server closed bugly in this app. please check your appid if is correct, and re-install it", new Object[0]);
                    return;
                }
                if (list != null && list.size() != 0) {
                    try {
                        String str = this.c.f202a ? strategyBeanC.s : strategyBeanC.t;
                        String str2 = this.c.f202a ? StrategyBean.c : StrategyBean.f143a;
                        int i = this.c.f202a ? 830 : 630;
                        Context context = this.b;
                        com.tencent.bugly.crashreport.common.info.a aVarB = com.tencent.bugly.crashreport.common.info.a.b();
                        if (context == null || list == null || list.size() == 0 || aVarB == null) {
                            x.d("enEXPPkg args == null!", new Object[0]);
                            alVar = null;
                        } else {
                            al alVar2 = new al();
                            alVar2.f180a = new ArrayList<>();
                            Iterator<CrashDetailBean> it = list.iterator();
                            while (it.hasNext()) {
                                alVar2.f180a.add(a(context, it.next(), aVarB));
                            }
                            alVar = alVar2;
                        }
                        if (alVar == null) {
                            x.d("create eupPkg fail!", new Object[0]);
                            return;
                        }
                        byte[] bArrA = com.tencent.bugly.proguard.a.a((k) alVar);
                        if (bArrA == null) {
                            x.d("send encode fail!", new Object[0]);
                            return;
                        }
                        am amVarA = com.tencent.bugly.proguard.a.a(this.b, i, bArrA);
                        if (amVarA == null) {
                            x.d("request package is null.", new Object[0]);
                            return;
                        }
                        t tVar = new t() { // from class: com.tencent.bugly.crashreport.crash.b.1
                            @Override // com.tencent.bugly.proguard.t
                            public final void a(boolean z4) {
                                b bVar = b.this;
                                b.a(z4, (List<CrashDetailBean>) list);
                            }
                        };
                        if (z) {
                            this.c.a(f157a, amVarA, str, str2, tVar, j, z2);
                        } else {
                            this.c.a(f157a, amVarA, str, str2, tVar, false);
                        }
                    } catch (Throwable th) {
                        x.e("req cr error %s", th.toString());
                        if (!x.b(th)) {
                            th.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void a(boolean z, List<CrashDetailBean> list) {
        if (list != null && list.size() > 0) {
            x.c("up finish update state %b", Boolean.valueOf(z));
            for (CrashDetailBean crashDetailBean : list) {
                x.c("pre uid:%s uc:%d re:%b me:%b", crashDetailBean.c, Integer.valueOf(crashDetailBean.l), Boolean.valueOf(crashDetailBean.d), Boolean.valueOf(crashDetailBean.j));
                crashDetailBean.l++;
                crashDetailBean.d = z;
                x.c("set uid:%s uc:%d re:%b me:%b", crashDetailBean.c, Integer.valueOf(crashDetailBean.l), Boolean.valueOf(crashDetailBean.d), Boolean.valueOf(crashDetailBean.j));
            }
            Iterator<CrashDetailBean> it = list.iterator();
            while (it.hasNext()) {
                c.a().a(it.next());
            }
            x.c("update state size %d", Integer.valueOf(list.size()));
        }
        if (!z) {
            x.b("[crash] upload fail.", new Object[0]);
        }
    }

    public final void c(CrashDetailBean crashDetailBean) {
        int i;
        String strSubstring;
        if (crashDetailBean != null) {
            if (this.g != null || this.f != null) {
                try {
                    x.a("[crash callback] start user's callback:onCrashHandleStart()", new Object[0]);
                    switch (crashDetailBean.b) {
                        case 0:
                            i = 0;
                            break;
                        case 1:
                            i = 2;
                            break;
                        case 2:
                            i = 1;
                            break;
                        case 3:
                            i = 4;
                            break;
                        case 4:
                            i = 3;
                            break;
                        case 5:
                            i = 5;
                            break;
                        case 6:
                            i = 6;
                            break;
                        case 7:
                            i = 7;
                            break;
                        default:
                            return;
                    }
                    int i2 = crashDetailBean.b;
                    String str = crashDetailBean.n;
                    String str2 = crashDetailBean.p;
                    String str3 = crashDetailBean.q;
                    long j = crashDetailBean.r;
                    Map<String, String> mapOnCrashHandleStart = null;
                    if (this.f != null) {
                        o oVar = this.f;
                        String strB = this.f.b();
                        if (strB != null) {
                            mapOnCrashHandleStart = new HashMap<>(1);
                            mapOnCrashHandleStart.put("userData", strB);
                        }
                    } else if (this.g != null) {
                        mapOnCrashHandleStart = this.g.onCrashHandleStart(i, crashDetailBean.n, crashDetailBean.o, crashDetailBean.q);
                    }
                    if (mapOnCrashHandleStart != null && mapOnCrashHandleStart.size() > 0) {
                        crashDetailBean.N = new LinkedHashMap(mapOnCrashHandleStart.size());
                        for (Map.Entry<String, String> entry : mapOnCrashHandleStart.entrySet()) {
                            if (!z.a(entry.getKey())) {
                                String key = entry.getKey();
                                if (key.length() > 100) {
                                    key = key.substring(0, 100);
                                    x.d("setted key length is over limit %d substring to %s", 100, key);
                                }
                                String str4 = key;
                                if (!z.a(entry.getValue()) && entry.getValue().length() > 30000) {
                                    strSubstring = entry.getValue().substring(entry.getValue().length() - 30000);
                                    x.d("setted %s value length is over limit %d substring", str4, 30000);
                                } else {
                                    strSubstring = entry.getValue();
                                }
                                crashDetailBean.N.put(str4, strSubstring);
                                x.a("add setted key %s value size:%d", str4, Integer.valueOf(strSubstring.length()));
                            }
                        }
                    }
                    x.a("[crash callback] start user's callback:onCrashHandleStart2GetExtraDatas()", new Object[0]);
                    byte[] bArrOnCrashHandleStart2GetExtraDatas = null;
                    if (this.f != null) {
                        bArrOnCrashHandleStart2GetExtraDatas = this.f.a();
                    } else if (this.g != null) {
                        bArrOnCrashHandleStart2GetExtraDatas = this.g.onCrashHandleStart2GetExtraDatas(i, crashDetailBean.n, crashDetailBean.o, crashDetailBean.q);
                    }
                    crashDetailBean.S = bArrOnCrashHandleStart2GetExtraDatas;
                    if (crashDetailBean.S != null) {
                        if (crashDetailBean.S.length > 30000) {
                            x.d("extra bytes size %d is over limit %d will drop over part", Integer.valueOf(crashDetailBean.S.length), 30000);
                        }
                        x.a("add extra bytes %d ", Integer.valueOf(crashDetailBean.S.length));
                    }
                } catch (Throwable th) {
                    x.d("crash handle callback somthing wrong! %s", th.getClass().getName());
                    if (!x.a(th)) {
                        th.printStackTrace();
                    }
                }
            }
        }
    }

    private static ContentValues e(CrashDetailBean crashDetailBean) {
        if (crashDetailBean == null) {
            return null;
        }
        try {
            ContentValues contentValues = new ContentValues();
            if (crashDetailBean.f148a > 0) {
                contentValues.put("_id", Long.valueOf(crashDetailBean.f148a));
            }
            contentValues.put("_tm", Long.valueOf(crashDetailBean.r));
            contentValues.put("_s1", crashDetailBean.u);
            contentValues.put("_up", Integer.valueOf(crashDetailBean.d ? 1 : 0));
            contentValues.put("_me", Integer.valueOf(crashDetailBean.j ? 1 : 0));
            contentValues.put("_uc", Integer.valueOf(crashDetailBean.l));
            contentValues.put("_dt", z.a(crashDetailBean));
            return contentValues;
        } catch (Throwable th) {
            if (x.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    private static CrashDetailBean a(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            byte[] blob = cursor.getBlob(cursor.getColumnIndex("_dt"));
            if (blob == null) {
                return null;
            }
            long j = cursor.getLong(cursor.getColumnIndex("_id"));
            CrashDetailBean crashDetailBean = (CrashDetailBean) z.a(blob, CrashDetailBean.CREATOR);
            if (crashDetailBean != null) {
                crashDetailBean.f148a = j;
                return crashDetailBean;
            }
            return crashDetailBean;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public final void d(CrashDetailBean crashDetailBean) {
        ContentValues contentValuesE;
        if (crashDetailBean != null && (contentValuesE = e(crashDetailBean)) != null) {
            long jA = p.a().a("t_cr", contentValuesE, (o) null, true);
            if (jA >= 0) {
                x.c("insert %s success!", "t_cr");
                crashDetailBean.f148a = jA;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x00aa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.util.List<com.tencent.bugly.crashreport.crash.CrashDetailBean> b(java.util.List<com.tencent.bugly.crashreport.crash.a> r11) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 239
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.b.b(java.util.List):java.util.List");
    }

    private static a b(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            a aVar = new a();
            aVar.f149a = cursor.getLong(cursor.getColumnIndex("_id"));
            aVar.b = cursor.getLong(cursor.getColumnIndex("_tm"));
            aVar.c = cursor.getString(cursor.getColumnIndex("_s1"));
            aVar.d = cursor.getInt(cursor.getColumnIndex("_up")) == 1;
            aVar.e = cursor.getInt(cursor.getColumnIndex("_me")) == 1;
            aVar.f = cursor.getInt(cursor.getColumnIndex("_uc"));
            return aVar;
        } catch (Throwable th) {
            if (x.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x008c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.util.List<com.tencent.bugly.crashreport.crash.a> b() throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 208
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.b.b():java.util.List");
    }

    private static void c(List<a> list) {
        if (list != null && list.size() != 0) {
            StringBuilder sb = new StringBuilder();
            Iterator<a> it = list.iterator();
            while (it.hasNext()) {
                sb.append(" or _id").append(" = ").append(it.next().f149a);
            }
            String string = sb.toString();
            if (string.length() > 0) {
                string = string.substring(4);
            }
            sb.setLength(0);
            try {
                x.c("deleted %s data %d", "t_cr", Integer.valueOf(p.a().a("t_cr", string, (String[]) null, (o) null, true)));
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    private static void d(List<CrashDetailBean> list) {
        if (list != null) {
            try {
                if (list.size() != 0) {
                    StringBuilder sb = new StringBuilder();
                    Iterator<CrashDetailBean> it = list.iterator();
                    while (it.hasNext()) {
                        sb.append(" or _id").append(" = ").append(it.next().f148a);
                    }
                    String string = sb.toString();
                    if (string.length() > 0) {
                        string = string.substring(4);
                    }
                    sb.setLength(0);
                    x.c("deleted %s data %d", "t_cr", Integer.valueOf(p.a().a("t_cr", string, (String[]) null, (o) null, true)));
                }
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    private static ak a(Context context, CrashDetailBean crashDetailBean, com.tencent.bugly.crashreport.common.info.a aVar) throws Throwable {
        aj ajVarA;
        aj ajVarA2;
        aj ajVar;
        if (context == null || crashDetailBean == null || aVar == null) {
            x.d("enExp args == null", new Object[0]);
            return null;
        }
        ak akVar = new ak();
        switch (crashDetailBean.b) {
            case 0:
                akVar.f179a = crashDetailBean.j ? MusicStateMgr.PAGE_SIZE : "100";
                break;
            case 1:
                akVar.f179a = crashDetailBean.j ? "201" : "101";
                break;
            case 2:
                akVar.f179a = crashDetailBean.j ? "202" : "102";
                break;
            case 3:
                akVar.f179a = crashDetailBean.j ? "203" : "103";
                break;
            case 4:
                akVar.f179a = crashDetailBean.j ? "204" : "104";
                break;
            case 5:
                akVar.f179a = crashDetailBean.j ? "207" : "107";
                break;
            case 6:
                akVar.f179a = crashDetailBean.j ? "206" : "106";
                break;
            case 7:
                akVar.f179a = crashDetailBean.j ? "208" : "108";
                break;
            default:
                x.e("crash type error! %d", Integer.valueOf(crashDetailBean.b));
                break;
        }
        akVar.b = crashDetailBean.r;
        akVar.c = crashDetailBean.n;
        akVar.d = crashDetailBean.o;
        akVar.e = crashDetailBean.p;
        akVar.g = crashDetailBean.q;
        akVar.h = crashDetailBean.y;
        akVar.i = crashDetailBean.c;
        akVar.j = null;
        akVar.l = crashDetailBean.m;
        akVar.m = crashDetailBean.e;
        akVar.f = crashDetailBean.A;
        akVar.t = com.tencent.bugly.crashreport.common.info.a.b().i();
        akVar.n = null;
        if (crashDetailBean.i != null && crashDetailBean.i.size() > 0) {
            akVar.o = new ArrayList<>();
            for (Map.Entry<String, PlugInBean> entry : crashDetailBean.i.entrySet()) {
                ah ahVar = new ah();
                ahVar.f176a = entry.getValue().f140a;
                ahVar.c = entry.getValue().c;
                ahVar.d = entry.getValue().b;
                ahVar.b = aVar.r();
                akVar.o.add(ahVar);
            }
        }
        if (crashDetailBean.h != null && crashDetailBean.h.size() > 0) {
            akVar.p = new ArrayList<>();
            for (Map.Entry<String, PlugInBean> entry2 : crashDetailBean.h.entrySet()) {
                ah ahVar2 = new ah();
                ahVar2.f176a = entry2.getValue().f140a;
                ahVar2.c = entry2.getValue().c;
                ahVar2.d = entry2.getValue().b;
                akVar.p.add(ahVar2);
            }
        }
        if (crashDetailBean.j) {
            akVar.k = crashDetailBean.t;
            if (crashDetailBean.s != null && crashDetailBean.s.length() > 0) {
                if (akVar.q == null) {
                    akVar.q = new ArrayList<>();
                }
                try {
                    akVar.q.add(new aj((byte) 1, "alltimes.txt", crashDetailBean.s.getBytes(f.b)));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    akVar.q = null;
                }
            }
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(akVar.k);
            objArr[1] = Integer.valueOf(akVar.q != null ? akVar.q.size() : 0);
            x.c("crashcount:%d sz:%d", objArr);
        }
        if (crashDetailBean.w != null) {
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            try {
                akVar.q.add(new aj((byte) 1, "log.txt", crashDetailBean.w.getBytes(f.b)));
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
                akVar.q = null;
            }
        }
        if (!z.a(crashDetailBean.T)) {
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            try {
                ajVar = new aj((byte) 1, "crashInfos.txt", crashDetailBean.T.getBytes(f.b));
            } catch (UnsupportedEncodingException e3) {
                e3.printStackTrace();
                ajVar = null;
            }
            if (ajVar != null) {
                x.c("attach crash infos", new Object[0]);
                akVar.q.add(ajVar);
            }
        }
        if (crashDetailBean.U != null) {
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            aj ajVarA3 = a("backupRecord.zip", context, crashDetailBean.U);
            if (ajVarA3 != null) {
                x.c("attach backup record", new Object[0]);
                akVar.q.add(ajVarA3);
            }
        }
        if (crashDetailBean.x != null && crashDetailBean.x.length > 0) {
            aj ajVar2 = new aj((byte) 2, "buglylog.zip", crashDetailBean.x);
            x.c("attach user log", new Object[0]);
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            akVar.q.add(ajVar2);
        }
        if (crashDetailBean.b == 3) {
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            if (crashDetailBean.N != null && crashDetailBean.N.containsKey("BUGLY_CR_01")) {
                try {
                    akVar.q.add(new aj((byte) 1, "anrMessage.txt", crashDetailBean.N.get("BUGLY_CR_01").getBytes(f.b)));
                    x.c("attach anr message", new Object[0]);
                } catch (UnsupportedEncodingException e4) {
                    e4.printStackTrace();
                    akVar.q = null;
                }
                crashDetailBean.N.remove("BUGLY_CR_01");
            }
            if (crashDetailBean.v != null && (ajVarA2 = a("trace.zip", context, crashDetailBean.v)) != null) {
                x.c("attach traces", new Object[0]);
                akVar.q.add(ajVarA2);
            }
        }
        if (crashDetailBean.b == 1) {
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            if (crashDetailBean.v != null && (ajVarA = a("tomb.zip", context, crashDetailBean.v)) != null) {
                x.c("attach tombs", new Object[0]);
                akVar.q.add(ajVarA);
            }
        }
        if (aVar.C != null && !aVar.C.isEmpty()) {
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            StringBuilder sb = new StringBuilder();
            Iterator<String> it = aVar.C.iterator();
            while (it.hasNext()) {
                sb.append(it.next());
            }
            try {
                akVar.q.add(new aj((byte) 1, "martianlog.txt", sb.toString().getBytes(f.b)));
                x.c("attach pageTracingList", new Object[0]);
            } catch (UnsupportedEncodingException e5) {
                e5.printStackTrace();
            }
        }
        if (crashDetailBean.S != null && crashDetailBean.S.length > 0) {
            if (akVar.q == null) {
                akVar.q = new ArrayList<>();
            }
            akVar.q.add(new aj((byte) 1, "userExtraByteData", crashDetailBean.S));
            x.c("attach extraData", new Object[0]);
        }
        akVar.r = new HashMap();
        akVar.r.put("A9", new StringBuilder().append(crashDetailBean.B).toString());
        akVar.r.put("A11", new StringBuilder().append(crashDetailBean.C).toString());
        akVar.r.put("A10", new StringBuilder().append(crashDetailBean.D).toString());
        akVar.r.put("A23", crashDetailBean.f);
        akVar.r.put("A7", aVar.f);
        akVar.r.put("A6", aVar.s());
        akVar.r.put("A5", aVar.r());
        akVar.r.put("A22", aVar.h());
        akVar.r.put("A2", new StringBuilder().append(crashDetailBean.F).toString());
        akVar.r.put("A1", new StringBuilder().append(crashDetailBean.E).toString());
        akVar.r.put("A24", aVar.h);
        akVar.r.put("A17", new StringBuilder().append(crashDetailBean.G).toString());
        akVar.r.put("A3", aVar.k());
        akVar.r.put("A16", aVar.m());
        akVar.r.put("A25", aVar.n());
        akVar.r.put("A14", aVar.l());
        akVar.r.put("A15", aVar.w());
        akVar.r.put("A13", new StringBuilder().append(aVar.x()).toString());
        akVar.r.put("A34", crashDetailBean.z);
        if (aVar.x != null) {
            akVar.r.put("productIdentify", aVar.x);
        }
        try {
            akVar.r.put("A26", URLEncoder.encode(crashDetailBean.H, f.b));
        } catch (UnsupportedEncodingException e6) {
            e6.printStackTrace();
        }
        if (crashDetailBean.b == 1) {
            akVar.r.put("A27", crashDetailBean.J);
            akVar.r.put("A28", crashDetailBean.I);
            akVar.r.put("A29", new StringBuilder().append(crashDetailBean.k).toString());
        }
        akVar.r.put("A30", crashDetailBean.K);
        akVar.r.put("A18", new StringBuilder().append(crashDetailBean.L).toString());
        akVar.r.put("A36", new StringBuilder().append(!crashDetailBean.M).toString());
        akVar.r.put("F02", new StringBuilder().append(aVar.q).toString());
        akVar.r.put("F03", new StringBuilder().append(aVar.r).toString());
        akVar.r.put("F04", aVar.e());
        akVar.r.put("F05", new StringBuilder().append(aVar.s).toString());
        akVar.r.put("F06", aVar.p);
        akVar.r.put("F08", aVar.v);
        akVar.r.put("F09", aVar.w);
        akVar.r.put("F10", new StringBuilder().append(aVar.t).toString());
        if (crashDetailBean.O >= 0) {
            akVar.r.put("C01", new StringBuilder().append(crashDetailBean.O).toString());
        }
        if (crashDetailBean.P >= 0) {
            akVar.r.put("C02", new StringBuilder().append(crashDetailBean.P).toString());
        }
        if (crashDetailBean.Q != null && crashDetailBean.Q.size() > 0) {
            for (Map.Entry<String, String> entry3 : crashDetailBean.Q.entrySet()) {
                akVar.r.put("C03_" + entry3.getKey(), entry3.getValue());
            }
        }
        if (crashDetailBean.R != null && crashDetailBean.R.size() > 0) {
            for (Map.Entry<String, String> entry4 : crashDetailBean.R.entrySet()) {
                akVar.r.put("C04_" + entry4.getKey(), entry4.getValue());
            }
        }
        akVar.s = null;
        if (crashDetailBean.N != null && crashDetailBean.N.size() > 0) {
            akVar.s = crashDetailBean.N;
            x.a("setted message size %d", Integer.valueOf(akVar.s.size()));
        }
        Object[] objArr2 = new Object[12];
        objArr2[0] = crashDetailBean.n;
        objArr2[1] = crashDetailBean.c;
        objArr2[2] = aVar.e();
        objArr2[3] = Long.valueOf((crashDetailBean.r - crashDetailBean.L) / 1000);
        objArr2[4] = Boolean.valueOf(crashDetailBean.k);
        objArr2[5] = Boolean.valueOf(crashDetailBean.M);
        objArr2[6] = Boolean.valueOf(crashDetailBean.j);
        objArr2[7] = Boolean.valueOf(crashDetailBean.b == 1);
        objArr2[8] = Integer.valueOf(crashDetailBean.t);
        objArr2[9] = crashDetailBean.s;
        objArr2[10] = Boolean.valueOf(crashDetailBean.d);
        objArr2[11] = Integer.valueOf(akVar.r.size());
        x.c("%s rid:%s sess:%s ls:%ds isR:%b isF:%b isM:%b isN:%b mc:%d ,%s ,isUp:%b ,vm:%d", objArr2);
        return akVar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00ca  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00c1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7, types: [java.io.FileInputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static com.tencent.bugly.proguard.aj a(java.lang.String r9, android.content.Context r10, java.lang.String r11) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 230
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.b.a(java.lang.String, android.content.Context, java.lang.String):com.tencent.bugly.proguard.aj");
    }

    public static void a(String str, String str2, String str3, Thread thread, String str4, CrashDetailBean crashDetailBean) {
        com.tencent.bugly.crashreport.common.info.a aVarB = com.tencent.bugly.crashreport.common.info.a.b();
        if (aVarB != null) {
            x.e("#++++++++++Record By Bugly++++++++++#", new Object[0]);
            x.e("# You can use Bugly(http:\\\\bugly.qq.com) to get more Crash Detail!", new Object[0]);
            x.e("# PKG NAME: %s", aVarB.c);
            x.e("# APP VER: %s", aVarB.j);
            x.e("# LAUNCH TIME: %s", z.a(new Date(com.tencent.bugly.crashreport.common.info.a.b().f141a)));
            x.e("# CRASH TYPE: %s", str);
            x.e("# CRASH TIME: %s", str2);
            x.e("# CRASH PROCESS: %s", str3);
            if (thread != null) {
                x.e("# CRASH THREAD: %s", thread.getName());
            }
            if (crashDetailBean != null) {
                x.e("# REPORT ID: %s", crashDetailBean.c);
                Object[] objArr = new Object[2];
                objArr[0] = aVarB.g;
                objArr[1] = aVarB.x().booleanValue() ? "ROOTED" : "UNROOT";
                x.e("# CRASH DEVICE: %s %s", objArr);
                x.e("# RUNTIME AVAIL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.B), Long.valueOf(crashDetailBean.C), Long.valueOf(crashDetailBean.D));
                x.e("# RUNTIME TOTAL RAM:%d ROM:%d SD:%d", Long.valueOf(crashDetailBean.E), Long.valueOf(crashDetailBean.F), Long.valueOf(crashDetailBean.G));
                if (!z.a(crashDetailBean.J)) {
                    x.e("# EXCEPTION FIRED BY %s %s", crashDetailBean.J, crashDetailBean.I);
                } else if (crashDetailBean.b == 3) {
                    Object[] objArr2 = new Object[1];
                    objArr2[0] = crashDetailBean.N == null ? JniUscClient.az : crashDetailBean.N.get("BUGLY_CR_01");
                    x.e("# EXCEPTION ANR MESSAGE:\n %s", objArr2);
                }
            }
            if (!z.a(str4)) {
                x.e("# CRASH STACK: ", new Object[0]);
                x.e(str4, new Object[0]);
            }
            x.e("#++++++++++++++++++++++++++++++++++++++++++#", new Object[0]);
        }
    }
}
