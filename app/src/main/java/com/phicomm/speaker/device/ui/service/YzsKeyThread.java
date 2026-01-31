package com.phicomm.speaker.device.ui.service;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import com.unisound.vui.util.LogMgr;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class YzsKeyThread extends Thread {
    private static final String TAG = "YzsKeyThread";
    private Handler handler;

    public YzsKeyThread(Handler mHandler) {
        this.handler = mHandler;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IOException {
        String resultValue;
        int temp;
        LocalSocket s = new LocalSocket();
        LocalSocketAddress l = new LocalSocketAddress("yzskey", LocalSocketAddress.Namespace.RESERVED);
        try {
            LogMgr.i(TAG, "yzskey socket connect start");
            s.connect(l);
            InputStream is = s.getInputStream();
            byte[] socketReadBuffer = new byte[4];
            while (true) {
                LogMgr.i(TAG, "while yzskey socket connect");
                int socketCount = is.read(socketReadBuffer);
                if (socketCount > 0 && (temp = stringToInt((resultValue = String.valueOf(getChars(socketReadBuffer))))) != 0) {
                    int keyEvent = temp >> 12;
                    int keyCode = temp & 4095;
                    LogMgr.i(TAG, " socket resultValue = " + resultValue + " ; socketCount = " + socketCount + " keyEvent=" + keyEvent + "  keyCode=" + keyCode + " temp=" + temp);
                    if (this.handler != null) {
                        this.handler.sendMessage(Message.obtain(this.handler, keyEvent, Integer.valueOf(keyCode)));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int byte2int(byte[] res) {
        int targets = (res[0] & 255) | ((res[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }

    private char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

    private int stringToInt(String str) throws NumberFormatException {
        if (str == null || !isNumeric(str)) {
            return 0;
        }
        try {
            int a2 = Integer.parseInt(str);
            return a2;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
}
