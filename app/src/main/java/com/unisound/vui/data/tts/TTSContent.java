package com.unisound.vui.data.tts;

import android.content.Context;
import android.text.TextUtils;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public class TTSContent extends a {
    public TTSContent(Context context) {
        super(context);
    }

    public String getLocalAnswerPath(String ttsContent) {
        if (isLocalAnswer(ttsContent)) {
            return this.ttsContentMap.get(ttsContent);
        }
        throw new IllegalArgumentException(ttsContent + " is not in map !!");
    }

    public boolean isLocalAnswer(String ttsContent) {
        if (TextUtils.isEmpty(ttsContent)) {
            return false;
        }
        Iterator<Map.Entry<String, String>> it = this.ttsContentMap.entrySet().iterator();
        while (it.hasNext()) {
            if (ttsContent.equals(it.next().getKey())) {
                return true;
            }
        }
        return false;
    }
}
