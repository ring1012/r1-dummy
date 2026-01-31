package com.unisound.vui.transport.out;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.unisound.vui.auth.BasicCredentials;
import com.unisound.vui.bootstrap.ANTELocalConfiguration;
import com.unisound.vui.engine.ANTEngineOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SdkCreatorInfo {

    @SerializedName("credentials")
    @JSONField(name = "credentials")
    private BasicCredentials credentials;

    @SerializedName("engineOption")
    @JSONField(name = "engineOption")
    private Map<String, Object> engineOption;

    @SerializedName("localConfiguration")
    @JSONField(name = "localConfiguration")
    private ANTELocalConfiguration localConfiguration;

    @SerializedName("mainVocab")
    @JSONField(name = "mainVocab")
    private Map<String, List<String>> mainVocab;

    @SerializedName("maintag")
    @JSONField(name = "maintag")
    private String maintag;

    @SerializedName("wakeupWord")
    @JSONField(name = "wakeupWord")
    private List<String> wakeupWord;

    public BasicCredentials getCredentials() {
        return this.credentials;
    }

    public Map<String, Object> getEngineOption() {
        return this.engineOption;
    }

    public ANTELocalConfiguration getLocalConfiguration() {
        return this.localConfiguration;
    }

    public Map<String, List<String>> getMainVocab() {
        return this.mainVocab;
    }

    public String getMaintag() {
        return this.maintag;
    }

    public List<String> getWakeupWord() {
        return this.wakeupWord;
    }

    public Map<String, Object> parseFromANTEngineOption(Map<ANTEngineOption<?>, Object> options) {
        HashMap map = new HashMap();
        for (ANTEngineOption<?> aNTEngineOption : options.keySet()) {
            map.put(aNTEngineOption.name(), options.get(aNTEngineOption));
        }
        return map;
    }

    public void setCredentials(BasicCredentials credentials) {
        this.credentials = credentials;
    }

    public void setEngineOption(Map<String, Object> engineOption) {
        this.engineOption = engineOption;
    }

    public void setLocalConfiguration(ANTELocalConfiguration localConfiguration) {
        this.localConfiguration = localConfiguration;
    }

    public void setMainVocab(Map<String, List<String>> mainVocab) {
        this.mainVocab = mainVocab;
    }

    public void setMaintag(String maintag) {
        this.maintag = maintag;
    }

    public void setWakeupWord(List<String> wakeupWord) {
        this.wakeupWord = wakeupWord;
    }

    public Map<ANTEngineOption<?>, Object> toANTEngineOption(Map<String, Object> optionsMap) {
        HashMap map = new HashMap();
        for (String str : optionsMap.keySet()) {
            map.put(ANTEngineOption.valueOf(str), optionsMap.get(str));
        }
        return map;
    }
}
