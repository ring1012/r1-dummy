package com.unisound.vui.util.upload.user;

import com.unisound.vui.util.CrashHandler;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.upload.SimpleRequestListener;
import com.unisound.vui.util.upload.SimpleRequester;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class LogUploader implements Uploader {
    private static final String LOG_UPLOADER_TAG = "LogUploader";
    private static final String LOG_UPLOADER_URL = "http://10.200.19.108:8080/app_wx_adapt_service/m/uploadAppLogFile";
    private static final String TAG = "LogUploader";
    private List<File> crashFileList;
    private UploaderListener uploaderListener;
    private SimpleRequestListener simpleRequestListener = new SimpleRequestListener() { // from class: com.unisound.vui.util.upload.user.LogUploader.1
        @Override // com.unisound.vui.util.upload.SimpleRequestListener
        public void onError(String errorMessage) {
            LogMgr.d("LogUploader", "onError : " + errorMessage);
            LogUploader.this.uploaderListener.onError(errorMessage);
        }

        @Override // com.unisound.vui.util.upload.SimpleRequestListener
        public void onResponse(String response) {
            LogMgr.d("LogUploader", "onResponse : " + response);
            LogUploader.this.deleteCrashFile();
            LogUploader.this.uploaderListener.onSuccess();
        }
    };
    private boolean isCrashFileExist = getCrashLogFile(CrashHandler.HPROF_FILE_PATH);
    private SimpleRequester requester = new SimpleRequester();
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteCrashFile() {
        this.threadPool.submit(new Runnable() { // from class: com.unisound.vui.util.upload.user.LogUploader.2
            @Override // java.lang.Runnable
            public void run() {
                FileUtils.deleteQuietly((File) LogUploader.this.crashFileList.get(0));
            }
        });
    }

    private boolean getCrashLogFile(String hprofFilePath) {
        File[] fileArrListFiles = new File(hprofFilePath).listFiles();
        if (fileArrListFiles.length == 0) {
            return false;
        }
        this.crashFileList = Arrays.asList(fileArrListFiles);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renderByte(File crashFile, byte[] crashLogByte) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(crashFile);
        byte[] crashLogByte2 = new byte[fileInputStream.available()];
        fileInputStream.read(crashLogByte2);
    }

    public byte[] getCrashLogByte() {
        final byte[] bArr = null;
        this.threadPool.submit(new Runnable() { // from class: com.unisound.vui.util.upload.user.LogUploader.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    LogUploader.this.renderByte((File) LogUploader.this.crashFileList.get(0), bArr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return null;
    }

    @Override // com.unisound.vui.util.upload.user.Uploader
    public void upload(JSONObject body, UploaderListener uploaderListener) {
        if (!this.isCrashFileExist) {
            LogMgr.d("LogUploader", "no crash file exist");
            return;
        }
        this.uploaderListener = uploaderListener;
        this.requester.request("LogUploader", LOG_UPLOADER_URL, getCrashLogByte(), this.simpleRequestListener);
    }
}
