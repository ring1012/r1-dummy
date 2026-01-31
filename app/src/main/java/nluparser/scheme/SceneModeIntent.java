package nluparser.scheme;

import android.support.annotation.Nullable;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class SceneModeIntent implements Intent {

    @SerializedName("isOpen")
    @Nullable
    @JSONField(name = "isOpen")
    boolean isOpen;

    @SerializedName("lightParam")
    @Nullable
    @JSONField(name = "lightParam")
    LightControl lightParam;

    @SerializedName("modeState")
    @Nullable
    @JSONField(name = "modeState")
    String modeState;

    @SerializedName("sceneName")
    @Nullable
    @JSONField(name = "sceneName")
    String sceneName;

    @SerializedName("smartAppliancesParam")
    @Nullable
    @JSONField(name = "smartAppliancesParam")
    SmartAppliancesControl smartAppliancesParam;

    @SerializedName("speakerParam")
    @Nullable
    @JSONField(name = "speakerParam")
    SpeakerEngineControl speakerParam;

    @SerializedName("startTime")
    @Nullable
    @JSONField(name = "startTime")
    String startTime;

    @SerializedName("stopTime")
    @Nullable
    @JSONField(name = "stopTime")
    String stopTime;

    public static class LightControl {

        @SerializedName("brightness")
        @Nullable
        @JSONField(name = "brightness")
        String brightness;

        @SerializedName("lightColor")
        @Nullable
        @JSONField(name = "lightColor")
        String lightColor;

        @SerializedName("lightEffectType")
        @Nullable
        @JSONField(name = "lightEffectType")
        String lightEffectType;

        public String getBrightness() {
            return this.brightness;
        }

        public String getLightColor() {
            return this.lightColor;
        }

        public String getLightEffectType() {
            return this.lightEffectType;
        }

        public void setBrightness(String brightness) {
            this.brightness = brightness;
        }

        public void setLightColor(String lightColor) {
            this.lightColor = lightColor;
        }

        public void setLightEffectType(String lightEffectType) {
            this.lightEffectType = lightEffectType;
        }
    }

    public static class SmartAppliancesControl {
    }

    public static class SpeakerEngineControl {
        private String agent;
        private boolean isOpen;

        public String getAgent() {
            return this.agent;
        }

        public boolean isOpen() {
            return this.isOpen;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }

        public void setOpen(boolean open) {
            this.isOpen = open;
        }
    }

    public LightControl getLightParam() {
        return this.lightParam;
    }

    @Nullable
    public String getModeState() {
        return this.modeState;
    }

    public String getSceneName() {
        return this.sceneName;
    }

    public SmartAppliancesControl getSmartAppliancesParam() {
        return this.smartAppliancesParam;
    }

    public SpeakerEngineControl getSpeakerParam() {
        return this.speakerParam;
    }

    @Nullable
    public String getStartTime() {
        return this.startTime;
    }

    @Nullable
    public String getStopTime() {
        return this.stopTime;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setLightParam(LightControl lightParam) {
        this.lightParam = lightParam;
    }

    public void setModeState(@Nullable String modeState) {
        this.modeState = modeState;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public void setSmartAppliancesParam(SmartAppliancesControl smartAppliancesParam) {
        this.smartAppliancesParam = smartAppliancesParam;
    }

    public void setSpeakerParam(SpeakerEngineControl speakerParam) {
        this.speakerParam = speakerParam;
    }

    public void setStartTime(@Nullable String startTime) {
        this.startTime = startTime;
    }

    public void setStopTime(@Nullable String stopTime) {
        this.stopTime = stopTime;
    }
}
