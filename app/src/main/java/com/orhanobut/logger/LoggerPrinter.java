package com.orhanobut.logger;

import cn.yunzhisheng.common.PinyinConverter;
import com.unisound.ant.device.sessionlayer.DialogProfile;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
final class LoggerPrinter implements Printer {
    private static final int ASSERT = 7;
    private static final String BOTTOM_BORDER = "╚════════════════════════════════════════════════════════════════════════════════════════";
    private static final char BOTTOM_LEFT_CORNER = 9562;
    private static final int CHUNK_SIZE = 4000;
    private static final int DEBUG = 3;
    private static final String DEFAULT_TAG = "PRETTYLOGGER";
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
    private static final int ERROR = 6;
    private static final char HORIZONTAL_DOUBLE_LINE = 9553;
    private static final int INFO = 4;
    private static final int JSON_INDENT = 2;
    private static final String MIDDLE_BORDER = "╟────────────────────────────────────────────────────────────────────────────────────────";
    private static final char MIDDLE_CORNER = 9567;
    private static final int MIN_STACK_OFFSET = 3;
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
    private static final String TOP_BORDER = "╔════════════════════════════════════════════════════════════════════════════════════════";
    private static final char TOP_LEFT_CORNER = 9556;
    private static final int VERBOSE = 2;
    private static final int WARN = 5;
    private String tag;
    private final ThreadLocal<String> localTag = new ThreadLocal<>();
    private final ThreadLocal<Integer> localMethodCount = new ThreadLocal<>();
    private final Settings settings = new Settings();

    public LoggerPrinter() {
        init(DEFAULT_TAG);
    }

    @Override // com.orhanobut.logger.Printer
    public Settings init(String tag) {
        if (tag == null) {
            throw new NullPointerException("tag may not be null");
        }
        if (tag.trim().length() == 0) {
            throw new IllegalStateException("tag may not be empty");
        }
        this.tag = tag;
        return this.settings;
    }

    @Override // com.orhanobut.logger.Printer
    public Settings getSettings() {
        return this.settings;
    }

    @Override // com.orhanobut.logger.Printer
    public Printer t(String tag, int methodCount) {
        if (tag != null) {
            this.localTag.set(tag);
        }
        this.localMethodCount.set(Integer.valueOf(methodCount));
        return this;
    }

    @Override // com.orhanobut.logger.Printer
    public void d(String message, Object... args) {
        log(3, (Throwable) null, message, args);
    }

    @Override // com.orhanobut.logger.Printer
    public void d(Object object) {
        String message;
        if (object.getClass().isArray()) {
            message = Arrays.deepToString((Object[]) object);
        } else {
            message = object.toString();
        }
        log(3, (Throwable) null, message, new Object[0]);
    }

    @Override // com.orhanobut.logger.Printer
    public void e(String message, Object... args) {
        e(null, message, args);
    }

    @Override // com.orhanobut.logger.Printer
    public void e(Throwable throwable, String message, Object... args) {
        log(6, throwable, message, args);
    }

    @Override // com.orhanobut.logger.Printer
    public void w(String message, Object... args) {
        log(5, (Throwable) null, message, args);
    }

    @Override // com.orhanobut.logger.Printer
    public void i(String message, Object... args) {
        log(4, (Throwable) null, message, args);
    }

    @Override // com.orhanobut.logger.Printer
    public void v(String message, Object... args) {
        log(2, (Throwable) null, message, args);
    }

    @Override // com.orhanobut.logger.Printer
    public void wtf(String message, Object... args) {
        log(7, (Throwable) null, message, args);
    }

    @Override // com.orhanobut.logger.Printer
    public void json(String json) throws JSONException {
        if (Helper.isEmpty(json)) {
            d("Empty/Null json content");
            return;
        }
        try {
            String json2 = json.trim();
            if (json2.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json2);
                String message = jsonObject.toString(2);
                d(message);
            } else if (json2.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json2);
                String message2 = jsonArray.toString(2);
                d(message2);
            } else {
                e("Invalid Json", new Object[0]);
            }
        } catch (JSONException e) {
            e("Invalid Json", new Object[0]);
        }
    }

    @Override // com.orhanobut.logger.Printer
    public void xml(String xml) throws TransformerException, IllegalArgumentException {
        if (Helper.isEmpty(xml)) {
            d("Empty/Null xml content");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", DialogProfile.DIALOG_FINISH);
            transformer.transform(xmlInput, xmlOutput);
            d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            e("Invalid xml", new Object[0]);
        }
    }

    @Override // com.orhanobut.logger.Printer
    public synchronized void log(int priority, String tag, String message, Throwable throwable) {
        if (this.settings.getLogLevel() != LogLevel.NONE) {
            if (throwable != null && message != null) {
                message = message + " : " + Helper.getStackTraceString(throwable);
            }
            if (throwable != null && message == null) {
                message = Helper.getStackTraceString(throwable);
            }
            if (message == null) {
                message = "No message/exception is set";
            }
            int methodCount = getMethodCount();
            if (Helper.isEmpty(message)) {
                message = "Empty/NULL log message";
            }
            logTopBorder(priority, tag);
            logHeaderContent(priority, tag, methodCount);
            byte[] bytes = message.getBytes();
            int length = bytes.length;
            if (length <= CHUNK_SIZE) {
                if (methodCount > 0) {
                    logDivider(priority, tag);
                }
                logContent(priority, tag, message);
                logBottomBorder(priority, tag);
            } else {
                if (methodCount > 0) {
                    logDivider(priority, tag);
                }
                for (int i = 0; i < length; i += CHUNK_SIZE) {
                    int count = Math.min(length - i, CHUNK_SIZE);
                    logContent(priority, tag, new String(bytes, i, count));
                }
                logBottomBorder(priority, tag);
            }
        }
    }

    @Override // com.orhanobut.logger.Printer
    public void resetSettings() {
        this.settings.reset();
    }

    private synchronized void log(int priority, Throwable throwable, String msg, Object... args) {
        if (this.settings.getLogLevel() != LogLevel.NONE) {
            String tag = getTag();
            String message = createMessage(msg, args);
            log(priority, tag, message, throwable);
        }
    }

    private void logTopBorder(int logType, String tag) {
        logChunk(logType, tag, TOP_BORDER);
    }

    private void logHeaderContent(int logType, String tag, int methodCount) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if (this.settings.isShowThreadInfo()) {
            logChunk(logType, tag, "║ Thread: " + Thread.currentThread().getName());
            logDivider(logType, tag);
        }
        String level = "";
        int stackOffset = getStackOffset(trace) + this.settings.getMethodOffset();
        if (methodCount + stackOffset > trace.length) {
            methodCount = (trace.length - stackOffset) - 1;
        }
        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + stackOffset;
            if (stackIndex < trace.length) {
                StringBuilder builder = new StringBuilder();
                builder.append("║ ").append(level).append(getSimpleClassName(trace[stackIndex].getClassName())).append(".").append(trace[stackIndex].getMethodName()).append(PinyinConverter.PINYIN_SEPARATOR).append(" (").append(trace[stackIndex].getFileName()).append(":").append(trace[stackIndex].getLineNumber()).append(")");
                level = level + "   ";
                logChunk(logType, tag, builder.toString());
            }
        }
    }

    private void logBottomBorder(int logType, String tag) {
        logChunk(logType, tag, BOTTOM_BORDER);
    }

    private void logDivider(int logType, String tag) {
        logChunk(logType, tag, MIDDLE_BORDER);
    }

    private void logContent(int logType, String tag, String chunk) {
        String[] lines = chunk.split(System.getProperty("line.separator"));
        for (String line : lines) {
            logChunk(logType, tag, "║ " + line);
        }
    }

    private void logChunk(int logType, String tag, String chunk) {
        String finalTag = formatTag(tag);
        switch (logType) {
            case 2:
                this.settings.getLogAdapter().v(finalTag, chunk);
                break;
            case 3:
            default:
                this.settings.getLogAdapter().d(finalTag, chunk);
                break;
            case 4:
                this.settings.getLogAdapter().i(finalTag, chunk);
                break;
            case 5:
                this.settings.getLogAdapter().w(finalTag, chunk);
                break;
            case 6:
                this.settings.getLogAdapter().e(finalTag, chunk);
                break;
            case 7:
                this.settings.getLogAdapter().wtf(finalTag, chunk);
                break;
        }
    }

    private String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    private String formatTag(String tag) {
        return (Helper.isEmpty(tag) || Helper.equals(this.tag, tag)) ? this.tag : this.tag + "-" + tag;
    }

    private String getTag() {
        String tag = this.localTag.get();
        if (tag == null) {
            return this.tag;
        }
        this.localTag.remove();
        return tag;
    }

    private String createMessage(String message, Object... args) {
        return (args == null || args.length == 0) ? message : String.format(message, args);
    }

    private int getMethodCount() {
        Integer count = this.localMethodCount.get();
        int result = this.settings.getMethodCount();
        if (count != null) {
            this.localMethodCount.remove();
            result = count.intValue();
        }
        if (result < 0) {
            throw new IllegalStateException("methodCount cannot be negative");
        }
        return result;
    }

    private int getStackOffset(StackTraceElement[] trace) {
        for (int i = 3; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(LoggerPrinter.class.getName()) && !name.equals(Logger.class.getName())) {
                return i - 1;
            }
        }
        return -1;
    }
}
