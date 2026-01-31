package com.unisound.sdk;

/* loaded from: classes.dex */
public class aq {

    /* renamed from: a, reason: collision with root package name */
    public static com.unisound.common.a f302a = new com.unisound.common.a("asrv3.hivoice.cn", 80, "117.121.49.41", 80);
    public static com.unisound.common.a b = new com.unisound.common.a("v_eng.hivoice.cn", 80, "117.121.55.43", 80);
    public static com.unisound.common.a c = new com.unisound.common.a("v_cnt.hivoice.cn", 80, "117.121.55.41", 80);
    public static com.unisound.common.a d = new com.unisound.common.a("eval.hivoice.cn", 80, "140.207.193.59", 80);
    public static com.unisound.common.a e = new com.unisound.common.a("117.121.55.39", 9001, "117.121.55.39", 9001);
    public static com.unisound.common.a f = new com.unisound.common.a("v_zhen.hivoice.cn", 9004, "v_zhen.hivoice.cn", 9004);

    public static com.unisound.common.a a(String str) {
        return str.equals("en") ? b : str.equals("co") ? c : str.equals(as.aE) ? d : as.aF.equals(str) ? f : f302a;
    }
}
