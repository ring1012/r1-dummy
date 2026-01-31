package com.unisound.passport;

/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    public static final int f280a = 1501;
    public static final int b = 1502;
    public static final int c = 1503;
    public static final int d = 1;
    public static final int e = 2;
    public static final int f = 3;
    public static final int g = 4;
    public static final int h = 5;
    public static final int i = 6;
    public static final int j = 7;
    public static final int k = 8;
    public static final int l = 9;

    public static String a(int i2) {
        switch (i2) {
            case 1:
                return "应用不存在";
            case 2:
                return "签名错误";
            case 3:
                return "没有权限";
            case 4:
                return "登录用户的TOKEN无效";
            case 5:
                return "数据超出容量限制";
            case 6:
                return "请求时间戳超出了请求有效期";
            case 7:
                return "访问频率过快";
            case 8:
                return "客户端Token无效";
            case 9:
                return "客户端MQTT连接未断开";
            case 1501:
                return "appkey或udid为空";
            case 1502:
                return "打开长连接失败";
            case 1503:
                return "关闭长连接失败";
            default:
                return "其他错误";
        }
    }
}
