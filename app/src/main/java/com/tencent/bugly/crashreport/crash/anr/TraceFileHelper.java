package com.tencent.bugly.crashreport.crash.anr;

import cn.yunzhisheng.common.PinyinConverter;
import com.tencent.bugly.proguard.x;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class TraceFileHelper {

    /* compiled from: BUGLY */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        public long f152a;
        public String b;
        public long c;
        public Map<String, String[]> d;
    }

    /* compiled from: BUGLY */
    public interface b {
        boolean a(long j);

        boolean a(long j, long j2, String str);

        boolean a(String str, int i, String str2, String str3);
    }

    public static a readTargetDumpInfo(String str, String str2, final boolean z) throws Throwable {
        if (str == null || str2 == null) {
            return null;
        }
        final a aVar = new a();
        readTraceFile(str2, new b() { // from class: com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.1
            @Override // com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.b
            public final boolean a(String str3, int i, String str4, String str5) {
                x.c("new thread %s", str3);
                if (aVar.f152a > 0 && aVar.c > 0 && aVar.b != null) {
                    if (aVar.d == null) {
                        aVar.d = new HashMap();
                    }
                    aVar.d.put(str3, new String[]{str4, str5, new StringBuilder().append(i).toString()});
                }
                return true;
            }

            @Override // com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.b
            public final boolean a(long j, long j2, String str3) {
                x.c("new process %s", str3);
                if (!str3.equals(str3)) {
                    return true;
                }
                aVar.f152a = j;
                aVar.b = str3;
                aVar.c = j2;
                return z;
            }

            @Override // com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.b
            public final boolean a(long j) {
                x.c("process end %d", Long.valueOf(j));
                return aVar.f152a <= 0 || aVar.c <= 0 || aVar.b == null;
            }
        });
        if (aVar.f152a <= 0 || aVar.c <= 0 || aVar.b == null) {
            return null;
        }
        return aVar;
    }

    public static a readFirstDumpInfo(String str, final boolean z) throws Throwable {
        if (str == null) {
            x.e("path:%s", str);
            return null;
        }
        final a aVar = new a();
        readTraceFile(str, new b() { // from class: com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.2
            @Override // com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.b
            public final boolean a(String str2, int i, String str3, String str4) {
                x.c("new thread %s", str2);
                if (aVar.d == null) {
                    aVar.d = new HashMap();
                }
                aVar.d.put(str2, new String[]{str3, str4, new StringBuilder().append(i).toString()});
                return true;
            }

            @Override // com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.b
            public final boolean a(long j, long j2, String str2) {
                x.c("new process %s", str2);
                aVar.f152a = j;
                aVar.b = str2;
                aVar.c = j2;
                return z;
            }

            @Override // com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.b
            public final boolean a(long j) {
                x.c("process end %d", Long.valueOf(j));
                return false;
            }
        });
        if (aVar.f152a > 0 && aVar.c > 0 && aVar.b != null) {
            return aVar;
        }
        x.e("first dump error %s", aVar.f152a + PinyinConverter.PINYIN_SEPARATOR + aVar.c + PinyinConverter.PINYIN_SEPARATOR + aVar.b);
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:85:0x01c1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void readTraceFile(java.lang.String r12, com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.b r13) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 472
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.anr.TraceFileHelper.readTraceFile(java.lang.String, com.tencent.bugly.crashreport.crash.anr.TraceFileHelper$b):void");
    }

    private static Object[] a(BufferedReader bufferedReader, Pattern... patternArr) throws IOException {
        if (bufferedReader == null || patternArr == null) {
            return null;
        }
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                return null;
            }
            for (Pattern pattern : patternArr) {
                if (pattern.matcher(line).matches()) {
                    return new Object[]{pattern, line};
                }
            }
        }
    }

    private static String a(BufferedReader bufferedReader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 3; i++) {
            String line = bufferedReader.readLine();
            if (line == null) {
                return null;
            }
            stringBuffer.append(line + "\n");
        }
        return stringBuffer.toString();
    }

    private static String b(BufferedReader bufferedReader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null || line.trim().length() <= 0) {
                break;
            }
            stringBuffer.append(line + "\n");
        }
        return stringBuffer.toString();
    }
}
