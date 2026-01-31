package com.tencent.bugly;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.tencent.bugly.proguard.n;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.u;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class b {

    /* renamed from: a, reason: collision with root package name */
    public static boolean f126a = true;
    public static List<a> b = new ArrayList();
    public static boolean c;
    private static p d;
    private static boolean e;

    private static boolean a(com.tencent.bugly.crashreport.common.info.a aVar) {
        List<String> list = aVar.o;
        aVar.getClass();
        return list != null && list.contains("bugly");
    }

    public static synchronized void a(Context context) {
        a(context, null);
    }

    public static synchronized void a(Context context, BuglyStrategy buglyStrategy) {
        if (e) {
            x.d("[init] initial Multi-times, ignore this.", new Object[0]);
        } else if (context == null) {
            Log.w(x.f208a, "[init] context of init() is null, check it.");
        } else {
            com.tencent.bugly.crashreport.common.info.a aVarA = com.tencent.bugly.crashreport.common.info.a.a(context);
            if (a(aVarA)) {
                f126a = false;
            } else {
                String strF = aVarA.f();
                if (strF == null) {
                    Log.e(x.f208a, "[init] meta data of BUGLY_APPID in AndroidManifest.xml should be set.");
                } else {
                    a(context, strF, aVarA.u, buglyStrategy);
                }
            }
        }
    }

    public static synchronized void a(Context context, String str, boolean z, BuglyStrategy buglyStrategy) {
        String strSubstring;
        String strSubstring2;
        byte[] bArr;
        String str2;
        String strSubstring3;
        if (e) {
            x.d("[init] initial Multi-times, ignore this.", new Object[0]);
        } else if (context == null) {
            Log.w(x.f208a, "[init] context is null, check it.");
        } else if (str == null) {
            Log.e(x.f208a, "init arg 'crashReportAppID' should not be null!");
        } else {
            e = true;
            if (z) {
                c = true;
                x.b = true;
                x.d("Bugly debug模式开启，请在发布时把isDebug关闭。 -- Running in debug model for 'isDebug' is enabled. Please disable it when you release.", new Object[0]);
                x.e("--------------------------------------------------------------------------------------------", new Object[0]);
                x.d("Bugly debug模式将有以下行为特性 -- The following list shows the behaviour of debug model: ", new Object[0]);
                x.d("[1] 输出详细的Bugly SDK的Log -- More detailed log of Bugly SDK will be output to logcat;", new Object[0]);
                x.d("[2] 每一条Crash都会被立即上报 -- Every crash caught by Bugly will be uploaded immediately.", new Object[0]);
                x.d("[3] 自定义日志将会在Logcat中输出 -- Custom log will be output to logcat.", new Object[0]);
                x.e("--------------------------------------------------------------------------------------------", new Object[0]);
                x.b("[init] Open debug mode of Bugly.", new Object[0]);
            }
            x.a("[init] Bugly version: v%s", "2.6.6");
            x.a(" crash report start initializing...", new Object[0]);
            x.b("[init] Bugly start initializing...", new Object[0]);
            x.a("[init] Bugly complete version: v%s", "2.6.6");
            Context contextA = z.a(context);
            com.tencent.bugly.crashreport.common.info.a aVarA = com.tencent.bugly.crashreport.common.info.a.a(contextA);
            aVarA.t();
            y.a(contextA);
            d = p.a(contextA, b);
            u.a(contextA);
            com.tencent.bugly.crashreport.common.strategy.a aVarA2 = com.tencent.bugly.crashreport.common.strategy.a.a(contextA, b);
            n nVarA = n.a(contextA);
            if (a(aVarA)) {
                f126a = false;
            } else {
                aVarA.a(str);
                x.a("[param] Set APP ID:%s", str);
                if (buglyStrategy != null) {
                    String appVersion = buglyStrategy.getAppVersion();
                    if (!TextUtils.isEmpty(appVersion)) {
                        if (appVersion.length() > 100) {
                            strSubstring3 = appVersion.substring(0, 100);
                            x.d("appVersion %s length is over limit %d substring to %s", appVersion, 100, strSubstring3);
                        } else {
                            strSubstring3 = appVersion;
                        }
                        aVarA.j = strSubstring3;
                        x.a("[param] Set App version: %s", buglyStrategy.getAppVersion());
                    }
                    try {
                        if (buglyStrategy.isReplaceOldChannel()) {
                            String appChannel = buglyStrategy.getAppChannel();
                            if (!TextUtils.isEmpty(appChannel)) {
                                if (appChannel.length() > 100) {
                                    String strSubstring4 = appChannel.substring(0, 100);
                                    x.d("appChannel %s length is over limit %d substring to %s", appChannel, 100, strSubstring4);
                                    str2 = strSubstring4;
                                } else {
                                    str2 = appChannel;
                                }
                                d.a(556, "app_channel", str2.getBytes(), (o) null, false);
                                aVarA.l = str2;
                            }
                        } else {
                            Map<String, byte[]> mapA = d.a(556, (o) null, true);
                            if (mapA != null && (bArr = mapA.get("app_channel")) != null) {
                                aVarA.l = new String(bArr);
                            }
                        }
                        x.a("[param] Set App channel: %s", aVarA.l);
                    } catch (Exception e2) {
                        if (c) {
                            e2.printStackTrace();
                        }
                    }
                    String appPackageName = buglyStrategy.getAppPackageName();
                    if (!TextUtils.isEmpty(appPackageName)) {
                        if (appPackageName.length() > 100) {
                            strSubstring2 = appPackageName.substring(0, 100);
                            x.d("appPackageName %s length is over limit %d substring to %s", appPackageName, 100, strSubstring2);
                        } else {
                            strSubstring2 = appPackageName;
                        }
                        aVarA.c = strSubstring2;
                        x.a("[param] Set App package: %s", buglyStrategy.getAppPackageName());
                    }
                    String deviceID = buglyStrategy.getDeviceID();
                    if (deviceID != null) {
                        if (deviceID.length() > 100) {
                            strSubstring = deviceID.substring(0, 100);
                            x.d("deviceId %s length is over limit %d substring to %s", deviceID, 100, strSubstring);
                        } else {
                            strSubstring = deviceID;
                        }
                        aVarA.c(strSubstring);
                        x.a("s[param] Set device ID: %s", strSubstring);
                    }
                    aVarA.e = buglyStrategy.isUploadProcess();
                    y.f209a = buglyStrategy.isBuglyLogUpload();
                }
                com.tencent.bugly.crashreport.biz.b.a(contextA, buglyStrategy);
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 >= b.size()) {
                        break;
                    }
                    try {
                        if (nVarA.a(b.get(i2).id)) {
                            b.get(i2).init(contextA, z, buglyStrategy);
                        }
                    } catch (Throwable th) {
                        if (!x.a(th)) {
                            th.printStackTrace();
                        }
                    }
                    i = i2 + 1;
                }
                aVarA2.a(buglyStrategy != null ? buglyStrategy.getAppReportDelay() : 0L);
                x.b("[init] Bugly initialization finished.", new Object[0]);
            }
        }
    }

    public static synchronized void a(a aVar) {
        if (!b.contains(aVar)) {
            b.add(aVar);
        }
    }
}
