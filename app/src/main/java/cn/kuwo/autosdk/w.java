package cn.kuwo.autosdk;

import android.text.TextUtils;
import cn.kuwo.autosdk.api.SearchMode;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/* loaded from: classes.dex */
public class w {

    /* renamed from: a, reason: collision with root package name */
    protected static String f23a = null;
    public static String b = "http://mobi.kuwo.cn/mobi.s?f=kuwo&q=";

    private static String a(String str) {
        StringBuilder sbA = a();
        sbA.append(str);
        byte[] bytes = sbA.toString().getBytes();
        byte[] bArrA = p.a(bytes, bytes.length, p.f18a, p.b);
        return String.valueOf(b) + new String(m.a(bArrA, bArrA.length));
    }

    public static String a(String str, SearchMode searchMode, int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append("type=new_search");
        if (searchMode != null) {
            sb.append("&mode=all");
        }
        if (TextUtils.isEmpty(str)) {
            sb.append("&word=");
        } else {
            try {
                sb.append("&word=" + URLEncoder.encode(str, com.unisound.b.f.b));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (i >= 0) {
            sb.append("&pn=" + i);
        }
        if (i2 >= 0) {
            sb.append("&rn=" + i2);
        }
        return a(sb.toString());
    }

    public static synchronized StringBuilder a() {
        StringBuilder sb;
        sb = new StringBuilder();
        if (f23a == null) {
            sb.append("user=").append(n.b);
            sb.append("&prod=").append(n.f);
            sb.append("&corp=kuwo");
            sb.append("&source=").append(n.g);
            sb.append("&");
            f23a = sb.toString();
        } else {
            sb.append(f23a);
        }
        return sb;
    }
}
