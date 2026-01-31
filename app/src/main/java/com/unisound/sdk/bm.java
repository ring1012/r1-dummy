package com.unisound.sdk;

import android.content.SharedPreferences;
import android.text.TextUtils;
import com.unisound.client.ErrorCode;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
class bm implements com.unisound.b.a.c {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bg f318a;

    bm(bg bgVar) {
        this.f318a = bgVar;
    }

    @Override // com.unisound.b.a.c
    public void a(String str) throws JSONException {
        com.unisound.common.y.b("SpeechUnderstanderInterface init activate result= " + str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("returnCode");
            SharedPreferences.Editor editorEdit = this.f318a.ak.getSharedPreferences("ACTIVITY_FLAG", 0).edit();
            if (string.equals("dc_0000")) {
                editorEdit.putString("activityFlag", "activity success");
                String string2 = new JSONObject(jSONObject.getString("result")).getString("token");
                if (!TextUtils.isEmpty(string2)) {
                    this.f318a.d(this.f318a.x, com.unisound.c.a.a(this.f318a.x), string2);
                }
                this.f318a.Q();
                editorEdit.commit();
                this.f318a.am.sendEmptyMessage(40);
                return;
            }
            com.unisound.common.y.a(com.unisound.common.y.v, "activate errorCode = ", string);
            if (string.equals("dc_0002")) {
                this.f318a.refreshActivate();
                return;
            }
            if (string.equals("dc_0006") && this.f318a.at) {
                this.f318a.aK.a(0);
                this.f318a.at = false;
                return;
            }
            if (string.equals("dc_0003")) {
                this.f318a.q(ErrorCode.ASR_SDK_ACTIVATE_ERROR);
                this.f318a.q(ErrorCode.ASR_SDK_ACTIVATE_SIGN_ERROR);
            } else if (string.equals("dc_0004")) {
                this.f318a.q(ErrorCode.ASR_SDK_ACTIVATE_ERROR);
                this.f318a.q(ErrorCode.ASR_SDK_ACTIVATE_NO_PERMISSION);
            } else if (string.equals("dc_0005")) {
                this.f318a.q(ErrorCode.ASR_SDK_ACTIVATE_ERROR);
                this.f318a.q(ErrorCode.ASR_SDK_ACTIVATE_OVER_MAX_ACT_FREQUENCY);
            }
            this.f318a.d(this.f318a.x, com.unisound.c.a.a(this.f318a.x), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
