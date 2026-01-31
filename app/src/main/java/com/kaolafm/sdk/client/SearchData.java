package com.kaolafm.sdk.client;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class SearchData implements Parcelable {
    public static final Parcelable.Creator<SearchData> CREATOR = new Parcelable.Creator<SearchData>() { // from class: com.kaolafm.sdk.client.SearchData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SearchData createFromParcel(Parcel in) {
            return new SearchData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SearchData[] newArray(int size) {
            return new SearchData[size];
        }
    };
    private String host;
    private long id;
    private String img;
    private long listenNum;
    private String name;
    public String singleTitle;
    private int type;

    public SearchData() {
    }

    public SearchData(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.type = in.readInt();
        this.listenNum = in.readLong();
        this.img = in.readString();
        this.host = in.readString();
        this.singleTitle = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.type);
        dest.writeLong(this.listenNum);
        dest.writeString(this.img);
        dest.writeString(this.host);
        dest.writeString(this.singleTitle);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getListenNum() {
        return this.listenNum;
    }

    public void setListenNum(long listenNum) {
        this.listenNum = listenNum;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSingleTitle() {
        return this.singleTitle;
    }

    public void setSingleTitle(String singleTitle) {
        this.singleTitle = singleTitle;
    }
}
