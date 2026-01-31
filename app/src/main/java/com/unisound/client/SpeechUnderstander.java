package com.unisound.client;

import android.content.Context;
import com.unisound.sdk.bg;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

/* loaded from: classes.dex */
public class SpeechUnderstander extends bg {
    public SpeechUnderstander(Context context, String str, String str2) {
        super(context, str, str2);
    }

    @Override // com.unisound.sdk.bg, com.unisound.sdk.o
    public void cancel() {
        super.cancel();
    }

    @Override // com.unisound.sdk.bg
    public String convertToArabicNumber(String str) {
        return super.convertToArabicNumber(str);
    }

    @Override // com.unisound.sdk.bg, com.unisound.sdk.o
    public String getFixEngineVersion() {
        return super.getFixEngineVersion();
    }

    @Override // com.unisound.sdk.bg, com.unisound.sdk.o
    public Object getOption(int i) {
        return super.getOption(i);
    }

    @Override // com.unisound.sdk.bg, com.unisound.sdk.o
    public String getVersion() {
        return super.getVersion();
    }

    @Override // com.unisound.sdk.bg
    public void init(String str) throws JSONException {
        super.init(str);
    }

    @Override // com.unisound.sdk.bg
    public int insertVocab(List<String> list, String str) {
        return super.insertVocab(list, str);
    }

    @Override // com.unisound.sdk.bg
    public int insertVocab(Map<String, List<String>> map, String str) {
        return super.insertVocab(map, str);
    }

    @Override // com.unisound.sdk.bg
    public int insertVocab_ext(String str, String str2, String str3) {
        return super.insertVocab_ext(str, str2, str3);
    }

    @Override // com.unisound.sdk.bg
    public int insertVocab_ext(String str, String str2, Map<String, List<String>> map) {
        return super.insertVocab_ext(str, str2, map);
    }

    @Override // com.unisound.sdk.bg
    public int loadCompiledJsgf(String str, String str2) {
        return super.loadCompiledJsgf(str, str2);
    }

    @Override // com.unisound.sdk.bg
    public int loadGrammar(String str, String str2) {
        return super.loadGrammar(str, str2);
    }

    @Override // com.unisound.sdk.bg
    public int release(int i, String str) {
        return super.release(i, str);
    }

    @Override // com.unisound.sdk.bg, com.unisound.sdk.o
    public int setAudioSource(IAudioSource iAudioSource) {
        return super.setAudioSource(iAudioSource);
    }

    @Override // com.unisound.sdk.bg
    public void setListener(SpeechUnderstanderListener speechUnderstanderListener) {
        super.setListener(speechUnderstanderListener);
    }

    @Override // com.unisound.sdk.bg
    public String setOnlineWakeupWord(List<String> list) {
        return super.setOnlineWakeupWord(list);
    }

    @Override // com.unisound.sdk.bg, com.unisound.sdk.o
    public void setOption(int i, Object obj) {
        super.setOption(i, obj);
    }

    @Override // com.unisound.sdk.bg
    public int setWakeupWord(List<String> list) {
        return super.setWakeupWord(list);
    }

    @Override // com.unisound.sdk.bg
    public int setWakeupWord(Map<String, List<String>> map) {
        return super.setWakeupWord(map);
    }

    @Override // com.unisound.sdk.bg, com.unisound.sdk.o
    public void start() {
        super.start();
    }

    @Override // com.unisound.sdk.bg, com.unisound.sdk.o
    public void start(String str) {
        super.start(str);
    }

    @Override // com.unisound.sdk.bg, com.unisound.sdk.o
    public void stop() {
        super.stop();
    }

    @Override // com.unisound.sdk.bg
    public int unloadGrammar(String str) {
        return super.unloadGrammar(str);
    }

    @Override // com.unisound.sdk.bg
    public void uploadUserData(Map<Integer, List<String>> map) {
        super.uploadUserData(map);
    }
}
