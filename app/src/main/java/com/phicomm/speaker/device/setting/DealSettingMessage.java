package com.phicomm.speaker.device.setting;

import android.content.Context;
import android.os.PowerManager;
import com.unisound.vui.handler.session.setting.SettingInterface;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.entity.CommandInfo;

/* loaded from: classes.dex */
public class DealSettingMessage implements SettingInterface {
    private static final String TAG = "DealSettingMessage";
    private static DealSettingMessage dealSettingMessage;
    private Context context;
    private PowerManager mPM = null;

    private DealSettingMessage(Context context) {
        this.context = context.getApplicationContext();
    }

    public static void init(Context context) {
        if (dealSettingMessage == null) {
            dealSettingMessage = new DealSettingMessage(context);
        }
    }

    public static DealSettingMessage getInstance() {
        return dealSettingMessage;
    }

    @Override // com.unisound.vui.handler.session.setting.SettingInterface
    public void dealMessage(Object object) {
        if (object != null && (object instanceof CommandInfo)) {
            CommandInfo cmdInfo = (CommandInfo) object;
            LogMgr.d(TAG, "handleControlWakeup cmdInfo.getCommand()=" + cmdInfo.getCommand());
        }
    }
}
