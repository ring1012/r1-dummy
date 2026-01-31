package com.unisound.vui.transport.out;

public class SimulateWakeupEvent extends OutputEvent<String> {
    public SimulateWakeupEvent(String data) {
        super(2, data);
    }
}
