package com.unisound.vui.util;

import android.content.Context;
import android.content.res.Resources;
import com.unisound.vui.common.R;
import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes.dex */
public class SpeakerTTSUtil {
    public static final int TTS_SPEAKER_DEFAULT = 0;
    public static final int TTS_SPEAKER_FEMALE = 2;
    public static final int TTS_SPEAKER_MALE = 1;
    public static ArrayList<Integer> arrayString = new ArrayList<>();
    private static int speakerType = 0;

    static {
        arrayString.add(Integer.valueOf(R.array.tts_weather_no_result));
        arrayString.add(Integer.valueOf(R.array.tts_unsupport_answer));
    }

    private SpeakerTTSUtil() {
    }

    private static String getRadom(String s) {
        String[] strArrSplit = s.split(MqttTopic.MULTI_LEVEL_WILDCARD);
        return strArrSplit.length > 1 ? strArrSplit[(int) (Math.random() * strArrSplit.length)] : strArrSplit[0];
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static String getSpeakerTTSArray(int id, int speaker, Context context) throws Resources.NotFoundException {
        String[] stringArray = context.getResources().getStringArray(id);
        if (stringArray != null) {
            switch (speakerType) {
                case 0:
                    if (stringArray.length > 0) {
                        return getRadom(stringArray[0]);
                    }
                    break;
                case 1:
                    if (stringArray.length > 1) {
                        return getRadom(stringArray[1]);
                    }
                    break;
                case 2:
                    if (stringArray.length > 2) {
                        return getRadom(stringArray[2]);
                    }
                    break;
            }
        }
        return null;
    }

    private static String getSpeakerTTSString(int id, Context context) {
        return context.getResources().getString(id);
    }

    public static String getTTSString(int id, int speaker, Context context) {
        return arrayString.contains(Integer.valueOf(id)) ? getSpeakerTTSArray(id, speaker, context) : getSpeakerTTSString(id, context);
    }

    public static String[] getTTSStringArray(int id, Context context) {
        return context.getResources().getStringArray(id);
    }

    public static String getTTSStringFromArray(int id, Context context) {
        String[] strArrSplit = getTTSStringArray(id, context)[(int) (Math.random() * r0.length)].split(MqttTopic.MULTI_LEVEL_WILDCARD);
        return strArrSplit.length > speakerType ? strArrSplit[speakerType] : strArrSplit[0];
    }

    public static void setSpeakerTTSType(Context context, String type) {
        boolean enableRecognizePersonal = UserPerferenceUtil.getEnableRecognizePersonal(context);
        LogMgr.d("--enable " + enableRecognizePersonal);
        if (!enableRecognizePersonal) {
            speakerType = 0;
        } else if ("male".equals(type)) {
            speakerType = 1;
        } else if ("female".equals(type)) {
            speakerType = 2;
        }
    }
}
