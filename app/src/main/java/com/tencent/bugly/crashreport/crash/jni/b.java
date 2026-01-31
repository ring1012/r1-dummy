package com.tencent.bugly.crashreport.crash.jni;

import android.content.Context;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import com.unisound.b.f;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class b {

    /* renamed from: a, reason: collision with root package name */
    private static String f172a = null;

    private static Map<String, Integer> c(String str) {
        if (str == null) {
            return null;
        }
        try {
            HashMap map = new HashMap();
            for (String str2 : str.split(",")) {
                String[] strArrSplit = str2.split(":");
                if (strArrSplit.length != 2) {
                    x.e("error format at %s", str2);
                    return null;
                }
                map.put(strArrSplit[0], Integer.valueOf(Integer.parseInt(strArrSplit[1])));
            }
            return map;
        } catch (Exception e) {
            x.e("error format intStateStr %s", str);
            e.printStackTrace();
            return null;
        }
    }

    protected static String a(String str) {
        if (str == null) {
            return "";
        }
        String[] strArrSplit = str.split("\n");
        if (strArrSplit != null && strArrSplit.length != 0) {
            StringBuilder sb = new StringBuilder();
            for (String str2 : strArrSplit) {
                if (!str2.contains("java.lang.Thread.getStackTrace(")) {
                    sb.append(str2).append("\n");
                }
            }
            return sb.toString();
        }
        return str;
    }

    private static CrashDetailBean a(Context context, Map<String, String> map, NativeExceptionHandler nativeExceptionHandler) {
        if (map == null) {
            return null;
        }
        if (com.tencent.bugly.crashreport.common.info.a.a(context) == null) {
            x.e("abnormal com info not created", new Object[0]);
            return null;
        }
        String str = map.get("intStateStr");
        if (str == null || str.trim().length() <= 0) {
            x.e("no intStateStr", new Object[0]);
            return null;
        }
        Map<String, Integer> mapC = c(str);
        if (mapC == null) {
            x.e("parse intSateMap fail", Integer.valueOf(map.size()));
            return null;
        }
        try {
            mapC.get("sino").intValue();
            mapC.get("sud").intValue();
            String str2 = map.get("soVersion");
            if (str2 == null) {
                x.e("error format at version", new Object[0]);
                return null;
            }
            String str3 = map.get("errorAddr");
            String str4 = str3 == null ? "unknown" : str3;
            String str5 = map.get("codeMsg");
            String str6 = str5 == null ? "unknown" : str5;
            String str7 = map.get("tombPath");
            String str8 = str7 == null ? "unknown" : str7;
            String str9 = map.get("signalName");
            String str10 = str9 == null ? "unknown" : str9;
            map.get("errnoMsg");
            String str11 = map.get("stack");
            String str12 = str11 == null ? "unknown" : str11;
            String str13 = map.get("jstack");
            if (str13 != null) {
                str12 = str12 + "java:\n" + str13;
            }
            Integer num = mapC.get("sico");
            if (num != null && num.intValue() > 0) {
                str10 = str10 + "(" + str6 + ")";
                str6 = "KERNEL";
            }
            String str14 = map.get("nativeLog");
            byte[] bArrA = null;
            if (str14 != null && !str14.isEmpty()) {
                bArrA = z.a((File) null, str14, "BuglyNativeLog.txt");
            }
            String str15 = map.get("sendingProcess");
            String str16 = str15 == null ? "unknown" : str15;
            Integer num2 = mapC.get("spd");
            if (num2 != null) {
                str16 = str16 + "(" + num2 + ")";
            }
            String str17 = map.get("threadName");
            String str18 = str17 == null ? "unknown" : str17;
            Integer num3 = mapC.get("et");
            if (num3 != null) {
                str18 = str18 + "(" + num3 + ")";
            }
            String str19 = map.get("processName");
            String str20 = str19 == null ? "unknown" : str19;
            Integer num4 = mapC.get("ep");
            if (num4 != null) {
                str20 = str20 + "(" + num4 + ")";
            }
            HashMap map2 = null;
            String str21 = map.get("key-value");
            if (str21 != null) {
                map2 = new HashMap();
                String[] strArrSplit = str21.split("\n");
                for (String str22 : strArrSplit) {
                    String[] strArrSplit2 = str22.split("=");
                    if (strArrSplit2.length == 2) {
                        map2.put(strArrSplit2[0], strArrSplit2[1]);
                    }
                }
            }
            CrashDetailBean crashDetailBeanPackageCrashDatas = nativeExceptionHandler.packageCrashDatas(str20, str18, (mapC.get("etms").intValue() / 1000) + (mapC.get("ets").intValue() * 1000), str10, str4, a(str12), str6, str16, str8, map.get("sysLogPath"), str2, bArrA, map2, false);
            if (crashDetailBeanPackageCrashDatas != null) {
                String str23 = map.get("userId");
                if (str23 != null) {
                    x.c("[Native record info] userId: %s", str23);
                    crashDetailBeanPackageCrashDatas.m = str23;
                }
                String str24 = map.get("sysLog");
                if (str24 != null) {
                    crashDetailBeanPackageCrashDatas.w = str24;
                }
                String str25 = map.get("appVersion");
                if (str25 != null) {
                    x.c("[Native record info] appVersion: %s", str25);
                    crashDetailBeanPackageCrashDatas.f = str25;
                }
                String str26 = map.get("isAppForeground");
                if (str26 != null) {
                    x.c("[Native record info] isAppForeground: %s", str26);
                    crashDetailBeanPackageCrashDatas.M = str26.equalsIgnoreCase("true");
                }
                String str27 = map.get("launchTime");
                if (str27 != null) {
                    x.c("[Native record info] launchTime: %s", str27);
                    try {
                        crashDetailBeanPackageCrashDatas.L = Long.parseLong(str27);
                    } catch (NumberFormatException e) {
                        if (!x.a(e)) {
                            e.printStackTrace();
                        }
                    }
                }
                crashDetailBeanPackageCrashDatas.y = null;
                crashDetailBeanPackageCrashDatas.k = true;
            }
            return crashDetailBeanPackageCrashDatas;
        } catch (Throwable th) {
            x.e("error format", new Object[0]);
            th.printStackTrace();
            return null;
        }
    }

    private static String a(BufferedInputStream bufferedInputStream) throws IOException {
        if (bufferedInputStream == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        while (true) {
            int i = bufferedInputStream.read();
            if (i == -1) {
                return null;
            }
            if (i == 0) {
                return sb.toString();
            }
            sb.append((char) i);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v4, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v6 */
    public static CrashDetailBean a(Context context, String str, NativeExceptionHandler nativeExceptionHandler) throws Throwable {
        BufferedInputStream bufferedInputStream;
        CrashDetailBean crashDetailBeanA = null;
        if (context == null || str == null || nativeExceptionHandler == null) {
            x.e("get eup record file args error", new Object[0]);
        } else {
            File file = new File(str, "rqd_record.eup");
            if (file.exists()) {
                BufferedInputStream bufferedInputStreamCanRead = file.canRead();
                try {
                    if (bufferedInputStreamCanRead != 0) {
                        try {
                            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                        } catch (IOException e) {
                            e = e;
                            bufferedInputStream = null;
                        } catch (Throwable th) {
                            bufferedInputStreamCanRead = 0;
                            th = th;
                            if (bufferedInputStreamCanRead != 0) {
                                try {
                                    bufferedInputStreamCanRead.close();
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                            }
                            throw th;
                        }
                        try {
                            String strA = a(bufferedInputStream);
                            if (strA == null || !strA.equals("NATIVE_RQD_REPORT")) {
                                x.e("record read fail! %s", strA);
                                try {
                                    bufferedInputStream.close();
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                            } else {
                                HashMap map = new HashMap();
                                String str2 = null;
                                while (true) {
                                    String strA2 = a(bufferedInputStream);
                                    if (strA2 == null) {
                                        break;
                                    }
                                    if (str2 == null) {
                                        str2 = strA2;
                                    } else {
                                        map.put(str2, strA2);
                                        str2 = null;
                                    }
                                }
                                if (str2 != null) {
                                    x.e("record not pair! drop! %s", str2);
                                    try {
                                        bufferedInputStream.close();
                                    } catch (IOException e4) {
                                        e4.printStackTrace();
                                    }
                                } else {
                                    crashDetailBeanA = a(context, map, nativeExceptionHandler);
                                    try {
                                        bufferedInputStream.close();
                                    } catch (IOException e5) {
                                        e5.printStackTrace();
                                    }
                                }
                            }
                        } catch (IOException e6) {
                            e = e6;
                            e.printStackTrace();
                            if (bufferedInputStream != null) {
                                try {
                                    bufferedInputStream.close();
                                } catch (IOException e7) {
                                    e7.printStackTrace();
                                }
                            }
                            return crashDetailBeanA;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
        }
        return crashDetailBeanA;
    }

    private static String b(String str, String str2) throws IOException {
        String string = null;
        BufferedReader bufferedReaderA = z.a(str, "reg_record.txt");
        try {
            if (bufferedReaderA != null) {
                try {
                    StringBuilder sb = new StringBuilder();
                    String line = bufferedReaderA.readLine();
                    if (line != null && line.startsWith(str2)) {
                        int length = 0;
                        int i = 18;
                        int i2 = 0;
                        while (true) {
                            String line2 = bufferedReaderA.readLine();
                            if (line2 == null) {
                                break;
                            }
                            if (i2 % 4 == 0) {
                                if (i2 > 0) {
                                    sb.append("\n");
                                }
                                sb.append("  ");
                            } else {
                                if (line2.length() > 16) {
                                    i = 28;
                                }
                                sb.append("                ".substring(0, i - length));
                            }
                            length = line2.length();
                            sb.append(line2);
                            i2++;
                        }
                        sb.append("\n");
                        string = sb.toString();
                        if (bufferedReaderA != null) {
                            try {
                                bufferedReaderA.close();
                            } catch (Exception e) {
                                x.a(e);
                            }
                        }
                    } else if (bufferedReaderA != null) {
                        try {
                            bufferedReaderA.close();
                        } catch (Exception e2) {
                            x.a(e2);
                        }
                    }
                } catch (Throwable th) {
                    x.a(th);
                    if (bufferedReaderA != null) {
                        try {
                            bufferedReaderA.close();
                        } catch (Exception e3) {
                            x.a(e3);
                        }
                    }
                }
            }
            return string;
        } catch (Throwable th2) {
            if (bufferedReaderA != null) {
                try {
                    bufferedReaderA.close();
                } catch (Exception e4) {
                    x.a(e4);
                }
            }
            throw th2;
        }
    }

    private static String c(String str, String str2) throws IOException {
        String string = null;
        BufferedReader bufferedReaderA = z.a(str, "map_record.txt");
        if (bufferedReaderA != null) {
            try {
                try {
                    StringBuilder sb = new StringBuilder();
                    String line = bufferedReaderA.readLine();
                    if (line != null && line.startsWith(str2)) {
                        while (true) {
                            String line2 = bufferedReaderA.readLine();
                            if (line2 == null) {
                                break;
                            }
                            sb.append("  ");
                            sb.append(line2);
                            sb.append("\n");
                        }
                        string = sb.toString();
                        if (bufferedReaderA != null) {
                            try {
                                bufferedReaderA.close();
                            } catch (Exception e) {
                                x.a(e);
                            }
                        }
                    } else if (bufferedReaderA != null) {
                        try {
                            bufferedReaderA.close();
                        } catch (Exception e2) {
                            x.a(e2);
                        }
                    }
                } catch (Throwable th) {
                    x.a(th);
                    if (bufferedReaderA != null) {
                        try {
                            bufferedReaderA.close();
                        } catch (Exception e3) {
                            x.a(e3);
                        }
                    }
                }
            } catch (Throwable th2) {
                if (bufferedReaderA != null) {
                    try {
                        bufferedReaderA.close();
                    } catch (Exception e4) {
                        x.a(e4);
                    }
                }
                throw th2;
            }
        }
        return string;
    }

    public static String a(String str, String str2) throws IOException {
        if (str == null || str2 == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String strB = b(str, str2);
        if (strB != null && !strB.isEmpty()) {
            sb.append("Register infos:\n");
            sb.append(strB);
        }
        String strC = c(str, str2);
        if (strC != null && !strC.isEmpty()) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append("System SO infos:\n");
            sb.append(strC);
        }
        return sb.toString();
    }

    public static String b(String str) {
        if (str == null) {
            return null;
        }
        File file = new File(str, "backup_record.txt");
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        return null;
    }

    public static void a(boolean z, String str) {
        File[] fileArrListFiles;
        if (str != null) {
            File file = new File(str, "rqd_record.eup");
            if (file.exists() && file.canWrite()) {
                file.delete();
                x.c("delete record file %s", file.getAbsoluteFile());
            }
            File file2 = new File(str, "reg_record.txt");
            if (file2.exists() && file2.canWrite()) {
                file2.delete();
                x.c("delete record file %s", file2.getAbsoluteFile());
            }
            File file3 = new File(str, "map_record.txt");
            if (file3.exists() && file3.canWrite()) {
                file3.delete();
                x.c("delete record file %s", file3.getAbsoluteFile());
            }
            File file4 = new File(str, "backup_record.txt");
            if (file4.exists() && file4.canWrite()) {
                file4.delete();
                x.c("delete record file %s", file4.getAbsoluteFile());
            }
            if (f172a != null) {
                File file5 = new File(f172a);
                if (file5.exists() && file5.canWrite()) {
                    file5.delete();
                    x.c("delete record file %s", file5.getAbsoluteFile());
                }
            }
            if (z) {
                File file6 = new File(str);
                if (file6.canRead() && file6.isDirectory() && (fileArrListFiles = file6.listFiles()) != null) {
                    for (File file7 : fileArrListFiles) {
                        if (file7.canRead() && file7.canWrite() && file7.length() == 0) {
                            file7.delete();
                            x.c("delete invalid record file %s", file7.getAbsoluteFile());
                        }
                    }
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v2, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r2v4 */
    public static String a(String str, int i, String str2) throws Throwable {
        BufferedReader bufferedReader;
        String string;
        if (str == null || i <= 0) {
            return null;
        }
        File file = new File(str);
        if (!file.exists() || !file.canRead()) {
            return null;
        }
        f172a = str;
        ?? r2 = "Read system log from native record file(length: %s bytes): %s";
        x.a("Read system log from native record file(length: %s bytes): %s", Long.valueOf(file.length()), file.getAbsolutePath());
        try {
            if (str2 == null) {
                string = z.a(new File(str));
            } else {
                try {
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), f.b));
                    while (true) {
                        try {
                            String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            if (Pattern.compile(str2 + "[ ]*:").matcher(line).find()) {
                                sb.append(line);
                                sb.append("\n");
                            }
                        } catch (Throwable th) {
                            th = th;
                            x.a(th);
                            if (bufferedReader == null) {
                                return null;
                            }
                            try {
                                bufferedReader.close();
                                return null;
                            } catch (Exception e) {
                                x.a(e);
                                return null;
                            }
                        }
                    }
                    string = sb.toString();
                    try {
                        bufferedReader.close();
                    } catch (Exception e2) {
                        x.a(e2);
                    }
                } catch (Throwable th2) {
                    r2 = 0;
                    th = th2;
                    if (r2 != 0) {
                        try {
                            r2.close();
                        } catch (Exception e3) {
                            x.a(e3);
                        }
                    }
                    throw th;
                }
            }
            if (string != null && string.length() > i) {
                return string.substring(string.length() - i);
            }
            return string;
        } catch (Throwable th3) {
            th = th3;
        }
    }
}
