package com.unisound.ant.device.profile;

import com.unisound.ant.device.bean.Accelerate;
import com.unisound.ant.device.bean.UnisoundDeviceCommand;

/* loaded from: classes.dex */
public class DstServiceProfile<T> {
    private Accelerate accelerate;
    private String dstServiceName;
    private String dstState;
    private T parameter;
    private UnisoundDeviceCommand<T> uniCommand;

    public String getDstState() {
        return this.dstState;
    }

    public void setDstState(String dstState) {
        this.dstState = dstState;
    }

    public String getDstServiceName() {
        return this.dstServiceName;
    }

    public void setDstServiceName(String dstServiceName) {
        this.dstServiceName = dstServiceName;
    }

    public UnisoundDeviceCommand<T> getUniCommand() {
        return this.uniCommand;
    }

    public void setUniCommand(UnisoundDeviceCommand<T> uniCommand) {
        this.uniCommand = uniCommand;
    }

    public Accelerate getAccelerate() {
        return this.accelerate;
    }

    public void setAccelerate(Accelerate accelerate) {
        this.accelerate = accelerate;
    }

    public T getParameter() {
        return this.parameter;
    }

    public void setParameter(T parameter) {
        this.parameter = parameter;
    }
}
