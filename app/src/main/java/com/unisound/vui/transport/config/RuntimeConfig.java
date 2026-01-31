package com.unisound.vui.transport.config;

/* loaded from: classes.dex */
public final class RuntimeConfig {
    private static boolean isOnceConnectSuc = false;
    private static boolean isConnectedWifi = false;
    private float fixAsrResBenchmark = -6.25f;
    private float wakUpBenchmark = -3.0f;
    private boolean isUseOutRecordSource = false;

    public static boolean isConnectedWifi() {
        return isConnectedWifi;
    }

    public static boolean isOnceConnectSuc() {
        return isOnceConnectSuc;
    }

    public static void setConnectedWifi(boolean isConnectedWifi2) {
        isConnectedWifi = isConnectedWifi2;
    }

    public static void setOnceConnectSuc(boolean isOnceConnectSuc2) {
        isOnceConnectSuc = isOnceConnectSuc2;
    }

    public float getFixAsrResBenchmark() {
        return this.fixAsrResBenchmark;
    }

    public float getWakUpBenchmark() {
        return this.wakUpBenchmark;
    }

    public boolean isUseOutRecordSource() {
        return this.isUseOutRecordSource;
    }

    public void setFixAsrResBenchmark(float fixAsrResBenchmark) {
        this.fixAsrResBenchmark = fixAsrResBenchmark;
    }

    public void setIsUseOutRecordSource(boolean isUseOutRecordSource) {
        this.isUseOutRecordSource = isUseOutRecordSource;
    }

    public void setWakUpBenchmark(float wakUpBenchmark) {
        this.wakUpBenchmark = wakUpBenchmark;
    }
}
