package com.phicomm.speaker.device.custom.api;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;
import com.google.gson.JsonObject;
import com.phicomm.speaker.device.R;
import com.phicomm.speaker.device.custom.ipc.PhicommLightController;
import com.phicomm.speaker.device.custom.ipc.PhicommXController;
import com.phicomm.speaker.device.custom.music.PhicommPlayer;
import com.phicomm.speaker.device.custom.status.PhicommDeviceStatusProcessor;
import com.phicomm.speaker.device.utils.LogUtils;
import com.phicomm.speaker.device.utils.PhicommPerferenceUtil;
import com.phicomm.speaker.device.utils.TTSUtils;
import com.unisound.ant.device.bean.ActionStatus;
import com.unisound.ant.device.bean.DevicePlayingType;
import com.unisound.ant.device.bean.DstServiceName;
import com.unisound.ant.device.bean.SelfDefinationRequestInfo;
import com.unisound.ant.device.bean.SelfDefinationResponseInfo;
import com.unisound.ant.device.bean.UnisoundDeviceCommand;
import com.unisound.ant.device.service.ActionResponse;
import com.unisound.ant.device.sessionlayer.SessionExecuteHandler;
import com.unisound.ant.device.sessionlayer.SessionRegister;
import com.unisound.vui.common.media.UniMediaPlayer;
import com.unisound.vui.engine.ANTEngine;
import com.unisound.vui.handler.session.memo.utils.RingingUtils;
import com.unisound.vui.transport.out.ChangeWakeupWordEvent;
import com.unisound.vui.util.JsonTool;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.UserPerferenceUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/* loaded from: classes.dex */
public class CustomApiManager extends SessionExecuteHandler {
    public static final String AMBIENT_LIGHT_OFF = "0";
    public static final String AMBIENT_LIGHT_ON = "1";
    public static final String DORMANT_OFF = "0";
    public static final String DORMANT_ON = "1";
    private static final String TAG = CustomApiManager.class.getSimpleName();
    private static final String TYPE_LIGHT_OFF = "1";
    private static final String TYPE_LIGHT_ON = "0";
    public static final int TYPE_RINGING = 0;
    private final Context mContext;
    private ANTEngine mEngine;
    private SparseArray<List<CustomApiListener>> mListenerMap = new SparseArray<>();
    private final PhicommPlayer mPhicommPlayer;
    private PhicommLightController phicommLightController;
    private PhicommXController phicommXController;

    public CustomApiManager(Context context, ANTEngine engine, PhicommPlayer phicommPlayer) {
        this.mContext = context;
        this.mEngine = engine;
        this.mPhicommPlayer = phicommPlayer;
        SessionRegister.associateSessionCenter(DstServiceName.DST_SERVICE_SELF_DEFINATION, this);
        this.phicommLightController = new PhicommLightController(context);
        this.phicommXController = new PhicommXController(context);
    }

    public void addListener(int type, CustomApiListener listener) {
        List<CustomApiListener> list = this.mListenerMap.get(type);
        if (list == null) {
            list = new ArrayList<>();
            this.mListenerMap.put(type, list);
        }
        list.add(listener);
    }

    public void removeListener(int type, CustomApiListener listener) {
        List<CustomApiListener> list = this.mListenerMap.get(type);
        if (list != null) {
            list.remove(listener);
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                UnisoundDeviceCommand command = (UnisoundDeviceCommand) msg.obj;
                handleSelfDefinedCommand(command);
                break;
        }
    }

    private void handleSelfDefinedCommand(UnisoundDeviceCommand command) {
        JsonObject parameter = (JsonObject) command.getParameter();
        SelfDefinationRequestInfo selfDefinationRequestInfo = (SelfDefinationRequestInfo) JsonTool.fromJson(parameter, SelfDefinationRequestInfo.class);
        String operationType = selfDefinationRequestInfo.getOperationType();
        if (TextUtils.isEmpty(operationType)) {
            LogMgr.w(TAG, "operationType is null, ignore");
            return;
        }
        switch (operationType) {
            case "checkDeviceStateManager":
                handleCheckDeviceState();
                break;
            case "modifyWakeUpWord":
                handleModifyWakeUpWord(selfDefinationRequestInfo.getContent());
                break;
            case "modifyTtsPlayer":
                handleModifyTtsPlayer(selfDefinationRequestInfo.getContent());
                break;
            case "modifyDormantLightStatus":
                handleModifyDormantLightStatus(selfDefinationRequestInfo.getContent());
                break;
            case "modifyDormantStatus":
                handleModifyDormantStatus(selfDefinationRequestInfo.getContent());
                break;
            case "getDormantStatus":
                handleGetDormantStatus();
                break;
            case "modifyAmbientLightStatus":
                handleModifyAmbientLightStatus(selfDefinationRequestInfo.getContent());
                break;
            case "getAmbientLightStatus":
                handleGetAmbientLightStatus();
                break;
            case "audition":
                handleAuditionRinging(selfDefinationRequestInfo.getContent());
                break;
            case "auditionTtsSpeaker":
                handleAuditionTtsSpeaker(selfDefinationRequestInfo.getContent());
                break;
            case "resetDevice":
                handleResetDevice();
                break;
            case "getLightingStatusManager":
                handleGetLightingStatus();
                break;
            case "getDeviceInfoManager":
                handleGetDeviceInfo();
                break;
        }
        SessionRegister.getUpDownMessageManager().reponseCloudCommandWithoutAck(command.getOperation(), getActionResponse(0));
    }

    private void handleResetDevice() {
        LogMgr.d(TAG, "handleResetDevice: ");
        this.phicommXController.resetDevice();
        responseToClient(SelfDefinationRequestInfo.RESET_DEVICE, 0);
    }

    private void handleCheckDeviceState() {
        DevicePlayingType devicePlayingType = new DevicePlayingType(this.mPhicommPlayer.getDevicePlayingType());
        responseToClient(SelfDefinationRequestInfo.CHECK_DEVICE_STATE, 0, devicePlayingType);
    }

    private void handleModifyWakeUpWord(Map<String, Object> content) {
        int status = -1;
        try {
            Object wakeUpWord = content.get("wakeUpWord");
            if (wakeUpWord != null) {
                status = processChangeWakeupWord((String) wakeUpWord);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "handleModifyWakeUpWord error : ", e);
        }
        responseToClient(SelfDefinationRequestInfo.MODIFY_WAKE_UP_WORD, status);
    }

    private void handleModifyTtsPlayer(Map<String, Object> content) {
        int status = -1;
        boolean ttsPlaying = this.mEngine.isTTSPlaying();
        LogMgr.d(TAG, "isTTSPlaying = " + ttsPlaying);
        if (!ttsPlaying) {
            try {
                Object ttsPlayer = content.get("ttsPlayer");
                if (ttsPlayer != null) {
                    status = processSwitchTTSSpeaker((String) ttsPlayer);
                }
            } catch (Exception e) {
                LogUtils.e(TAG, "handleModifyTtsPlayer error : ", e);
            }
        } else {
            status = ResponseStatus.STATUS_FAIL_SWITCH_TTS_SPEAKER_TTS_PLAYING;
        }
        responseToClient(SelfDefinationRequestInfo.MODIFY_TTS_PLAYER, status);
    }

    private void handleModifyDormantLightStatus(Map<String, Object> content) {
        int status = -1;
        try {
            Object isLighting = content.get("isLighting");
            if (isLighting != null) {
                status = processChangeLightingStatus((String) isLighting);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "handleModifyDormantLightStatus error : ", e);
        }
        responseToClient(SelfDefinationRequestInfo.MODIFY_DORMANT_LIGHT_STATUS, status);
    }

    private void handleModifyDormantStatus(Map<String, Object> content) {
        int status = -1;
        Map<String, String> resContent = new HashMap<>();
        try {
            Object switchSleepMode = content.get("isDormant");
            LogUtils.w(TAG, "modifyDormantStatus switchSleepMode = " + switchSleepMode);
            if (switchSleepMode != null) {
                boolean isDormant = Integer.parseInt(switchSleepMode.toString()) == 1;
                if (isDormant) {
                    status = openSleepMode();
                    if (status == 0) {
                        resContent.put(SelfDefinationRequestInfo.CURRENT_DORMANT_STATUS, "1");
                    } else {
                        resContent.put(SelfDefinationRequestInfo.CURRENT_DORMANT_STATUS, "0");
                    }
                } else {
                    status = closeSleepMode();
                    if (status == 0) {
                        resContent.put(SelfDefinationRequestInfo.CURRENT_DORMANT_STATUS, "0");
                    } else {
                        resContent.put(SelfDefinationRequestInfo.CURRENT_DORMANT_STATUS, "1");
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "modifyDormantStatus error : ", e);
        }
        responseToClient(SelfDefinationRequestInfo.MODIFY_DORMANT_STATUS, status, resContent);
    }

    private void handleGetDormantStatus() {
        Map<String, String> content = new HashMap<>();
        content.put(SelfDefinationRequestInfo.CURRENT_DORMANT_STATUS, getDormantStatus());
        responseToClient(SelfDefinationRequestInfo.GET_DORMANT_STATUS, 0, content);
    }

    private void handleModifyAmbientLightStatus(Map<String, Object> content) {
        int status = -1;
        Map<String, String> resContent = new HashMap<>();
        try {
            Object switchAmbientLight = content.get("isAmbientLight");
            LogUtils.w(TAG, "modifyAmbientLightStatus switchAmbientLight = " + switchAmbientLight);
            if (switchAmbientLight != null) {
                boolean isAmbientLight = Integer.parseInt(switchAmbientLight.toString()) == 1;
                if (isAmbientLight) {
                    status = openAmbientLight();
                    if (status == 0) {
                        resContent.put(SelfDefinationRequestInfo.CURRENT_AMBIENT_LIGHT_STATUS, "1");
                    } else {
                        resContent.put(SelfDefinationRequestInfo.CURRENT_AMBIENT_LIGHT_STATUS, "0");
                    }
                } else {
                    status = closeAmbientLight();
                    if (status == 0) {
                        resContent.put(SelfDefinationRequestInfo.CURRENT_AMBIENT_LIGHT_STATUS, "0");
                    } else {
                        resContent.put(SelfDefinationRequestInfo.CURRENT_AMBIENT_LIGHT_STATUS, "1");
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "modifyAmbientLightStatus error : ", e);
        }
        responseToClient(SelfDefinationRequestInfo.MODIFY_AMBIENT_LIGHT_STATUS, status, resContent);
    }

    private void handleGetAmbientLightStatus() {
        Map<String, String> content = new HashMap<>();
        content.put(SelfDefinationRequestInfo.CURRENT_AMBIENT_LIGHT_STATUS, PhicommPerferenceUtil.getAmbientLightState(this.mContext) ? "1" : "0");
        responseToClient(SelfDefinationRequestInfo.GET_AMBIENT_LIGHT_STATUS, 0, content);
    }

    private void handleAuditionRinging(Map<String, Object> content) {
        int status = -1;
        try {
            Object alarmFile = content.get("alarmFile");
            if (alarmFile != null && RingingUtils.getRingingFile((String) alarmFile).exists()) {
                status = 0;
                List<CustomApiListener> list = this.mListenerMap.get(0);
                if (list != null) {
                    for (CustomApiListener listener : list) {
                        listener.onCustomEvent(1, alarmFile);
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "handleAuditionRinging error : ", e);
        }
        responseToClient(SelfDefinationRequestInfo.AUDITION_RINGING, status);
    }

    private void handleAuditionTtsSpeaker(Map<String, Object> content) {
        int status = -1;
        try {
            String speaker = content.get("ttsSpeaker").toString();
            LogUtils.d(TAG, "audition tts speaker, speaker is " + speaker);
            if (!TextUtils.isEmpty(speaker)) {
                status = listeningTtsSpeaker(speaker);
            } else {
                LogUtils.d(TAG, "audition tts speaker, but speaker is null");
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "handleAuditionTtsSpeaker error : ", e);
        }
        responseToClient(SelfDefinationRequestInfo.AUDITION_TTS_SPEAKER, status);
    }

    private int listeningTtsSpeaker(String speaker) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        String speakerFile;
        switch (speaker) {
            case "FEMALE":
                speakerFile = "system/unisound/audio/tts_audition/tts_speaker_xiaowen.wav";
                break;
            case "MALE":
                speakerFile = "system/unisound/audio/tts_audition/tts_speaker_xiaofeng.wav";
                break;
            case "CHILDREN":
                speakerFile = "system/unisound/audio/tts_audition/tts_speaker_tangtang.wav";
                break;
            case "SWEET":
                speakerFile = "system/unisound/audio/tts_audition/tts_speaker_xuanxuan.wav";
                break;
            case "LZL":
                speakerFile = "system/unisound/audio/tts_audition/tts_speaker_lingling.wav";
                break;
            default:
                LogUtils.d(TAG, "audition tts speaker, but speaker is wrong, speaker = " + speaker);
                return ResponseStatus.STATUS_FAIL_AUDITION_TTS_SPEAKER;
        }
        UniMediaPlayer.getInstance().play(speakerFile, null);
        return 0;
    }

    private void handleGetLightingStatus() {
        Map<String, String> content = new HashMap<>();
        content.put("isLighting", parseDormantLightStatus(UserPerferenceUtil.getDormantLightState(this.mContext)));
        responseToClient(SelfDefinationRequestInfo.GET_LIGHTING_STATUS, 0, content);
    }

    private void handleGetDeviceInfo() {
        Map<String, Object> content = new HashMap<>();
        content.put("ttsPlayer", UserPerferenceUtil.getUserTTSModelType(this.mContext));
        content.put("wakeUpWord", UserPerferenceUtil.getActuallyMainWakeupWord(this.mContext));
        responseToClient(SelfDefinationRequestInfo.GET_DEVICE_INFO, 0, content);
    }

    private int processChangeWakeupWord(@NonNull String wakeupWord) {
        if (!isWakeupWordValid(wakeupWord)) {
            return ResponseStatus.STATUS_FAIL_SET_WAKEUP_WORD;
        }
        ArrayList<String> list = new ArrayList<>();
        list.add(wakeupWord);
        this.mEngine.cancelTTS();
        this.mEngine.cancelASR();
        this.mEngine.stopWakeup();
        this.mEngine.updateWakeupWord(list);
        this.mEngine.pipeline().fireUserEventTriggered(new ChangeWakeupWordEvent(wakeupWord));
        return 0;
    }

    private boolean isWakeupWordValid(@NonNull String wakeupWord) {
        if (!TextUtils.isEmpty(wakeupWord) && wakeupWord.length() >= 4 && wakeupWord.length() <= 6) {
            return true;
        }
        LogUtils.d(TAG, "change wakeup word error,new wakeup word is " + wakeupWord);
        return false;
    }

    private int processSwitchTTSSpeaker(String ttsSpeaker) {
        TTSUtils.switchSpeaker(this.mContext, this.mEngine, ttsSpeaker);
        return 0;
    }

    private String parseDormantLightStatus(boolean dormantLightState) {
        return dormantLightState ? "0" : "1";
    }

    private String getDormantStatus() {
        return PhicommDeviceStatusProcessor.getInstance().getDeviceStatus() == 5 ? "1" : "0";
    }

    private void responseToClient(String type, int status) {
        responseToClient(type, status, null);
    }

    private void responseToClient(String type, int status, Object content) {
        SelfDefinationResponseInfo selfDefinationResponseInfo = new SelfDefinationResponseInfo(type, status, content);
        SessionRegister.getUpDownMessageManager().onReportStatus(DstServiceName.DST_SERVICE_SELF_DEFINATION, null, DstServiceName.DST_SERVICE_SELF_DEFINATION, DstServiceName.DST_SERVICE_SELF_DEFINATION, selfDefinationResponseInfo);
    }

    private ActionResponse getActionResponse(int statusCode) {
        ActionResponse response = new ActionResponse();
        response.setActionStatus(statusCode);
        response.setDetailInfo(ActionStatus.getStateDetail(statusCode));
        response.setActionResponseId(UUID.randomUUID().toString());
        response.setActionTimestamp(System.currentTimeMillis() + "");
        return response;
    }

    private int processChangeLightingStatus(String lighting) {
        LogMgr.d(TAG, "processChangeLightingStatus: " + lighting);
        if (PhicommDeviceStatusProcessor.getInstance().getDeviceStatus() != 5) {
            return ResponseStatus.STATUS_FAIL_CHANGE_LIGHTING_STATUS;
        }
        if (lighting.equals("0")) {
            this.phicommLightController.turnOnDormantLight();
            UserPerferenceUtil.setDormantLightState(this.mContext, true);
            return 0;
        }
        this.phicommLightController.turnOffDormantLight();
        UserPerferenceUtil.setDormantLightState(this.mContext, false);
        return 0;
    }

    private int openSleepMode() {
        if (PhicommDeviceStatusProcessor.getInstance().getDeviceStatus() != 5) {
            this.phicommXController.triggeredDoubleClickEvent();
            return 0;
        }
        return 0;
    }

    private int closeSleepMode() {
        if (PhicommDeviceStatusProcessor.getInstance().getDeviceStatus() == 5) {
            this.phicommXController.triggeredOneClickEvent();
            return 0;
        }
        return 0;
    }

    private int openAmbientLight() {
        if (!PhicommPerferenceUtil.getAmbientLightState(this.mContext)) {
            this.phicommXController.openAmbientLight();
            PhicommPerferenceUtil.setAmbientLightState(this.mContext, true);
        }
        this.mEngine.playTTS(this.mContext.getString(R.string.tts_open_ambientlight));
        return 0;
    }

    private int closeAmbientLight() {
        if (PhicommPerferenceUtil.getAmbientLightState(this.mContext)) {
            this.phicommXController.closeAmbientLight();
            PhicommPerferenceUtil.setAmbientLightState(this.mContext, false);
        }
        this.mEngine.playTTS(this.mContext.getString(R.string.tts_close_ambientlight));
        return 0;
    }
}
