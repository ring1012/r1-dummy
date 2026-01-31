package org.eclipse.paho.client.mqttv3;

import java.io.UnsupportedEncodingException;
import org.eclipse.paho.client.mqttv3.a.a;
import org.eclipse.paho.client.mqttv3.a.b.o;
import org.eclipse.paho.client.mqttv3.c.b;

/* loaded from: classes.dex */
public class MqttTopic {
    public static final String MULTI_LEVEL_WILDCARD = "#";
    public static final String MULTI_LEVEL_WILDCARD_PATTERN = "/#";
    public static final String SINGLE_LEVEL_WILDCARD = "+";
    public static final String TOPIC_LEVEL_SEPARATOR = "/";
    public static final String TOPIC_WILDCARDS = "#+";

    /* renamed from: a, reason: collision with root package name */
    private static final int f465a = 1;
    private static final int b = 65535;
    private static final char c = 0;
    private a d;
    private String e;

    public MqttTopic(String str, a aVar) {
        this.d = aVar;
        this.e = str;
    }

    private o a(MqttMessage mqttMessage) {
        return new o(getName(), mqttMessage);
    }

    private static void a(String str) {
        char cCharAt = SINGLE_LEVEL_WILDCARD.charAt(0);
        char cCharAt2 = TOPIC_LEVEL_SEPARATOR.charAt(0);
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        for (int i = 0; i < length; i++) {
            char c2 = i + (-1) >= 0 ? charArray[i - 1] : (char) 0;
            char c3 = i + 1 < length ? charArray[i + 1] : (char) 0;
            if (charArray[i] == cCharAt && ((c2 != cCharAt2 && c2 != 0) || (c3 != cCharAt2 && c3 != 0))) {
                throw new IllegalArgumentException(String.format("Invalid usage of single-level wildcard in topic string '%s'!", str));
            }
        }
    }

    public static void validate(String str, boolean z) {
        try {
            int length = str.getBytes("UTF-8").length;
            if (length < 1 || length > 65535) {
                throw new IllegalArgumentException(String.format("Invalid topic length, should be in range[%d, %d]!", new Integer(1), new Integer(65535)));
            }
            if (!z) {
                if (b.a(str, TOPIC_WILDCARDS)) {
                    throw new IllegalArgumentException("The topic name MUST NOT contain any wildcard characters (#+)");
                }
            } else {
                if (b.a(str, new String[]{MULTI_LEVEL_WILDCARD, SINGLE_LEVEL_WILDCARD})) {
                    return;
                }
                if (b.b(str, MULTI_LEVEL_WILDCARD) > 1 || (str.contains(MULTI_LEVEL_WILDCARD) && !str.endsWith(MULTI_LEVEL_WILDCARD_PATTERN))) {
                    throw new IllegalArgumentException(new StringBuffer("Invalid usage of multi-level wildcard in topic string: ").append(str).toString());
                }
                a(str);
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getName() {
        return this.e;
    }

    public MqttDeliveryToken publish(MqttMessage mqttMessage) throws MqttException {
        MqttDeliveryToken mqttDeliveryToken = new MqttDeliveryToken(this.d.k().getClientId());
        mqttDeliveryToken.a(mqttMessage);
        this.d.b(a(mqttMessage), mqttDeliveryToken);
        mqttDeliveryToken.internalTok.k();
        return mqttDeliveryToken;
    }

    public MqttDeliveryToken publish(byte[] bArr, int i, boolean z) {
        MqttMessage mqttMessage = new MqttMessage(bArr);
        mqttMessage.setQos(i);
        mqttMessage.setRetained(z);
        return publish(mqttMessage);
    }

    public String toString() {
        return getName();
    }
}
