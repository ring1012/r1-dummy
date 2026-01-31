package com.unisound.ant.device.service;

/* loaded from: classes.dex */
public class BaseRequest<V> {
    public static final String MESSAGE_TYPE_GD_REQUEST = "uploadGDRequest";
    public static final String MESSAGE_TYPE_INTENT_ACK = "intentAck";
    public static final String MESSAGE_TYPE_PD_REQUEST = "uploadPDRequest";
    public static final String MESSAGE_TYPE_SYNC_INFO = "synInfo";
    private V messageBody;
    private String messageType;
    private String version;

    protected BaseRequest(MessageHeader header, V messageBody) {
        this.messageType = header.getMessageType();
        this.version = header.getVersion();
        this.messageBody = messageBody;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public String getVersion() {
        return this.version;
    }

    public V getMessageBody() {
        return this.messageBody;
    }

    public static class Builder<E> {
        private E data;
        private MessageHeader header;

        public Builder setRequestHeader(MessageHeader header) {
            this.header = header;
            return this;
        }

        public Builder setData(E data) {
            this.data = data;
            return this;
        }

        public BaseRequest build() {
            if (this.header == null) {
                throw new IllegalArgumentException("header may not be null.");
            }
            return new BaseRequest(this.header, this.data);
        }
    }
}
