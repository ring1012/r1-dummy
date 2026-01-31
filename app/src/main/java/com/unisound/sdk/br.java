package com.unisound.sdk;

import com.unisound.client.SpeechConstants;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
class br {
    private String g;
    private String h;
    private String i;
    private boolean j;
    private String k;
    private String l;
    private String m;

    /* renamed from: a, reason: collision with root package name */
    private int f323a = 0;
    private int b = 1;
    private boolean c = false;
    private boolean d = true;
    private boolean e = false;
    private int f = cn.yunzhisheng.asr.a.U;
    private boolean n = false;
    private boolean o = false;
    private boolean p = false;
    private boolean q = false;

    protected br() {
    }

    protected br(String str) {
        com.unisound.common.v.a(str, l());
    }

    public String a() {
        return this.l;
    }

    public void a(int i) {
        this.f = i;
    }

    public void a(String str) {
        this.l = str;
    }

    public void a(boolean z) {
        this.j = z;
    }

    public String b() {
        return this.k;
    }

    protected void b(int i) {
        this.f323a = i;
    }

    public void b(String str) {
        this.k = str;
    }

    public void b(boolean z) {
        this.e = z;
    }

    public void c(int i) {
        this.b = i;
    }

    public void c(String str) {
        this.h = str;
    }

    public void c(boolean z) {
        this.c = z;
    }

    public boolean c() {
        return this.j;
    }

    public String d() {
        return this.h;
    }

    public void d(String str) {
        this.g = str;
    }

    public void d(boolean z) {
        this.d = z;
    }

    public String e() {
        return this.g;
    }

    protected String e(String str) {
        return "";
    }

    public void e(boolean z) {
        this.n = z;
    }

    public void f(String str) {
        this.m = str;
    }

    public void f(boolean z) {
        this.o = z;
    }

    public boolean f() {
        return this.e;
    }

    public int g() {
        return this.f;
    }

    public void g(String str) {
        this.i = str;
    }

    public void g(boolean z) {
        this.p = z;
    }

    protected int h() {
        return this.f323a;
    }

    public void h(boolean z) {
        this.q = z;
    }

    public int i() {
        return this.b;
    }

    public String j() {
        return this.m;
    }

    public String k() {
        return this.i;
    }

    public Map<String, Integer> l() {
        HashMap map = new HashMap();
        map.put(SpeechConstants.ASR_SERVICE_MODE_JSONKEY, 1001);
        map.put(SpeechConstants.ASR_VOICE_FIELD_JSONKEY, 1003);
        map.put(SpeechConstants.ASR_LANGUAGE_JSONKEY, 1004);
        map.put(SpeechConstants.ASR_DOMAIN_JSONKEY, 1008);
        map.put(SpeechConstants.ASR_SERVER_ADDR_JSONKEY, 1009);
        map.put(SpeechConstants.ASR_VAD_TIMEOUT_FRONTSIL_JSONKEY, 1010);
        map.put(SpeechConstants.ASR_VAD_TIMEOUT_BACKSIL_JSONKEY, 1011);
        map.put(SpeechConstants.ASR_WAKEUP_WORD_JSONKEY, 1013);
        map.put(SpeechConstants.ASR_NET_TIMEOUT_JSONKEY, 1014);
        map.put(SpeechConstants.NLU_ENABLE_JSONKEY, 1020);
        map.put(SpeechConstants.NLU_SCENARIO_JSONKEY, 1021);
        map.put(SpeechConstants.NLU_SERVER_ADDR_JSONKEY, 1022);
        map.put(SpeechConstants.GENERAL_HISTORY_JSONKEY, 1030);
        map.put(SpeechConstants.GENERAL_CITY_JSONKEY, 1031);
        map.put(SpeechConstants.GENERAL_VOICEID_JSONKEY, 1032);
        map.put(SpeechConstants.GENERAL_GPS_JSONKEY, 1033);
        map.put(SpeechConstants.ASR_SAMPLING_RATE_JSONKEY, 1044);
        map.put(SpeechConstants.ASR_OPT_ENGINE_TAG_JSONKEY, Integer.valueOf(SpeechConstants.ASR_OPT_ENGINE_TAG));
        map.put(SpeechConstants.ASR_OPT_RESULT_FILTER_JSONKEY, 1051);
        map.put(SpeechConstants.ASR_OPT_RECORDING_ENABLED_JSONKEY, 1053);
        map.put(SpeechConstants.ASR_OPT_PRINT_LOG_JSONKEY, 1054);
        map.put(SpeechConstants.ASR_OPT_FIX_ASR_CONTINUOUS_JSONKEY, Integer.valueOf(SpeechConstants.ASR_OPT_FIX_ASR_CONTINUOUS));
        map.put(SpeechConstants.ASR_OPT_VAD_ENABLED_JSONKEY, 1061);
        map.put(SpeechConstants.ASR_OPT_FRONT_VAD_ENABLED_JSONKEY, Integer.valueOf(SpeechConstants.ASR_OPT_FRONT_VAD_ENABLED));
        map.put(SpeechConstants.ASR_OPT_SAVE_RECORDING_DATA_JSONKEY, 1058);
        map.put(SpeechConstants.ASR_OPT_RESULT_JSON_JSONKEY, 1059);
        map.put(SpeechConstants.ASR_OPT_FRONT_CACHE_TIME_JSONKEY, 1060);
        map.put(SpeechConstants.ASR_OPT_PRINT_ENGINE_LOG_JSONKEY, 1062);
        map.put(SpeechConstants.ASR_OPT_PRINT_TIME_LOG_JSONKEY, 1063);
        map.put(SpeechConstants.ASR_OPT_FRONT_RESET_CACHE_BYTE_TIME_JSONKEY, 1070);
        map.put(SpeechConstants.ASR_OPT_DEBUG_SAVELOG_JSONKEY, 1071);
        map.put(SpeechConstants.ASR_OPT_DEBUG_POSTLOG_JSONKEY, 1072);
        map.put(SpeechConstants.ASR_OPT_USE_HANDLERTHREAD_JSONKEY, 1073);
        map.put(SpeechConstants.ASR_OPT_SAVE_AFTERVAD_RECORDING_DATA_JSONKEY, 1074);
        map.put(SpeechConstants.ASR_OPT_MARK_VAD_JSONKEY, 1075);
        map.put(SpeechConstants.ASR_OPT_RESULT_JSON_JSONKEY, 1059);
        map.put(SpeechConstants.ASR_OPT_RESULT_JSON_JSONKEY, 1059);
        map.put(SpeechConstants.ASR_OPT_RESULT_JSON_JSONKEY, 1059);
        map.put(SpeechConstants.WAKEUP_OPT_THRESHOLD_VALUE_JSONKEY, 3150);
        return map;
    }

    public boolean m() {
        return this.c;
    }

    public boolean n() {
        return this.d;
    }

    public boolean o() {
        return this.n;
    }

    public boolean p() {
        return this.o;
    }

    public boolean q() {
        return this.p;
    }

    public boolean r() {
        return this.q;
    }
}
