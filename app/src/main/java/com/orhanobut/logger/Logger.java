package com.orhanobut.logger;

/* loaded from: classes.dex */
public final class Logger {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    private static final String DEFAULT_TAG = "PRETTYLOGGER";
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    private static Printer printer = new LoggerPrinter();

    private Logger() {
    }

    public static Settings init() {
        return init(DEFAULT_TAG);
    }

    public static Settings init(String tag) {
        printer = new LoggerPrinter();
        return printer.init(tag);
    }

    public static void resetSettings() {
        printer.resetSettings();
    }

    public static Printer t(String tag) {
        return printer.t(tag, printer.getSettings().getMethodCount());
    }

    public static Printer t(int methodCount) {
        return printer.t(null, methodCount);
    }

    public static Printer t(String tag, int methodCount) {
        return printer.t(tag, methodCount);
    }

    public static void log(int priority, String tag, String message, Throwable throwable) {
        printer.log(priority, tag, message, throwable);
    }

    public static void d(String message, Object... args) {
        printer.d(message, args);
    }

    public static void d(Object object) {
        printer.d(object);
    }

    public static void e(String message, Object... args) {
        printer.e(null, message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        printer.e(throwable, message, args);
    }

    public static void i(String message, Object... args) {
        printer.i(message, args);
    }

    public static void v(String message, Object... args) {
        printer.v(message, args);
    }

    public static void w(String message, Object... args) {
        printer.w(message, args);
    }

    public static void wtf(String message, Object... args) {
        printer.wtf(message, args);
    }

    public static void json(String json) {
        printer.json(json);
    }

    public static void xml(String xml) {
        printer.xml(xml);
    }
}
