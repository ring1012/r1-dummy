package com.unisound.ant.device.netmodule;

import android.support.annotation.WorkerThread;
import com.unisound.ant.device.bean.RequestInfo;
import com.unisound.vui.common.config.ANTConfigPreference;
import com.unisound.vui.util.HttpUtils;
import com.unisound.vui.util.JsonTool;
import com.unisound.vui.util.LogMgr;
import java.io.IOException;
import okhttp3.Response;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DeviceInfoUtils {
    private static final String TAG = DeviceInfoUtils.class.getSimpleName();

    @WorkerThread
    public static boolean isDeviceBounded(String udid) throws Exception {
        RequestInfo.PageInfo pageInfo = new RequestInfo.PageInfo("1", "10", udid);
        RequestInfo.ClientInfo clientInfo = new RequestInfo.ClientInfo(9);
        RequestInfo requestInfo = new RequestInfo("UserManager", "getUserInfo", pageInfo, clientInfo, "2.0.0");
        Response response = HttpUtils.getInstance().postSync(ANTConfigPreference.getAppServerUrl() + "getUserInfo", JsonTool.toJson(requestInfo));
        if (HttpUtils.isResponseCorrect(response)) {
            try {
                String result = response.body().string();
                JSONObject jsonObject = JsonTool.parseToJSONObject(result);
                int status = JsonTool.getJsonIntValue(jsonObject, "status");
                if (500 == status) {
                    LogMgr.d(TAG, "fetch device bound status, device is not bound");
                    return false;
                }
                if (status == 0) {
                    LogMgr.d(TAG, "fetch device bound status, device is bound");
                    return true;
                }
                String detailInfo = JsonTool.getJsonValue(jsonObject, "detailInfo");
                LogMgr.d(TAG, "fetch device bound status failure, errorCode = " + status + ", errorDetail = " + detailInfo);
                throw new Exception("errorCode = " + status + ", errorDetail = " + detailInfo);
            } catch (IOException e) {
                LogMgr.e(TAG, "query bound bound error: " + e.getMessage());
                throw e;
            }
        }
        throw new Exception("query bound status error, " + (response == null ? "response is null" : " response code is " + response.code()));
    }
}
