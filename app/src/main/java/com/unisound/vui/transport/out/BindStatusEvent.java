package com.unisound.vui.transport.out;

public class BindStatusEvent extends OutputEvent<Boolean> {
    public BindStatusEvent(Boolean isBound) {
        super(1, isBound);
    }
}
