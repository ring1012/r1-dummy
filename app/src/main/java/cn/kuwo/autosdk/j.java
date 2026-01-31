package cn.kuwo.autosdk;

import android.text.TextUtils;
import cn.kuwo.autosdk.api.SearchMode;
import cn.kuwo.autosdk.bean.Constants;
import cn.kuwo.autosdk.bean.Music;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class j {

    /* renamed from: a, reason: collision with root package name */
    private SearchMode f13a;

    public j(SearchMode searchMode) {
        this.f13a = SearchMode.ALL;
        this.f13a = searchMode;
    }

    public int a(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            return i;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return i;
        }
    }

    public Music a(JSONObject jSONObject) throws NumberFormatException {
        if (jSONObject == null) {
            return null;
        }
        Music music = new Music();
        try {
            music.rid = b(jSONObject.getString("MUSICID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (music.rid == -1) {
            return null;
        }
        music.name = jSONObject.getString("SONGNAME").replaceAll("&quot;", "\"");
        music.artist = jSONObject.getString("ARTIST").replaceAll("&quot;", "\"");
        music.album = jSONObject.getString("ALBUM").replaceAll("&quot;", "\"");
        music.fileFormat = jSONObject.getString("FORMAT").replaceAll("&quot;", "\"");
        music.duration = c(jSONObject.getString("DURATION"));
        music.tag = jSONObject.getString("TAG").replaceAll("&quot;", "\"");
        music.hasMv = a(jSONObject.getString("MVFLAG"));
        music.mvQuality = jSONObject.getString("MVQUALITY").replaceAll("&quot;", "\"");
        music.parseResourceStringFromQuku(jSONObject.getString("MINFO"));
        music.hasKalaok = a(jSONObject.getString("KMARK"), -1);
        return music;
    }

    public String a(byte[] bArr, int i, int i2) throws IOException {
        int i3;
        String string = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr, i, i2);
        Inflater inflater = new Inflater();
        InflaterInputStream inflaterInputStream = new InflaterInputStream(byteArrayInputStream, inflater);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] bArr2 = new byte[512];
            while (true) {
                try {
                    i3 = inflaterInputStream.read(bArr2);
                } catch (EOFException e) {
                    i3 = -1;
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                if (i3 == -1) {
                    try {
                        break;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                } else {
                    byteArrayOutputStream.write(bArr2, 0, i3);
                }
            }
            inflaterInputStream.close();
            inflater.end();
            try {
                string = byteArrayOutputStream.toString(com.unisound.b.f.b);
            } catch (UnsupportedEncodingException e4) {
                e4.printStackTrace();
            } catch (Error e5) {
                e5.printStackTrace();
            }
            byteArrayOutputStream.reset();
        } catch (OutOfMemoryError e6) {
            e6.printStackTrace();
        }
        return string;
    }

    public List a(String str, int i, int[] iArr) {
        if (iArr != null && iArr.length > 0) {
            iArr[0] = 0;
        }
        String strA = w.a(str, this.f13a, i, 30);
        if (TextUtils.isEmpty(strA)) {
            return null;
        }
        b bVar = new b();
        bVar.a(Constants.SEARCH_TIMEOUT);
        a aVarA = bVar.a(strA);
        if (aVarA != null && aVarA.a() && aVarA.c != null) {
            return a(aVarA.c);
        }
        if (iArr == null || iArr.length <= 0) {
            return null;
        }
        iArr[0] = 1;
        return null;
    }

    public List a(byte[] bArr) throws JSONException, IOException, NumberFormatException {
        if (bArr == null) {
            return null;
        }
        try {
            byte[] bArr2 = new byte[4];
            try {
                System.arraycopy(bArr, 0, bArr2, 0, 4);
                System.arraycopy(bArr, 4, bArr2, 0, 4);
                String strA = a(bArr, 8, o.a(bArr2, false));
                if (TextUtils.isEmpty(strA)) {
                    return null;
                }
                ArrayList arrayList = new ArrayList();
                try {
                    JSONObject jSONObject = new JSONObject(strA);
                    String string = jSONObject.getString("hitmode");
                    int i = jSONObject.getInt("Hit");
                    if ("song".equals(string) && i > 0) {
                        JSONArray jSONArray = jSONObject.getJSONArray("musiclist");
                        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                            Music musicA = a(jSONArray.getJSONObject(i2));
                            if (musicA != null) {
                                arrayList.add(musicA);
                            }
                        }
                    }
                } catch (JSONException e) {
                }
                if (arrayList == null || arrayList.isEmpty()) {
                    return null;
                }
                return arrayList;
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        } catch (OutOfMemoryError e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public boolean a(String str) {
        return !TextUtils.isEmpty(str) && "1".equalsIgnoreCase(str);
    }

    public long b(String str) {
        if (TextUtils.isEmpty(str) || str.indexOf("MUSIC_") < 0) {
            return -1L;
        }
        try {
            return Long.parseLong(str.substring("MUSIC_".length(), str.length()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    public int c(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
