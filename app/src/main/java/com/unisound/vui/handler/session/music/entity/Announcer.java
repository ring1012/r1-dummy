package com.unisound.vui.handler.session.music.entity;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class Announcer implements Parcelable {
    public static final Parcelable.Creator<Announcer> CREATOR = new Parcelable.Creator<Announcer>() { // from class: com.unisound.vui.handler.session.music.entity.Announcer.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Announcer createFromParcel(Parcel source) {
            Announcer announcer = new Announcer();
            announcer.readFromParacel(source);
            return announcer;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Announcer[] newArray(int size) {
            return new Announcer[size];
        }
    };

    @SerializedName("announcerId")
    @JSONField(name = "announcerId")
    private long announcerId;

    @SerializedName("avatarUrl")
    @JSONField(name = "avatarUrl")
    private String avatarUrl;

    @SerializedName("nickname")
    @JSONField(name = "nickname")
    private String nickname;

    @SerializedName("verified")
    @JSONField(name = "verified")
    private boolean verified;

    public Announcer() {
    }

    public Announcer(long announcerId, String nickname, String avatarUrl, boolean verified) {
        this.announcerId = announcerId;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.verified = verified;
    }

    public long getAnnouncerId() {
        return this.announcerId;
    }

    public void setAnnouncerId(long announcerId) {
        this.announcerId = announcerId;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isVerified() {
        return this.verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.announcerId);
        dest.writeString(this.nickname);
        dest.writeString(this.avatarUrl);
        dest.writeBooleanArray(new boolean[]{this.verified});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readFromParacel(Parcel in) {
        this.announcerId = in.readLong();
        this.nickname = in.readString();
        this.avatarUrl = in.readString();
        boolean[] temp = new boolean[1];
        in.readBooleanArray(temp);
        this.verified = temp[0];
    }

    public String toString() {
        return "Announcer [id=" + this.announcerId + ", nickname=" + this.nickname + ", avatarUrl=" + this.avatarUrl + ", isVerified=" + this.verified + "]";
    }
}
