package com.orhanobut.logger;

/* loaded from: classes.dex */
public final class Settings {
    private LogAdapter logAdapter;
    private int methodCount = 2;
    private boolean showThreadInfo = true;
    private int methodOffset = 0;
    private LogLevel logLevel = LogLevel.FULL;

    public Settings hideThreadInfo() {
        this.showThreadInfo = false;
        return this;
    }

    public Settings methodCount(int methodCount) {
        if (methodCount < 0) {
            methodCount = 0;
        }
        this.methodCount = methodCount;
        return this;
    }

    public Settings logLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public Settings methodOffset(int offset) {
        this.methodOffset = offset;
        return this;
    }

    public Settings logAdapter(LogAdapter logAdapter) {
        this.logAdapter = logAdapter;
        return this;
    }

    public int getMethodCount() {
        return this.methodCount;
    }

    public boolean isShowThreadInfo() {
        return this.showThreadInfo;
    }

    public LogLevel getLogLevel() {
        return this.logLevel;
    }

    public int getMethodOffset() {
        return this.methodOffset;
    }

    public LogAdapter getLogAdapter() {
        if (this.logAdapter == null) {
            this.logAdapter = new AndroidLogAdapter();
        }
        return this.logAdapter;
    }

    public void reset() {
        this.methodCount = 2;
        this.methodOffset = 0;
        this.showThreadInfo = true;
        this.logLevel = LogLevel.FULL;
    }
}
