package cn.kuwo.autosdk.bean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;
import cn.kuwo.autosdk.bean.DownloadQuality;
import cn.kuwo.autosdk.q;
import cn.kuwo.autosdk.u;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.io.FilenameUtils;
import org.litepal.util.Const;

/* loaded from: classes.dex */
public class Music implements Serializable, Cloneable {
    private static final long serialVersionUID = 3985672550071260552L;
    public long artistId;
    public boolean checked;
    public long downSize;
    public int duration;
    private boolean eq;
    public long fileSize;
    private boolean flac;
    public int hasKalaok;
    public boolean hasMv;
    public int hot;
    public boolean playFail;
    public String psrc;
    private Collection resourceCollection;
    public long rid;
    private long storageId;
    public int trend;
    public String name = "";
    public String artist = "";
    public String album = "";
    public String tag = "";
    public String mvQuality = "";
    public String mvIconUrl = "";
    public String source = "";
    public q createDate = new q();
    public LocalFileState localFileState = LocalFileState.NOT_CHECK;
    public String filePath = "";
    public String fileFormat = "";
    public DownloadQuality.Quality downQuality = DownloadQuality.Quality.Q_AUTO;

    public enum LocalFileState {
        NOT_CHECK,
        EXIST,
        NOT_EXIST;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static LocalFileState[] valuesCustom() {
            LocalFileState[] localFileStateArrValuesCustom = values();
            int length = localFileStateArrValuesCustom.length;
            LocalFileState[] localFileStateArr = new LocalFileState[length];
            System.arraycopy(localFileStateArrValuesCustom, 0, localFileStateArr, 0, length);
            return localFileStateArr;
        }
    }

    public boolean Contain(Music music) {
        if (music.rid > 0 && this.rid > 0) {
            return music.rid == this.rid;
        }
        if (this.rid <= 0 && music.rid != 0) {
            return false;
        }
        return u.a(music.filePath, this.filePath);
    }

    public boolean addResource(MusicQuality musicQuality, int i, MusicFormat musicFormat, int i2) {
        return addResource(new NetResource(musicQuality, i, musicFormat, i2));
    }

    public boolean addResource(NetResource netResource) {
        if (netResource == null) {
            return false;
        }
        if (this.resourceCollection == null) {
            this.resourceCollection = new ArrayList();
        }
        Iterator it = this.resourceCollection.iterator();
        while (it.hasNext()) {
            if (((NetResource) it.next()).equals(netResource)) {
                return false;
            }
        }
        if (netResource.isEQ()) {
            this.eq = true;
        }
        if (netResource.isFLAC()) {
            this.flac = true;
        }
        return this.resourceCollection.add(netResource);
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Music m4clone() {
        try {
            Music music = (Music) super.clone();
            if (this.resourceCollection == null) {
                return music;
            }
            music.resourceCollection = new ArrayList();
            Iterator it = this.resourceCollection.iterator();
            while (it.hasNext()) {
                music.resourceCollection.add(((NetResource) it.next()).m5clone());
            }
            return music;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean equalsEx(Music music) {
        return music.rid > 0 ? music.rid == this.rid : (this.filePath == null || music.filePath == null) ? this.filePath == null && music.filePath == null : this.filePath.equals(music.filePath);
    }

    public NetResource getBestResource() {
        if (this.resourceCollection == null) {
            return null;
        }
        NetResource netResource = null;
        for (NetResource netResource2 : this.resourceCollection) {
            if (netResource == null) {
                netResource = netResource2;
            } else if (netResource.bitrate < netResource2.bitrate) {
                netResource = netResource2;
            }
        }
        return netResource;
    }

    public NetResource getBestResource(MusicQuality musicQuality) {
        if (this.resourceCollection == null) {
            return null;
        }
        NetResource netResource = null;
        for (NetResource netResource2 : this.resourceCollection) {
            if (netResource2.quality.ordinal() <= musicQuality.ordinal() && (netResource == null || netResource.bitrate < netResource2.bitrate)) {
                netResource = netResource2;
            }
        }
        return netResource;
    }

    public boolean getInfoFromDatabase(Cursor cursor) {
        try {
            setStorageId(cursor.getLong(cursor.getColumnIndex(TtmlNode.ATTR_ID)));
            this.rid = cursor.getLong(cursor.getColumnIndex("rid"));
            this.name = u.c(cursor.getString(cursor.getColumnIndex(Const.TableSchema.COLUMN_NAME)));
            this.artist = u.c(cursor.getString(cursor.getColumnIndex("artist")));
            this.artistId = cursor.getLong(cursor.getColumnIndex("artistid"));
            this.album = u.c(cursor.getString(cursor.getColumnIndex("album")));
            this.duration = cursor.getInt(cursor.getColumnIndex("duration"));
            this.hasMv = cursor.getInt(cursor.getColumnIndex("hasmv")) > 0;
            this.mvQuality = u.c(cursor.getString(cursor.getColumnIndex("mvquality")));
            this.hasKalaok = cursor.getInt(cursor.getColumnIndex("haskalaok"));
            this.downSize = cursor.getInt(cursor.getColumnIndex("downsize"));
            this.downQuality = DownloadQuality.Quality.valueOf(u.c(cursor.getString(cursor.getColumnIndex("downquality"))));
            this.filePath = u.c(cursor.getString(cursor.getColumnIndex("filepath")));
            this.fileSize = cursor.getLong(cursor.getColumnIndex("filesize"));
            this.fileFormat = u.c(cursor.getString(cursor.getColumnIndex("fileformat")));
            if (cursor.getColumnIndex("resource") >= 0) {
                parseResourceStringFromDatabase(u.c(cursor.getString(cursor.getColumnIndex("resource"))));
            }
            if (cursor.getColumnIndex("createtime") < 0) {
                this.createDate = new q();
                return true;
            }
            String strC = u.c(cursor.getString(cursor.getColumnIndex("createtime")));
            if (TextUtils.isEmpty(strC)) {
                this.createDate = new q();
                return true;
            }
            this.createDate = new q(strC);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ContentValues getMusicContentValues(long j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("rid", Long.valueOf(this.rid));
        contentValues.put("listid", Long.valueOf(j));
        contentValues.put(Const.TableSchema.COLUMN_NAME, u.c(this.name));
        contentValues.put("artist", u.c(this.artist));
        contentValues.put("artistid", Long.valueOf(this.artistId));
        contentValues.put("album", u.c(this.album));
        contentValues.put("duration", Integer.valueOf(this.duration));
        contentValues.put("hot", Integer.valueOf(this.hot));
        contentValues.put("source", u.c(this.source));
        contentValues.put("resource", u.c(getResourceStringForDatabase()));
        contentValues.put("hasmv", Integer.valueOf(this.hasMv ? 1 : 0));
        contentValues.put("mvquality", u.c(this.mvQuality));
        contentValues.put("haskalaok", Integer.valueOf(this.hasKalaok));
        contentValues.put("downsize", Long.valueOf(this.downSize));
        contentValues.put("downquality", this.downQuality == null ? "" : this.downQuality.toString());
        contentValues.put("filepath", u.c(this.filePath));
        contentValues.put("fileformat", u.c(this.fileFormat));
        contentValues.put("filesize", Long.valueOf(this.fileSize));
        contentValues.put("createtime", u.c(this.createDate.a()));
        return contentValues;
    }

    public NetResource getResource(MusicFormat musicFormat) {
        if (this.resourceCollection == null) {
            return null;
        }
        NetResource netResource = null;
        for (NetResource netResource2 : this.resourceCollection) {
            if (netResource2.format == musicFormat && (netResource == null || netResource.bitrate < netResource2.bitrate)) {
                netResource = netResource2;
            }
        }
        return netResource;
    }

    public NetResource getResource(MusicQuality musicQuality) {
        if (this.resourceCollection == null) {
            return null;
        }
        NetResource netResource = null;
        for (NetResource netResource2 : this.resourceCollection) {
            if (netResource2.quality == musicQuality && (netResource == null || netResource.bitrate < netResource2.bitrate)) {
                netResource = netResource2;
            }
        }
        return netResource;
    }

    public Collection getResourceCollection() {
        return this.resourceCollection;
    }

    public String getResourceStringForDatabase() {
        if (this.resourceCollection == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (NetResource netResource : this.resourceCollection) {
            sb.append(netResource.quality.getDiscribe()).append(".").append(netResource.bitrate).append(".");
            sb.append(netResource.format.getDiscribe()).append(".").append(netResource.size).append(";");
        }
        return sb.toString();
    }

    public long getStorageId() {
        return this.storageId;
    }

    public boolean hasHighMv() {
        if (!this.hasMv || this.mvQuality == null) {
            return false;
        }
        for (String str : u.a(this.mvQuality, ';')) {
            if (str.equalsIgnoreCase("MP4")) {
                return true;
            }
        }
        return false;
    }

    public boolean hasLowMv() {
        if (!this.hasMv || this.mvQuality == null) {
            return false;
        }
        for (String str : u.a(this.mvQuality, ';')) {
            if (str.equalsIgnoreCase("MP4L")) {
                return true;
            }
        }
        return false;
    }

    public int hashCodeEx() {
        if (this.rid > 0) {
            return (int) this.rid;
        }
        if (this.filePath == null) {
            return 0;
        }
        return this.filePath.hashCode();
    }

    public boolean isEQ() {
        return this.eq;
    }

    public boolean isFLAC() {
        return this.flac;
    }

    public boolean isLocalFile() {
        return this.rid <= 0;
    }

    public int parseResourceStringFromDatabase(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int i = 0;
        for (String str2 : u.a(str, ';')) {
            if (!TextUtils.isEmpty(str2)) {
                String[] strArrA = u.a(str2, FilenameUtils.EXTENSION_SEPARATOR);
                if (strArrA.length == 4) {
                    try {
                        if (addResource(new NetResource(MusicQuality.getQualityFromDiscribe(strArrA[0]), Integer.valueOf(strArrA[1]).intValue(), MusicFormat.getFormatFromDiscribe(strArrA[2]), Integer.valueOf(strArrA[3]).intValue()))) {
                            i++;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return i;
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x00c4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int parseResourceStringFromQuku(java.lang.String r13) {
        /*
            r12 = this;
            r11 = 1149239296(0x44800000, float:1024.0)
            r1 = 0
            boolean r0 = android.text.TextUtils.isEmpty(r13)
            if (r0 == 0) goto La
        L9:
            return r1
        La:
            r0 = 59
            java.lang.String[] r5 = cn.kuwo.autosdk.u.a(r13, r0)
            int r6 = r5.length
            r4 = r1
            r3 = r1
        L13:
            if (r4 < r6) goto L17
            r1 = r3
            goto L9
        L17:
            r0 = r5[r4]
            r2 = 44
            java.lang.String[] r0 = cn.kuwo.autosdk.u.a(r0, r2)
            int r2 = r0.length
            r7 = 4
            if (r2 != r7) goto Lc4
            r2 = r0[r1]
            java.lang.String r2 = cn.kuwo.autosdk.u.b(r2)
            r7 = 1
            r7 = r0[r7]
            java.lang.String r7 = cn.kuwo.autosdk.u.b(r7)
            r8 = 2
            r8 = r0[r8]
            java.lang.String r8 = cn.kuwo.autosdk.u.b(r8)
            r9 = 3
            r0 = r0[r9]
            java.lang.String r9 = cn.kuwo.autosdk.u.b(r0)
            cn.kuwo.autosdk.bean.MusicQuality r10 = cn.kuwo.autosdk.bean.MusicQuality.getQualityFromDiscribe4Quku(r2)
            boolean r0 = cn.kuwo.autosdk.u.a(r7)
            if (r0 == 0) goto Lc6
            int r0 = java.lang.Integer.parseInt(r7)
        L4c:
            cn.kuwo.autosdk.bean.MusicFormat r7 = cn.kuwo.autosdk.bean.MusicFormat.getFormatFromDiscribe4Quku(r8)
            java.lang.String r2 = r9.toUpperCase()
            java.lang.String r8 = "KB"
            int r2 = r2.indexOf(r8)
            if (r2 <= 0) goto L82
            java.lang.String r2 = "(?i)kb"
            java.lang.String r8 = ""
            java.lang.String r2 = r9.replaceAll(r2, r8)
            float r2 = java.lang.Float.parseFloat(r2)     // Catch: java.lang.Exception -> L7c
            float r2 = r2 * r11
            int r2 = (int) r2
        L6a:
            cn.kuwo.autosdk.bean.NetResource r8 = new cn.kuwo.autosdk.bean.NetResource
            r8.<init>(r10, r0, r7, r2)
            boolean r0 = r12.addResource(r8)
            if (r0 == 0) goto Lc4
            int r0 = r3 + 1
        L77:
            int r2 = r4 + 1
            r4 = r2
            r3 = r0
            goto L13
        L7c:
            r2 = move-exception
            r2.printStackTrace()
            r2 = r1
            goto L6a
        L82:
            java.lang.String r2 = r9.toUpperCase()
            java.lang.String r8 = "MB"
            int r2 = r2.indexOf(r8)
            if (r2 <= 0) goto La4
            java.lang.String r2 = "(?i)mb"
            java.lang.String r8 = ""
            java.lang.String r2 = r9.replaceAll(r2, r8)
            float r2 = java.lang.Float.parseFloat(r2)     // Catch: java.lang.Exception -> L9e
            float r2 = r2 * r11
            float r2 = r2 * r11
            int r2 = (int) r2
            goto L6a
        L9e:
            r2 = move-exception
            r2.printStackTrace()
            r2 = r1
            goto L6a
        La4:
            java.lang.String r2 = r9.toUpperCase()
            java.lang.String r8 = "B"
            int r2 = r2.indexOf(r8)
            if (r2 <= 0) goto Lc2
            java.lang.String r2 = "(?i)b"
            java.lang.String r8 = ""
            java.lang.String r2 = r9.replaceAll(r2, r8)
            float r2 = java.lang.Float.parseFloat(r2)     // Catch: java.lang.Exception -> Lbe
            int r2 = (int) r2
            goto L6a
        Lbe:
            r2 = move-exception
            r2.printStackTrace()
        Lc2:
            r2 = r1
            goto L6a
        Lc4:
            r0 = r3
            goto L77
        Lc6:
            r0 = r1
            goto L4c
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuwo.autosdk.bean.Music.parseResourceStringFromQuku(java.lang.String):int");
    }

    public void setLocalFileExist(boolean z) {
        this.localFileState = z ? LocalFileState.EXIST : LocalFileState.NOT_EXIST;
    }

    public void setResourceCollection(Collection collection) {
        this.resourceCollection = collection;
    }

    public void setStorageId(long j) {
        if (0 > j) {
            return;
        }
        this.storageId = j;
    }

    public String toDebugString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name:").append(this.name);
        sb.append(", Artist:").append(this.artist);
        sb.append(", Album:").append(this.album);
        sb.append(", Rid:").append(this.rid);
        sb.append(", Path:").append(this.filePath);
        return sb.toString();
    }

    public boolean vaild() {
        return this.rid > 0 || !TextUtils.isEmpty(this.filePath);
    }
}
