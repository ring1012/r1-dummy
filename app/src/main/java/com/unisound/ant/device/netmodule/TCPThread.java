package com.unisound.ant.device.netmodule;

import com.unisound.vui.util.ExoConstants;
import com.unisound.vui.util.LogMgr;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/* loaded from: classes.dex */
public class TCPThread extends Thread {
    private static final String TAG = "ApWifi-TCPThread";
    private static final int UDP_CLIENT_PORT = 9997;
    String deviceId;
    String ip;
    String mDeviceName;
    PrintWriter mWriter;
    String udid;
    Socket mSocket = null;
    boolean isStop = false;

    public TCPThread(String Ip, String DeviceId, String udid, String deviceName) {
        this.ip = Ip;
        this.deviceId = DeviceId;
        this.udid = udid;
        this.mDeviceName = deviceName;
        LogMgr.d(TAG, "Ip:" + Ip + " DeviceId:" + DeviceId + " udid=" + udid + " deviceName=" + deviceName);
    }

    public void stopRun() throws IOException {
        LogMgr.d(TAG, "stopRun");
        this.isStop = true;
        clearSocket();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IOException {
        LogMgr.d(TAG, "tcp long conenct started");
        if (this.ip != null) {
            boolean isSuc = false;
            int tryCount = 0;
            while (!isSuc && tryCount < 10 && !this.isStop) {
                try {
                    LogMgr.d(TAG, "will conenct client ip : %s", this.ip);
                    Thread.sleep(1000L);
                    tryCount++;
                    this.mSocket = new Socket(this.ip, UDP_CLIENT_PORT);
                    this.mWriter = new PrintWriter(this.mSocket.getOutputStream());
                    StringBuffer buffer = new StringBuffer();
                    String split = String.valueOf((char) 31);
                    buffer.append(this.mDeviceName).append(split).append(this.deviceId).append(split).append(this.udid).append(split).append(ExoConstants.APP_KEY);
                    this.mWriter.print(buffer);
                    this.mWriter.flush();
                    LogMgr.d(TAG, "response device name : %s,deviceId: %s,udid: %s", this.mDeviceName, this.deviceId, this.udid);
                    isSuc = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    isSuc = false;
                } finally {
                    clearSocket();
                }
            }
        }
    }

    private void clearSocket() throws IOException {
        try {
            if (this.mSocket != null) {
                this.mSocket.close();
                this.mSocket = null;
            }
            if (this.mWriter != null) {
                this.mWriter.close();
                this.mWriter = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
