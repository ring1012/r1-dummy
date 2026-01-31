package com.unisound.vui.transport.out;

public class ChangeWakeupWordEvent extends OutputEvent<String> {
    public ChangeWakeupWordEvent(String data) {
        super(3, data);
    }
}
