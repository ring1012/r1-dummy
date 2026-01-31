package org.apache.commons.io.input;

import com.google.android.exoplayer2.C;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class ReversedLinesFileReader implements Closeable {
    private final int avoidNewlineSplitBufferSize;
    private final int blockSize;
    private final int byteDecrement;
    private FilePart currentFilePart;
    private final Charset encoding;
    private final byte[][] newLineSequences;
    private final RandomAccessFile randomAccessFile;
    private final long totalBlockCount;
    private final long totalByteLength;
    private boolean trailingNewlineOfFileSkipped;

    public ReversedLinesFileReader(File file) throws IOException {
        this(file, 4096, Charset.defaultCharset().toString());
    }

    public ReversedLinesFileReader(File file, int blockSize, Charset encoding) throws IOException {
        this.trailingNewlineOfFileSkipped = false;
        this.blockSize = blockSize;
        this.encoding = encoding;
        this.randomAccessFile = new RandomAccessFile(file, "r");
        this.totalByteLength = this.randomAccessFile.length();
        int lastBlockLength = (int) (this.totalByteLength % blockSize);
        if (lastBlockLength > 0) {
            this.totalBlockCount = (this.totalByteLength / blockSize) + 1;
        } else {
            this.totalBlockCount = this.totalByteLength / blockSize;
            if (this.totalByteLength > 0) {
                lastBlockLength = blockSize;
            }
        }
        this.currentFilePart = new FilePart(this.totalBlockCount, lastBlockLength, null);
        Charset charset = Charsets.toCharset(encoding);
        CharsetEncoder charsetEncoder = charset.newEncoder();
        float maxBytesPerChar = charsetEncoder.maxBytesPerChar();
        if (maxBytesPerChar == 1.0f || charset == Charset.forName("UTF-8") || charset == Charset.forName("Shift_JIS")) {
            this.byteDecrement = 1;
        } else if (charset == Charset.forName("UTF-16BE") || charset == Charset.forName("UTF-16LE")) {
            this.byteDecrement = 2;
        } else {
            if (charset == Charset.forName(C.UTF16_NAME)) {
                throw new UnsupportedEncodingException("For UTF-16, you need to specify the byte order (use UTF-16BE or UTF-16LE)");
            }
            throw new UnsupportedEncodingException("Encoding " + encoding + " is not supported yet (feel free to submit a patch)");
        }
        this.newLineSequences = new byte[][]{IOUtils.LINE_SEPARATOR_WINDOWS.getBytes(encoding), "\n".getBytes(encoding), "\r".getBytes(encoding)};
        this.avoidNewlineSplitBufferSize = this.newLineSequences[0].length;
    }

    public ReversedLinesFileReader(File file, int blockSize, String encoding) throws IOException {
        this(file, blockSize, Charsets.toCharset(encoding));
    }

    public String readLine() throws IOException {
        String line = this.currentFilePart.readLine();
        while (line == null) {
            this.currentFilePart = this.currentFilePart.rollOver();
            if (this.currentFilePart == null) {
                break;
            }
            line = this.currentFilePart.readLine();
        }
        if ("".equals(line) && !this.trailingNewlineOfFileSkipped) {
            this.trailingNewlineOfFileSkipped = true;
            String line2 = readLine();
            return line2;
        }
        return line;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.randomAccessFile.close();
    }

    private class FilePart {
        private int currentLastBytePos;
        private final byte[] data;
        private byte[] leftOver;
        private final long no;

        private FilePart(long no, int length, byte[] leftOverOfLastFilePart) throws IOException {
            this.no = no;
            int dataLength = length + (leftOverOfLastFilePart != null ? leftOverOfLastFilePart.length : 0);
            this.data = new byte[dataLength];
            long off = (no - 1) * ReversedLinesFileReader.this.blockSize;
            if (no > 0) {
                ReversedLinesFileReader.this.randomAccessFile.seek(off);
                int countRead = ReversedLinesFileReader.this.randomAccessFile.read(this.data, 0, length);
                if (countRead != length) {
                    throw new IllegalStateException("Count of requested bytes and actually read bytes don't match");
                }
            }
            if (leftOverOfLastFilePart != null) {
                System.arraycopy(leftOverOfLastFilePart, 0, this.data, length, leftOverOfLastFilePart.length);
            }
            this.currentLastBytePos = this.data.length - 1;
            this.leftOver = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public FilePart rollOver() throws IOException {
            if (this.currentLastBytePos > -1) {
                throw new IllegalStateException("Current currentLastCharPos unexpectedly positive... last readLine() should have returned something! currentLastCharPos=" + this.currentLastBytePos);
            }
            if (this.no > 1) {
                return ReversedLinesFileReader.this.new FilePart(this.no - 1, ReversedLinesFileReader.this.blockSize, this.leftOver);
            }
            if (this.leftOver != null) {
                throw new IllegalStateException("Unexpected leftover of the last block: leftOverOfThisFilePart=" + new String(this.leftOver, ReversedLinesFileReader.this.encoding));
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String readLine() throws IOException {
            String line = null;
            boolean isLastFilePart = this.no == 1;
            int i = this.currentLastBytePos;
            while (true) {
                if (i > -1) {
                    if (!isLastFilePart && i < ReversedLinesFileReader.this.avoidNewlineSplitBufferSize) {
                        createLeftOver();
                        break;
                    }
                    int newLineMatchByteCount = getNewLineMatchByteCount(this.data, i);
                    if (newLineMatchByteCount <= 0) {
                        i -= ReversedLinesFileReader.this.byteDecrement;
                        if (i < 0) {
                            createLeftOver();
                            break;
                        }
                    } else {
                        int lineStart = i + 1;
                        int lineLengthBytes = (this.currentLastBytePos - lineStart) + 1;
                        if (lineLengthBytes < 0) {
                            throw new IllegalStateException("Unexpected negative line length=" + lineLengthBytes);
                        }
                        byte[] lineData = new byte[lineLengthBytes];
                        System.arraycopy(this.data, lineStart, lineData, 0, lineLengthBytes);
                        line = new String(lineData, ReversedLinesFileReader.this.encoding);
                        this.currentLastBytePos = i - newLineMatchByteCount;
                    }
                } else {
                    break;
                }
            }
            if (isLastFilePart && this.leftOver != null) {
                String line2 = new String(this.leftOver, ReversedLinesFileReader.this.encoding);
                this.leftOver = null;
                return line2;
            }
            return line;
        }

        private void createLeftOver() {
            int lineLengthBytes = this.currentLastBytePos + 1;
            if (lineLengthBytes > 0) {
                this.leftOver = new byte[lineLengthBytes];
                System.arraycopy(this.data, 0, this.leftOver, 0, lineLengthBytes);
            } else {
                this.leftOver = null;
            }
            this.currentLastBytePos = -1;
        }

        private int getNewLineMatchByteCount(byte[] data, int i) {
            byte[][] arr$ = ReversedLinesFileReader.this.newLineSequences;
            for (byte[] newLineSequence : arr$) {
                boolean match = true;
                for (int j = newLineSequence.length - 1; j >= 0; j--) {
                    int k = (i + j) - (newLineSequence.length - 1);
                    match &= k >= 0 && data[k] == newLineSequence[j];
                }
                if (match) {
                    return newLineSequence.length;
                }
            }
            return 0;
        }
    }
}
