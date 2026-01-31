package com.unisound.vui.common.file;

import android.content.Context;
import com.unisound.vui.common.a.c;
import com.unisound.vui.util.ThreadUtils;
import com.unisound.vui.util.UserPerferenceUtil;
import java.util.Random;

/* loaded from: classes.dex */
public class AudioFileHelper {
    public static final String GENDER_CHILDREN = "CHILDREN";
    public static final String GENDER_FEMALE = "FEMALE";
    public static final String GENDER_LZL = "LZL";
    public static final String GENDER_MALE = "MALE";
    public static final String GENDER_SWEET = "SWEET";
    private static final int SWEET_MORE_SIZE = 6;
    private Context mContext;
    private c[] mWakeupAudios;
    private int randomIndex;

    public AudioFileHelper(Context context) {
        this.mContext = context;
    }

    private void getRandomIndex(Random random) {
        int length = this.mWakeupAudios.length - 6;
        if (getTTSFrontFromSp().equals("SWEET")) {
            this.randomIndex = random.nextInt(this.mWakeupAudios.length);
        } else {
            this.randomIndex = random.nextInt(length);
        }
    }

    private String getTTSFrontFromSp() {
        return UserPerferenceUtil.getUserTTSModelType(this.mContext);
    }

    public String getRandomWakeUpFilePath() {
        return FileHelper.getWakeupAudioFile(this.mWakeupAudios[this.randomIndex].a()).getAbsolutePath();
    }

    public byte[] getRandomWakeUpTips() {
        getRandomIndex(new Random());
        return this.mWakeupAudios[this.randomIndex].b();
    }

    public String getRelativeAudioFilePathByName(String wavFileName) {
        String str;
        str = "";
        switch (getTTSFrontFromSp()) {
            case "FEMALE":
                str = "female/";
                break;
            case "MALE":
                str = "male/";
                break;
            case "CHILDREN":
                str = "child/";
                break;
            case "SWEET":
                str = "sweet/";
                break;
            case "LZL":
                str = "lzl/";
                break;
        }
        return str + wavFileName;
    }

    public void loadWakeupAudio() {
        this.mWakeupAudios = new c[]{new c(getRelativeAudioFilePathByName("hi.pcm")), new c(getRelativeAudioFilePathByName("im_here.pcm")), new c(getRelativeAudioFilePathByName("please_say.pcm")), new c(getRelativeAudioFilePathByName("hi_1.pcm")), new c(getRelativeAudioFilePathByName("im_here_1.pcm")), new c(getRelativeAudioFilePathByName("please_say_1.pcm")), new c(getRelativeAudioFilePathByName("hi_2.pcm")), new c(getRelativeAudioFilePathByName("im_here_2.pcm")), new c(getRelativeAudioFilePathByName("please_say_2.pcm"))};
        for (int i = 0; i < this.mWakeupAudios.length; i++) {
            ThreadUtils.execute(new com.unisound.vui.common.a.a(this.mContext, this.mWakeupAudios[i]));
        }
    }
}
