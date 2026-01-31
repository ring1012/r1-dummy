package com.unisound.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import cn.yunzhisheng.casr.EncodeContent;
import com.unisound.client.ErrorCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class co {

    /* renamed from: a, reason: collision with root package name */
    public static String f335a = "http://rtc.hivoice.cn";
    private static final int d = 10000;
    private String b = "/data-process-service/oneshot";
    private EncodeContent c = new EncodeContent();
    private Context e;
    private String f;

    public co(Context context, String str) {
        this.e = context;
        this.f = str;
    }

    private Set<String> a(Context context) {
        return context.getSharedPreferences("onlinewakeup", 0).getStringSet("onlineWakeupWord", null);
    }

    public static boolean a(String str) {
        return Pattern.compile("[`~!@#$%^&*()_\\+\\-\\={}|\\\\\\[\\]\\:\";'<>?,./~·！@#￥%……&*（）——\\+\\-\\={}|【】、：“”；‘’《》？，。、｀～！＠＃＄％＾＆＊（）＿＋－＝｛｝｜［］＼：＂＂；＇＇＜＞？，．／·～！＠＃￥％……＆×（）——＋－＝｛｝｜【】＼：“”；‘’《》？，。、]").matcher(str).find();
    }

    public String a(int i) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("status", i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public String a(String str, String str2) throws JSONException, IOException {
        String strA;
        com.unisound.common.y.b("UploadOneShotOnlineWakeupData onlineWakeupWord => ", str2);
        a(0);
        try {
            byte[] bytes = str.getBytes();
            byte[] bytes2 = str2.getBytes();
            byte[] bArr = new byte[bytes.length + bytes2.length + 10];
            if (this.c.EncodeTotalContent(bytes, bytes2, bArr) != 0) {
                strA = a(ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ONLINE_ENCODE_ERROR);
            } else {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(f335a + this.b).openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setConnectTimeout(10000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                com.unisound.common.y.b("UploadOneShotOnlineWakeupData conn param => ", httpURLConnection.toString());
                outputStream.write(bArr);
                outputStream.flush();
                outputStream.close();
                if (httpURLConnection.getResponseCode() == 200) {
                    strA = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())).readLine();
                    com.unisound.common.y.c("upload response codeStr=", strA);
                } else {
                    strA = a(ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ONLINE_NETWORK_ERROR);
                }
            }
            return strA;
        } catch (Exception e) {
            e.printStackTrace();
            com.unisound.common.y.a("UploadOneShotOnlineWakeupData exception =>" + e.getMessage());
            return a(ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ONLINE_ERROR);
        }
    }

    public String a(List<String> list) {
        com.unisound.common.af afVar = new com.unisound.common.af();
        afVar.a("1.0");
        afVar.b(this.f);
        afVar.c(com.unisound.c.a.a(this.f));
        afVar.a(a(this.e));
        HashSet hashSet = new HashSet();
        hashSet.addAll(list);
        afVar.b(hashSet);
        return afVar.g();
    }

    public void a(Set<String> set) {
        SharedPreferences.Editor editorEdit = this.e.getSharedPreferences("onlinewakeup", 0).edit();
        editorEdit.putStringSet("onlineWakeupWord", set);
        editorEdit.commit();
    }
}
