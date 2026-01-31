package cn.kuwo.autosdk.bean;

/* loaded from: classes.dex */
public class NetResource implements Cloneable {
    public int bitrate;
    public MusicFormat format;
    public MusicQuality quality;
    public int size;

    public NetResource(MusicQuality musicQuality, int i, MusicFormat musicFormat, int i2) {
        this.quality = musicQuality;
        this.bitrate = i;
        this.format = musicFormat;
        this.size = i2;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public NetResource m5clone() {
        try {
            return (NetResource) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof NetResource)) {
            return false;
        }
        NetResource netResource = (NetResource) obj;
        return netResource.quality == this.quality && netResource.bitrate == this.bitrate && netResource.format == this.format && netResource.size == this.size;
    }

    public int hashCode() {
        return this.quality.ordinal() + this.bitrate + this.format.ordinal() + this.size;
    }

    public boolean isEQ() {
        return this.quality.isEQ();
    }

    public boolean isFLAC() {
        return this.quality.isFLAC();
    }
}
