package android.os;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class MessageDispatchManager {
    private static final String LOG_TAG = "MessageDispatchManager";
    public static final int MSG_TYPE_BT = 64;
    public static final int MSG_TYPE_BT_ACK = 128;
    public static final int MSG_TYPE_HIVOICE = 16384;
    public static final int MSG_TYPE_HIVOICE_ACK = 32768;
    public static final int MSG_TYPE_INPUT_KEY = 256;
    public static final int MSG_TYPE_INPUT_KEY_ACK = 512;
    public static final int MSG_TYPE_INPUT_TOUCH = 1024;
    public static final int MSG_TYPE_INPUT_TOUCH_ACK = 2048;
    public static final int MSG_TYPE_LIGHT = 4096;
    public static final int MSG_TYPE_LIGHT_ACK = 8192;
    public static final int MSG_TYPE_MATCH = 262144;
    public static final int MSG_TYPE_MATCH_ACK = 524288;
    public static final int MSG_TYPE_MQTT = 2097152;
    public static final int MSG_TYPE_PLAYER = 4;
    public static final int MSG_TYPE_PLAYER_ACK = 8;
    public static final int MSG_TYPE_PLAY_TTS = 65536;
    public static final int MSG_TYPE_PLAY_TTS_ACK = 131072;
    public static final int MSG_TYPE_REGISTER_RECEIVER = 1;
    public static final int MSG_TYPE_SMART_HOME = 8388608;
    public static final int MSG_TYPE_SMART_HOME_ACK = 16777216;
    public static final int MSG_TYPE_UNREGISTER_RECEIVER = 2;
    public static final int MSG_TYPE_USER_DATA = 33554432;
    public static final int MSG_TYPE_USER_DATA_ACK = 67108864;
    public static final int MSG_TYPE_WIFI = 16;
    public static final int MSG_TYPE_WIFI_ACK = 32;
    private Context mContext;
    private MSGHandler mMsgHandler = new MSGHandler();
    private IMessenger mService;

    public interface MessageReceiver {
        void notifyMsg(int i, int i2, int i3, Object obj);
    }

    private class MSGHandler implements Runnable {
        Messenger mMessenger;
        public final ArrayList<MsgRecvFlaged> mRecvs;

        private MSGHandler() {
            this.mMessenger = null;
            this.mRecvs = new ArrayList<>();
        }

        @Override // java.lang.Runnable
        public void run() {
            Looper.prepare();
            this.mMessenger = new Messenger(new Handler() { // from class: android.os.MessageDispatchManager.MSGHandler.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    synchronized (this) {
                        Iterator<MsgRecvFlaged> it = MSGHandler.this.mRecvs.iterator();
                        while (it.hasNext()) {
                            MsgRecvFlaged recv = it.next();
                            if ((recv.mFlags & msg.what) == msg.what) {
                                recv.mRecv.notifyMsg(msg.what, msg.arg1, msg.arg2, msg.obj);
                            }
                        }
                    }
                }
            });
            Looper.loop();
        }
    }

    public MessageDispatchManager(Context ctx, IMessenger serv) {
        this.mContext = ctx;
        this.mService = serv;
        new Thread(this.mMsgHandler).start();
    }

    public void sendMessage(int what, int arg1, int arg2, Parcelable obj) {
        Message msg = Message.obtain(null, what, arg1, arg2, obj);
        while (this.mMsgHandler.mMessenger == null) {
        }
        msg.replyTo = this.mMsgHandler.mMessenger;
        try {
            this.mService.send(msg);
        } catch (RemoteException e) {
            Log.w(LOG_TAG, "Error while calling remote serivce", e);
        }
    }

    public void registerMessageReceiver(MessageReceiver recv, int flags) {
        synchronized (this.mMsgHandler) {
            this.mMsgHandler.mRecvs.add(new MsgRecvFlaged(recv, flags));
        }
        sendMessage(1, flags, 0, null);
    }

    public void unregisterMessageReceiver(MessageReceiver recv) {
        sendMessage(2, 0, 0, null);
    }

    private class MsgRecvFlaged {
        int mFlags;
        MessageReceiver mRecv;

        public MsgRecvFlaged(MessageReceiver recv, int flags) {
            this.mRecv = recv;
            this.mFlags = flags;
        }
    }
}
