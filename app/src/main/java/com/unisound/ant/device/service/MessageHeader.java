package com.unisound.ant.device.service;

import android.text.TextUtils;

/* loaded from: classes.dex */
public class MessageHeader {
    private String messageType;
    private String version;

    MessageHeader(String messageType, String version) {
        this.messageType = messageType;
        this.version = version;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getVersion() {
        return this.version;
    }

    public static class Builder {
        private String messageType;
        private String version;

        public Builder setMessageType(String messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        void check() {
            if (TextUtils.isEmpty(this.messageType)) {
                throw new IllegalArgumentException("messageType may not be null.");
            }
            if (TextUtils.isEmpty(this.version)) {
                throw new IllegalArgumentException("version may not be null.");
            }
        }

        public MessageHeader build() {
            check();
            return new MessageHeader(this.messageType, this.version);
        }
    }
}
