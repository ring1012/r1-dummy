package com.unisound.vui.common.a;

import android.content.Context;
import com.unisound.vui.common.file.FileHelper;
import com.unisound.vui.util.LogMgr;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class a implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    private Context f364a;
    private c b;

    public a(Context context, c cVar) {
        this.f364a = context;
        this.b = cVar;
    }

    @Override // java.lang.Runnable
    public void run() throws IOException {
        InputStream inputStreamOpen = null;
        try {
            try {
                inputStreamOpen = this.f364a.getAssets().open(this.b.a());
                byte[] byteArray = IOUtils.toByteArray(inputStreamOpen);
                this.b.a(byteArray);
                File file = new File(FileHelper.getWakeupPcmDirectory(), this.b.a());
                if (!file.exists()) {
                    FileUtils.writeByteArrayToFile(file, byteArray);
                }
                if (inputStreamOpen != null) {
                    try {
                        inputStreamOpen.close();
                    } catch (IOException e) {
                        LogMgr.e("LoadWakeupPcmRunnable", "inputStream close fail");
                    }
                }
            } catch (IOException e2) {
                LogMgr.e("LoadWakeupPcmRunnable", "open assets file " + this.b.a() + " error: " + e2.getMessage());
                if (inputStreamOpen != null) {
                    try {
                        inputStreamOpen.close();
                    } catch (IOException e3) {
                        LogMgr.e("LoadWakeupPcmRunnable", "inputStream close fail");
                    }
                }
            }
        } catch (Throwable th) {
            if (inputStreamOpen != null) {
                try {
                    inputStreamOpen.close();
                } catch (IOException e4) {
                    LogMgr.e("LoadWakeupPcmRunnable", "inputStream close fail");
                }
            }
            throw th;
        }
    }
}
