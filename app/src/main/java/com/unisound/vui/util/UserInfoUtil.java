package com.unisound.vui.util;

import android.content.Context;
import com.unisound.vui.data.entity.out.UniContact;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.util.Const;

/* loaded from: classes.dex */
public class UserInfoUtil {
    private static final String TAG = "UserInfoUtil";
    private static Context mContext;
    private static UserInfoUtil mInstance;

    private UserInfoUtil() {
    }

    private void addCarReportPreference(JSONObject userInfo) throws JSONException {
        putValueInfoJson(userInfo, "report", Boolean.valueOf(UserPerferenceUtil.getTrafficInfoSwitchState(mContext)));
    }

    private void addCarTheftPreference(JSONObject userInfo) throws JSONException {
        putValueInfoJson(userInfo, "theft", Boolean.valueOf(UserPerferenceUtil.getAntiTheftSwitchState(mContext)));
    }

    private void addCompanyPreference(JSONObject userInfo) throws JSONException {
        try {
            String poiDefaultOfficeInfo = UserPerferenceUtil.getPoiDefaultOfficeInfo(mContext);
            if (poiDefaultOfficeInfo == null || "".equals(poiDefaultOfficeInfo)) {
                return;
            }
            userInfo.put("company", new JSONObject(poiDefaultOfficeInfo));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addContactPreference(JSONObject userInfo) throws JSONException {
        putValueInfoJson(userInfo, "contacts", constructContactJson(com.unisound.vui.data.a.a.a(mContext).a()));
    }

    private void addHomePreference(JSONObject userInfo) throws JSONException {
        try {
            String poiDefaultHomeInfo = UserPerferenceUtil.getPoiDefaultHomeInfo(mContext);
            if (poiDefaultHomeInfo == null || "".equals(poiDefaultHomeInfo)) {
                return;
            }
            userInfo.put("home", new JSONObject(poiDefaultHomeInfo));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addMapPreference(JSONObject userInfo) throws JSONException {
        putValueInfoJson(userInfo, "map", UserPerferenceUtil.getNaviSelectedState(mContext));
    }

    private void addPoiPreference(JSONObject userInfo) throws JSONException {
        putValueInfoJson(userInfo, "pois", constructPoiJson(com.unisound.vui.data.a.a.a(mContext).b()));
    }

    private void addSettingPreference(JSONObject userInfo) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        constructSettingPreference(jSONObject);
        try {
            userInfo.put("setting", jSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addSexPreference(JSONObject userInfo) throws JSONException {
        putValueInfoJson(userInfo, "sex", Boolean.valueOf(UserPerferenceUtil.getEnableRecognizePersonal(mContext)));
    }

    private void addTTSPreference(JSONObject userInfo) throws JSONException {
        putValueInfoJson(userInfo, "tts", Integer.valueOf(UserPerferenceUtil.getTTSSelectedState()));
    }

    private JSONArray constructContactJson(List<UniContact> uniContacts) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        for (UniContact uniContact : uniContacts) {
            String contactName = uniContact.getContactName();
            ArrayList<String> contactPhoneNO = uniContact.getContactPhoneNO();
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(Const.TableSchema.COLUMN_NAME, contactName);
                jSONObject.put("numbers", contactPhoneNO);
                jSONArray.put(jSONObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONArray;
    }

    private JSONArray constructPoiJson(List<com.unisound.vui.data.entity.a.c> poiList) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        for (com.unisound.vui.data.entity.a.c cVar : poiList) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("address", cVar.b());
                jSONArray.put(jSONObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONArray;
    }

    private void constructSettingPreference(JSONObject settingJson) throws JSONException {
        addMapPreference(settingJson);
        addTTSPreference(settingJson);
        addCarTheftPreference(settingJson);
        addCarReportPreference(settingJson);
        addSexPreference(settingJson);
        addHomePreference(settingJson);
        addCompanyPreference(settingJson);
    }

    public static UserInfoUtil getInstance() {
        if (mInstance == null) {
            throw new RuntimeException("WeChatUserInfo must init first");
        }
        return mInstance;
    }

    public static void init(Context mContext2) {
        mContext = mContext2;
        initInstance();
    }

    private static void initInstance() {
        if (mInstance == null) {
            synchronized (UserInfoUtil.class) {
                if (mInstance == null) {
                    mInstance = new UserInfoUtil();
                }
            }
        }
    }

    private void putValueInfoJson(JSONObject json, String name, Object value) throws JSONException {
        try {
            json.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getUserInfo() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        addSettingPreference(jSONObject);
        return jSONObject;
    }
}
