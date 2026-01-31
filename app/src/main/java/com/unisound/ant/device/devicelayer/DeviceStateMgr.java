package com.unisound.ant.device.devicelayer;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import com.google.gson.JsonObject;
import com.unisound.ant.device.bean.ActionStatus;
import com.unisound.ant.device.bean.CommandOperate;
import com.unisound.ant.device.bean.DeviceSummaryInfo;
import com.unisound.ant.device.bean.DstServiceState;
import com.unisound.ant.device.bean.GpsInfo;
import com.unisound.ant.device.bean.OnOffLineMessage;
import com.unisound.ant.device.bean.SceneModeInfo;
import com.unisound.ant.device.bean.UnisoundDeviceCommand;
import com.unisound.ant.device.bean.UserBinderStatus;
import com.unisound.ant.device.netmodule.DeviceInfoUtils;
import com.unisound.ant.device.profile.DeviceProfileInfo;
import com.unisound.ant.device.service.ActionResponse;
import com.unisound.ant.device.service.DeviceCommandFactory;
import com.unisound.ant.device.sessionlayer.SessionExecuteHandler;
import com.unisound.ant.device.sessionlayer.SessionRegister;
import com.unisound.vui.common.location.bean.LocationInfo;
import com.unisound.vui.common.media.IMediaPlayerStateListener;
import com.unisound.vui.engine.ANTEngineOption;
import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.transport.out.BindStatusEvent;
import com.unisound.vui.util.AppGlobalConstant;
import com.unisound.vui.util.JsonTool;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.ThreadUtils;
import com.unisound.vui.util.UserPerferenceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import nluparser.scheme.General;
import nluparser.scheme.NLU;
import nluparser.scheme.Operands;
import nluparser.scheme.SCode;
import nluparser.scheme.SName;
import nluparser.scheme.SceneModeIntent;
import nluparser.scheme.Semantic;
import nluparser.scheme.SettingExtIntent;
import nluparser.scheme.SettingIntent;

/* loaded from: classes.dex */
public class DeviceStateMgr extends SessionExecuteHandler implements IMediaPlayerStateListener {
    private static final String DEVICE_STATUS_BIND = "binded";
    private static final String DEVICE_STATUS_UNBIND = "unBinded";
    private static final String START_TAG = "start_tag";
    private static final String TAG = "DeviceStateMgr";
    private Map<String, String> actionResponses = new HashMap();
    private Context context;
    protected ANTHandlerContext ctx;

    public DeviceStateMgr(Context context) {
        this.context = context;
        SessionRegister.associateSessionCenter("deviceManagement", this);
    }

    public void bindAntHandlerContext(ANTHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 0:
                UnisoundDeviceCommand command = (UnisoundDeviceCommand) msg.obj;
                disPatchDeviceStateUpdateCommand(command);
                break;
            case 1:
                ActionResponse actionResponse = (ActionResponse) msg.obj;
                dispatcherCloudResponse(actionResponse);
                break;
        }
    }

    public void disPatchDeviceStateUpdateCommand(UnisoundDeviceCommand command) {
        String operation = command.getOperation();
        JsonObject parameter = (JsonObject) command.getParameter();
        LogMgr.d(TAG, "--->>disPatchDeviceStateUpdateCommand command:" + command);
        if (CommandOperate.COMMAND_OPERATE_DEVICE_STATE_BIND.equals(operation)) {
            UserBinderStatus binderStatus = (UserBinderStatus) JsonTool.fromJson(parameter, UserBinderStatus.class);
            String status = binderStatus.getState();
            if (DEVICE_STATUS_BIND.equals(status)) {
                this.ctx.pipeline().write(new BindStatusEvent(true));
                return;
            } else {
                if (DEVICE_STATUS_UNBIND.equals(status)) {
                    this.ctx.pipeline().write(new BindStatusEvent(false));
                    return;
                }
                return;
            }
        }
        if (CommandOperate.COMMAND_OPERATE_MODIFY_DEVICE_NAME.equals(operation)) {
            DeviceSummaryInfo info = (DeviceSummaryInfo) JsonTool.fromJson(parameter, DeviceSummaryInfo.class);
            String nickName = info.getDeviceNickName();
            fireModifyDeviceNickNameNlu(nickName);
        } else if (CommandOperate.COMMAND_OPERATE_MODE_SETTING.equals(operation) || CommandOperate.COMMAND_OPERATE_SET_MODE_STATE.equals(operation)) {
            SceneModeInfo info2 = (SceneModeInfo) JsonTool.fromJson(parameter, SceneModeInfo.class);
            fireModeSetting(info2);
        }
    }

    private void dispatcherCloudResponse(ActionResponse actionResponse) {
        String cloudResponseId = actionResponse.getActionResponseId();
        String operateWithId = getOperateResponseId("deviceOnline");
        LogMgr.d(TAG, "--->>dispatcherCloudResponse cloudResponseId:" + cloudResponseId + ",operateWithId:" + operateWithId);
        if (cloudResponseId.equals(operateWithId)) {
            responseCloudCommand(CommandOperate.COMMAND_OPERATE_CHANGE_WIFI, getActionResponse(0));
        }
    }

    public ActionResponse getActionResponse(int statusCode) {
        ActionResponse response = new ActionResponse();
        response.setActionStatus(statusCode);
        response.setDetailInfo(ActionStatus.getStateDetail(statusCode));
        response.setActionResponseId(UUID.randomUUID().toString());
        response.setActionTimestamp(System.currentTimeMillis() + "");
        return response;
    }

    public void fireModeSetting(SceneModeInfo info) {
        NLU nlu = new NLU();
        nlu.setService(SName.SCENE_MODE);
        General general = new General();
        general.setText(info.getModeTip());
        nlu.setGeneral(general);
        Semantic semantic = new Semantic();
        SceneModeIntent intent = new SceneModeIntent();
        intent.setSceneName(info.getModeType());
        intent.setOpen(info.isOpen());
        intent.setModeState(info.getModeState());
        semantic.setIntent(intent);
        nlu.setSemantic(semantic);
        if (this.ctx != null) {
            this.ctx.pipeline().fireUserEventTriggered(nlu);
            responseCloudCommand(CommandOperate.COMMAND_OPERATE_MODE_SETTING, getActionResponse(0));
        } else {
            LogMgr.e("-->>ctx is null");
            responseCloudCommand(CommandOperate.COMMAND_OPERATE_MODE_SETTING, getActionResponse(-1));
        }
    }

    private void fireModifyDeviceNickNameNlu(String nickName) {
        LogMgr.d(TAG, "--->>fireModifyDeviceNickNameNlu nickName:" + nickName);
        NLU nlu = new NLU();
        nlu.setService(SName.SETTING);
        nlu.setCode(SCode.SETTING_EXEC);
        Semantic semantic = new Semantic();
        SettingExtIntent intent = new SettingExtIntent();
        SettingIntent operatorIntent = new SettingIntent();
        List<SettingIntent> operators = new ArrayList<>();
        operatorIntent.setOperands(Operands.ATTR_NAME);
        operatorIntent.setOperator("ACT_SET");
        operatorIntent.setValue(nickName);
        operators.add(operatorIntent);
        intent.setOperations(operators);
        semantic.setIntent(intent);
        nlu.setSemantic(semantic);
        if (this.ctx != null) {
            this.ctx.engine().pipeline().fireUserEventTriggered(nlu);
            responseCloudCommand(CommandOperate.COMMAND_OPERATE_MODIFY_DEVICE_NAME, getActionResponse(0));
        } else {
            LogMgr.d(TAG, "-->>fireModifyDeviceNickNameNlu current ctx is null");
            responseCloudCommand(CommandOperate.COMMAND_OPERATE_MODIFY_DEVICE_NAME, getActionResponse(-1));
        }
    }

    public void onNetConnectedSuccess() {
    }

    public void onTransportChannelConencted() {
        reportHardwareParameters();
        ThreadUtils.execute(new Runnable() { // from class: com.unisound.ant.device.devicelayer.DeviceStateMgr.1
            @Override // java.lang.Runnable
            public void run() {
                DeviceStateMgr.this.queryDeviceBoundStatus();
            }
        });
        post(new Runnable() { // from class: com.unisound.ant.device.devicelayer.DeviceStateMgr.2
            @Override // java.lang.Runnable
            public void run() {
                DeviceStateMgr.this.reportDeviceLocationInfo();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void queryDeviceBoundStatus() {
        if (this.ctx == null) {
            reQueryDeviceBoundStatus();
            return;
        }
        LogMgr.d(TAG, "query device bound status");
        String udid = (String) this.ctx.engine().config().getOption(ANTEngineOption.GENERAL_UDID);
        try {
            boolean bound = DeviceInfoUtils.isDeviceBounded(udid);
            UserPerferenceUtil.setDeviceBindState(this.context, bound);
        } catch (Exception e) {
            LogMgr.e(TAG, "queryDeviceBoundStatus error, " + e.getMessage());
            reQueryDeviceBoundStatus();
        }
    }

    private void reQueryDeviceBoundStatus() {
        ThreadUtils.execute(new Runnable() { // from class: com.unisound.ant.device.devicelayer.DeviceStateMgr.3
            @Override // java.lang.Runnable
            public void run() throws InterruptedException {
                ThreadUtils.sleep(3000L);
                DeviceStateMgr.this.queryDeviceBoundStatus();
            }
        });
    }

    private void reportDevcieOnlineMsg(String accessId) {
        OnOffLineMessage message = new OnOffLineMessage();
        message.setUdid(AppGlobalConstant.getUdid());
        message.setConnAccessId(accessId);
        UnisoundDeviceCommand command = DeviceCommandFactory.buildCommand("deviceManagement", "deviceOnline", message);
        reportDeviceParametersInfo(true, DstServiceState.SERVICE_STATE_SETTING_OVER, command);
    }

    public void reportDeviceLocationInfo() {
        String locationInfo = UserPerferenceUtil.getLocationInfo(this.context);
        if (!TextUtils.isEmpty(locationInfo)) {
            LogMgr.d(TAG, "get userLocationInfo :" + locationInfo);
            LocationInfo info = (LocationInfo) JsonTool.fromJson(locationInfo, LocationInfo.class);
            GpsInfo gpsInfo = new GpsInfo();
            gpsInfo.setCountry(info.getmCountry());
            gpsInfo.setProvince(info.getProvince());
            gpsInfo.setCity(info.getCity());
            gpsInfo.setRegion(info.getDistrict());
            gpsInfo.setAddress(info.getAddress());
            gpsInfo.setLat(info.getLatitude());
            gpsInfo.setLon(info.getLongitude());
            UnisoundDeviceCommand command = DeviceCommandFactory.buildCommand("deviceManagement", CommandOperate.COMMAND_OPERATE_UPDATE_LOCATION, gpsInfo);
            reportDeviceParametersInfo(false, DstServiceState.SERVICE_STATE_SETTING_OVER, command);
        }
    }

    private void reportHardwareParameters() {
        DeviceProfileInfo deviceProfile = DeviceUtils.getDeviceProfile(this.context);
        UnisoundDeviceCommand command = DeviceCommandFactory.buildCommand("deviceManagement", "uploadDeviceInfo", deviceProfile);
        reportDeviceParametersInfo(true, DstServiceState.SERVICE_STATE_SETTING_OVER, command);
    }

    public void reportLocalAsrLog(String messageType, Object content) {
        LogMgr.d(TAG, "--->>reportLocalAsrLog content:" + content);
        if (SessionRegister.getUpDownMessageManager() == null) {
            LogMgr.d(TAG, "--->>messgaeMonitor is null");
        } else {
            SessionRegister.getUpDownMessageManager().onReportStatus(messageType, content);
        }
    }

    private void reportDeviceParametersInfo(boolean isNeedAck, String dstState, UnisoundDeviceCommand unisoundDeviceCommand) {
        if (SessionRegister.getUpDownMessageManager() == null) {
            LogMgr.d(TAG, "--->>messgaeMonitor is null");
        } else if (isNeedAck) {
            SessionRegister.getUpDownMessageManager().onReportDeviceStatusWithAck(dstState, saveActionResponseId(unisoundDeviceCommand.getOperation()), unisoundDeviceCommand);
        } else {
            SessionRegister.getUpDownMessageManager().onReportDeviceStatusWithAckWithOutAck(dstState, unisoundDeviceCommand);
        }
    }

    private void responseCloudCommand(String action, ActionResponse response) {
        SessionRegister.getUpDownMessageManager().reponseCloudCommandWithoutAck(action, response);
    }

    @Override // com.unisound.vui.common.media.IMediaPlayerStateListener
    public void onPlayerState(int state, String tag) {
        LogMgr.d(TAG, "--->>onPlayerState state:" + state);
        if (state == 4) {
            if (this.ctx != null) {
                this.ctx.enterWakeup(false);
            }
            if (START_TAG.equals(tag) && UserPerferenceUtil.getNeedInstallUpdate(this.context)) {
                startInstallAPK();
                LogMgr.e(TAG, "OtaUtil.notifyToInstall");
            }
        }
    }

    public void startInstallAPK() {
        LogMgr.d(TAG, "startInstallAPK()");
        NLU nlu = new NLU();
        nlu.setService(SName.OTA);
        nlu.setCode("OTA_NOTIFY_INSTALL");
        if (this.ctx != null) {
            this.ctx.cancelEngine();
            this.ctx.pipeline().fireUserEventTriggered(nlu);
        } else {
            LogMgr.e("-->>ctx is null");
        }
    }

    private void saveActionRespIds(String operate, String actionRespId) {
        this.actionResponses.put(operate, actionRespId);
    }

    private String getOperateResponseId(String operate) {
        return this.actionResponses.get(operate);
    }

    private String saveActionResponseId(String operate) {
        String actionResponseId = UUID.randomUUID().toString();
        saveActionRespIds(operate, actionResponseId);
        return actionResponseId;
    }
}
