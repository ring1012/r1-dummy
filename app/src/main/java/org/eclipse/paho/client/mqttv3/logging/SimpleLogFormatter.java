package org.eclipse.paho.client.mqttv3.logging;

import cn.yunzhisheng.common.PinyinConverter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/* loaded from: classes.dex */
public class SimpleLogFormatter extends Formatter {

    /* renamed from: a, reason: collision with root package name */
    private static final String f511a = System.getProperty("line.separator");

    public static String left(String str, int i, char c) {
        if (str.length() >= i) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(i);
        stringBuffer.append(str);
        int length = i - str.length();
        while (true) {
            length--;
            if (length < 0) {
                return stringBuffer.toString();
            }
            stringBuffer.append(c);
        }
    }

    @Override // java.util.logging.Formatter
    public String format(LogRecord logRecord) throws Throwable {
        PrintWriter printWriter;
        StringWriter stringWriter;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(logRecord.getLevel().getName()).append("\t");
        stringBuffer.append(new StringBuffer(String.valueOf(MessageFormat.format("{0, date, yy-MM-dd} {0, time, kk:mm:ss.SSSS} ", new Date(logRecord.getMillis())))).append("\t").toString());
        String sourceClassName = logRecord.getSourceClassName();
        String strSubstring = "";
        if (sourceClassName != null) {
            int length = sourceClassName.length();
            strSubstring = length > 20 ? logRecord.getSourceClassName().substring(length - 19) : new StringBuffer().append(sourceClassName).append(new char[]{' '}, 0, 1).toString();
        }
        stringBuffer.append(strSubstring).append("\t").append(PinyinConverter.PINYIN_SEPARATOR);
        stringBuffer.append(left(logRecord.getSourceMethodName(), 23, ' ')).append("\t");
        stringBuffer.append(logRecord.getThreadID()).append("\t");
        stringBuffer.append(formatMessage(logRecord)).append(f511a);
        if (logRecord.getThrown() != null) {
            stringBuffer.append("Throwable occurred: ");
            Throwable thrown = logRecord.getThrown();
            try {
                stringWriter = new StringWriter();
                printWriter = new PrintWriter(stringWriter);
            } catch (Throwable th) {
                th = th;
                printWriter = null;
            }
            try {
                thrown.printStackTrace(printWriter);
                stringBuffer.append(stringWriter.toString());
                if (printWriter != null) {
                    try {
                        printWriter.close();
                    } catch (Exception e) {
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                if (printWriter != null) {
                    try {
                        printWriter.close();
                    } catch (Exception e2) {
                    }
                }
                throw th;
            }
        }
        return stringBuffer.toString();
    }
}
