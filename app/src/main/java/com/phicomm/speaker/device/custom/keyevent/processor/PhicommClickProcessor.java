package com.phicomm.speaker.device.custom.keyevent.processor;

import android.content.Context;
import com.phicomm.speaker.device.custom.ipc.PhicommLightController;
import com.phicomm.speaker.device.custom.ipc.PhicommXController;
import com.phicomm.speaker.device.custom.speech.SpeechManager;
import com.phicomm.speaker.device.custom.status.PhicommDeviceStatusProcessor;
import com.unisound.vui.engine.ANTEngine;
import com.unisound.vui.handler.session.memo.DefaultMemoRingingHandler;

/* loaded from: classes.dex */
public abstract class PhicommClickProcessor implements PhicommStatusListener {
    private PhicommDeviceStatusProcessor deviceStatusProcessor = PhicommDeviceStatusProcessor.getInstance();
    protected ANTEngine mANTEngine;
    protected Context mContext;
    protected PhicommLightController mPhicommLightController;
    protected PhicommXController mPhicommXController;
    protected SpeechManager mSpeechManager;

    public PhicommClickProcessor(ANTEngine antEngine, Context context) {
        this.mANTEngine = antEngine;
        this.mContext = context;
        this.mPhicommXController = new PhicommXController(context);
        this.mSpeechManager = new SpeechManager(antEngine);
        this.mPhicommLightController = new PhicommLightController(context);
    }

    public final void onTriggered() {
        int deviceStatus = this.deviceStatusProcessor.getDeviceStatus();
        dispatchDeviceStatus(deviceStatus);
    }

    protected void turnOffWakeupLightsIfNeeds() {
        if (this.mANTEngine.isASR() || this.mANTEngine.isRecognition()) {
            this.mPhicommLightController.turnOffALLWakeupLight();
        }
    }

    private void dispatchDeviceStatus(int deviceStatus) {
        if (isRingingStatus()) {
            onRingingStatus();
        }
        switch (deviceStatus) {
            case 0:
                dispatchReadyStatus();
                break;
            case 1:
                dispatchMusicStatus();
                break;
            case 2:
                onNetStatus();
                break;
            case 3:
                onBlueToothStatus();
                break;
            case 5:
                onDormantStatus();
                break;
        }
    }

    private boolean isRingingStatus() {
        return this.mANTEngine.hasAttr(DefaultMemoRingingHandler.ALARM_PLAYING) && ((Boolean) this.mANTEngine.attr(DefaultMemoRingingHandler.ALARM_PLAYING).get()).booleanValue();
    }

    private void dispatchReadyStatus() {
        if (isRecordingStatus(this.mANTEngine)) {
            onRecordingStatus();
        } else if (isSpeechHandling(this.mANTEngine)) {
            onSpeechHandingStatus();
        } else {
            onReadyStatus();
        }
    }

    private void dispatchMusicStatus() {
        onMusicStatus();
    }

    private boolean isRecordingStatus(ANTEngine mANTEngine) {
        return mANTEngine.isASR();
    }

    private boolean isSpeechHandling(ANTEngine mANTEngine) {
        return mANTEngine.isRecognition() || mANTEngine.isTTSPlaying();
    }
}
