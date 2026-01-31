package android.os;

import android.content.Context;
import android.util.Log;

/* loaded from: classes.dex */
public final class SysPrivateManager {
    private static final boolean DEBUG = true;
    private static final String LOG_TAG = "SysPrivateManager";
    private static final int MSG_LED_CMD = 3;
    private static final int MSG_MODE_CHANGED = 2;
    private static final int MSG_SYSTEM_INIT = 1;
    public static final String PRIVATE_KEY_RECOVERY_AFTER_PT = "recoveryAfterProductionTest";
    public static final int PRIVATE_LEVEL_COMMON = 1003;
    public static final int PRIVATE_LEVEL_CRITICAL = 1000;
    public static final int PRIVATE_LEVEL_SYSTEM = 1001;
    public static final int PRIVATE_LEVEL_USER = 1002;
    public static final int SYSTEM_MODE_FACTORY_TEST = 1;
    public static final int SYSTEM_MODE_NORMAL = 0;
    private final Context mContext;
    private final Handler mHandler;
    private final ISysPrivate mService;
    private final HandlerThread mThread = new HandlerThread(LOG_TAG);

    public interface modeChangedListener {
        void onModeChanged(int i, int i2);
    }

    public SysPrivateManager(Context context, ISysPrivate service) {
        this.mContext = context;
        this.mService = service;
        this.mThread.start();
        this.mHandler = new Handler(this.mThread.getLooper()) { // from class: android.os.SysPrivateManager.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 2:
                        modeChangedListener listener = (modeChangedListener) message.obj;
                        if (listener != null) {
                            listener.onModeChanged(message.arg1, message.arg2);
                            break;
                        }
                        break;
                    default:
                        Log.w(SysPrivateManager.LOG_TAG, "nothing to do");
                        break;
                }
            }
        };
    }

    public void writePropKey(String key, String value) {
        if (this.mService == null) {
            Log.e(LOG_TAG, "Feature android.private.service not available");
            return;
        }
        try {
            this.mService.writeProp(key, value);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error set writePropKey", re);
        }
    }

    public void writePropKey(String file, String key, String value) {
        if (this.mService == null) {
            Log.e(LOG_TAG, "Feature android.private.service not available");
            return;
        }
        if (file == null || file.equals("") || key == null || key.equals("")) {
            Log.e(LOG_TAG, "bad parameter!");
            return;
        }
        try {
            this.mService.writeFileProp(file, key, value);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error set writePropKey", re);
        }
    }

    public void writePropKey(int secLevel, String key, String value) {
        if (secLevel != 1000 && secLevel != 1001 && secLevel != 1002 && secLevel != 1003) {
            Log.e(LOG_TAG, "bad level");
            return;
        }
        if (key == null || key.equals("")) {
            Log.e(LOG_TAG, "bad parameter!");
            return;
        }
        try {
            this.mService.writeLevelProp(secLevel, key, value);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error set writePropKey", re);
        }
    }

    public String readPropKey(String file, String key) {
        if (this.mService == null) {
            Log.e(LOG_TAG, "Feature android.private.service not available");
            return null;
        }
        if (file == null || file.equals("") || key == null || key.equals("")) {
            Log.e(LOG_TAG, "bad parameter!");
            return null;
        }
        try {
            return this.mService.readFileProp(file, key);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error set writePropKey", re);
            return null;
        }
    }

    public String readPropKey(int secLevel, String key) {
        if (this.mService == null) {
            Log.e(LOG_TAG, "Feature android.private.service not available");
            return null;
        }
        if (key == null || key.equals("")) {
            Log.e(LOG_TAG, "bad parameter!");
            return null;
        }
        if (secLevel != 1000 && secLevel != 1001 && secLevel != 1002 && secLevel != 1003) {
            Log.e(LOG_TAG, "bad level");
            return null;
        }
        try {
            return this.mService.readLevelProp(secLevel, key);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error set writePropKey", re);
            return null;
        }
    }

    public String readPropKey(String key) {
        if (this.mService == null) {
            Log.e(LOG_TAG, "Feature android.private.service not available");
            return null;
        }
        try {
            return this.mService.readProp(key);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error set writePropKey", re);
            return null;
        }
    }

    public int setSysMode(int mode) {
        int ret = 0;
        if (this.mService == null) {
            Log.e(LOG_TAG, "Feature android.private.service not available");
            return -1;
        }
        try {
            ret = this.mService.setSysMode(mode);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error set Sys Mode", re);
        }
        return ret;
    }

    public int getSysMode() {
        int ret = 0;
        if (this.mService == null) {
            Log.e(LOG_TAG, "Feature android.private.service not available");
            return -1;
        }
        try {
            ret = this.mService.getSysMode();
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "Error get Sys Mode", re);
        }
        return ret;
    }

    public String getBootProp(String key) {
        return null;
    }

    public int setBootProp(String key, String value) {
        return -1;
    }
}
