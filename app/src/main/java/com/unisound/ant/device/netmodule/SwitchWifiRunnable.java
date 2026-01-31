package com.unisound.ant.device.netmodule;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.unisound.ant.device.listener.WifiChangeListener;
import com.unisound.ant.device.netmodule.NetChangeReceiver;
import com.unisound.ant.device.netmodule.WifiConnect;
import com.unisound.vui.util.LogMgr;

/* loaded from: classes.dex */
public class SwitchWifiRunnable implements Runnable, NetChangeReceiver.NetAliveConnectListener, Handler.Callback {
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int NET_CONNECT_TIMEOUT = 1;
    private static final String TAG = "SwitchWifiRunnable";
    private Context context;
    private WifiChangeListener listener;
    private String password;
    private NetChangeReceiver receiver = new NetChangeReceiver();
    private String ssid;
    private Handler timeoutHandler;
    private WifiConnect wifiConnect;

    public SwitchWifiRunnable(Context context, WifiChangeListener wifiChangeListener) {
        this.context = context;
        this.listener = wifiChangeListener;
        this.wifiConnect = new WifiConnect(context);
        this.receiver.setAliveConnectListener(this);
        this.receiver.registerNetStateReceiver(context);
        this.timeoutHandler = new Handler(this);
    }

    public void setWifiInfo(String ssid, String password) {
        this.ssid = ssid;
        this.password = password;
    }

    @Override // java.lang.Runnable
    public void run() throws InterruptedException {
        if (TextUtils.isEmpty(this.ssid)) {
            LogMgr.d(TAG, "ssid is not set and plese check setWifiInfo");
            return;
        }
        int code = this.wifiConnect.connect(this.ssid, this.password, WifiConnect.WifiCipherType.WIFICIPHER_WPA);
        if (this.listener != null && code != 3) {
            this.listener.onChangeWifiFail(code);
            dealWithConnectWifiFail();
        } else {
            this.timeoutHandler.removeCallbacksAndMessages(null);
            this.timeoutHandler.sendEmptyMessageDelayed(1, 10000L);
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                dealWithConnectWifiFail();
                break;
        }
        return false;
    }

    private void dealWithConnectWifiFail() {
        if (this.receiver != null) {
            this.receiver.unregisterNetStateReceiver(this.context);
        }
        this.timeoutHandler.removeCallbacksAndMessages(null);
    }

    private void dealWitchConnectWifiSuccess() {
        if (this.listener != null) {
            this.listener.onChangeWifiSuccess();
        }
        if (this.receiver != null) {
            this.receiver.unregisterNetStateReceiver(this.context);
        }
        this.timeoutHandler.removeCallbacksAndMessages(null);
    }

    @Override // com.unisound.ant.device.netmodule.NetChangeReceiver.NetAliveConnectListener
    public void onNetAliveConnected() {
        LogMgr.d(TAG, "onNetAliveConnected");
        dealWitchConnectWifiSuccess();
    }
}
