package com.unisound.ant.device;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import com.unisound.ant.device.bean.Accelerate;
import com.unisound.ant.device.bean.CommandOperate;
import com.unisound.ant.device.bean.DstServiceName;
import com.unisound.ant.device.bean.SessionData;
import com.unisound.ant.device.bean.VoiceChatMessage;
import com.unisound.ant.device.controlor.DefaultVolumeOperator;
import com.unisound.ant.device.devicelayer.DeviceStateMgr;
import com.unisound.ant.device.devicelayer.button.ButtonControl;
import com.unisound.ant.device.listener.MusicStatusListener;
import com.unisound.ant.device.listener.VoiceConnectStateListener;
import com.unisound.ant.device.mqtt.AliveTransportChannel;
import com.unisound.ant.device.mqtt.ChannelListener;
import com.unisound.ant.device.mqtt.OnMQTTStatusChangeListener;
import com.unisound.ant.device.mqtt.bean.ChannelParams;
import com.unisound.ant.device.mqtt.bean.LconInfo;
import com.unisound.ant.device.mqtt.bean.LconRequest;
import com.unisound.ant.device.mqtt.bean.ParamConfig;
import com.unisound.ant.device.netmodule.HttpReportUtils;
import com.unisound.ant.device.netmodule.NetChangeReceiver;
import com.unisound.ant.device.profile.DstServiceProfile;
import com.unisound.ant.device.receiver.AutoLocationReceiver;
import com.unisound.ant.device.receiver.InstallBroadcastReceiver;
import com.unisound.ant.device.service.ActionResponse;
import com.unisound.ant.device.service.BaseRequest;
import com.unisound.ant.device.service.ServiceProtocolUtil;
import com.unisound.ant.device.sessionlayer.BaseSessionLayer;
import com.unisound.ant.device.sessionlayer.DialogProfile;
import com.unisound.ant.device.sessionlayer.SessionUpdateCallBack;
import com.unisound.vui.common.config.ANTConfigPreference;
import com.unisound.vui.common.location.bean.LocationInfo;
import com.unisound.vui.common.location.listener.LocationListener;
import com.unisound.vui.common.media.UniMediaPlayer;
import com.unisound.vui.engine.ANTEngine;
import com.unisound.vui.engine.ANTEngineConfig;
import com.unisound.vui.engine.ANTEngineOption;
import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.handler.SimpleUserEventInboundHandler;
import com.unisound.vui.transport.config.RuntimeConfig;
import com.unisound.vui.util.AppGlobalConstant;
import com.unisound.vui.util.ExoConstants;
import com.unisound.vui.util.JsonTool;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.NetworkConnectChangedReceiver;
import com.unisound.vui.util.SystemUitls;
import com.unisound.vui.util.UserPerferenceUtil;
import java.util.List;
import java.util.UUID;
import logreport.FullLog;
import nluparser.MixtureProcessor;
import nluparser.scheme.ASR;
import nluparser.scheme.AudioResult;
import nluparser.scheme.LocalASR;
import nluparser.scheme.Mixture;
import nluparser.scheme.MusicResult;
import nluparser.scheme.NLU;
import nluparser.scheme.Result;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DeviceCenterHandler extends SimpleUserEventInboundHandler implements ChannelListener, SessionUpdateCallBack, NetChangeReceiver.NetStateListener, VoiceConnectStateListener, LocationListener, MusicStatusListener, InstallBroadcastReceiver.InstallStateListener {
    private static final String TAG = "DeviceCenterHandler";
    private static ButtonControl buttonControlMusic;
    private static DeviceCenterHandler deviceCenterMgr;
    private AliveTransportChannel aliveTransportChannel;
    private AutoLocationReceiver autoLocationReceiver;
    private ANTHandlerContext ctx;
    private DeviceStateMgr deviceStateMgr;
    private Handler handler;
    private InstallBroadcastReceiver installBroadcastReceiver;
    private boolean isInSceneControl;
    private Context mContext;
    private LocationInfo mLocationInfo;
    private String mUdid;
    private MixtureProcessor mixtureProcessor;
    private NetChangeReceiver netChangeReceiver;
    private OnMQTTStatusChangeListener phicommMQTTStatausChange;
    private BaseSessionLayer sessionLayer;
    private HandlerThread taskThread;
    private int delayTime = 0;
    private int utteranceTime = 0;
    private int mMusicStatus = -1;

    public DeviceCenterHandler(Context context, OnMQTTStatusChangeListener onMQTTStatusChangeListener) {
        LogMgr.d(TAG, "-->>DeviceCenterHandler");
        this.taskThread = new HandlerThread("deviceCenterHandler");
        this.taskThread.start();
        this.mContext = context.getApplicationContext();
        this.phicommMQTTStatausChange = onMQTTStatusChangeListener;
        this.handler = new Handler(this.taskThread.getLooper());
        this.aliveTransportChannel = AliveTransportChannel.getChannelInstance();
        this.aliveTransportChannel.setChannelListener(this);
        this.netChangeReceiver = new NetChangeReceiver();
        this.netChangeReceiver.setStateListener(this);
        this.netChangeReceiver.registerNetStateReceiver(context);
        this.installBroadcastReceiver = new InstallBroadcastReceiver();
        this.installBroadcastReceiver.setStateListener(this);
        this.installBroadcastReceiver.registerInstallStateReceiver(context);
        this.sessionLayer = new BaseSessionLayer(context, this);
        this.deviceStateMgr = new DeviceStateMgr(context);
        this.autoLocationReceiver = new AutoLocationReceiver(context, this);
        this.autoLocationReceiver.startLocation();
    }

    public static void init(Context context, OnMQTTStatusChangeListener onMQTTStatusChangeListener) {
        if (deviceCenterMgr == null) {
            synchronized (DeviceCenterHandler.class) {
                if (deviceCenterMgr == null) {
                    deviceCenterMgr = new DeviceCenterHandler(context, onMQTTStatusChangeListener);
                }
            }
        }
    }

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    protected void initPriority() {
        setPriority(300);
    }

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler, com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void userEventTriggered(final Object evt, ANTHandlerContext ctx) throws Exception {
        if (ANTConfigPreference.isWakeupTest) {
            List<String> wakeupList = UserPerferenceUtil.getWakeupWord(this.mContext, true);
            if (evt instanceof LocalASR) {
                String wakeupWord = ((LocalASR) evt).getRecognitionResult().replaceAll("\\s", "");
                if (wakeupList.contains(wakeupWord)) {
                    ctx.enterWakeup(false);
                    return;
                }
            }
        }
        if (evt instanceof NLU) {
            this.handler.post(new Runnable() { // from class: com.unisound.ant.device.DeviceCenterHandler.1
                @Override // java.lang.Runnable
                public void run() {
                    List<AudioResult.Music> musics;
                    NLU nlu = (NLU) evt;
                    String result = nlu.getAsrResult();
                    Mixture mixture = DeviceCenterHandler.this.mixtureProcessor.from(result);
                    if (mixture != null && mixture.getNluList() != null) {
                        List<NLU> nluList = mixture.getNluList();
                        if (nluList.size() <= 0) {
                            LogMgr.d(DeviceCenterHandler.TAG, "nluList size is 0");
                            return;
                        }
                        NLU reportNlu = nluList.get(0);
                        if (reportNlu.getData() != null && reportNlu.getData().getResult() != null) {
                            Result nluResult = reportNlu.getData().getResult();
                            if (nluResult instanceof MusicResult) {
                                List<MusicResult.Music> musics2 = ((MusicResult) nluResult).getMusicinfo();
                                if (musics2 != null && musics2.size() > 1) {
                                    MusicResult.Music musicFirst = musics2.get(0);
                                    musics2.clear();
                                    musics2.add(musicFirst);
                                    return;
                                }
                                return;
                            }
                            if ((nluResult instanceof AudioResult) && (musics = ((AudioResult) nluResult).getPlaylist()) != null && musics.size() > 1) {
                                AudioResult.Music musicFirst2 = musics.get(0);
                                musics.clear();
                                musics.add(musicFirst2);
                            }
                        }
                    }
                }
            });
        }
        if (evt instanceof FullLog) {
            this.handler.post(new Runnable() { // from class: com.unisound.ant.device.DeviceCenterHandler.2
                @Override // java.lang.Runnable
                public void run() {
                    FullLog fullLog = (FullLog) evt;
                    String result = fullLog.getNlu().getAsrResult();
                    Mixture mixtureData = DeviceCenterHandler.this.mixtureProcessor.from(result);
                    String ttsData = fullLog.getTtsData().replaceAll("<py>[\\d|\\w]+</py>", "");
                    if (mixtureData != null && mixtureData.getLocalASRList() != null) {
                        mixtureData.setTtsDataValue(ttsData);
                        DeviceCenterHandler.this.reportLogToCloud(mixtureData, CommandOperate.CMD_VAL_SYNC_LOCAL_LOG);
                    } else if (mixtureData != null && mixtureData.getNluList() != null) {
                        mixtureData.setTtsDataValue(ttsData);
                        DeviceCenterHandler.this.reportLogToCloud(mixtureData, CommandOperate.CMD_VAL_SYNC_NET_LOG);
                    }
                }
            });
        }
        super.userEventTriggered(evt, ctx);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportLogToCloud(Mixture evt, String cmdValue) {
        VoiceChatMessage chatMessage = new VoiceChatMessage();
        chatMessage.setLogId(UUID.randomUUID().toString());
        chatMessage.setCreateTime(SystemUitls.getTime());
        chatMessage.setMsg(JsonTool.toJson(evt));
        this.deviceStateMgr.reportLocalAsrLog(cmdValue, chatMessage);
    }

    public static DeviceCenterHandler getDeviceCenterMgr() {
        if (deviceCenterMgr == null) {
            throw new IllegalArgumentException("please init deviceCenterMgr");
        }
        return deviceCenterMgr;
    }

    public DeviceCenterHandler associateEngine(ANTEngine engine, MixtureProcessor processor) {
        this.mixtureProcessor = processor;
        return deviceCenterMgr;
    }

    private void initHardware(ANTHandlerContext mCtx) {
        buttonControlMusic = new ButtonControl(mCtx);
        this.deviceStateMgr.bindAntHandlerContext(mCtx);
        checkNetState();
    }

    private void checkNetState() {
        boolean isBinded = UserPerferenceUtil.getDeviceBindState(this.mContext);
        LogMgr.d(TAG, "--->>initHardware current device isBinded:" + isBinded);
        this.netChangeReceiver.registerNetReceiverAndCheck(this.mContext);
    }

    public static ButtonControl getButtonControler() {
        return buttonControlMusic;
    }

    public DeviceStateMgr getDeviceStateMgr() {
        return this.deviceStateMgr;
    }

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler, com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventEngineInitDone(ANTHandlerContext ctx) {
        LogMgr.d(TAG, "-->>onASREventEngineInitDone");
        this.ctx = ctx;
        this.mUdid = (String) ctx.engine().config().getOption(ANTEngineOption.GENERAL_UDID);
        LogMgr.d(TAG, "engine init done, udid is " + this.mUdid);
        AppGlobalConstant.setUdid(this.mUdid);
        if (this.mLocationInfo != null) {
            updateLocation(this.mLocationInfo);
            this.mLocationInfo = null;
        }
        initHardware(ctx);
        return super.onASREventEngineInitDone(ctx);
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onWakeupResult(ANTHandlerContext ctx, String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject localAsr = (JSONObject) jsonObject.getJSONArray(ASR.LOCAL_ASR).get(0);
            this.delayTime = localAsr.getInt("delayTime");
            this.utteranceTime = localAsr.getInt("utteranceTime");
        } catch (Exception e) {
            LogMgr.d(TAG, "exception:" + e);
        }
        if (UserPerferenceUtil.getNeedInstallUpdate(this.mContext)) {
            this.deviceStateMgr.startInstallAPK();
            return false;
        }
        LogMgr.d(TAG, "Continues onWakeupEventRecognitionSuccess");
        ctx.engine().setWakeupRecord(false);
        if (ANTConfigPreference.isWakeupAecTest) {
            LogMgr.d(TAG, "mMusicStatus = " + this.mMusicStatus + " -->music_wakeup_test");
            if (this.mMusicStatus != 3) {
                return true;
            }
            ctx.enterWakeup(false);
            return true;
        }
        if (!ANTConfigPreference.isWakeupTest) {
            return false;
        }
        ctx.enterWakeup(false);
        return true;
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventSynthesizerStart(ANTHandlerContext ctx) throws IllegalStateException {
        LogMgr.d(TAG, "-->>onTTSEventSynthesizerStart");
        if (UniMediaPlayer.getInstance().isPlaying()) {
            UniMediaPlayer.getInstance().stop();
        }
        return super.onTTSEventSynthesizerStart(ctx);
    }

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler, com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventPlayingEnd(ANTHandlerContext ctx) {
        LogMgr.d(TAG, "-->>onTTSEventPlayingEnd ctx.engine().getEngineState=" + ctx.engine().getEngineState());
        if (this.eventReceived) {
            exit(true);
        }
        return super.onTTSEventPlayingEnd(ctx);
    }

    @Override // com.unisound.ant.device.mqtt.ChannelListener
    public void onChannelConnected() {
        LogMgr.d(TAG, "-->>onChannelConnected");
        this.deviceStateMgr.onTransportChannelConencted();
        this.mContext.sendBroadcast(new Intent(NetworkConnectChangedReceiver.NET_ALIVE_CONNECTED));
    }

    @Override // com.unisound.ant.device.mqtt.ChannelListener
    public void onChannelDisConnected() {
        LogMgr.d(TAG, "-->>onChannelConnected");
    }

    @Override // com.unisound.ant.device.mqtt.ChannelListener
    public void onSendDataResult(int state, String descript) {
    }

    @Override // com.unisound.ant.device.mqtt.ChannelListener
    public void onReceivedMsg(int state, String message) {
        LogMgr.d(TAG, "-->>onReceivedMsg state:" + state + ",message:" + message);
        if (this.sessionLayer == null) {
            throw new IllegalArgumentException("sessionLayer may be null.");
        }
        this.sessionLayer.filterSessionContent(message);
    }

    @Override // com.unisound.ant.device.mqtt.ChannelListener
    public void onChannelClose() {
        LogMgr.d(TAG, "-->>onChannelClose");
    }

    @Override // com.unisound.ant.device.netmodule.NetChangeReceiver.NetStateListener
    public void onNetConnecting() {
        LogMgr.d(TAG, "-->>onChannelClose");
    }

    @Override // com.unisound.ant.device.netmodule.NetChangeReceiver.NetStateListener
    public void onNetConnected() {
        LogMgr.d(TAG, "--->>onNetConnected aliveTransportChannel:" + this.aliveTransportChannel);
        RuntimeConfig.setOnceConnectSuc(true);
        LogMgr.d(TAG, "current wifi is connected:" + RuntimeConfig.isConnectedWifi());
        if (this.aliveTransportChannel != null) {
            initLconParam(this.mContext);
            ChannelParams params = new ChannelParams(this.mContext, AppGlobalConstant.getUdid());
            params.setAppKey(ExoConstants.APP_KEY);
            params.setAppSecret(ExoConstants.APP_SECRET);
            this.aliveTransportChannel.openChannel(this.mContext, params, this.phicommMQTTStatausChange);
        }
        this.deviceStateMgr.onNetConnectedSuccess();
    }

    private void initLconParam(Context context) {
        LconRequest<LconInfo> request = new LconRequest<>();
        request.setCommand("queryLongConnInfo");
        request.setVersion(SystemUitls.getAppVersion(context));
        request.setTcl(new LconRequest.EffectiveToken(SystemUitls.getIMEI(context), ""));
        LconInfo info = new LconInfo();
        info.setAppKey(ExoConstants.APP_KEY);
        info.setDeviceType(ParamConfig.DEVICE_TYPE_GENERAL);
        info.setUdid(AppGlobalConstant.getUdid());
        request.setData(info);
        ParamConfig.setLconRequest(request);
    }

    @Override // com.unisound.ant.device.netmodule.NetChangeReceiver.NetStateListener
    public void onNetDisconnected() {
        LogMgr.d(TAG, "--->>onNetDisconnected");
        if (this.aliveTransportChannel != null) {
            this.aliveTransportChannel.closeChannel();
        }
    }

    @Override // com.unisound.ant.device.sessionlayer.SessionUpdateCallBack
    public void onSessionDataUpdate(String messageType, SessionData sessionData) {
        BaseRequest<SessionData> reqContent = ServiceProtocolUtil.getReqContent(messageType, sessionData);
        if (isMusicData(sessionData)) {
            HttpReportUtils.httpReportMusicInfo(this.mUdid, reqContent);
            return;
        }
        if (isASRData(sessionData)) {
            HttpReportUtils.httpReportASRLog(this.mUdid, reqContent);
            return;
        }
        String statusJson = JsonTool.toJson(reqContent);
        LogMgr.d(TAG, "send to server data:" + statusJson);
        if (this.aliveTransportChannel != null && statusJson != null) {
            this.aliveTransportChannel.sendData(statusJson);
        } else {
            LogMgr.e(TAG, "---->>onSessionDataUpdate aliveTransportChannel is null");
        }
    }

    private boolean isMusicData(SessionData data) {
        String service = data.getDialog().getDstService();
        return DstServiceName.DST_SERVICE_MUSIC.equals(service) || DstServiceName.DST_SERVICE_AUDIO.equals(service) || DstServiceName.DST_SERVICE_NEWS.equals(service);
    }

    private boolean isASRData(SessionData data) {
        DstServiceProfile dstService = data.getDstService();
        Accelerate accelerate = dstService.getAccelerate();
        return accelerate != null && (CommandOperate.CMD_VAL_SYNC_NET_LOG.equals(accelerate.getValuse()) || CommandOperate.CMD_VAL_SYNC_LOCAL_LOG.equals(accelerate.getValuse()));
    }

    @Override // com.unisound.ant.device.sessionlayer.SessionUpdateCallBack
    public void onCloudSessionResponse(ActionResponse actionReponse) {
    }

    @Override // com.unisound.ant.device.sessionlayer.SessionUpdateCallBack
    public void onSessionToSceneControl(String sceneFlag) {
        if ("start".equals(sceneFlag) || DialogProfile.SCENE_FLAG_PROCESSING.equals(sceneFlag)) {
            this.isInSceneControl = true;
        } else if ("end".equals(sceneFlag)) {
            this.isInSceneControl = false;
        }
    }

    @Override // com.unisound.ant.device.listener.VoiceConnectStateListener
    public void onStartVoiceConnect() {
        this.netChangeReceiver.unregisterNetStateReceiver(this.mContext);
    }

    @Override // com.unisound.ant.device.listener.VoiceConnectStateListener
    public void onVoiceConnectSuccess() {
        LogMgr.d(TAG, "--->>onVoiceConnectSuccess");
        this.netChangeReceiver.registerNetReceiverAndCheck(this.mContext);
    }

    @Override // com.unisound.ant.device.listener.VoiceConnectStateListener
    public void onVoiceConnectFailStart() {
    }

    @Override // com.unisound.vui.handler.SimpleSessionManagementHandler
    protected void doInterrupt(ANTHandlerContext ctx, String interruptType) throws IllegalStateException {
        if (this.eventReceived && UniMediaPlayer.getInstance().isPlaying()) {
            UniMediaPlayer.getInstance().stop();
        }
    }

    @Override // com.unisound.ant.device.listener.VoiceConnectStateListener
    public void onVoiceConnectFailEnd() {
    }

    public void exit(boolean fireResume) {
        this.ctx.cancelTTS();
        reset();
        if (fireResume) {
            fireResume(this.ctx);
        }
    }

    @Override // com.unisound.ant.device.listener.MusicStatusListener
    public void onMusicStatusChanged(int status) {
        float currentBenchMark = getEffectBenchmark();
        LogMgr.d(TAG, "onMusicStatusChanged status:" + status + ",currentBenchMark" + currentBenchMark);
        ANTConfigPreference.effectWakeupBenchmark = currentBenchMark;
        this.mMusicStatus = status;
    }

    private float getCurrentMusicWakeupBenchmark() {
        int currentVolume = DefaultVolumeOperator.getInstance(this.mContext).getCurrentVolume();
        LogMgr.d(TAG, "onMusicStatusChanged music playing  volume:" + currentVolume);
        if (currentVolume < 8) {
            return ANTConfigPreference.wakeupBenchmark;
        }
        if (currentVolume < 10) {
            return ANTConfigPreference.wakeupBenchmarkForMusicPlaying;
        }
        if (currentVolume < 12) {
            return ANTConfigPreference.wakeupBenchmarkForMusicPlayingTwo;
        }
        return ANTConfigPreference.wakeupBenchmarkForMusicPlayingThree;
    }

    private float getEffectBenchmark() {
        return this.mMusicStatus == 3 ? getCurrentMusicWakeupBenchmark() : ANTConfigPreference.wakeupBenchmark;
    }

    @Override // com.unisound.ant.device.receiver.InstallBroadcastReceiver.InstallStateListener
    public void onInstallFailed() {
        LogMgr.d(TAG, "onInstallFailed()");
        this.ctx.enterWakeup(false);
    }

    @Override // com.unisound.vui.common.location.listener.LocationListener
    public void onLocationSuccess(LocationInfo locationInfo) {
        LogMgr.d(TAG, "onLocationChange info:" + locationInfo);
        if (this.ctx != null) {
            LogMgr.d(TAG, "ctx != null, update location info");
            updateLocation(locationInfo);
            this.autoLocationReceiver.onDestory();
            this.mLocationInfo = null;
            return;
        }
        LogMgr.e(TAG, "onLocationChange ANTEngineConfig ctx is null");
        this.mLocationInfo = locationInfo;
    }

    private void updateLocation(LocationInfo info) {
        ANTEngineConfig config = this.ctx.engine().config();
        config.setOption(ANTEngineOption.GENERAL_GPS, info.getLatitude() + "," + info.getLongitude());
        config.setOption(ANTEngineOption.GENERAL_CITY, info.getCity());
    }

    @Override // com.unisound.vui.common.location.listener.LocationListener
    public void onLocationFail(String locationErrorMessage) {
        LogMgr.d(TAG, "onLocationFail : " + locationErrorMessage);
    }
}
