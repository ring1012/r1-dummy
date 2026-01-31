package com.unisound.vui.common.config;

import android.content.Context;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import com.unisound.b.f;
import com.unisound.common.y;
import com.unisound.vui.common.file.FileHelper;
import com.unisound.vui.util.LogMgr;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.io.IOUtils;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    private static boolean f366a = false;
    private static HashMap<String, String> b = new HashMap<>();
    private static String c = "";
    private static String d = "";
    private static String e = "";
    private static ArrayList<InterfaceC0010a> f = new ArrayList<>();

    /* renamed from: com.unisound.vui.common.config.a$a, reason: collision with other inner class name */
    public interface InterfaceC0010a {
    }

    public static float a(String str, float f2) {
        return Float.valueOf(a(str, String.valueOf(f2))).floatValue();
    }

    public static int a(String str, int i) {
        return Integer.valueOf(a(str, String.valueOf(i))).intValue();
    }

    public static String a(String str) {
        String strTrim = str == null ? "" : str.trim();
        return strTrim.contains("%sdcard%") ? strTrim.replaceAll("%sdcard%", c) : strTrim.contains("%files%") ? strTrim.replaceAll("%files%", d) : strTrim.contains("%cache%") ? strTrim.replaceAll("%cache%", e) : strTrim;
    }

    public static String a(String str, String str2) {
        return (b == null || !b.containsKey(str)) ? str2 : b.get(str);
    }

    private static void a() {
        LogMgr.d("ANTPrivatePreference", "printConfig");
        if (b != null) {
            for (String str : b.keySet()) {
                LogMgr.d("ANTPrivatePreference", "[" + str + "=" + b.get(str) + "]");
            }
        }
    }

    public static void a(Context context) throws IOException {
        LogMgr.d("ANTPrivatePreference", y.y);
        if (context == null || f366a) {
            return;
        }
        d = context.getFilesDir().getAbsolutePath();
        e = context.getCacheDir().getAbsolutePath();
        if (Process.myUid() == 1000) {
            LogMgr.e("ANTPrivatePreference", "Process is SYSTEM Process, Can't use getExternalStorageDirectory, will use %files% folder");
            c = d;
        } else {
            c = Environment.getExternalStorageDirectory().getPath();
        }
        File file = new File("system", "unisound/config");
        File file2 = new File(new File("sdcard", "unisound/config").getAbsolutePath(), "config.mg");
        File file3 = new File(file.getAbsolutePath(), "config.mg");
        if (file2.exists()) {
            try {
                LogMgr.d("ANTPrivatePreference", "read sdcard file");
                a(new FileInputStream(file2), context);
                return;
            } catch (FileNotFoundException e2) {
                return;
            }
        }
        if (!file3.exists()) {
            FileHelper.copyFileFromAssets(context, "config", "config.mg", file);
        }
        if (file3.exists()) {
            try {
                LogMgr.d("ANTPrivatePreference", "read system file");
                a(new FileInputStream(file3), context);
            } catch (FileNotFoundException e3) {
            }
        } else {
            try {
                LogMgr.d("ANTPrivatePreference", "read assest file");
                a(context.getAssets().open("config/config.mg"), context);
            } catch (IOException e4) {
            }
        }
        a();
    }

    public static void a(InterfaceC0010a interfaceC0010a) {
        f.add(interfaceC0010a);
    }

    private static void a(InputStream inputStream, Context context) throws IOException {
        if (inputStream == null) {
            return;
        }
        try {
            if (inputStream.available() > 0) {
                byte[] bArr = new byte[inputStream.available()];
                inputStream.read(bArr);
                inputStream.close();
                String str = new String(bArr, f.b);
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                str.replaceAll(IOUtils.LINE_SEPARATOR_WINDOWS, "\n");
                String[] strArrSplit = str.split("\n");
                if (strArrSplit.length > 0) {
                    for (String str2 : strArrSplit) {
                        if (!TextUtils.isEmpty(str)) {
                            String strTrim = str2.trim();
                            if (strTrim.indexOf(MqttTopic.MULTI_LEVEL_WILDCARD) != 0) {
                                String[] strArrSplit2 = strTrim.split("=");
                                if (strArrSplit2.length > 1) {
                                    String str3 = strArrSplit2.length >= 1 ? strArrSplit2[0] : "";
                                    b.put(str3 == null ? "" : str3.trim(), a(strArrSplit2.length >= 2 ? strArrSplit2[1] : ""));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e2) {
        }
    }

    public static boolean a(String str, boolean z) {
        return Boolean.valueOf(a(str, String.valueOf(z))).booleanValue();
    }
}
