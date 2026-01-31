package com.unisound.vui.util.upload.user;

import android.content.Context;
import com.unisound.vui.data.entity.out.a;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.UserPerferenceUtil;
import com.unisound.vui.util.auth.UserAuthUtil;
import com.unisound.vui.util.upload.SimpleRequestListener;
import com.unisound.vui.util.upload.SimpleRequester;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class IndividualSettingUploader implements Uploader {
    private static final String INDEVIDUAL_SETTING_REQUEST_ADDRESS = "http://10.200.19.108:8080/app_wx_adapt_service/m/uploadClientInfo/setPersonalOptions";
    public static final String TAG = "IndividualSettingUploader";
    public static final String UPLOAD_TAG = "IndividualSetting";
    private Context mContext;
    private UploaderListener uploaderListener;
    private SimpleRequestListener simpleRequestListener = new SimpleRequestListener() { // from class: com.unisound.vui.util.upload.user.IndividualSettingUploader.1
        @Override // com.unisound.vui.util.upload.SimpleRequestListener
        public void onError(String errorMessage) {
            IndividualSettingUploader.this.uploaderListener.onError(errorMessage);
        }

        @Override // com.unisound.vui.util.upload.SimpleRequestListener
        public void onResponse(String response) {
            IndividualSettingUploader.this.saveCurVersion(response);
            if ("0".equals(IndividualSettingUploader.this.parserResponse(response, "processStatus"))) {
                IndividualSettingUploader.this.uploaderListener.onSuccess();
            }
        }
    };
    private SimpleRequester simpleRequester = new SimpleRequester();
    private JSONObject body = new JSONObject();

    public IndividualSettingUploader(Context mContext) {
        this.mContext = mContext;
    }

    private void constructBody(JSONObject options) throws JSONException {
        a userAuth = getUserAuth();
        if (userAuth != null) {
            setParamsForBody(userAuth, options);
        } else {
            LogMgr.e(TAG, "fetch user auth fail !!");
        }
    }

    private a getUserAuth() {
        return UserAuthUtil.getInstance().getUniUserAuth();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String parserResponse(String response, String name) {
        try {
            return (String) new JSONObject(response).get(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveCurVersion(String response) {
        UserPerferenceUtil.setCurVersion(this.mContext, parserResponse(response, "version"));
    }

    private void sendRequest() {
        this.simpleRequester.request(UPLOAD_TAG, INDEVIDUAL_SETTING_REQUEST_ADDRESS, this.body, this.simpleRequestListener);
    }

    private void setParamsForBody(a userAuth, JSONObject options) throws JSONException {
        try {
            this.body.put("passportId", userAuth.b());
            this.body.put("passportToken", userAuth.c());
            this.body.put("udid", userAuth.a());
            this.body.put("options", options);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // com.unisound.vui.util.upload.user.Uploader
    public void upload(JSONObject options, UploaderListener uploaderListener) throws JSONException {
        this.uploaderListener = uploaderListener;
        constructBody(options);
        sendRequest();
    }
}
