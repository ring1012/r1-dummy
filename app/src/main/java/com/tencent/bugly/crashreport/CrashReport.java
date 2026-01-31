package com.tencent.bugly.crashreport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.CrashModule;
import com.tencent.bugly.b;
import com.tencent.bugly.crashreport.crash.BuglyBroadcastRecevier;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.crashreport.crash.h5.H5JavaScriptInterface;
import com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler;
import com.tencent.bugly.proguard.q;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class CrashReport {

    /* renamed from: a, reason: collision with root package name */
    private static Context f127a;

    /* compiled from: BUGLY */
    public static class CrashHandleCallback extends BuglyStrategy.a {
    }

    /* compiled from: BUGLY */
    public interface WebViewInterface {
        void addJavascriptInterface(H5JavaScriptInterface h5JavaScriptInterface, String str);

        CharSequence getContentDescription();

        String getUrl();

        void loadUrl(String str);

        void setJavaScriptEnabled(boolean z);
    }

    public static void enableBugly(boolean z) {
        b.f126a = z;
    }

    public static void initCrashReport(Context context) {
        f127a = context;
        b.a(CrashModule.getInstance());
        b.a(context);
    }

    public static void initCrashReport(Context context, UserStrategy userStrategy) {
        f127a = context;
        b.a(CrashModule.getInstance());
        b.a(context, userStrategy);
    }

    public static void initCrashReport(Context context, String str, boolean z) {
        if (context != null) {
            f127a = context;
            b.a(CrashModule.getInstance());
            b.a(context, str, z, null);
        }
    }

    public static void initCrashReport(Context context, String str, boolean z, UserStrategy userStrategy) {
        if (context != null) {
            f127a = context;
            b.a(CrashModule.getInstance());
            b.a(context, str, z, userStrategy);
        }
    }

    public static String getBuglyVersion(Context context) {
        if (context == null) {
            x.d("Please call with context.", new Object[0]);
            return "unknown";
        }
        com.tencent.bugly.crashreport.common.info.a.a(context);
        return com.tencent.bugly.crashreport.common.info.a.c();
    }

    public static void testJavaCrash() {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not test Java crash because bugly is disable.");
        } else {
            if (!CrashModule.hasInitialized()) {
                Log.e(x.f208a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
                return;
            }
            com.tencent.bugly.crashreport.common.info.a aVarB = com.tencent.bugly.crashreport.common.info.a.b();
            if (aVarB != null) {
                aVarB.b(24096);
            }
            throw new RuntimeException("This Crash create for Test! You can go to Bugly see more detail!");
        }
    }

    public static void testNativeCrash() {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not test native crash because bugly is disable.");
        } else if (!CrashModule.hasInitialized()) {
            Log.e(x.f208a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            x.a("start to create a native crash for test!", new Object[0]);
            c.a().j();
        }
    }

    public static void testANRCrash() {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not test ANR crash because bugly is disable.");
        } else if (!CrashModule.hasInitialized()) {
            Log.e(x.f208a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            x.a("start to create a anr crash for test!", new Object[0]);
            c.a().k();
        }
    }

    public static void postCatchedException(Throwable th) {
        postCatchedException(th, Thread.currentThread(), false);
    }

    public static void postCatchedException(Throwable th, Thread thread) {
        postCatchedException(th, thread, false);
    }

    public static void postCatchedException(Throwable th, Thread thread, boolean z) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not post crash caught because bugly is disable.");
            return;
        }
        if (!CrashModule.hasInitialized()) {
            Log.e(x.f208a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else if (th == null) {
            x.d("throwable is null, just return", new Object[0]);
        } else {
            c.a().a(thread == null ? Thread.currentThread() : thread, th, false, (String) null, (byte[]) null, z);
        }
    }

    public static void closeNativeReport() {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not close native report because bugly is disable.");
        } else if (!CrashModule.hasInitialized()) {
            Log.e(x.f208a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            c.a().f();
        }
    }

    public static void startCrashReport() {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not start crash report because bugly is disable.");
        } else if (!CrashModule.hasInitialized()) {
            Log.w(x.f208a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            c.a().c();
        }
    }

    public static void closeCrashReport() {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not close crash report because bugly is disable.");
        } else if (!CrashModule.hasInitialized()) {
            Log.w(x.f208a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            c.a().d();
        }
    }

    public static void closeBugly() {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not close bugly because bugly is disable.");
            return;
        }
        if (!CrashModule.hasInitialized()) {
            Log.w(x.f208a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return;
        }
        if (f127a != null) {
            BuglyBroadcastRecevier buglyBroadcastRecevier = BuglyBroadcastRecevier.getInstance();
            if (buglyBroadcastRecevier != null) {
                buglyBroadcastRecevier.unregister(f127a);
            }
            closeCrashReport();
            com.tencent.bugly.crashreport.biz.b.a(f127a);
            w wVarA = w.a();
            if (wVarA != null) {
                wVarA.b();
            }
        }
    }

    public static void setUserSceneTag(Context context, int i) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not set tag caught because bugly is disable.");
            return;
        }
        if (context == null) {
            Log.e(x.f208a, "setTag args context should not be null");
            return;
        }
        if (i <= 0) {
            x.d("setTag args tagId should > 0", new Object[0]);
        }
        com.tencent.bugly.crashreport.common.info.a.a(context).a(i);
        x.b("[param] set user scene tag: %d", Integer.valueOf(i));
    }

    public static int getUserSceneTagId(Context context) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not get user scene tag because bugly is disable.");
            return -1;
        }
        if (context == null) {
            Log.e(x.f208a, "getUserSceneTagId args context should not be null");
            return -1;
        }
        return com.tencent.bugly.crashreport.common.info.a.a(context).H();
    }

    public static String getUserData(Context context, String str) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not get user data because bugly is disable.");
            return "unknown";
        }
        if (context == null) {
            Log.e(x.f208a, "getUserDataValue args context should not be null");
            return "unknown";
        }
        if (z.a(str)) {
            return null;
        }
        return com.tencent.bugly.crashreport.common.info.a.a(context).g(str);
    }

    public static void putUserData(Context context, String str, String str2) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not put user data because bugly is disable.");
            return;
        }
        if (context == null) {
            Log.w(x.f208a, "putUserData args context should not be null");
            return;
        }
        if (str == null) {
            String str3 = str;
            x.d("putUserData args key should not be null or empty", new Object[0]);
            return;
        }
        if (str2 == null) {
            String str4 = str2;
            x.d("putUserData args value should not be null", new Object[0]);
            return;
        }
        if (!str.matches("[a-zA-Z[0-9]]+")) {
            x.d("putUserData args key should match [a-zA-Z[0-9]]+  {" + str + "}", new Object[0]);
            return;
        }
        if (str2.length() > 200) {
            x.d("user data value length over limit %d, it will be cutted!", 200);
            str2 = str2.substring(0, 200);
        }
        com.tencent.bugly.crashreport.common.info.a aVarA = com.tencent.bugly.crashreport.common.info.a.a(context);
        if (aVarA.E().contains(str)) {
            NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
            if (nativeCrashHandler != null) {
                nativeCrashHandler.putKeyValueToNative(str, str2);
            }
            com.tencent.bugly.crashreport.common.info.a.a(context).b(str, str2);
            x.c("replace KV %s %s", str, str2);
            return;
        }
        if (aVarA.D() >= 10) {
            x.d("user data size is over limit %d, it will be cutted!", 10);
            return;
        }
        if (str.length() > 50) {
            x.d("user data key length over limit %d , will drop this new key %s", 50, str);
            str = str.substring(0, 50);
        }
        NativeCrashHandler nativeCrashHandler2 = NativeCrashHandler.getInstance();
        if (nativeCrashHandler2 != null) {
            nativeCrashHandler2.putKeyValueToNative(str, str2);
        }
        com.tencent.bugly.crashreport.common.info.a.a(context).b(str, str2);
        x.b("[param] set user data: %s - %s", str, str2);
    }

    public static String removeUserData(Context context, String str) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not remove user data because bugly is disable.");
            return "unknown";
        }
        if (context == null) {
            Log.e(x.f208a, "removeUserData args context should not be null");
            return "unknown";
        }
        if (z.a(str)) {
            return null;
        }
        x.b("[param] remove user data: %s", str);
        return com.tencent.bugly.crashreport.common.info.a.a(context).f(str);
    }

    public static Set<String> getAllUserDataKeys(Context context) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not get all keys of user data because bugly is disable.");
            return new HashSet();
        }
        if (context == null) {
            Log.e(x.f208a, "getAllUserDataKeys args context should not be null");
            return new HashSet();
        }
        return com.tencent.bugly.crashreport.common.info.a.a(context).E();
    }

    public static int getUserDatasSize(Context context) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not get size of user data because bugly is disable.");
            return -1;
        }
        if (context == null) {
            Log.e(x.f208a, "getUserDatasSize args context should not be null");
            return -1;
        }
        return com.tencent.bugly.crashreport.common.info.a.a(context).D();
    }

    public static String getAppID() {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not get App ID because bugly is disable.");
            return "unknown";
        }
        if (!CrashModule.hasInitialized()) {
            Log.e(x.f208a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return "unknown";
        }
        return com.tencent.bugly.crashreport.common.info.a.a(f127a).f();
    }

    public static void setUserId(String str) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not set user ID because bugly is disable.");
        } else if (!CrashModule.hasInitialized()) {
            Log.e(x.f208a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            setUserId(f127a, str);
        }
    }

    public static void setUserId(Context context, String str) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not set user ID because bugly is disable.");
            return;
        }
        if (context == null) {
            Log.e(x.f208a, "Context should not be null when bugly has not been initialed!");
            return;
        }
        if (str == null) {
            x.d("userId should not be null", new Object[0]);
            return;
        }
        if (str.length() > 100) {
            String strSubstring = str.substring(0, 100);
            x.d("userId %s length is over limit %d substring to %s", str, 100, strSubstring);
            str = strSubstring;
        }
        if (!str.equals(com.tencent.bugly.crashreport.common.info.a.a(context).g())) {
            com.tencent.bugly.crashreport.common.info.a.a(context).b(str);
            x.b("[user] set userId : %s", str);
            NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
            if (nativeCrashHandler != null) {
                nativeCrashHandler.setNativeUserId(str);
            }
            if (CrashModule.hasInitialized()) {
                com.tencent.bugly.crashreport.biz.b.a();
            }
        }
    }

    public static String getUserId() {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not get user ID because bugly is disable.");
            return "unknown";
        }
        if (!CrashModule.hasInitialized()) {
            Log.e(x.f208a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return "unknown";
        }
        return com.tencent.bugly.crashreport.common.info.a.a(f127a).g();
    }

    public static String getAppVer() {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not get app version because bugly is disable.");
            return "unknown";
        }
        if (!CrashModule.hasInitialized()) {
            Log.e(x.f208a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return "unknown";
        }
        return com.tencent.bugly.crashreport.common.info.a.a(f127a).j;
    }

    public static String getAppChannel() {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not get App channel because bugly is disable.");
            return "unknown";
        }
        if (!CrashModule.hasInitialized()) {
            Log.e(x.f208a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return "unknown";
        }
        return com.tencent.bugly.crashreport.common.info.a.a(f127a).l;
    }

    public static void setContext(Context context) {
        f127a = context;
    }

    public static boolean isLastSessionCrash() {
        if (!b.f126a) {
            Log.w(x.f208a, "The info 'isLastSessionCrash' is not accurate because bugly is disable.");
            return false;
        }
        if (!CrashModule.hasInitialized()) {
            Log.e(x.f208a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return false;
        }
        return c.a().b();
    }

    public static void setSdkExtraData(Context context, String str, String str2) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not put SDK extra data because bugly is disable.");
        } else if (context != null && !z.a(str) && !z.a(str2)) {
            com.tencent.bugly.crashreport.common.info.a.a(context).a(str, str2);
        }
    }

    public static Map<String, String> getSdkExtraData() {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not get SDK extra data because bugly is disable.");
            return new HashMap();
        }
        if (!CrashModule.hasInitialized()) {
            Log.e(x.f208a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return null;
        }
        return com.tencent.bugly.crashreport.common.info.a.a(f127a).A;
    }

    public static Map<String, String> getSdkExtraData(Context context) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not get SDK extra data because bugly is disable.");
            return new HashMap();
        }
        if (context == null) {
            x.d("Context should not be null.", new Object[0]);
            return null;
        }
        return com.tencent.bugly.crashreport.common.info.a.a(context).A;
    }

    private static void putSdkData(Context context, String str, String str2) {
        if (context != null && !z.a(str) && !z.a(str2)) {
            String strReplace = str.replace("[a-zA-Z[0-9]]+", "");
            if (strReplace.length() > 100) {
                Log.w(x.f208a, String.format("putSdkData key length over limit %d, will be cutted.", 50));
                strReplace = strReplace.substring(0, 50);
            }
            if (str2.length() > 500) {
                Log.w(x.f208a, String.format("putSdkData value length over limit %d, will be cutted!", 200));
                str2 = str2.substring(0, 200);
            }
            com.tencent.bugly.crashreport.common.info.a.a(context).c(strReplace, str2);
            x.b(String.format("[param] putSdkData data: %s - %s", strReplace, str2), new Object[0]);
        }
    }

    public static void setIsAppForeground(Context context, boolean z) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not set 'isAppForeground' because bugly is disable.");
            return;
        }
        if (context == null) {
            x.d("Context should not be null.", new Object[0]);
            return;
        }
        if (z) {
            x.c("App is in foreground.", new Object[0]);
        } else {
            x.c("App is in background.", new Object[0]);
        }
        com.tencent.bugly.crashreport.common.info.a.a(context).a(z);
    }

    public static void setIsDevelopmentDevice(Context context, boolean z) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not set 'isDevelopmentDevice' because bugly is disable.");
            return;
        }
        if (context == null) {
            x.d("Context should not be null.", new Object[0]);
            return;
        }
        if (z) {
            x.c("This is a development device.", new Object[0]);
        } else {
            x.c("This is not a development device.", new Object[0]);
        }
        com.tencent.bugly.crashreport.common.info.a.a(context).y = z;
    }

    public static void setSessionIntervalMills(long j) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not set 'SessionIntervalMills' because bugly is disable.");
        } else {
            com.tencent.bugly.crashreport.biz.b.a(j);
        }
    }

    public static void setAppVersion(Context context, String str) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not set App version because bugly is disable.");
            return;
        }
        if (context == null) {
            Log.w(x.f208a, "setAppVersion args context should not be null");
            return;
        }
        if (str == null) {
            Log.w(x.f208a, "App version is null, will not set");
            return;
        }
        com.tencent.bugly.crashreport.common.info.a.a(context).j = str;
        NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
        if (nativeCrashHandler != null) {
            nativeCrashHandler.setNativeAppVersion(str);
        }
    }

    public static void setAppChannel(Context context, String str) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not set App channel because Bugly is disable.");
            return;
        }
        if (context == null) {
            Log.w(x.f208a, "setAppChannel args context should not be null");
            return;
        }
        if (str == null) {
            Log.w(x.f208a, "App channel is null, will not set");
            return;
        }
        com.tencent.bugly.crashreport.common.info.a.a(context).l = str;
        NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
        if (nativeCrashHandler != null) {
            nativeCrashHandler.setNativeAppChannel(str);
        }
    }

    public static void setAppPackage(Context context, String str) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not set App package because bugly is disable.");
            return;
        }
        if (context == null) {
            Log.w(x.f208a, "setAppPackage args context should not be null");
            return;
        }
        if (str == null) {
            Log.w(x.f208a, "App package is null, will not set");
            return;
        }
        com.tencent.bugly.crashreport.common.info.a.a(context).c = str;
        NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
        if (nativeCrashHandler != null) {
            nativeCrashHandler.setNativeAppPackage(str);
        }
    }

    public static void setCrashFilter(String str) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not set App package because bugly is disable.");
        } else {
            Log.w(x.f208a, "Set crash stack filter: " + str);
            c.m = str;
        }
    }

    public static void setCrashRegularFilter(String str) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not set App package because bugly is disable.");
        } else {
            Log.w(x.f208a, "Set crash stack filter: " + str);
            c.n = str;
        }
    }

    public static void setBuglyDbName(String str) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not set DB name because bugly is disable.");
        } else {
            Log.i(x.f208a, "Set Bugly DB name: " + str);
            q.f199a = str;
        }
    }

    public static void setAuditEnable(Context context, boolean z) {
        if (!b.f126a) {
            Log.w(x.f208a, "Can not set App package because bugly is disable.");
        } else if (context == null) {
            Log.w(x.f208a, "setAppPackage args context should not be null");
        } else {
            Log.i(x.f208a, "Set audit enable: " + z);
            com.tencent.bugly.crashreport.common.info.a.a(context).B = z;
        }
    }

    public static boolean setJavascriptMonitor(WebView webView, boolean z) {
        return setJavascriptMonitor(webView, z, false);
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public static boolean setJavascriptMonitor(final WebView webView, boolean z, boolean z2) {
        if (webView == null) {
            Log.w(x.f208a, "WebView is null.");
            return false;
        }
        return setJavascriptMonitor(new WebViewInterface() { // from class: com.tencent.bugly.crashreport.CrashReport.1
            @Override // com.tencent.bugly.crashreport.CrashReport.WebViewInterface
            public final String getUrl() {
                return webView.getUrl();
            }

            @Override // com.tencent.bugly.crashreport.CrashReport.WebViewInterface
            public final void setJavaScriptEnabled(boolean z3) {
                WebSettings settings = webView.getSettings();
                if (!settings.getJavaScriptEnabled()) {
                    settings.setJavaScriptEnabled(true);
                }
            }

            @Override // com.tencent.bugly.crashreport.CrashReport.WebViewInterface
            public final void loadUrl(String str) {
                webView.loadUrl(str);
            }

            @Override // com.tencent.bugly.crashreport.CrashReport.WebViewInterface
            public final void addJavascriptInterface(H5JavaScriptInterface h5JavaScriptInterface, String str) {
                webView.addJavascriptInterface(h5JavaScriptInterface, str);
            }

            @Override // com.tencent.bugly.crashreport.CrashReport.WebViewInterface
            public final CharSequence getContentDescription() {
                return webView.getContentDescription();
            }
        }, z, z2);
    }

    public static boolean setJavascriptMonitor(WebViewInterface webViewInterface, boolean z) {
        return setJavascriptMonitor(webViewInterface, z, false);
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public static boolean setJavascriptMonitor(WebViewInterface webViewInterface, boolean z, boolean z2) {
        if (webViewInterface == null) {
            Log.w(x.f208a, "WebViewInterface is null.");
            return false;
        }
        if (!CrashModule.hasInitialized()) {
            x.e("CrashReport has not been initialed! please to call method 'initCrashReport' first!", new Object[0]);
            return false;
        }
        x.a("Set Javascript exception monitor of webview.", new Object[0]);
        if (!b.f126a) {
            Log.w(x.f208a, "Can not set JavaScript monitor because bugly is disable.");
            return false;
        }
        x.c("URL of webview is %s", webViewInterface.getUrl());
        if (!z2 && Build.VERSION.SDK_INT < 19) {
            x.e("This interface is only available for Android 4.4 or later.", new Object[0]);
            return false;
        }
        x.a("Enable the javascript needed by webview monitor.", new Object[0]);
        webViewInterface.setJavaScriptEnabled(true);
        H5JavaScriptInterface h5JavaScriptInterface = H5JavaScriptInterface.getInstance(webViewInterface);
        if (h5JavaScriptInterface != null) {
            x.a("Add a secure javascript interface to the webview.", new Object[0]);
            webViewInterface.addJavascriptInterface(h5JavaScriptInterface, "exceptionUploader");
        }
        if (z) {
            x.a("Inject bugly.js(v%s) to the webview.", com.tencent.bugly.crashreport.crash.h5.b.b());
            String strA = com.tencent.bugly.crashreport.crash.h5.b.a();
            if (strA == null) {
                x.e("Failed to inject Bugly.js.", com.tencent.bugly.crashreport.crash.h5.b.b());
                return false;
            }
            webViewInterface.loadUrl("javascript:" + strA);
        }
        return true;
    }

    /* compiled from: BUGLY */
    public static class UserStrategy extends BuglyStrategy {

        /* renamed from: a, reason: collision with root package name */
        private CrashHandleCallback f129a;

        public UserStrategy(Context context) {
        }

        @Override // com.tencent.bugly.BuglyStrategy
        public synchronized CrashHandleCallback getCrashHandleCallback() {
            return this.f129a;
        }

        public synchronized void setCrashHandleCallback(CrashHandleCallback crashHandleCallback) {
            this.f129a = crashHandleCallback;
        }
    }
}
