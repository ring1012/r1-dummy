package com.unisound.vui.util.downloader;

import org.json.JSONObject;

/* loaded from: classes.dex */
public interface DownloaderListener {
    void onError(String str);

    void onSameVersion();

    void onUpdate(JSONObject jSONObject);
}
