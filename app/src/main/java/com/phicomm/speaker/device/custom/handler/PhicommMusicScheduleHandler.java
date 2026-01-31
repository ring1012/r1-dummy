package com.phicomm.speaker.device.custom.handler;

import com.phicomm.speaker.device.custom.status.PhicommDeviceStatusProcessor;
import com.unisound.vui.handler.session.music.AbstractMusicScheduleHandler;
import com.unisound.vui.util.LogMgr;

/* loaded from: classes.dex */
public class PhicommMusicScheduleHandler extends AbstractMusicScheduleHandler {
    @Override // com.unisound.vui.handler.session.music.AbstractMusicScheduleHandler
    protected boolean isInMusicStatus() {
        int deviceStatus = PhicommDeviceStatusProcessor.getInstance().getDeviceStatus();
        LogMgr.d("AbstractMusicScheduleHandler", "deviceStatus:" + deviceStatus);
        return deviceStatus == 1;
    }
}
