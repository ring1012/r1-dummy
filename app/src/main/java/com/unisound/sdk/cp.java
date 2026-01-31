package com.unisound.sdk;

import android.util.SparseArray;
import cn.yunzhisheng.casr.EncodeContent;
import com.unisound.client.ErrorCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class cp extends Thread {

    /* renamed from: a, reason: collision with root package name */
    public static String f336a = "http://v2.hivoice.cn:8081/casr/upload";
    public static final int b = 10000;
    public static final int c = 3000;
    public static final int d = 3000;
    public static final int e = 3000;
    public static final int f = 3000;
    public static final int g = 3000;
    public static final int h = 3000;
    private cq i;
    private String k;
    private EncodeContent j = new EncodeContent();
    private String l = "";

    private Map<Integer, List<String>> a(Map<Integer, List<String>> map) {
        Iterator<Integer> it = map.keySet().iterator();
        while (it.hasNext()) {
            List<String> list = map.get(it.next());
            if (list != null) {
                a(list);
                if (list.size() > 0) {
                }
            }
        }
        return map;
    }

    private void a(String str) {
        com.unisound.common.y.a("UploadUserData:" + str);
    }

    private void a(List<String> list) {
        String strReplaceAll;
        boolean z;
        for (int size = list.size() - 1; size > -1; size--) {
            String str = list.get(size);
            if (str == null || str.length() == 0) {
                list.remove(size);
            } else {
                if (str.indexOf(">") > -1) {
                    strReplaceAll = str.replaceAll(">", "");
                    z = true;
                } else {
                    strReplaceAll = str;
                    z = false;
                }
                if (strReplaceAll.indexOf("<") > -1) {
                    strReplaceAll = strReplaceAll.replaceAll("<", "");
                    z = true;
                }
                if (strReplaceAll.length() == 0) {
                    list.remove(size);
                } else if (z) {
                    list.set(size, strReplaceAll);
                }
            }
        }
    }

    private boolean a(int i, List<String> list, StringBuilder sb) {
        String strC = c(i);
        if (strC == null) {
            a(" not find tag id = " + i);
            return true;
        }
        if (list == null) {
            a(strC + " = NULL");
            return true;
        }
        int iB = b(i);
        if (list.size() > iB) {
            a(strC + " Number of over count > " + iB);
            a(ErrorCode.UPLOAD_USER_TOO_LARGE);
            return false;
        }
        a(list);
        sb.append("<" + strC + ">\n");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            sb.append(it.next()).append("\n");
        }
        sb.append("</" + strC + ">\n");
        return true;
    }

    private int b(int i) {
        switch (i) {
            case 1:
                return 10000;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                return 3000;
            default:
                return -1;
        }
    }

    private void b(String str) {
        this.k = str;
        start();
    }

    private String c(int i) {
        switch (i) {
            case 1:
                return "NAME";
            case 2:
                return "APP";
            case 3:
                return "SONG";
            case 4:
                return "SINGER";
            case 5:
                return "ALBUM";
            case 6:
                return "COMMAND";
            case 7:
                return "wechat_contact";
            default:
                return null;
        }
    }

    public void a(int i) {
        cq cqVar = this.i;
        if (cqVar != null) {
            cqVar.e(i);
        }
    }

    public void a(cq cqVar) {
        this.i = cqVar;
    }

    public void a(String str, SparseArray<List<String>> sparseArray) {
        this.l = str;
        StringBuilder sb = new StringBuilder();
        sb.append(str).append(";").append(com.unisound.c.a.a(str)).append("\n");
        int size = sparseArray.size();
        for (int i = 0; i < size; i++) {
            int iKeyAt = sparseArray.keyAt(i);
            if (!a(iKeyAt, sparseArray.get(iKeyAt), sb)) {
                return;
            }
        }
        b(sb.toString());
    }

    public void a(String str, String str2) {
        this.l = str;
        StringBuilder sb = new StringBuilder();
        sb.append(str).append(";").append(com.unisound.c.a.a(str)).append("\n");
        b(sb.toString());
    }

    public void a(String str, Map<Integer, List<String>> map) {
        this.l = str;
        if (this.i == null || map == null) {
            a(ErrorCode.UPLOAD_USER_DATA_EMPTY);
            return;
        }
        if (map.isEmpty()) {
            a(ErrorCode.UPLOAD_USER_DATA_EMPTY);
            return;
        }
        Map<Integer, List<String>> mapA = a(map);
        StringBuilder sb = new StringBuilder();
        sb.append(str).append(";").append(com.unisound.c.a.a(str)).append("\n");
        for (Integer num : mapA.keySet()) {
            if (!a(num.intValue(), mapA.get(num), sb)) {
                return;
            }
        }
        b(sb.toString());
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IOException, NumberFormatException {
        int i;
        try {
            byte[] bytes = com.unisound.c.a.a(this.l).getBytes();
            com.unisound.common.y.c("uploadUserData server is :\n", f336a, "\n Data is \n", this.k);
            byte[] bytes2 = this.k.getBytes();
            byte[] bArr = new byte[bytes.length + bytes2.length + 10];
            if (this.j.EncodeTotalContent(bytes, bytes2, bArr) != 0) {
                i = ErrorCode.UPLOAD_USER_ENCODE_ERROR;
            } else {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(f336a).openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setConnectTimeout(30000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(bArr);
                outputStream.flush();
                outputStream.close();
                if (httpURLConnection.getResponseCode() == 200) {
                    int i2 = Integer.parseInt(new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())).readLine());
                    com.unisound.common.y.c("upload userdata code=", Integer.valueOf(i2));
                    i = i2 == 0 ? 0 : i2 == -6 ? ErrorCode.UPLOAD_USER_DATA_TOO_FAST : ErrorCode.UPLOAD_USER_DATA_SERVER_REFUSED;
                } else {
                    i = -63002;
                }
            }
        } catch (Exception e2) {
            i = -63002;
        }
        a(i);
    }
}
