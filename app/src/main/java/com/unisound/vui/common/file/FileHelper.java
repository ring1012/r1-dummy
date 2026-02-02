package com.unisound.vui.common.file;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Base64;
import com.unisound.sdk.c;
import com.unisound.vui.util.LogMgr;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.io.FileUtils;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes.dex */
public final class FileHelper {
    public static final String CUSTOM_FILE_DIR = "system/unisound";
    public static final String GRAMMAR_RELATIVE_PATH = "unisound/asrfix/jsgf_model";
    public static final String JSGF_RELATIVE_PATH = "unisound/jsgf_clg";
    public static final String NLU_RELATIVE_PATH = "unisound/nlu";
    public static final String SAVE_ASR_RECORDING_PATH = "unisound/record/asr/";
    public static final String SAVE_TTS_RECORDING_PATH = "unisound/record/tts/";
    public static final String SAVE_WAKEUP_RECORDING_PATH = "unisound/record/wakeup/";
    private static final String TAG = "FileHelper";
    public static final String TTS_MODEL_PATH = "system/unisound/ttsmodel";
    public static final String TTS_PCM_PATH = "system/unisound/audio";
    public static final String TTS_PCM_WAKEUP_PATH = "system/unisound/audio/wakeup";

    private FileHelper() {
        throw new AssertionError("");
    }

    public static Bitmap base64ToBitmap(String string) {
        try {
            byte[] bArrDecode = Base64.decode(string, 0);
            return BitmapFactory.decodeByteArray(bArrDecode, 0, bArrDecode.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean copyDirectoryFromAssets(Context context, String srcDir, File destDir)  {
        if (context == null) {
            throw new NullPointerException("context");
        }
        try {
            AssetManager assets = context.getAssets();
            for (String str : assets.list(srcDir)) {
                String str2 = srcDir + File.separator + str;
                InputStream inputStreamOpen = assets.open(str2);
                File file = new File(destDir, str);
                if (!"ttsmodel".equals(srcDir)) {
                    FileUtils.copyInputStreamToFile(inputStreamOpen, file);
                } else if (file.exists()) {
                    boolean zIsSameFileHeader = isSameFileHeader(inputStreamOpen, file, str);
                    LogMgr.d(TAG, "2 file header is same " + zIsSameFileHeader);
                    if (!zIsSameFileHeader) {
                        FileUtils.copyInputStreamToFile(assets.open(str2), file);
                    }
                } else {
                    LogMgr.d(TAG, "outPutFile is not exist");
                    FileUtils.copyInputStreamToFile(inputStreamOpen, file);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyDirectoryFromAssets(Context context, String srcDir, String destDir) {
        return copyDirectoryFromAssets(context, srcDir, new File(destDir));
    }

    public static boolean copyFileFromAssets(Context context, String srcDir, String srcFile, File destDir) {
        if (context == null) {
            throw new NullPointerException("context");
        }
        try {
            FileUtils.copyInputStreamToFile(context.getAssets().open(srcDir + File.separator + srcFile), new File(destDir, srcFile));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteAllFile(String path) {
        boolean z = false;
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            return false;
        }
        String[] list = file.list();
        if (list == null || list.length == 0) {
            return true;
        }
        int i = 0;
        while (true) {
            boolean z2 = z;
            if (i >= list.length) {
                return z2;
            }
            File file2 = path.endsWith(File.separator) ? new File(path + list[i]) : new File(path + File.separator + list[i]);
            if (file2.isFile()) {
                file2.delete();
            }
            if (file2.isDirectory()) {
                deleteAllFile(path + MqttTopic.TOPIC_LEVEL_SEPARATOR + list[i]);
                z = true;
            } else {
                z = z2;
            }
            i++;
        }
    }

    public static String formatPcmToWav(String path, String fileName) {
        String fileName2 = fileName.replace(".pcm", "");
        String str = path + MqttTopic.TOPIC_LEVEL_SEPARATOR + fileName2 + ".wav";
        try {
            FileInputStream fileInputStream = new FileInputStream(path + MqttTopic.TOPIC_LEVEL_SEPARATOR + fileName2 + ".pcm");
            byte[] bArr = new byte[1024];
            ArrayList arrayList = new ArrayList();
            while (true) {
                byte[] bArr2 = new byte[1024];
                if (fileInputStream.read(bArr, 0, bArr.length) <= 0) {
                    a.a(path, fileName2 + ".wav", arrayList);
                    try {
                        fileInputStream.close();
                        return str;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
                arrayList.add(bArr2);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String generateFileName(String ex) {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault()).format(new Date(System.currentTimeMillis())) + "." + ex;
    }

    public static String getASRSavedRecordingPath(int type) {
        String path = Environment.getExternalStorageDirectory().getPath();
        switch (type) {
            case 0:
                String str = path + File.separator + SAVE_WAKEUP_RECORDING_PATH;
                pathExist(str);
                return str + generateFileName(c.b);
            case 1:
                String str2 = path + File.separator + SAVE_ASR_RECORDING_PATH;
                pathExist(str2);
                return str2 + generateFileName(c.b);
            case 2:
                String str3 = path + File.separator + SAVE_TTS_RECORDING_PATH;
                pathExist(str3);
                return str3;
            default:
                return null;
        }
    }

    public static String getExternalStoragePath() {
        return isMediaMounted() ? Environment.getExternalStorageDirectory().getAbsolutePath() : "";
    }

    public static String[] getFileListFromAssets(Context context, String srcDir) {
        if (context == null) {
            throw new NullPointerException("context");
        }
        try {
            return context.getAssets().list(srcDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File getGrammarDirectory(Context context) {
        if (context == null) {
            throw new NullPointerException("context");
        }
        return new File(context.getFilesDir(), GRAMMAR_RELATIVE_PATH);
    }

    public static File getGrammarFile(Context context, String fileName) {
        return new File(getGrammarDirectory(context), fileName);
    }

    public static File getJSGFDirectory(Context context) {
        if (context == null) {
            throw new NullPointerException("context");
        }
        return new File(context.getFilesDir(), JSGF_RELATIVE_PATH);
    }

    public static File getJSGFFile(Context context, String fileName) {
        return new File(getJSGFDirectory(context), fileName);
    }

    public static File getNluDirectory(Context context) {
        if (context == null) {
            throw new NullPointerException("context");
        }
        return new File(context.getFilesDir(), NLU_RELATIVE_PATH);
    }

    public static File getNluFile(Context context, String fileName) {
        return new File(getNluDirectory(context), fileName);
    }

    public static int getPcmLength(String filePcm) {
        File file = new File(filePcm);
        if (file.exists()) {
            return ((int) (file.length() / 32)) / 1000;
        }
        return 0;
    }

    public static File getTTSModelDirectory() {
        return new File(TTS_MODEL_PATH);
    }

    public static File getTTSModelFile(String fileName) {
        return new File(getTTSModelDirectory(), fileName);
    }

    public static File getTTSPCMDirectory() {
        return new File(TTS_PCM_PATH);
    }

    public static File getTTSPCMFile(String fileName) {
        return new File(getTTSPCMDirectory(), fileName);
    }

    public static File getWakeupAudioFile(String fileName) {
        return new File(getWakeupPcmDirectory(), fileName);
    }

    public static File getWakeupPcmDirectory() {
        return new File(TTS_PCM_WAKEUP_PATH);
    }

    public static int getWavTime(String file) throws IllegalArgumentException {
        int iLongValue;
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(file);
            iLongValue = (int) (Long.valueOf(mediaMetadataRetriever.extractMetadata(9)).longValue() / 1000);
            if (iLongValue == 0) {
                return 1;
            }
        } catch (Exception e) {
            iLongValue = 1;
        }
        return iLongValue;
    }

    public static boolean isFileExist(String path) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File(path).exists();
    }

    public static boolean isFileExists(String absolutePath) {
        return new File(absolutePath).exists();
    }

    public static boolean isMediaMounted() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    private static boolean isSameFileHeader(InputStream in, File destFile, String srcFile) throws IOException {
        LogMgr.d(TAG, "copy file name" + srcFile);
        byte[] bArr = new byte[256];
        byte[] bArr2 = new byte[256];
        in.read(bArr);
        if (destFile.exists()) {
            new FileInputStream(destFile).read(bArr2);
        }
        in.close();
        return Arrays.equals(bArr, bArr2);
    }

    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    private static void pathExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            return;
        }
        file.mkdirs();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 1, insn: 0x0058: MOVE (r2 I:??[OBJECT, ARRAY]) = (r1 I:??[OBJECT, ARRAY]), block:B:33:0x0058 */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v10 */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v2, types: [java.io.BufferedInputStream] */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v9 */
    private static byte[] readByteArrFromFile(File file)  {
        ByteArrayOutputStream baos = new ByteArrayOutputStream((int) file.length());
        BufferedInputStream bis = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            int read;
            while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return baos.toByteArray();
    }

    public static byte[] readByteArrFromPath(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return readByteArrFromFile(file);
        }
        LogMgr.e(TAG, "fileName is not exist !");
        return null;
    }

    public static void saveLogFile(String log) throws IOException {
        try {
            File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + System.currentTimeMillis() + ".txt");
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(log.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeBitmapToFile(String filePath, Bitmap bitmap) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            bitmap.recycle();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x0040 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void writeDataToFile(java.lang.String r3, byte[] r4) throws java.lang.Throwable {
        /*
            java.io.File r0 = new java.io.File
            r0.<init>(r3)
            r2 = 0
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch: java.io.FileNotFoundException -> L1c java.io.IOException -> L2c java.lang.Throwable -> L3c
            r1.<init>(r0)     // Catch: java.io.FileNotFoundException -> L1c java.io.IOException -> L2c java.lang.Throwable -> L3c
            r1.write(r4)     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L4b java.io.FileNotFoundException -> L4d
            r1.flush()     // Catch: java.lang.Throwable -> L49 java.io.IOException -> L4b java.io.FileNotFoundException -> L4d
            if (r1 == 0) goto L16
            r1.close()     // Catch: java.io.IOException -> L17
        L16:
            return
        L17:
            r0 = move-exception
            r0.printStackTrace()
            goto L16
        L1c:
            r0 = move-exception
            r1 = r2
        L1e:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L49
            if (r1 == 0) goto L16
            r1.close()     // Catch: java.io.IOException -> L27
            goto L16
        L27:
            r0 = move-exception
            r0.printStackTrace()
            goto L16
        L2c:
            r0 = move-exception
            r1 = r2
        L2e:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L49
            if (r1 == 0) goto L16
            r1.close()     // Catch: java.io.IOException -> L37
            goto L16
        L37:
            r0 = move-exception
            r0.printStackTrace()
            goto L16
        L3c:
            r0 = move-exception
            r1 = r2
        L3e:
            if (r1 == 0) goto L43
            r1.close()     // Catch: java.io.IOException -> L44
        L43:
            throw r0
        L44:
            r1 = move-exception
            r1.printStackTrace()
            goto L43
        L49:
            r0 = move-exception
            goto L3e
        L4b:
            r0 = move-exception
            goto L2e
        L4d:
            r0 = move-exception
            goto L1e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.vui.common.file.FileHelper.writeDataToFile(java.lang.String, byte[]):void");
    }
}
