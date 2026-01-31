package com.unisound.vui.custom.event.interaction.key;

/* loaded from: classes.dex */
public abstract class AbsKeyEventProcessor {
    private KeyEventController mController;

    public AbsKeyEventProcessor(KeyEventController controller) {
        this.mController = controller;
    }

    protected void onClick() {
        this.mController.onClickEvent();
    }

    protected void onDoubleClick() {
        this.mController.onDoubleClickEvent();
    }

    protected void onTripleClick() {
        this.mController.onTripleClickEvent();
    }

    protected void onLongClick() {
        this.mController.onLongClickEvent();
    }
}
