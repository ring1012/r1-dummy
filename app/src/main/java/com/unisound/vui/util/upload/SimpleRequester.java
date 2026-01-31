package com.unisound.vui.util.upload;

import android.text.TextUtils;
import com.unisound.vui.common.network.d;
import com.unisound.vui.common.network.e;
import com.unisound.vui.util.LogMgr;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SimpleRequester {
    public static final String CODE_SUCCESS = "0";
    public static final String TAG = "SimpleRequester";
    private Map<String, String> head;
    private SimpleRequestListener listener;
    private String tag;
    private d responseListener = new d<String>() { // from class: com.unisound.vui.util.upload.SimpleRequester.1
        @Override // com.unisound.vui.common.network.d
        public void onError(String errorMessage) {
            if (TextUtils.isEmpty(errorMessage)) {
                return;
            }
            SimpleRequester.this.volleyUtils.a(SimpleRequester.this.tag);
            SimpleRequester.this.listener.onError(errorMessage);
        }

        @Override // com.unisound.vui.common.network.d
        public void onResponse(String response) {
            if (TextUtils.isEmpty(response)) {
                return;
            }
            SimpleRequester.this.volleyUtils.a(SimpleRequester.this.tag);
            SimpleRequester.this.listener.onResponse(ReqDataUtils.decoder(response));
        }
    };
    private e volleyUtils = e.a();

    public SimpleRequester() {
        initHead();
    }

    private void initHead() {
        this.head = new HashMap();
        this.head.put("encodeType", "add_base64");
    }

    public void request(String tag, String url, JSONObject body, SimpleRequestListener listener) {
        this.listener = listener;
        this.tag = tag;
        String string = body.toString();
        LogMgr.d(TAG, "bodyStr : " + string.toString());
        this.volleyUtils.a(tag, 1, url, ReqDataUtils.encoder(string).getBytes(), this.head, this.responseListener);
    }

    public void request(String tag, String url, byte[] body, SimpleRequestListener listener) {
        this.listener = listener;
        this.tag = tag;
        String str = new String(body);
        LogMgr.d(TAG, "bodyStr : " + str);
        this.volleyUtils.a(tag, 1, url, ReqDataUtils.encoder(str).getBytes(), this.head, this.responseListener);
    }
}
