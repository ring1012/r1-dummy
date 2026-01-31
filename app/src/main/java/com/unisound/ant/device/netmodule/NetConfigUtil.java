package com.unisound.ant.device.netmodule;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import com.unisound.ant.device.listener.WifiChangeListener;
import com.unisound.vui.util.LogMgr;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/* loaded from: classes.dex */
public class NetConfigUtil {
    private static final String TAG = "NetConfigUtil";

    public static void removeAllNetConfig(Context context) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        if (existingConfigs != null) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                int networkId = existingConfig.networkId;
                LogMgr.d(TAG, "--->>removeAllNetConfig networkId:" + networkId);
                forgetWifi(wifiManager, networkId);
            }
        }
    }

    public static void closeWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        wifiManager.setWifiEnabled(false);
    }

    public static void closeWifiAndRemoveWifiCOnfig(Context context) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        if (existingConfigs != null) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                int networkId = existingConfig.networkId;
                forgetWifi(wifiManager, networkId);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.unisound.ant.device.netmodule.NetConfigUtil$1] */
    public static void changeConnectWifi(String ssid, String password, WifiChangeListener listener) {
        new Thread() { // from class: com.unisound.ant.device.netmodule.NetConfigUtil.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
            }
        }.start();
    }

    private static void forgetWifi(WifiManager wifiManager, int networkId) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        try {
            Class<?>[] classl = wifiManager.getClass().getDeclaredClasses();
            Class<?> ActionListener = null;
            int i = 0;
            while (true) {
                if (classl == null || i >= classl.length) {
                    break;
                }
                if (classl[i].getName().endsWith("ActionListener")) {
                    ActionListener = classl[i];
                    break;
                }
                i++;
            }
            Method method = wifiManager.getClass().getMethod("forget", Integer.TYPE, ActionListener);
            method.invoke(wifiManager, Integer.valueOf(networkId), null);
            LogMgr.d(TAG, "forgetWifi networkId:" + networkId);
        } catch (Exception e) {
            e.printStackTrace();
            LogMgr.e(TAG, "forgetWifi e:" + e);
        }
    }
}
