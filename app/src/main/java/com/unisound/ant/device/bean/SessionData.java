package com.unisound.ant.device.bean;

import com.unisound.ant.device.profile.DstServiceProfile;
import com.unisound.ant.device.service.ActionResponse;
import com.unisound.ant.device.sessionlayer.DialogProfile;

/* loaded from: classes.dex */
public class SessionData<T> {
    private ActionResponse actionResponse;
    private DialogProfile dialog;
    private DstServiceProfile<T> dstService;

    public ActionResponse getActionResponse() {
        return this.actionResponse;
    }

    public void setActionResponse(ActionResponse actionResponse) {
        this.actionResponse = actionResponse;
    }

    public DialogProfile getDialog() {
        return this.dialog;
    }

    public void setDialog(DialogProfile dialog) {
        this.dialog = dialog;
    }

    public DstServiceProfile<T> getDstService() {
        return this.dstService;
    }

    public void setDstService(DstServiceProfile<T> dstService) {
        this.dstService = dstService;
    }
}
