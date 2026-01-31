package com.unisound.vui.transport.out;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class VocabContent {

    @SerializedName("grammarTag")
    @JSONField(name = "grammarTag")
    private String grammarTag;

    @SerializedName("vocabContent")
    @JSONField(name = "vocabContent")
    private Map<String, List<String>> vocabContent;

    public VocabContent() {
    }

    public VocabContent(Map<String, List<String>> vocabContent, String grammarTag) {
        this.vocabContent = vocabContent;
        this.grammarTag = grammarTag;
    }

    public String getGrammarTag() {
        return this.grammarTag;
    }

    public Map<String, List<String>> getVocabContent() {
        return this.vocabContent;
    }

    public void setGrammarTag(String grammarTag) {
        this.grammarTag = grammarTag;
    }

    public void setVocabContent(Map<String, List<String>> vocabContent) {
        this.vocabContent = vocabContent;
    }
}
