package com.google.android.exoplayer2.metadata.id3;

import android.util.Log;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataDecoder;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public final class Id3Decoder implements MetadataDecoder {
    private static final int FRAME_FLAG_V3_HAS_GROUP_IDENTIFIER = 32;
    private static final int FRAME_FLAG_V3_IS_COMPRESSED = 128;
    private static final int FRAME_FLAG_V3_IS_ENCRYPTED = 64;
    private static final int FRAME_FLAG_V4_HAS_DATA_LENGTH = 1;
    private static final int FRAME_FLAG_V4_HAS_GROUP_IDENTIFIER = 64;
    private static final int FRAME_FLAG_V4_IS_COMPRESSED = 8;
    private static final int FRAME_FLAG_V4_IS_ENCRYPTED = 4;
    private static final int FRAME_FLAG_V4_IS_UNSYNCHRONIZED = 2;
    public static final int ID3_HEADER_LENGTH = 10;
    public static final int ID3_TAG = Util.getIntegerCodeForString("ID3");
    private static final int ID3_TEXT_ENCODING_ISO_8859_1 = 0;
    private static final int ID3_TEXT_ENCODING_UTF_16 = 1;
    private static final int ID3_TEXT_ENCODING_UTF_16BE = 2;
    private static final int ID3_TEXT_ENCODING_UTF_8 = 3;
    private static final String TAG = "Id3Decoder";
    private final FramePredicate framePredicate;

    public interface FramePredicate {
        boolean evaluate(int i, int i2, int i3, int i4, int i5);
    }

    public Id3Decoder() {
        this(null);
    }

    public Id3Decoder(FramePredicate framePredicate) {
        this.framePredicate = framePredicate;
    }

    @Override // com.google.android.exoplayer2.metadata.MetadataDecoder
    public Metadata decode(MetadataInputBuffer inputBuffer) {
        ByteBuffer buffer = inputBuffer.data;
        return decode(buffer.array(), buffer.limit());
    }

    public Metadata decode(byte[] data, int size) {
        List<Id3Frame> id3Frames = new ArrayList<>();
        ParsableByteArray id3Data = new ParsableByteArray(data, size);
        Id3Header id3Header = decodeHeader(id3Data);
        if (id3Header == null) {
            return null;
        }
        int startPosition = id3Data.getPosition();
        int frameHeaderSize = id3Header.majorVersion == 2 ? 6 : 10;
        int framesSize = id3Header.framesSize;
        if (id3Header.isUnsynchronized) {
            framesSize = removeUnsynchronization(id3Data, id3Header.framesSize);
        }
        id3Data.setLimit(startPosition + framesSize);
        boolean unsignedIntFrameSizeHack = false;
        if (!validateFrames(id3Data, id3Header.majorVersion, frameHeaderSize, false)) {
            if (id3Header.majorVersion == 4 && validateFrames(id3Data, 4, frameHeaderSize, true)) {
                unsignedIntFrameSizeHack = true;
            } else {
                Log.w(TAG, "Failed to validate ID3 tag with majorVersion=" + id3Header.majorVersion);
                return null;
            }
        }
        while (id3Data.bytesLeft() >= frameHeaderSize) {
            Id3Frame frame = decodeFrame(id3Header.majorVersion, id3Data, unsignedIntFrameSizeHack, frameHeaderSize, this.framePredicate);
            if (frame != null) {
                id3Frames.add(frame);
            }
        }
        return new Metadata(id3Frames);
    }

    private static Id3Header decodeHeader(ParsableByteArray data) {
        if (data.bytesLeft() < 10) {
            Log.w(TAG, "Data too short to be an ID3 tag");
            return null;
        }
        int id = data.readUnsignedInt24();
        if (id != ID3_TAG) {
            Log.w(TAG, "Unexpected first three bytes of ID3 tag header: " + id);
            return null;
        }
        int majorVersion = data.readUnsignedByte();
        data.skipBytes(1);
        int flags = data.readUnsignedByte();
        int framesSize = data.readSynchSafeInt();
        if (majorVersion == 2) {
            boolean isCompressed = (flags & 64) != 0;
            if (isCompressed) {
                Log.w(TAG, "Skipped ID3 tag with majorVersion=2 and undefined compression scheme");
                return null;
            }
        } else if (majorVersion == 3) {
            boolean hasExtendedHeader = (flags & 64) != 0;
            if (hasExtendedHeader) {
                int extendedHeaderSize = data.readInt();
                data.skipBytes(extendedHeaderSize);
                framesSize -= extendedHeaderSize + 4;
            }
        } else if (majorVersion == 4) {
            boolean hasExtendedHeader2 = (flags & 64) != 0;
            if (hasExtendedHeader2) {
                int extendedHeaderSize2 = data.readSynchSafeInt();
                data.skipBytes(extendedHeaderSize2 - 4);
                framesSize -= extendedHeaderSize2;
            }
            boolean hasFooter = (flags & 16) != 0;
            if (hasFooter) {
                framesSize -= 10;
            }
        } else {
            Log.w(TAG, "Skipped ID3 tag with unsupported majorVersion=" + majorVersion);
            return null;
        }
        boolean isUnsynchronized = majorVersion < 4 && (flags & 128) != 0;
        return new Id3Header(majorVersion, isUnsynchronized, framesSize);
    }

    private static boolean validateFrames(ParsableByteArray id3Data, int majorVersion, int frameHeaderSize, boolean unsignedIntFrameSizeHack) {
        int id;
        long frameSize;
        int flags;
        int startPosition = id3Data.getPosition();
        while (id3Data.bytesLeft() >= frameHeaderSize) {
            try {
                if (majorVersion >= 3) {
                    id = id3Data.readInt();
                    frameSize = id3Data.readUnsignedInt();
                    flags = id3Data.readUnsignedShort();
                } else {
                    id = id3Data.readUnsignedInt24();
                    frameSize = id3Data.readUnsignedInt24();
                    flags = 0;
                }
                if (id == 0 && frameSize == 0 && flags == 0) {
                    return true;
                }
                if (majorVersion == 4 && !unsignedIntFrameSizeHack) {
                    if ((8421504 & frameSize) != 0) {
                        return false;
                    }
                    frameSize = (255 & frameSize) | (((frameSize >> 8) & 255) << 7) | (((frameSize >> 16) & 255) << 14) | (((frameSize >> 24) & 255) << 21);
                }
                boolean hasGroupIdentifier = false;
                boolean hasDataLength = false;
                if (majorVersion == 4) {
                    hasGroupIdentifier = (flags & 64) != 0;
                    hasDataLength = (flags & 1) != 0;
                } else if (majorVersion == 3) {
                    hasGroupIdentifier = (flags & 32) != 0;
                    hasDataLength = (flags & 128) != 0;
                }
                int minimumFrameSize = hasGroupIdentifier ? 0 + 1 : 0;
                if (hasDataLength) {
                    minimumFrameSize += 4;
                }
                if (frameSize < minimumFrameSize) {
                    return false;
                }
                if (id3Data.bytesLeft() < frameSize) {
                    return false;
                }
                id3Data.skipBytes((int) frameSize);
            } finally {
                id3Data.setPosition(startPosition);
            }
        }
        return true;
    }

    private static Id3Frame decodeFrame(int majorVersion, ParsableByteArray id3Data, boolean unsignedIntFrameSizeHack, int frameHeaderSize, FramePredicate framePredicate) {
        int frameSize;
        Id3Frame frame;
        int frameId0 = id3Data.readUnsignedByte();
        int frameId1 = id3Data.readUnsignedByte();
        int frameId2 = id3Data.readUnsignedByte();
        int frameId3 = majorVersion >= 3 ? id3Data.readUnsignedByte() : 0;
        if (majorVersion == 4) {
            frameSize = id3Data.readUnsignedIntToInt();
            if (!unsignedIntFrameSizeHack) {
                frameSize = (frameSize & 255) | (((frameSize >> 8) & 255) << 7) | (((frameSize >> 16) & 255) << 14) | (((frameSize >> 24) & 255) << 21);
            }
        } else if (majorVersion == 3) {
            frameSize = id3Data.readUnsignedIntToInt();
        } else {
            frameSize = id3Data.readUnsignedInt24();
        }
        int flags = majorVersion >= 3 ? id3Data.readUnsignedShort() : 0;
        if (frameId0 == 0 && frameId1 == 0 && frameId2 == 0 && frameId3 == 0 && frameSize == 0 && flags == 0) {
            id3Data.setPosition(id3Data.limit());
            return null;
        }
        int nextFramePosition = id3Data.getPosition() + frameSize;
        if (nextFramePosition > id3Data.limit()) {
            Log.w(TAG, "Frame size exceeds remaining tag data");
            id3Data.setPosition(id3Data.limit());
            return null;
        }
        if (framePredicate != null && !framePredicate.evaluate(majorVersion, frameId0, frameId1, frameId2, frameId3)) {
            id3Data.setPosition(nextFramePosition);
            return null;
        }
        boolean isCompressed = false;
        boolean isEncrypted = false;
        boolean isUnsynchronized = false;
        boolean hasDataLength = false;
        boolean hasGroupIdentifier = false;
        if (majorVersion == 3) {
            isCompressed = (flags & 128) != 0;
            isEncrypted = (flags & 64) != 0;
            hasGroupIdentifier = (flags & 32) != 0;
            hasDataLength = isCompressed;
        } else if (majorVersion == 4) {
            hasGroupIdentifier = (flags & 64) != 0;
            isCompressed = (flags & 8) != 0;
            isEncrypted = (flags & 4) != 0;
            isUnsynchronized = (flags & 2) != 0;
            hasDataLength = (flags & 1) != 0;
        }
        if (isCompressed || isEncrypted) {
            Log.w(TAG, "Skipping unsupported compressed or encrypted frame");
            id3Data.setPosition(nextFramePosition);
            return null;
        }
        if (hasGroupIdentifier) {
            frameSize--;
            id3Data.skipBytes(1);
        }
        if (hasDataLength) {
            frameSize -= 4;
            id3Data.skipBytes(4);
        }
        if (isUnsynchronized) {
            frameSize = removeUnsynchronization(id3Data, frameSize);
        }
        try {
            if (frameId0 == 84 && frameId1 == 88 && frameId2 == 88 && (majorVersion == 2 || frameId3 == 88)) {
                frame = decodeTxxxFrame(id3Data, frameSize);
            } else if (frameId0 == 84) {
                String id = getFrameId(majorVersion, frameId0, frameId1, frameId2, frameId3);
                frame = decodeTextInformationFrame(id3Data, frameSize, id);
            } else if (frameId0 == 87 && frameId1 == 88 && frameId2 == 88 && (majorVersion == 2 || frameId3 == 88)) {
                frame = decodeWxxxFrame(id3Data, frameSize);
            } else if (frameId0 == 87) {
                String id2 = getFrameId(majorVersion, frameId0, frameId1, frameId2, frameId3);
                frame = decodeUrlLinkFrame(id3Data, frameSize, id2);
            } else if (frameId0 == 80 && frameId1 == 82 && frameId2 == 73 && frameId3 == 86) {
                frame = decodePrivFrame(id3Data, frameSize);
            } else if (frameId0 == 71 && frameId1 == 69 && frameId2 == 79 && (frameId3 == 66 || majorVersion == 2)) {
                frame = decodeGeobFrame(id3Data, frameSize);
            } else if (majorVersion != 2 ? !(frameId0 != 65 || frameId1 != 80 || frameId2 != 73 || frameId3 != 67) : !(frameId0 != 80 || frameId1 != 73 || frameId2 != 67)) {
                frame = decodeApicFrame(id3Data, frameSize, majorVersion);
            } else if (frameId0 == 67 && frameId1 == 79 && frameId2 == 77 && (frameId3 == 77 || majorVersion == 2)) {
                frame = decodeCommentFrame(id3Data, frameSize);
            } else if (frameId0 == 67 && frameId1 == 72 && frameId2 == 65 && frameId3 == 80) {
                frame = decodeChapterFrame(id3Data, frameSize, majorVersion, unsignedIntFrameSizeHack, frameHeaderSize, framePredicate);
            } else if (frameId0 == 67 && frameId1 == 84 && frameId2 == 79 && frameId3 == 67) {
                frame = decodeChapterTOCFrame(id3Data, frameSize, majorVersion, unsignedIntFrameSizeHack, frameHeaderSize, framePredicate);
            } else {
                String id3 = getFrameId(majorVersion, frameId0, frameId1, frameId2, frameId3);
                frame = decodeBinaryFrame(id3Data, frameSize, id3);
            }
            if (frame == null) {
                Log.w(TAG, "Failed to decode frame: id=" + getFrameId(majorVersion, frameId0, frameId1, frameId2, frameId3) + ", frameSize=" + frameSize);
            }
            return frame;
        } catch (UnsupportedEncodingException e) {
            Log.w(TAG, "Unsupported character encoding");
            return null;
        } finally {
            id3Data.setPosition(nextFramePosition);
        }
    }

    private static TextInformationFrame decodeTxxxFrame(ParsableByteArray id3Data, int frameSize) throws UnsupportedEncodingException {
        String value;
        if (frameSize < 1) {
            return null;
        }
        int encoding = id3Data.readUnsignedByte();
        String charset = getCharsetName(encoding);
        byte[] data = new byte[frameSize - 1];
        id3Data.readBytes(data, 0, frameSize - 1);
        int descriptionEndIndex = indexOfEos(data, 0, encoding);
        String description = new String(data, 0, descriptionEndIndex, charset);
        int valueStartIndex = descriptionEndIndex + delimiterLength(encoding);
        if (valueStartIndex < data.length) {
            int valueEndIndex = indexOfEos(data, valueStartIndex, encoding);
            value = new String(data, valueStartIndex, valueEndIndex - valueStartIndex, charset);
        } else {
            value = "";
        }
        return new TextInformationFrame("TXXX", description, value);
    }

    private static TextInformationFrame decodeTextInformationFrame(ParsableByteArray id3Data, int frameSize, String id) throws UnsupportedEncodingException {
        if (frameSize < 1) {
            return null;
        }
        int encoding = id3Data.readUnsignedByte();
        String charset = getCharsetName(encoding);
        byte[] data = new byte[frameSize - 1];
        id3Data.readBytes(data, 0, frameSize - 1);
        int valueEndIndex = indexOfEos(data, 0, encoding);
        String value = new String(data, 0, valueEndIndex, charset);
        return new TextInformationFrame(id, null, value);
    }

    private static UrlLinkFrame decodeWxxxFrame(ParsableByteArray id3Data, int frameSize) throws UnsupportedEncodingException {
        String url;
        if (frameSize < 1) {
            return null;
        }
        int encoding = id3Data.readUnsignedByte();
        String charset = getCharsetName(encoding);
        byte[] data = new byte[frameSize - 1];
        id3Data.readBytes(data, 0, frameSize - 1);
        int descriptionEndIndex = indexOfEos(data, 0, encoding);
        String description = new String(data, 0, descriptionEndIndex, charset);
        int urlStartIndex = descriptionEndIndex + delimiterLength(encoding);
        if (urlStartIndex < data.length) {
            int urlEndIndex = indexOfZeroByte(data, urlStartIndex);
            url = new String(data, urlStartIndex, urlEndIndex - urlStartIndex, "ISO-8859-1");
        } else {
            url = "";
        }
        return new UrlLinkFrame("WXXX", description, url);
    }

    private static UrlLinkFrame decodeUrlLinkFrame(ParsableByteArray id3Data, int frameSize, String id) throws UnsupportedEncodingException {
        byte[] data = new byte[frameSize];
        id3Data.readBytes(data, 0, frameSize);
        int urlEndIndex = indexOfZeroByte(data, 0);
        String url = new String(data, 0, urlEndIndex, "ISO-8859-1");
        return new UrlLinkFrame(id, null, url);
    }

    private static PrivFrame decodePrivFrame(ParsableByteArray id3Data, int frameSize) throws UnsupportedEncodingException {
        byte[] privateData;
        byte[] data = new byte[frameSize];
        id3Data.readBytes(data, 0, frameSize);
        int ownerEndIndex = indexOfZeroByte(data, 0);
        String owner = new String(data, 0, ownerEndIndex, "ISO-8859-1");
        int privateDataStartIndex = ownerEndIndex + 1;
        if (privateDataStartIndex < data.length) {
            privateData = Arrays.copyOfRange(data, privateDataStartIndex, data.length);
        } else {
            privateData = new byte[0];
        }
        return new PrivFrame(owner, privateData);
    }

    private static GeobFrame decodeGeobFrame(ParsableByteArray id3Data, int frameSize) throws UnsupportedEncodingException {
        int encoding = id3Data.readUnsignedByte();
        String charset = getCharsetName(encoding);
        byte[] data = new byte[frameSize - 1];
        id3Data.readBytes(data, 0, frameSize - 1);
        int mimeTypeEndIndex = indexOfZeroByte(data, 0);
        String mimeType = new String(data, 0, mimeTypeEndIndex, "ISO-8859-1");
        int filenameStartIndex = mimeTypeEndIndex + 1;
        int filenameEndIndex = indexOfEos(data, filenameStartIndex, encoding);
        String filename = new String(data, filenameStartIndex, filenameEndIndex - filenameStartIndex, charset);
        int descriptionStartIndex = filenameEndIndex + delimiterLength(encoding);
        int descriptionEndIndex = indexOfEos(data, descriptionStartIndex, encoding);
        String description = new String(data, descriptionStartIndex, descriptionEndIndex - descriptionStartIndex, charset);
        int objectDataStartIndex = descriptionEndIndex + delimiterLength(encoding);
        byte[] objectData = Arrays.copyOfRange(data, objectDataStartIndex, data.length);
        return new GeobFrame(mimeType, filename, description, objectData);
    }

    private static ApicFrame decodeApicFrame(ParsableByteArray id3Data, int frameSize, int majorVersion) throws UnsupportedEncodingException {
        int mimeTypeEndIndex;
        String mimeType;
        int encoding = id3Data.readUnsignedByte();
        String charset = getCharsetName(encoding);
        byte[] data = new byte[frameSize - 1];
        id3Data.readBytes(data, 0, frameSize - 1);
        if (majorVersion == 2) {
            mimeTypeEndIndex = 2;
            mimeType = "image/" + Util.toLowerInvariant(new String(data, 0, 3, "ISO-8859-1"));
            if (mimeType.equals("image/jpg")) {
                mimeType = "image/jpeg";
            }
        } else {
            mimeTypeEndIndex = indexOfZeroByte(data, 0);
            mimeType = Util.toLowerInvariant(new String(data, 0, mimeTypeEndIndex, "ISO-8859-1"));
            if (mimeType.indexOf(47) == -1) {
                mimeType = "image/" + mimeType;
            }
        }
        int pictureType = data[mimeTypeEndIndex + 1] & 255;
        int descriptionStartIndex = mimeTypeEndIndex + 2;
        int descriptionEndIndex = indexOfEos(data, descriptionStartIndex, encoding);
        String description = new String(data, descriptionStartIndex, descriptionEndIndex - descriptionStartIndex, charset);
        int pictureDataStartIndex = descriptionEndIndex + delimiterLength(encoding);
        byte[] pictureData = Arrays.copyOfRange(data, pictureDataStartIndex, data.length);
        return new ApicFrame(mimeType, description, pictureType, pictureData);
    }

    private static CommentFrame decodeCommentFrame(ParsableByteArray id3Data, int frameSize) throws UnsupportedEncodingException {
        String text;
        if (frameSize < 4) {
            return null;
        }
        int encoding = id3Data.readUnsignedByte();
        String charset = getCharsetName(encoding);
        byte[] data = new byte[3];
        id3Data.readBytes(data, 0, 3);
        String language = new String(data, 0, 3);
        byte[] data2 = new byte[frameSize - 4];
        id3Data.readBytes(data2, 0, frameSize - 4);
        int descriptionEndIndex = indexOfEos(data2, 0, encoding);
        String description = new String(data2, 0, descriptionEndIndex, charset);
        int textStartIndex = descriptionEndIndex + delimiterLength(encoding);
        if (textStartIndex < data2.length) {
            int textEndIndex = indexOfEos(data2, textStartIndex, encoding);
            text = new String(data2, textStartIndex, textEndIndex - textStartIndex, charset);
        } else {
            text = "";
        }
        return new CommentFrame(language, description, text);
    }

    private static ChapterFrame decodeChapterFrame(ParsableByteArray id3Data, int frameSize, int majorVersion, boolean unsignedIntFrameSizeHack, int frameHeaderSize, FramePredicate framePredicate) throws UnsupportedEncodingException {
        int framePosition = id3Data.getPosition();
        int chapterIdEndIndex = indexOfZeroByte(id3Data.data, framePosition);
        String chapterId = new String(id3Data.data, framePosition, chapterIdEndIndex - framePosition, "ISO-8859-1");
        id3Data.setPosition(chapterIdEndIndex + 1);
        int startTime = id3Data.readInt();
        int endTime = id3Data.readInt();
        long startOffset = id3Data.readUnsignedInt();
        if (startOffset == 4294967295L) {
            startOffset = -1;
        }
        long endOffset = id3Data.readUnsignedInt();
        if (endOffset == 4294967295L) {
            endOffset = -1;
        }
        ArrayList<Id3Frame> subFrames = new ArrayList<>();
        int limit = framePosition + frameSize;
        while (id3Data.getPosition() < limit) {
            Id3Frame frame = decodeFrame(majorVersion, id3Data, unsignedIntFrameSizeHack, frameHeaderSize, framePredicate);
            if (frame != null) {
                subFrames.add(frame);
            }
        }
        Id3Frame[] subFrameArray = new Id3Frame[subFrames.size()];
        subFrames.toArray(subFrameArray);
        return new ChapterFrame(chapterId, startTime, endTime, startOffset, endOffset, subFrameArray);
    }

    private static ChapterTocFrame decodeChapterTOCFrame(ParsableByteArray id3Data, int frameSize, int majorVersion, boolean unsignedIntFrameSizeHack, int frameHeaderSize, FramePredicate framePredicate) throws UnsupportedEncodingException {
        int framePosition = id3Data.getPosition();
        int elementIdEndIndex = indexOfZeroByte(id3Data.data, framePosition);
        String elementId = new String(id3Data.data, framePosition, elementIdEndIndex - framePosition, "ISO-8859-1");
        id3Data.setPosition(elementIdEndIndex + 1);
        int ctocFlags = id3Data.readUnsignedByte();
        boolean isRoot = (ctocFlags & 2) != 0;
        boolean isOrdered = (ctocFlags & 1) != 0;
        int childCount = id3Data.readUnsignedByte();
        String[] children = new String[childCount];
        for (int i = 0; i < childCount; i++) {
            int startIndex = id3Data.getPosition();
            int endIndex = indexOfZeroByte(id3Data.data, startIndex);
            children[i] = new String(id3Data.data, startIndex, endIndex - startIndex, "ISO-8859-1");
            id3Data.setPosition(endIndex + 1);
        }
        ArrayList<Id3Frame> subFrames = new ArrayList<>();
        int limit = framePosition + frameSize;
        while (id3Data.getPosition() < limit) {
            Id3Frame frame = decodeFrame(majorVersion, id3Data, unsignedIntFrameSizeHack, frameHeaderSize, framePredicate);
            if (frame != null) {
                subFrames.add(frame);
            }
        }
        Id3Frame[] subFrameArray = new Id3Frame[subFrames.size()];
        subFrames.toArray(subFrameArray);
        return new ChapterTocFrame(elementId, isRoot, isOrdered, children, subFrameArray);
    }

    private static BinaryFrame decodeBinaryFrame(ParsableByteArray id3Data, int frameSize, String id) {
        byte[] frame = new byte[frameSize];
        id3Data.readBytes(frame, 0, frameSize);
        return new BinaryFrame(id, frame);
    }

    private static int removeUnsynchronization(ParsableByteArray data, int length) {
        byte[] bytes = data.data;
        for (int i = data.getPosition(); i + 1 < length; i++) {
            if ((bytes[i] & 255) == 255 && bytes[i + 1] == 0) {
                System.arraycopy(bytes, i + 2, bytes, i + 1, (length - i) - 2);
                length--;
            }
        }
        return length;
    }

    private static String getCharsetName(int encodingByte) {
        switch (encodingByte) {
        }
        return "ISO-8859-1";
    }

    private static String getFrameId(int majorVersion, int frameId0, int frameId1, int frameId2, int frameId3) {
        return majorVersion == 2 ? String.format(Locale.US, "%c%c%c", Integer.valueOf(frameId0), Integer.valueOf(frameId1), Integer.valueOf(frameId2)) : String.format(Locale.US, "%c%c%c%c", Integer.valueOf(frameId0), Integer.valueOf(frameId1), Integer.valueOf(frameId2), Integer.valueOf(frameId3));
    }

    private static int indexOfEos(byte[] data, int fromIndex, int encoding) {
        int terminationPos = indexOfZeroByte(data, fromIndex);
        if (encoding == 0 || encoding == 3) {
            return terminationPos;
        }
        while (terminationPos < data.length - 1) {
            if (terminationPos % 2 == 0 && data[terminationPos + 1] == 0) {
                return terminationPos;
            }
            terminationPos = indexOfZeroByte(data, terminationPos + 1);
        }
        return data.length;
    }

    private static int indexOfZeroByte(byte[] data, int fromIndex) {
        for (int i = fromIndex; i < data.length; i++) {
            if (data[i] == 0) {
                return i;
            }
        }
        int i2 = data.length;
        return i2;
    }

    private static int delimiterLength(int encodingByte) {
        return (encodingByte == 0 || encodingByte == 3) ? 1 : 2;
    }

    private static final class Id3Header {
        private final int framesSize;
        private final boolean isUnsynchronized;
        private final int majorVersion;

        public Id3Header(int majorVersion, boolean isUnsynchronized, int framesSize) {
            this.majorVersion = majorVersion;
            this.isUnsynchronized = isUnsynchronized;
            this.framesSize = framesSize;
        }
    }
}
