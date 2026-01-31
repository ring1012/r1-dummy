package okhttp3.internal.cache2;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

/* loaded from: classes.dex */
final class Relay {
    private static final long FILE_HEADER_SIZE = 32;
    static final ByteString PREFIX_CLEAN = ByteString.encodeUtf8("OkHttp cache v1\n");
    static final ByteString PREFIX_DIRTY = ByteString.encodeUtf8("OkHttp DIRTY :(\n");
    private static final int SOURCE_FILE = 2;
    private static final int SOURCE_UPSTREAM = 1;
    final long bufferMaxSize;
    boolean complete;
    RandomAccessFile file;
    private final ByteString metadata;
    int sourceCount;
    Source upstream;
    long upstreamPos;
    Thread upstreamReader;
    final Buffer upstreamBuffer = new Buffer();
    final Buffer buffer = new Buffer();

    private Relay(RandomAccessFile file, Source upstream, long upstreamPos, ByteString metadata, long bufferMaxSize) {
        this.file = file;
        this.upstream = upstream;
        this.complete = upstream == null;
        this.upstreamPos = upstreamPos;
        this.metadata = metadata;
        this.bufferMaxSize = bufferMaxSize;
    }

    public static Relay edit(File file, Source upstream, ByteString metadata, long bufferMaxSize) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        Relay result = new Relay(randomAccessFile, upstream, 0L, metadata, bufferMaxSize);
        randomAccessFile.setLength(0L);
        result.writeHeader(PREFIX_DIRTY, -1L, -1L);
        return result;
    }

    public static Relay read(File file) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileOperator fileOperator = new FileOperator(randomAccessFile.getChannel());
        Buffer header = new Buffer();
        fileOperator.read(0L, header, FILE_HEADER_SIZE);
        ByteString prefix = header.readByteString(PREFIX_CLEAN.size());
        if (!prefix.equals(PREFIX_CLEAN)) {
            throw new IOException("unreadable cache file");
        }
        long upstreamSize = header.readLong();
        long metadataSize = header.readLong();
        Buffer metadataBuffer = new Buffer();
        fileOperator.read(FILE_HEADER_SIZE + upstreamSize, metadataBuffer, metadataSize);
        ByteString metadata = metadataBuffer.readByteString();
        return new Relay(randomAccessFile, null, upstreamSize, metadata, 0L);
    }

    private void writeHeader(ByteString prefix, long upstreamSize, long metadataSize) throws IOException {
        Buffer header = new Buffer();
        header.write(prefix);
        header.writeLong(upstreamSize);
        header.writeLong(metadataSize);
        if (header.size() != FILE_HEADER_SIZE) {
            throw new IllegalArgumentException();
        }
        FileOperator fileOperator = new FileOperator(this.file.getChannel());
        fileOperator.write(0L, header, FILE_HEADER_SIZE);
    }

    private void writeMetadata(long upstreamSize) throws IOException {
        Buffer metadataBuffer = new Buffer();
        metadataBuffer.write(this.metadata);
        FileOperator fileOperator = new FileOperator(this.file.getChannel());
        fileOperator.write(FILE_HEADER_SIZE + upstreamSize, metadataBuffer, this.metadata.size());
    }

    void commit(long upstreamSize) throws IOException {
        writeMetadata(upstreamSize);
        this.file.getChannel().force(false);
        writeHeader(PREFIX_CLEAN, upstreamSize, this.metadata.size());
        this.file.getChannel().force(false);
        synchronized (this) {
            this.complete = true;
        }
        Util.closeQuietly(this.upstream);
        this.upstream = null;
    }

    boolean isClosed() {
        return this.file == null;
    }

    public ByteString metadata() {
        return this.metadata;
    }

    public Source newSource() {
        synchronized (this) {
            if (this.file == null) {
                return null;
            }
            this.sourceCount++;
            return new RelaySource();
        }
    }

    class RelaySource implements Source {
        private FileOperator fileOperator;
        private long sourcePos;
        private final Timeout timeout = new Timeout();

        RelaySource() {
            this.fileOperator = new FileOperator(Relay.this.file.getChannel());
        }

        /* JADX WARN: Code restructure failed: missing block: B:24:0x0056, code lost:
        
            if (r24 != 2) goto L81;
         */
        /* JADX WARN: Code restructure failed: missing block: B:25:0x0058, code lost:
        
            r8 = java.lang.Math.min(r30, r0 - r28.sourcePos);
            r28.fileOperator.read(okhttp3.internal.cache2.Relay.FILE_HEADER_SIZE + r28.sourcePos, r29, r8);
            r28.sourcePos += r8;
         */
        /* JADX WARN: Code restructure failed: missing block: B:34:0x00c0, code lost:
        
            r20 = r28.this$0.upstream.read(r28.this$0.upstreamBuffer, r28.this$0.bufferMaxSize);
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x00da, code lost:
        
            if (r20 != (-1)) goto L45;
         */
        /* JADX WARN: Code restructure failed: missing block: B:36:0x00dc, code lost:
        
            r28.this$0.commit(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:37:0x00e5, code lost:
        
            r5 = r28.this$0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:38:0x00eb, code lost:
        
            monitor-enter(r5);
         */
        /* JADX WARN: Code restructure failed: missing block: B:39:0x00ec, code lost:
        
            r28.this$0.upstreamReader = null;
            r28.this$0.notifyAll();
         */
        /* JADX WARN: Code restructure failed: missing block: B:40:0x00fa, code lost:
        
            monitor-exit(r5);
         */
        /* JADX WARN: Code restructure failed: missing block: B:45:0x0100, code lost:
        
            r14 = java.lang.Math.min(r20, r30);
            r28.this$0.upstreamBuffer.copyTo(r29, 0, r14);
            r28.sourcePos += r14;
            r28.fileOperator.write(okhttp3.internal.cache2.Relay.FILE_HEADER_SIZE + r0, r28.this$0.upstreamBuffer.clone(), r20);
            r5 = r28.this$0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:46:0x0139, code lost:
        
            monitor-enter(r5);
         */
        /* JADX WARN: Code restructure failed: missing block: B:47:0x013a, code lost:
        
            r28.this$0.buffer.write(r28.this$0.upstreamBuffer, r20);
         */
        /* JADX WARN: Code restructure failed: missing block: B:48:0x015d, code lost:
        
            if (r28.this$0.buffer.size() <= r28.this$0.bufferMaxSize) goto L50;
         */
        /* JADX WARN: Code restructure failed: missing block: B:49:0x015f, code lost:
        
            r28.this$0.buffer.skip(r28.this$0.buffer.size() - r28.this$0.bufferMaxSize);
         */
        /* JADX WARN: Code restructure failed: missing block: B:50:0x0179, code lost:
        
            r28.this$0.upstreamPos += r20;
         */
        /* JADX WARN: Code restructure failed: missing block: B:51:0x0183, code lost:
        
            monitor-exit(r5);
         */
        /* JADX WARN: Code restructure failed: missing block: B:52:0x0184, code lost:
        
            r5 = r28.this$0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:53:0x0188, code lost:
        
            monitor-enter(r5);
         */
        /* JADX WARN: Code restructure failed: missing block: B:54:0x0189, code lost:
        
            r28.this$0.upstreamReader = null;
            r28.this$0.notifyAll();
         */
        /* JADX WARN: Code restructure failed: missing block: B:55:0x0197, code lost:
        
            monitor-exit(r5);
         */
        /* JADX WARN: Code restructure failed: missing block: B:60:0x019e, code lost:
        
            r4 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:62:0x01a3, code lost:
        
            monitor-enter(r28.this$0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:63:0x01a4, code lost:
        
            r28.this$0.upstreamReader = null;
            r28.this$0.notifyAll();
         */
        /* JADX WARN: Code restructure failed: missing block: B:65:0x01b3, code lost:
        
            throw r4;
         */
        /* JADX WARN: Code restructure failed: missing block: B:85:?, code lost:
        
            return r8;
         */
        /* JADX WARN: Code restructure failed: missing block: B:86:?, code lost:
        
            return -1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:87:?, code lost:
        
            return r14;
         */
        @Override // okio.Source
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public long read(okio.Buffer r29, long r30) throws java.io.IOException {
            /*
                Method dump skipped, instructions count: 442
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache2.Relay.RelaySource.read(okio.Buffer, long):long");
        }

        @Override // okio.Source
        public Timeout timeout() {
            return this.timeout;
        }

        @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.fileOperator != null) {
                this.fileOperator = null;
                RandomAccessFile fileToClose = null;
                synchronized (Relay.this) {
                    Relay relay = Relay.this;
                    relay.sourceCount--;
                    if (Relay.this.sourceCount == 0) {
                        fileToClose = Relay.this.file;
                        Relay.this.file = null;
                    }
                }
                if (fileToClose != null) {
                    Util.closeQuietly(fileToClose);
                }
            }
        }
    }
}
