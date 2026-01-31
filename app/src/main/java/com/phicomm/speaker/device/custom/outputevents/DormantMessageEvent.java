package com.phicomm.speaker.device.custom.outputevents;

import com.unisound.vui.transport.out.OutputEvent;

/* loaded from: classes.dex */
public class DormantMessageEvent extends OutputEvent<Boolean> {
    public DormantMessageEvent(Boolean data) {
        super(1, data);
    }
}
