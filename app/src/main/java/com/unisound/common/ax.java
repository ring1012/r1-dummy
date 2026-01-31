package com.unisound.common;

import com.unisound.client.ErrorCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ax extends Thread {
    private am b;
    private String c;

    /* renamed from: a, reason: collision with root package name */
    public String f250a = "";
    private ak d = null;
    private ErrorCode e = new ErrorCode();

    private void a(int i) {
        am amVar = this.b;
        if (amVar != null) {
            amVar.a(this, this.e.createProfessionError(i));
        }
    }

    private void c(String str) {
        this.c = str;
        start();
    }

    public ak a() {
        return this.d;
    }

    void a(am amVar) {
        this.b = amVar;
    }

    public void a(String str) {
        this.f250a = str;
    }

    void a(String str, ak akVar, List<String> list) {
        this.d = akVar;
        this.d.a(false);
        this.f250a += "?ak=" + str + "&imei=" + com.unisound.c.a.q + "&an=wechar&si=" + akVar.c() + "&av=1.0&sn=abcdefg&trace=1";
        StringBuilder sb = new StringBuilder();
        sb.append("data=<SCENE>\n");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            sb.append(it.next()).append("\n");
        }
        sb.append("</SCENE>");
        c(sb.toString());
    }

    public void b() {
        this.b = null;
    }

    protected void b(String str) {
        y.a("UploadSceneTask:" + str);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IOException, NumberFormatException {
        int i;
        try {
            byte[] bytes = this.c.getBytes();
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.f250a).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(30000);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            if (httpURLConnection.getResponseCode() == 200) {
                JSONObject jSONObject = new JSONObject(new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())).readLine());
                int i2 = Integer.parseInt(jSONObject.has("errorCode") ? jSONObject.getString("errorCode") : "");
                y.c("upload userdata code=", Integer.valueOf(i2));
                if (i2 == 0) {
                    this.d.a(true);
                    i = 0;
                } else {
                    i = i2 == -1 ? ErrorCode.UPLOAD_SCENE_GENERAL_ERROR : i2 == -5 ? ErrorCode.UPLOAD_SCENE_INVALID_KEY : i2 == -8 ? ErrorCode.UPLOAD_SCENE_STREAM_IO_ERR : i2 == -11 ? ErrorCode.UPLOAD_SCENE_UNKNOWN_ERR : i2 == -12 ? ErrorCode.UPLOAD_SCENE_DATA_SIZE_IS_FORBIDDEN : i2 == -13 ? ErrorCode.UPLOAD_SCENE_INVALID_VER : i2 == -6 ? ErrorCode.UPLOAD_SCENE_DATA_TOO_FAST : ErrorCode.UPLOAD_SCENE_DATA_SERVER_REFUSED;
                }
            } else {
                i = -63012;
            }
        } catch (Exception e) {
            e.printStackTrace();
            i = -63012;
        }
        a(i);
    }
}
