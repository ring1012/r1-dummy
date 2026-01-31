package com.unisound.vui.util;

import android.os.Environment;
import com.google.gson.Gson;
import com.unisound.vui.common.file.FileHelper;
import com.unisound.vui.util.upload.ReqDataUtils;
import java.io.File;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ConfigUtils {
    private static final String TAG = "ConfigUtils";
    private static String getFilePathUrl = "http://10.30.2.231:8080/app_wx_adapt_service/m/getCustomerConfig";
    private static final String CONFIG_PATH = Environment.getExternalStorageDirectory() + File.separator + "config.txt";
    private static String configFileUrl = "";

    public static class ConfigPostData {
        private String appKey;
        private String udid;

        public String getAppKey() {
            return this.appKey;
        }

        public String getUdid() {
            return this.udid;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public void setUdid(String udid) {
            this.udid = udid;
        }
    }

    public static class ConfigResponseData {
        private String configFileUrl;
        private int processStatus;
        private String requestId;

        public String getConfigFileUrl() {
            return this.configFileUrl;
        }

        public int getProcessStatus() {
            return this.processStatus;
        }

        public String getRequestId() {
            return this.requestId;
        }

        public void setConfigFileUrl(String configFileUrl) {
            this.configFileUrl = configFileUrl;
        }

        public void setProcessStatus(int processStatus) {
            this.processStatus = processStatus;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }
    }

    private ConfigUtils() {
    }

    public static void downLoadConfig(String appKey, String udid) {
        ConfigPostData configPostData = new ConfigPostData();
        configPostData.setAppKey(appKey);
        configPostData.setUdid(udid);
        HashMap map = new HashMap();
        map.put("encodeType", "add_base64");
        LogMgr.d(TAG, "downLoadConfig para:" + new Gson().toJson(configPostData));
        String strEncoder = ReqDataUtils.encoder(new Gson().toJson(configPostData));
        LogMgr.d(TAG, "downLoadConfig postData:" + strEncoder);
        com.unisound.vui.common.network.e.a().a(TAG, 1, getFilePathUrl, strEncoder.getBytes(), map, new com.unisound.vui.common.network.d<String>() { // from class: com.unisound.vui.util.ConfigUtils.1
            @Override // com.unisound.vui.common.network.d
            public void onError(String errorMessage) {
                LogMgr.d(ConfigUtils.TAG, "downLoadConfig error:" + errorMessage.toString());
            }

            @Override // com.unisound.vui.common.network.d
            public void onResponse(String res) {
                LogMgr.d(ConfigUtils.TAG, "downLoadConfig res:" + res);
                String strDecoder = ReqDataUtils.decoder(res);
                LogMgr.d(ConfigUtils.TAG, "downLoadConfig response:" + strDecoder);
                try {
                    ConfigResponseData configResponseData = (ConfigResponseData) new Gson().fromJson(strDecoder.toString(), ConfigResponseData.class);
                    if (configResponseData.getProcessStatus() == 0) {
                        String unused = ConfigUtils.configFileUrl = configResponseData.getConfigFileUrl();
                        LogMgr.d(ConfigUtils.TAG, "downLoadConfig configFileUrl:" + ConfigUtils.configFileUrl);
                        com.unisound.vui.common.network.e.a().a(ConfigUtils.TAG, ConfigUtils.configFileUrl, new com.unisound.vui.common.network.d<byte[]>() { // from class: com.unisound.vui.util.ConfigUtils.1.1
                            @Override // com.unisound.vui.common.network.d
                            public void onError(String errorMessage) {
                                LogMgr.d(ConfigUtils.TAG, "write onError:" + errorMessage);
                            }

                            @Override // com.unisound.vui.common.network.d
                            public void onResponse(byte[] response) throws Throwable {
                                File file = new File(ConfigUtils.CONFIG_PATH);
                                if (file.exists()) {
                                    file.delete();
                                }
                                FileHelper.writeDataToFile(ConfigUtils.CONFIG_PATH, response);
                                LogMgr.d(ConfigUtils.TAG, "write success");
                            }
                        });
                    }
                } catch (Exception e) {
                    LogMgr.d(ConfigUtils.TAG, "downLoadConfig exception:" + e.toString());
                }
            }
        });
    }
}
