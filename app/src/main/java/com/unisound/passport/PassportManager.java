package com.unisound.passport;

import android.content.Context;

/* loaded from: classes.dex */
public class PassportManager extends d {
    PassportListener d;

    public PassportManager(Context context, String str, String str2) {
        super(context, str, str2);
        this.d = null;
    }

    @Override // com.unisound.passport.d
    public void closeConnection() {
        super.closeConnection();
    }

    @Override // com.unisound.passport.d
    public Object getOption(int i) {
        return super.getOption(i);
    }

    @Override // com.unisound.passport.d
    public void init() {
        super.init();
    }

    @Override // com.unisound.passport.d
    public void openConnection() {
        super.openConnection();
    }

    @Override // com.unisound.passport.d
    public void setOption(int i, Object obj) {
        super.setOption(i, obj);
    }

    @Override // com.unisound.passport.d
    public void setPassportListener(PassportListener passportListener) {
        super.setPassportListener(passportListener);
    }
}
