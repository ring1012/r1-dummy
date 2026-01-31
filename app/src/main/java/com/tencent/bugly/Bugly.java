package com.tencent.bugly;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class Bugly {
    public static final String SDK_IS_DEV = "false";

    /* renamed from: a, reason: collision with root package name */
    private static boolean f123a;
    public static Boolean isDev;
    public static boolean enable = true;
    public static Context applicationContext = null;
    private static String[] b = {"BuglyCrashModule", "BuglyRqdModule", "BuglyBetaModule"};
    private static String[] c = {"BuglyRqdModule", "BuglyCrashModule", "BuglyBetaModule"};

    public static void init(Context context, String str, boolean z) {
        init(context, str, z, null);
    }

    public static synchronized void init(Context context, String str, boolean z, BuglyStrategy buglyStrategy) {
        if (!f123a) {
            f123a = true;
            Context contextA = z.a(context);
            applicationContext = contextA;
            if (contextA == null) {
                Log.e(x.f208a, "init arg 'context' should not be null!");
            } else {
                if (isDev()) {
                    b = c;
                }
                for (String str2 : b) {
                    try {
                        if (str2.equals("BuglyCrashModule")) {
                            b.a(CrashModule.getInstance());
                        } else if (!str2.equals("BuglyBetaModule") && !str2.equals("BuglyRqdModule")) {
                            str2.equals("BuglyFeedbackModule");
                        }
                    } catch (Throwable th) {
                        x.b(th);
                    }
                }
                b.f126a = enable;
                b.a(applicationContext, str, z, buglyStrategy);
            }
        }
    }

    public static synchronized String getAppChannel() {
        p pVarA;
        Map<String, byte[]> mapA;
        byte[] bArr;
        String str = null;
        synchronized (Bugly.class) {
            com.tencent.bugly.crashreport.common.info.a aVarB = com.tencent.bugly.crashreport.common.info.a.b();
            if (aVarB != null) {
                if (TextUtils.isEmpty(aVarB.l) && (pVarA = p.a()) != null && (mapA = pVarA.a(556, (o) null, true)) != null && (bArr = mapA.get("app_channel")) != null) {
                    str = new String(bArr);
                } else {
                    str = aVarB.l;
                }
            }
        }
        return str;
    }

    public static boolean isDev() {
        if (isDev == null) {
            isDev = Boolean.valueOf(Boolean.parseBoolean(SDK_IS_DEV.replace("@", "")));
        }
        return isDev.booleanValue();
    }
}
