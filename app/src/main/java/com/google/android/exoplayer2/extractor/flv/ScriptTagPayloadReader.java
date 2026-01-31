package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
final class ScriptTagPayloadReader extends TagPayloadReader {
    private static final int AMF_TYPE_BOOLEAN = 1;
    private static final int AMF_TYPE_DATE = 11;
    private static final int AMF_TYPE_ECMA_ARRAY = 8;
    private static final int AMF_TYPE_END_MARKER = 9;
    private static final int AMF_TYPE_NUMBER = 0;
    private static final int AMF_TYPE_OBJECT = 3;
    private static final int AMF_TYPE_STRICT_ARRAY = 10;
    private static final int AMF_TYPE_STRING = 2;
    private static final String KEY_DURATION = "duration";
    private static final String NAME_METADATA = "onMetaData";
    private long durationUs;

    public ScriptTagPayloadReader(TrackOutput output) {
        super(output);
        this.durationUs = C.TIME_UNSET;
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    public void seek() {
    }

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    protected boolean parseHeader(ParsableByteArray data) {
        return true;
    }

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    protected void parsePayload(ParsableByteArray data, long timeUs) throws ParserException {
        int nameType = readAmfType(data);
        if (nameType != 2) {
            throw new ParserException();
        }
        String name = readAmfString(data);
        if (NAME_METADATA.equals(name)) {
            int type = readAmfType(data);
            if (type == 8) {
                Map<String, Object> metadata = readAmfEcmaArray(data);
                if (metadata.containsKey(KEY_DURATION)) {
                    double durationSeconds = ((Double) metadata.get(KEY_DURATION)).doubleValue();
                    if (durationSeconds > 0.0d) {
                        this.durationUs = (long) (1000000.0d * durationSeconds);
                    }
                }
            }
        }
    }

    private static int readAmfType(ParsableByteArray data) {
        return data.readUnsignedByte();
    }

    private static Boolean readAmfBoolean(ParsableByteArray data) {
        return Boolean.valueOf(data.readUnsignedByte() == 1);
    }

    private static Double readAmfDouble(ParsableByteArray data) {
        return Double.valueOf(Double.longBitsToDouble(data.readLong()));
    }

    private static String readAmfString(ParsableByteArray data) {
        int size = data.readUnsignedShort();
        int position = data.getPosition();
        data.skipBytes(size);
        return new String(data.data, position, size);
    }

    private static ArrayList<Object> readAmfStrictArray(ParsableByteArray data) {
        int count = data.readUnsignedIntToInt();
        ArrayList<Object> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int type = readAmfType(data);
            list.add(readAmfData(data, type));
        }
        return list;
    }

    private static HashMap<String, Object> readAmfObject(ParsableByteArray data) {
        HashMap<String, Object> array = new HashMap<>();
        while (true) {
            String key = readAmfString(data);
            int type = readAmfType(data);
            if (type != 9) {
                array.put(key, readAmfData(data, type));
            } else {
                return array;
            }
        }
    }

    private static HashMap<String, Object> readAmfEcmaArray(ParsableByteArray data) {
        int count = data.readUnsignedIntToInt();
        HashMap<String, Object> array = new HashMap<>(count);
        for (int i = 0; i < count; i++) {
            String key = readAmfString(data);
            int type = readAmfType(data);
            array.put(key, readAmfData(data, type));
        }
        return array;
    }

    private static Date readAmfDate(ParsableByteArray data) {
        Date date = new Date((long) readAmfDouble(data).doubleValue());
        data.skipBytes(2);
        return date;
    }

    private static Object readAmfData(ParsableByteArray data, int type) {
        switch (type) {
            case 0:
                return readAmfDouble(data);
            case 1:
                return readAmfBoolean(data);
            case 2:
                return readAmfString(data);
            case 3:
                return readAmfObject(data);
            case 4:
            case 5:
            case 6:
            case 7:
            case 9:
            default:
                return null;
            case 8:
                return readAmfEcmaArray(data);
            case 10:
                return readAmfStrictArray(data);
            case 11:
                return readAmfDate(data);
        }
    }
}
