package com.orhanobut.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/* loaded from: classes.dex */
final class Helper {
    private Helper() {
    }

    static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    static boolean equals(CharSequence a2, CharSequence b) {
        int length;
        if (a2 == b) {
            return true;
        }
        if (a2 == null || b == null || (length = a2.length()) != b.length()) {
            return false;
        }
        if ((a2 instanceof String) && (b instanceof String)) {
            return a2.equals(b);
        }
        for (int i = 0; i < length; i++) {
            if (a2.charAt(i) != b.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        for (Throwable t = tr; t != null; t = t.getCause()) {
            if (t instanceof UnknownHostException) {
                return "";
            }
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}
