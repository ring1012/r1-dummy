package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.upstream.Allocation;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public final class DefaultTrackOutput implements TrackOutput {
    private static final int INITIAL_SCRATCH_SIZE = 32;
    private static final int STATE_DISABLED = 2;
    private static final int STATE_ENABLED = 0;
    private static final int STATE_ENABLED_WRITING = 1;
    private final int allocationLength;
    private final Allocator allocator;
    private Format downstreamFormat;
    private Allocation lastAllocation;
    private int lastAllocationOffset;
    private Format lastUnadjustedFormat;
    private boolean pendingFormatAdjustment;
    private boolean pendingSplice;
    private long sampleOffsetUs;
    private long totalBytesDropped;
    private long totalBytesWritten;
    private UpstreamFormatChangedListener upstreamFormatChangeListener;
    private final InfoQueue infoQueue = new InfoQueue();
    private final LinkedBlockingDeque<Allocation> dataQueue = new LinkedBlockingDeque<>();
    private final BufferExtrasHolder extrasHolder = new BufferExtrasHolder();
    private final ParsableByteArray scratch = new ParsableByteArray(32);
    private final AtomicInteger state = new AtomicInteger();

    public interface UpstreamFormatChangedListener {
        void onUpstreamFormatChanged(Format format);
    }

    public DefaultTrackOutput(Allocator allocator) {
        this.allocator = allocator;
        this.allocationLength = allocator.getIndividualAllocationLength();
        this.lastAllocationOffset = this.allocationLength;
    }

    public void reset(boolean enable) {
        int previousState = this.state.getAndSet(enable ? 0 : 2);
        clearSampleData();
        this.infoQueue.resetLargestParsedTimestamps();
        if (previousState == 2) {
            this.downstreamFormat = null;
        }
    }

    public void sourceId(int sourceId) {
        this.infoQueue.sourceId(sourceId);
    }

    public void splice() {
        this.pendingSplice = true;
    }

    public int getWriteIndex() {
        return this.infoQueue.getWriteIndex();
    }

    public void discardUpstreamSamples(int discardFromIndex) {
        this.totalBytesWritten = this.infoQueue.discardUpstreamSamples(discardFromIndex);
        dropUpstreamFrom(this.totalBytesWritten);
    }

    private void dropUpstreamFrom(long absolutePosition) {
        int relativePosition = (int) (absolutePosition - this.totalBytesDropped);
        int allocationIndex = relativePosition / this.allocationLength;
        int allocationOffset = relativePosition % this.allocationLength;
        int allocationDiscardCount = (this.dataQueue.size() - allocationIndex) - 1;
        if (allocationOffset == 0) {
            allocationDiscardCount++;
        }
        for (int i = 0; i < allocationDiscardCount; i++) {
            this.allocator.release(this.dataQueue.removeLast());
        }
        this.lastAllocation = this.dataQueue.peekLast();
        if (allocationOffset == 0) {
            allocationOffset = this.allocationLength;
        }
        this.lastAllocationOffset = allocationOffset;
    }

    public void disable() {
        if (this.state.getAndSet(2) == 0) {
            clearSampleData();
        }
    }

    public boolean isEmpty() {
        return this.infoQueue.isEmpty();
    }

    public int getReadIndex() {
        return this.infoQueue.getReadIndex();
    }

    public int peekSourceId() {
        return this.infoQueue.peekSourceId();
    }

    public Format getUpstreamFormat() {
        return this.infoQueue.getUpstreamFormat();
    }

    public long getLargestQueuedTimestampUs() {
        return this.infoQueue.getLargestQueuedTimestampUs();
    }

    public void skipAll() {
        long nextOffset = this.infoQueue.skipAll();
        if (nextOffset != -1) {
            dropDownstreamTo(nextOffset);
        }
    }

    public boolean skipToKeyframeBefore(long timeUs, boolean allowTimeBeyondBuffer) {
        long nextOffset = this.infoQueue.skipToKeyframeBefore(timeUs, allowTimeBeyondBuffer);
        if (nextOffset == -1) {
            return false;
        }
        dropDownstreamTo(nextOffset);
        return true;
    }

    public int readData(FormatHolder formatHolder, DecoderInputBuffer buffer, boolean formatRequired, boolean loadingFinished, long decodeOnlyUntilUs) throws IllegalStateException {
        int result = this.infoQueue.readData(formatHolder, buffer, formatRequired, loadingFinished, this.downstreamFormat, this.extrasHolder);
        switch (result) {
            case -5:
                this.downstreamFormat = formatHolder.format;
                return -5;
            case -4:
                if (!buffer.isEndOfStream()) {
                    if (buffer.timeUs < decodeOnlyUntilUs) {
                        buffer.addFlag(Integer.MIN_VALUE);
                    }
                    if (buffer.isEncrypted()) {
                        readEncryptionData(buffer, this.extrasHolder);
                    }
                    buffer.ensureSpaceForWrite(this.extrasHolder.size);
                    readData(this.extrasHolder.offset, buffer.data, this.extrasHolder.size);
                    dropDownstreamTo(this.extrasHolder.nextOffset);
                }
                return -4;
            case -3:
                return -3;
            default:
                throw new IllegalStateException();
        }
    }

    private void readEncryptionData(DecoderInputBuffer buffer, BufferExtrasHolder extrasHolder) {
        int subsampleCount;
        long offset = extrasHolder.offset;
        this.scratch.reset(1);
        readData(offset, this.scratch.data, 1);
        long offset2 = offset + 1;
        byte signalByte = this.scratch.data[0];
        boolean subsampleEncryption = (signalByte & 128) != 0;
        int ivSize = signalByte & 127;
        if (buffer.cryptoInfo.iv == null) {
            buffer.cryptoInfo.iv = new byte[16];
        }
        readData(offset2, buffer.cryptoInfo.iv, ivSize);
        long offset3 = offset2 + ivSize;
        if (subsampleEncryption) {
            this.scratch.reset(2);
            readData(offset3, this.scratch.data, 2);
            offset3 += 2;
            subsampleCount = this.scratch.readUnsignedShort();
        } else {
            subsampleCount = 1;
        }
        int[] clearDataSizes = buffer.cryptoInfo.numBytesOfClearData;
        if (clearDataSizes == null || clearDataSizes.length < subsampleCount) {
            clearDataSizes = new int[subsampleCount];
        }
        int[] encryptedDataSizes = buffer.cryptoInfo.numBytesOfEncryptedData;
        if (encryptedDataSizes == null || encryptedDataSizes.length < subsampleCount) {
            encryptedDataSizes = new int[subsampleCount];
        }
        if (subsampleEncryption) {
            int subsampleDataLength = subsampleCount * 6;
            this.scratch.reset(subsampleDataLength);
            readData(offset3, this.scratch.data, subsampleDataLength);
            offset3 += subsampleDataLength;
            this.scratch.setPosition(0);
            for (int i = 0; i < subsampleCount; i++) {
                clearDataSizes[i] = this.scratch.readUnsignedShort();
                encryptedDataSizes[i] = this.scratch.readUnsignedIntToInt();
            }
        } else {
            clearDataSizes[0] = 0;
            encryptedDataSizes[0] = extrasHolder.size - ((int) (offset3 - extrasHolder.offset));
        }
        buffer.cryptoInfo.set(subsampleCount, clearDataSizes, encryptedDataSizes, extrasHolder.encryptionKeyId, buffer.cryptoInfo.iv, 1);
        int bytesRead = (int) (offset3 - extrasHolder.offset);
        extrasHolder.offset += bytesRead;
        extrasHolder.size -= bytesRead;
    }

    private void readData(long absolutePosition, ByteBuffer target, int length) {
        int remaining = length;
        while (remaining > 0) {
            dropDownstreamTo(absolutePosition);
            int positionInAllocation = (int) (absolutePosition - this.totalBytesDropped);
            int toCopy = Math.min(remaining, this.allocationLength - positionInAllocation);
            Allocation allocation = this.dataQueue.peek();
            target.put(allocation.data, allocation.translateOffset(positionInAllocation), toCopy);
            absolutePosition += toCopy;
            remaining -= toCopy;
        }
    }

    private void readData(long absolutePosition, byte[] target, int length) {
        int bytesRead = 0;
        while (bytesRead < length) {
            dropDownstreamTo(absolutePosition);
            int positionInAllocation = (int) (absolutePosition - this.totalBytesDropped);
            int toCopy = Math.min(length - bytesRead, this.allocationLength - positionInAllocation);
            Allocation allocation = this.dataQueue.peek();
            System.arraycopy(allocation.data, allocation.translateOffset(positionInAllocation), target, bytesRead, toCopy);
            absolutePosition += toCopy;
            bytesRead += toCopy;
        }
    }

    private void dropDownstreamTo(long absolutePosition) {
        int relativePosition = (int) (absolutePosition - this.totalBytesDropped);
        int allocationIndex = relativePosition / this.allocationLength;
        for (int i = 0; i < allocationIndex; i++) {
            this.allocator.release(this.dataQueue.remove());
            this.totalBytesDropped += this.allocationLength;
        }
    }

    public void setUpstreamFormatChangeListener(UpstreamFormatChangedListener listener) {
        this.upstreamFormatChangeListener = listener;
    }

    public void setSampleOffsetUs(long sampleOffsetUs) {
        if (this.sampleOffsetUs != sampleOffsetUs) {
            this.sampleOffsetUs = sampleOffsetUs;
            this.pendingFormatAdjustment = true;
        }
    }

    @Override // com.google.android.exoplayer2.extractor.TrackOutput
    public void format(Format format) {
        Format adjustedFormat = getAdjustedSampleFormat(format, this.sampleOffsetUs);
        boolean formatChanged = this.infoQueue.format(adjustedFormat);
        this.lastUnadjustedFormat = format;
        this.pendingFormatAdjustment = false;
        if (this.upstreamFormatChangeListener != null && formatChanged) {
            this.upstreamFormatChangeListener.onUpstreamFormatChanged(adjustedFormat);
        }
    }

    @Override // com.google.android.exoplayer2.extractor.TrackOutput
    public int sampleData(ExtractorInput input, int length, boolean allowEndOfInput) throws InterruptedException, IOException {
        if (!startWriteOperation()) {
            int bytesSkipped = input.skip(length);
            if (bytesSkipped != -1) {
                return bytesSkipped;
            }
            if (allowEndOfInput) {
                return -1;
            }
            throw new EOFException();
        }
        try {
            int bytesAppended = input.read(this.lastAllocation.data, this.lastAllocation.translateOffset(this.lastAllocationOffset), prepareForAppend(length));
            if (bytesAppended == -1) {
                if (allowEndOfInput) {
                    return -1;
                }
                throw new EOFException();
            }
            this.lastAllocationOffset += bytesAppended;
            this.totalBytesWritten += bytesAppended;
            return bytesAppended;
        } finally {
            endWriteOperation();
        }
    }

    @Override // com.google.android.exoplayer2.extractor.TrackOutput
    public void sampleData(ParsableByteArray buffer, int length) {
        if (!startWriteOperation()) {
            buffer.skipBytes(length);
            return;
        }
        while (length > 0) {
            int thisAppendLength = prepareForAppend(length);
            buffer.readBytes(this.lastAllocation.data, this.lastAllocation.translateOffset(this.lastAllocationOffset), thisAppendLength);
            this.lastAllocationOffset += thisAppendLength;
            this.totalBytesWritten += thisAppendLength;
            length -= thisAppendLength;
        }
        endWriteOperation();
    }

    @Override // com.google.android.exoplayer2.extractor.TrackOutput
    public void sampleMetadata(long timeUs, int flags, int size, int offset, byte[] encryptionKey) {
        if (this.pendingFormatAdjustment) {
            format(this.lastUnadjustedFormat);
        }
        if (!startWriteOperation()) {
            this.infoQueue.commitSampleTimestamp(timeUs);
            return;
        }
        try {
            if (this.pendingSplice) {
                if ((flags & 1) != 0 && this.infoQueue.attemptSplice(timeUs)) {
                    this.pendingSplice = false;
                } else {
                    return;
                }
            }
            long timeUs2 = timeUs + this.sampleOffsetUs;
            long absoluteOffset = (this.totalBytesWritten - size) - offset;
            this.infoQueue.commitSample(timeUs2, flags, absoluteOffset, size, encryptionKey);
        } finally {
            endWriteOperation();
        }
    }

    private boolean startWriteOperation() {
        return this.state.compareAndSet(0, 1);
    }

    private void endWriteOperation() {
        if (!this.state.compareAndSet(1, 0)) {
            clearSampleData();
        }
    }

    private void clearSampleData() {
        this.infoQueue.clearSampleData();
        this.allocator.release((Allocation[]) this.dataQueue.toArray(new Allocation[this.dataQueue.size()]));
        this.dataQueue.clear();
        this.allocator.trim();
        this.totalBytesDropped = 0L;
        this.totalBytesWritten = 0L;
        this.lastAllocation = null;
        this.lastAllocationOffset = this.allocationLength;
    }

    private int prepareForAppend(int length) {
        if (this.lastAllocationOffset == this.allocationLength) {
            this.lastAllocationOffset = 0;
            this.lastAllocation = this.allocator.allocate();
            this.dataQueue.add(this.lastAllocation);
        }
        return Math.min(length, this.allocationLength - this.lastAllocationOffset);
    }

    private static Format getAdjustedSampleFormat(Format format, long sampleOffsetUs) {
        if (format == null) {
            return null;
        }
        if (sampleOffsetUs != 0 && format.subsampleOffsetUs != Long.MAX_VALUE) {
            return format.copyWithSubsampleOffsetUs(format.subsampleOffsetUs + sampleOffsetUs);
        }
        return format;
    }

    private static final class InfoQueue {
        private static final int SAMPLE_CAPACITY_INCREMENT = 1000;
        private int absoluteReadIndex;
        private int queueSize;
        private int relativeReadIndex;
        private int relativeWriteIndex;
        private Format upstreamFormat;
        private int upstreamSourceId;
        private int capacity = 1000;
        private int[] sourceIds = new int[this.capacity];
        private long[] offsets = new long[this.capacity];
        private long[] timesUs = new long[this.capacity];
        private int[] flags = new int[this.capacity];
        private int[] sizes = new int[this.capacity];
        private byte[][] encryptionKeys = new byte[this.capacity][];
        private Format[] formats = new Format[this.capacity];
        private long largestDequeuedTimestampUs = Long.MIN_VALUE;
        private long largestQueuedTimestampUs = Long.MIN_VALUE;
        private boolean upstreamFormatRequired = true;
        private boolean upstreamKeyframeRequired = true;

        public void clearSampleData() {
            this.absoluteReadIndex = 0;
            this.relativeReadIndex = 0;
            this.relativeWriteIndex = 0;
            this.queueSize = 0;
            this.upstreamKeyframeRequired = true;
        }

        public void resetLargestParsedTimestamps() {
            this.largestDequeuedTimestampUs = Long.MIN_VALUE;
            this.largestQueuedTimestampUs = Long.MIN_VALUE;
        }

        public int getWriteIndex() {
            return this.absoluteReadIndex + this.queueSize;
        }

        public long discardUpstreamSamples(int discardFromIndex) {
            int discardCount = getWriteIndex() - discardFromIndex;
            Assertions.checkArgument(discardCount >= 0 && discardCount <= this.queueSize);
            if (discardCount == 0) {
                if (this.absoluteReadIndex == 0) {
                    return 0L;
                }
                int lastWriteIndex = (this.relativeWriteIndex == 0 ? this.capacity : this.relativeWriteIndex) - 1;
                return this.offsets[lastWriteIndex] + this.sizes[lastWriteIndex];
            }
            this.queueSize -= discardCount;
            this.relativeWriteIndex = ((this.relativeWriteIndex + this.capacity) - discardCount) % this.capacity;
            this.largestQueuedTimestampUs = Long.MIN_VALUE;
            for (int i = this.queueSize - 1; i >= 0; i--) {
                int sampleIndex = (this.relativeReadIndex + i) % this.capacity;
                this.largestQueuedTimestampUs = Math.max(this.largestQueuedTimestampUs, this.timesUs[sampleIndex]);
                if ((this.flags[sampleIndex] & 1) != 0) {
                    break;
                }
            }
            return this.offsets[this.relativeWriteIndex];
        }

        public void sourceId(int sourceId) {
            this.upstreamSourceId = sourceId;
        }

        public int getReadIndex() {
            return this.absoluteReadIndex;
        }

        public int peekSourceId() {
            return this.queueSize == 0 ? this.upstreamSourceId : this.sourceIds[this.relativeReadIndex];
        }

        public synchronized boolean isEmpty() {
            return this.queueSize == 0;
        }

        public synchronized Format getUpstreamFormat() {
            return this.upstreamFormatRequired ? null : this.upstreamFormat;
        }

        public synchronized long getLargestQueuedTimestampUs() {
            return Math.max(this.largestDequeuedTimestampUs, this.largestQueuedTimestampUs);
        }

        public synchronized int readData(FormatHolder formatHolder, DecoderInputBuffer buffer, boolean formatRequired, boolean loadingFinished, Format downstreamFormat, BufferExtrasHolder extrasHolder) {
            long j;
            int i = -4;
            synchronized (this) {
                if (this.queueSize == 0) {
                    if (loadingFinished) {
                        buffer.setFlags(4);
                    } else if (this.upstreamFormat == null || (!formatRequired && this.upstreamFormat == downstreamFormat)) {
                        i = -3;
                    } else {
                        formatHolder.format = this.upstreamFormat;
                        i = -5;
                    }
                } else if (formatRequired || this.formats[this.relativeReadIndex] != downstreamFormat) {
                    formatHolder.format = this.formats[this.relativeReadIndex];
                    i = -5;
                } else if (buffer.isFlagsOnly()) {
                    i = -3;
                } else {
                    buffer.timeUs = this.timesUs[this.relativeReadIndex];
                    buffer.setFlags(this.flags[this.relativeReadIndex]);
                    extrasHolder.size = this.sizes[this.relativeReadIndex];
                    extrasHolder.offset = this.offsets[this.relativeReadIndex];
                    extrasHolder.encryptionKeyId = this.encryptionKeys[this.relativeReadIndex];
                    this.largestDequeuedTimestampUs = Math.max(this.largestDequeuedTimestampUs, buffer.timeUs);
                    this.queueSize--;
                    this.relativeReadIndex++;
                    this.absoluteReadIndex++;
                    if (this.relativeReadIndex == this.capacity) {
                        this.relativeReadIndex = 0;
                    }
                    if (this.queueSize > 0) {
                        j = this.offsets[this.relativeReadIndex];
                    } else {
                        j = extrasHolder.offset + extrasHolder.size;
                    }
                    extrasHolder.nextOffset = j;
                }
            }
            return i;
        }

        public synchronized long skipAll() {
            long j;
            if (this.queueSize == 0) {
                j = -1;
            } else {
                int lastSampleIndex = ((this.relativeReadIndex + this.queueSize) - 1) % this.capacity;
                this.relativeReadIndex = (this.relativeReadIndex + this.queueSize) % this.capacity;
                this.absoluteReadIndex += this.queueSize;
                this.queueSize = 0;
                j = this.offsets[lastSampleIndex] + this.sizes[lastSampleIndex];
            }
            return j;
        }

        public synchronized long skipToKeyframeBefore(long timeUs, boolean allowTimeBeyondBuffer) {
            long j = -1;
            synchronized (this) {
                if (this.queueSize != 0 && timeUs >= this.timesUs[this.relativeReadIndex] && (timeUs <= this.largestQueuedTimestampUs || allowTimeBeyondBuffer)) {
                    int sampleCount = 0;
                    int sampleCountToKeyframe = -1;
                    int searchIndex = this.relativeReadIndex;
                    while (searchIndex != this.relativeWriteIndex && this.timesUs[searchIndex] <= timeUs) {
                        if ((this.flags[searchIndex] & 1) != 0) {
                            sampleCountToKeyframe = sampleCount;
                        }
                        searchIndex = (searchIndex + 1) % this.capacity;
                        sampleCount++;
                    }
                    if (sampleCountToKeyframe != -1) {
                        this.relativeReadIndex = (this.relativeReadIndex + sampleCountToKeyframe) % this.capacity;
                        this.absoluteReadIndex += sampleCountToKeyframe;
                        this.queueSize -= sampleCountToKeyframe;
                        j = this.offsets[this.relativeReadIndex];
                    }
                }
            }
            return j;
        }

        public synchronized boolean format(Format format) {
            boolean z = false;
            synchronized (this) {
                if (format == null) {
                    this.upstreamFormatRequired = true;
                } else {
                    this.upstreamFormatRequired = false;
                    if (!Util.areEqual(format, this.upstreamFormat)) {
                        this.upstreamFormat = format;
                        z = true;
                    }
                }
            }
            return z;
        }

        /* JADX WARN: Removed duplicated region for block: B:13:0x0012  */
        /* JADX WARN: Removed duplicated region for block: B:16:0x0053 A[Catch: all -> 0x00ec, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:10:0x000c, B:11:0x000e, B:14:0x0013, B:16:0x0053, B:22:0x00f2, B:24:0x00fe), top: B:26:0x0001 }] */
        /* JADX WARN: Removed duplicated region for block: B:21:0x00ef  */
        /* JADX WARN: Removed duplicated region for block: B:22:0x00f2 A[Catch: all -> 0x00ec, TRY_ENTER, TryCatch #0 {, blocks: (B:3:0x0001, B:10:0x000c, B:11:0x000e, B:14:0x0013, B:16:0x0053, B:22:0x00f2, B:24:0x00fe), top: B:26:0x0001 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public synchronized void commitSample(long r14, int r16, long r17, int r19, byte[] r20) {
            /*
                Method dump skipped, instructions count: 259
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.DefaultTrackOutput.InfoQueue.commitSample(long, int, long, int, byte[]):void");
        }

        public synchronized void commitSampleTimestamp(long timeUs) {
            this.largestQueuedTimestampUs = Math.max(this.largestQueuedTimestampUs, timeUs);
        }

        public synchronized boolean attemptSplice(long timeUs) {
            boolean z;
            if (this.largestDequeuedTimestampUs >= timeUs) {
                z = false;
            } else {
                int retainCount = this.queueSize;
                while (retainCount > 0 && this.timesUs[((this.relativeReadIndex + retainCount) - 1) % this.capacity] >= timeUs) {
                    retainCount--;
                }
                discardUpstreamSamples(this.absoluteReadIndex + retainCount);
                z = true;
            }
            return z;
        }
    }

    private static final class BufferExtrasHolder {
        public byte[] encryptionKeyId;
        public long nextOffset;
        public long offset;
        public int size;

        private BufferExtrasHolder() {
        }
    }
}
