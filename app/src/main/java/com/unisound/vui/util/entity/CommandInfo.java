package com.unisound.vui.util.entity;

/* loaded from: classes.dex */
public class CommandInfo {
    private String command;
    private String operands;
    private String operator;
    private String value;

    public CommandInfo() {
    }

    public CommandInfo(String mCommand, String mOperands, String mOperator, String mValue) {
        this.command = mCommand;
        this.operands = mOperands;
        this.operator = mOperator;
        this.value = mValue;
    }

    public String getCommand() {
        return this.command;
    }

    public String getOperands() {
        return this.operands;
    }

    public String getOperator() {
        return this.operator;
    }

    public String getValue() {
        return this.value;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setOperands(String operands) {
        this.operands = operands;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
