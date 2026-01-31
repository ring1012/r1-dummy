package com.unisound.sdk;

import android.os.AsyncTask;
import com.unisound.client.ErrorCode;
import java.util.List;

/* loaded from: classes.dex */
class bq extends AsyncTask<List<String>, Integer, Integer> {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bg f322a;

    bq(bg bgVar) {
        this.f322a = bgVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public Integer doInBackground(List<String>... listArr) {
        return Integer.valueOf(this.f322a.a((List<String>) listArr[0]));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onPostExecute(Integer num) {
        if (num.intValue() == 0) {
            this.f322a.am.sendEmptyMessage(50);
        }
        switch (num.intValue()) {
            case ErrorCode.ERR_ILLEGAL_CHAR /* 800 */:
                this.f322a.q(ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ERR_ILLEGAL_CHAR);
                break;
            case ErrorCode.ERR_TOO_MANY_WORDS /* 801 */:
                this.f322a.q(ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ERR_TOO_MANY_WORDS);
                break;
            case ErrorCode.ERR_NO_WORD /* 802 */:
                this.f322a.q(ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ERR_NO_WORD);
                break;
            case ErrorCode.ERR_DECODE /* 803 */:
                this.f322a.q(ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ERR_DECODE);
                break;
            case ErrorCode.ERR_IO /* 804 */:
                this.f322a.q(ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ERR_IO);
                break;
            case ErrorCode.ERR_NOT_CONSISTENCY /* 805 */:
                this.f322a.q(ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ERR_NOT_CONSISTENCY);
                break;
            case ErrorCode.ERR_WRONG_NEW_WORDS /* 806 */:
                this.f322a.q(ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ERR_WRONG_NEW_WORDS);
                break;
            case ErrorCode.ERR_BAD_PARA /* 807 */:
                this.f322a.q(ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ERR_BAD_PARA);
                break;
            case ErrorCode.ERR_SAME_DATA /* 808 */:
                this.f322a.q(ErrorCode.ASR_SDK_UPLOAD_ONESHOT_ERR_SAME_DATA);
                break;
            default:
                this.f322a.q(num.intValue());
                break;
        }
    }
}
