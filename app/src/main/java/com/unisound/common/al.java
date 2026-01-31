package com.unisound.common;

import android.text.TextUtils;
import com.unisound.client.ErrorCode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class al {
    private List<ak> c = new ArrayList();
    private com.unisound.sdk.as d;
    private static int b = 6;

    /* renamed from: a, reason: collision with root package name */
    public static String f241a = "http://10.30.2.13:8089/data-process-service/rtc";

    private int b() {
        for (int i = 1; i < b; i++) {
            if (b(i) == null) {
                return i;
            }
        }
        return -1;
    }

    private ak b(int i) {
        if (i == -1) {
            return null;
        }
        for (ak akVar : this.c) {
            if (akVar.c() == i) {
                return akVar;
            }
        }
        return null;
    }

    private ak c(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ak akVarA = a(str);
        if (akVarA != null) {
            return akVarA;
        }
        int iB = b();
        if (iB == -1) {
            return null;
        }
        ak akVar = new ak(iB, str);
        this.c.add(akVar);
        return akVar;
    }

    public int a() {
        return this.c.size();
    }

    public ak a(int i) {
        if (this.c.size() <= i || i <= -1) {
            return null;
        }
        return this.c.get(i);
    }

    public ak a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (ak akVar : this.c) {
            if (akVar.b().equals(str)) {
                return akVar;
            }
        }
        return null;
    }

    public ax a(String str, List<String> list, am amVar) {
        ax axVar = new ax();
        axVar.a(f241a);
        ak akVarA = a(str);
        ErrorCode errorCode = new ErrorCode();
        if (akVarA == null) {
            if (a() >= b) {
                amVar.a(axVar, errorCode.createProfessionError(ErrorCode.UPLOAD_SCENE_OUT_MAX_COUNT));
                return axVar;
            }
            akVarA = c(str);
            if (akVarA == null) {
                amVar.a(axVar, errorCode.createProfessionError(ErrorCode.UPLOAD_SCENE_OUT_MAX_COUNT));
                return axVar;
            }
        }
        axVar.a(this.d.aZ(), akVarA, list);
        axVar.a(amVar);
        return axVar;
    }

    public void a(ak akVar) {
        Iterator<ak> it = this.c.iterator();
        while (it.hasNext()) {
            if (it.next() == akVar) {
                this.c.remove(akVar);
            }
        }
    }

    public void a(com.unisound.sdk.as asVar) {
        this.d = asVar;
    }

    public void a(String str, int i) {
        f241a = "http://" + str + ":" + i + "/data-process-service/rtc";
    }

    public void b(String str) {
        ak akVarA = a(str);
        if (akVarA != null) {
            a(akVarA);
        }
    }
}
