package com.unisound.vui.common.a;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public abstract class b {
    private List<String> wakeUpWords = new ArrayList();

    public void clearWakeUpWords() {
        this.wakeUpWords.clear();
    }

    public List<String> getWakeUpWords() {
        return this.wakeUpWords;
    }

    public void setWakeUpWords(List<String> wakeUpWords) {
        this.wakeUpWords = wakeUpWords;
    }

    public abstract String toString();
}
