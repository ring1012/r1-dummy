package com.google.android.exoplayer2.extractor.mp4;

import android.util.Log;
import com.google.android.exoplayer2.metadata.id3.ApicFrame;
import com.google.android.exoplayer2.metadata.id3.CommentFrame;
import com.google.android.exoplayer2.metadata.id3.Id3Frame;
import com.google.android.exoplayer2.metadata.id3.TextInformationFrame;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes.dex */
final class MetadataUtil {
    private static final String LANGUAGE_UNDEFINED = "und";
    private static final String TAG = "MetadataUtil";
    private static final int SHORT_TYPE_NAME_1 = Util.getIntegerCodeForString("nam");
    private static final int SHORT_TYPE_NAME_2 = Util.getIntegerCodeForString("trk");
    private static final int SHORT_TYPE_COMMENT = Util.getIntegerCodeForString("cmt");
    private static final int SHORT_TYPE_YEAR = Util.getIntegerCodeForString("day");
    private static final int SHORT_TYPE_ARTIST = Util.getIntegerCodeForString("ART");
    private static final int SHORT_TYPE_ENCODER = Util.getIntegerCodeForString("too");
    private static final int SHORT_TYPE_ALBUM = Util.getIntegerCodeForString("alb");
    private static final int SHORT_TYPE_COMPOSER_1 = Util.getIntegerCodeForString("com");
    private static final int SHORT_TYPE_COMPOSER_2 = Util.getIntegerCodeForString("wrt");
    private static final int SHORT_TYPE_LYRICS = Util.getIntegerCodeForString("lyr");
    private static final int SHORT_TYPE_GENRE = Util.getIntegerCodeForString("gen");
    private static final int TYPE_COVER_ART = Util.getIntegerCodeForString("covr");
    private static final int TYPE_GENRE = Util.getIntegerCodeForString("gnre");
    private static final int TYPE_GROUPING = Util.getIntegerCodeForString("grp");
    private static final int TYPE_DISK_NUMBER = Util.getIntegerCodeForString("disk");
    private static final int TYPE_TRACK_NUMBER = Util.getIntegerCodeForString("trkn");
    private static final int TYPE_TEMPO = Util.getIntegerCodeForString("tmpo");
    private static final int TYPE_COMPILATION = Util.getIntegerCodeForString("cpil");
    private static final int TYPE_ALBUM_ARTIST = Util.getIntegerCodeForString("aART");
    private static final int TYPE_SORT_TRACK_NAME = Util.getIntegerCodeForString("sonm");
    private static final int TYPE_SORT_ALBUM = Util.getIntegerCodeForString("soal");
    private static final int TYPE_SORT_ARTIST = Util.getIntegerCodeForString("soar");
    private static final int TYPE_SORT_ALBUM_ARTIST = Util.getIntegerCodeForString("soaa");
    private static final int TYPE_SORT_COMPOSER = Util.getIntegerCodeForString("soco");
    private static final int TYPE_RATING = Util.getIntegerCodeForString("rtng");
    private static final int TYPE_GAPLESS_ALBUM = Util.getIntegerCodeForString("pgap");
    private static final int TYPE_TV_SORT_SHOW = Util.getIntegerCodeForString("sosn");
    private static final int TYPE_TV_SHOW = Util.getIntegerCodeForString("tvsh");
    private static final int TYPE_INTERNAL = Util.getIntegerCodeForString("----");
    private static final String[] STANDARD_GENRES = {"Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "A capella", "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "BritPop", "Negerpunk", "Polsk Punk", "Beat", "Christian Gangsta Rap", "Heavy Metal", "Black Metal", "Crossover", "Contemporary Christian", "Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "Jpop", "Synthpop"};

    private MetadataUtil() {
    }

    /* JADX WARN: Removed duplicated region for block: B:120:0x01b4 A[Catch: all -> 0x01d6, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x01d6, blocks: (B:52:0x00b3, B:54:0x00b7, B:56:0x00c0, B:58:0x00c4, B:60:0x00cf, B:62:0x00d3, B:64:0x00de, B:66:0x00e2, B:68:0x00ef, B:70:0x00f3, B:72:0x0100, B:74:0x0104, B:76:0x010d, B:78:0x0111, B:80:0x011c, B:82:0x0120, B:84:0x012b, B:86:0x012f, B:88:0x013a, B:90:0x013e, B:92:0x0149, B:94:0x014d, B:96:0x0158, B:98:0x015c, B:100:0x0167, B:102:0x016b, B:104:0x0178, B:106:0x017c, B:108:0x0189, B:110:0x018d, B:112:0x0198, B:114:0x019c, B:116:0x01a7, B:118:0x01ab, B:120:0x01b4, B:7:0x0020, B:9:0x0024, B:12:0x002c, B:14:0x0030, B:18:0x003e, B:20:0x0042, B:24:0x0050, B:26:0x0054, B:28:0x005e, B:30:0x0062, B:32:0x006c, B:34:0x0070, B:36:0x007a, B:38:0x007e, B:40:0x0088, B:42:0x008c, B:44:0x0096, B:46:0x009a, B:48:0x00a4, B:50:0x00a8, B:22:0x0046, B:16:0x0034), top: B:126:0x0014 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.google.android.exoplayer2.metadata.Metadata.Entry parseIlstElement(com.google.android.exoplayer2.util.ParsableByteArray r8) {
        /*
            Method dump skipped, instructions count: 475
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mp4.MetadataUtil.parseIlstElement(com.google.android.exoplayer2.util.ParsableByteArray):com.google.android.exoplayer2.metadata.Metadata$Entry");
    }

    private static TextInformationFrame parseTextAttribute(int type, String id, ParsableByteArray data) {
        int atomSize = data.readInt();
        int atomType = data.readInt();
        if (atomType == Atom.TYPE_data) {
            data.skipBytes(8);
            String value = data.readNullTerminatedString(atomSize - 16);
            return new TextInformationFrame(id, null, value);
        }
        Log.w(TAG, "Failed to parse text attribute: " + Atom.getAtomTypeString(type));
        return null;
    }

    private static CommentFrame parseCommentAttribute(int type, ParsableByteArray data) {
        int atomSize = data.readInt();
        int atomType = data.readInt();
        if (atomType == Atom.TYPE_data) {
            data.skipBytes(8);
            String value = data.readNullTerminatedString(atomSize - 16);
            return new CommentFrame(LANGUAGE_UNDEFINED, value, value);
        }
        Log.w(TAG, "Failed to parse comment attribute: " + Atom.getAtomTypeString(type));
        return null;
    }

    private static Id3Frame parseUint8Attribute(int type, String id, ParsableByteArray data, boolean isTextInformationFrame, boolean isBoolean) {
        int value = parseUint8AttributeValue(data);
        if (isBoolean) {
            value = Math.min(1, value);
        }
        if (value >= 0) {
            return isTextInformationFrame ? new TextInformationFrame(id, null, Integer.toString(value)) : new CommentFrame(LANGUAGE_UNDEFINED, id, Integer.toString(value));
        }
        Log.w(TAG, "Failed to parse uint8 attribute: " + Atom.getAtomTypeString(type));
        return null;
    }

    private static TextInformationFrame parseIndexAndCountAttribute(int type, String attributeName, ParsableByteArray data) {
        int atomSize = data.readInt();
        int atomType = data.readInt();
        if (atomType == Atom.TYPE_data && atomSize >= 22) {
            data.skipBytes(10);
            int index = data.readUnsignedShort();
            if (index > 0) {
                String value = "" + index;
                int count = data.readUnsignedShort();
                if (count > 0) {
                    value = value + MqttTopic.TOPIC_LEVEL_SEPARATOR + count;
                }
                return new TextInformationFrame(attributeName, null, value);
            }
        }
        Log.w(TAG, "Failed to parse index/count attribute: " + Atom.getAtomTypeString(type));
        return null;
    }

    private static TextInformationFrame parseStandardGenreAttribute(ParsableByteArray data) {
        int genreCode = parseUint8AttributeValue(data);
        String genreString = (genreCode <= 0 || genreCode > STANDARD_GENRES.length) ? null : STANDARD_GENRES[genreCode - 1];
        if (genreString != null) {
            return new TextInformationFrame("TCON", null, genreString);
        }
        Log.w(TAG, "Failed to parse standard genre code");
        return null;
    }

    private static ApicFrame parseCoverArt(ParsableByteArray data) {
        String mimeType;
        int atomSize = data.readInt();
        int atomType = data.readInt();
        if (atomType == Atom.TYPE_data) {
            int fullVersionInt = data.readInt();
            int flags = Atom.parseFullAtomFlags(fullVersionInt);
            if (flags == 13) {
                mimeType = "image/jpeg";
            } else {
                mimeType = flags == 14 ? "image/png" : null;
            }
            if (mimeType == null) {
                Log.w(TAG, "Unrecognized cover art flags: " + flags);
                return null;
            }
            data.skipBytes(4);
            byte[] pictureData = new byte[atomSize - 16];
            data.readBytes(pictureData, 0, pictureData.length);
            return new ApicFrame(mimeType, null, 3, pictureData);
        }
        Log.w(TAG, "Failed to parse cover art attribute");
        return null;
    }

    private static Id3Frame parseInternalAttribute(ParsableByteArray data, int endPosition) {
        String domain = null;
        String name = null;
        int dataAtomPosition = -1;
        int dataAtomSize = -1;
        while (data.getPosition() < endPosition) {
            int atomPosition = data.getPosition();
            int atomSize = data.readInt();
            int atomType = data.readInt();
            data.skipBytes(4);
            if (atomType == Atom.TYPE_mean) {
                domain = data.readNullTerminatedString(atomSize - 12);
            } else if (atomType == Atom.TYPE_name) {
                name = data.readNullTerminatedString(atomSize - 12);
            } else {
                if (atomType == Atom.TYPE_data) {
                    dataAtomPosition = atomPosition;
                    dataAtomSize = atomSize;
                }
                data.skipBytes(atomSize - 12);
            }
        }
        if (!"com.apple.iTunes".equals(domain) || !"iTunSMPB".equals(name) || dataAtomPosition == -1) {
            return null;
        }
        data.setPosition(dataAtomPosition);
        data.skipBytes(16);
        String value = data.readNullTerminatedString(dataAtomSize - 16);
        return new CommentFrame(LANGUAGE_UNDEFINED, name, value);
    }

    private static int parseUint8AttributeValue(ParsableByteArray data) {
        data.skipBytes(4);
        int atomType = data.readInt();
        if (atomType == Atom.TYPE_data) {
            data.skipBytes(8);
            return data.readUnsignedByte();
        }
        Log.w(TAG, "Failed to parse uint8 attribute value");
        return -1;
    }
}
