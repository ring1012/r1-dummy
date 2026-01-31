package org.eclipse.paho.client.mqttv3;

/* loaded from: classes.dex */
public interface MqttPersistable {
    byte[] getHeaderBytes();

    int getHeaderLength();

    int getHeaderOffset();

    byte[] getPayloadBytes();

    int getPayloadLength();

    int getPayloadOffset();
}
