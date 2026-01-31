package com.google.android.exoplayer2.metadata;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class MetadataRenderer extends BaseRenderer implements Handler.Callback {
    private static final int MAX_PENDING_METADATA_COUNT = 5;
    private static final int MSG_INVOKE_RENDERER = 0;
    private final MetadataInputBuffer buffer;
    private MetadataDecoder decoder;
    private final MetadataDecoderFactory decoderFactory;
    private final FormatHolder formatHolder;
    private boolean inputStreamEnded;
    private final Output output;
    private final Handler outputHandler;
    private final Metadata[] pendingMetadata;
    private int pendingMetadataCount;
    private int pendingMetadataIndex;
    private final long[] pendingMetadataTimestamps;

    public interface Output {
        void onMetadata(Metadata metadata);
    }

    public MetadataRenderer(Output output, Looper outputLooper) {
        this(output, outputLooper, MetadataDecoderFactory.DEFAULT);
    }

    public MetadataRenderer(Output output, Looper outputLooper, MetadataDecoderFactory decoderFactory) {
        super(4);
        this.output = (Output) Assertions.checkNotNull(output);
        this.outputHandler = outputLooper == null ? null : new Handler(outputLooper, this);
        this.decoderFactory = (MetadataDecoderFactory) Assertions.checkNotNull(decoderFactory);
        this.formatHolder = new FormatHolder();
        this.buffer = new MetadataInputBuffer();
        this.pendingMetadata = new Metadata[5];
        this.pendingMetadataTimestamps = new long[5];
    }

    @Override // com.google.android.exoplayer2.RendererCapabilities
    public int supportsFormat(Format format) {
        return this.decoderFactory.supportsFormat(format) ? 3 : 0;
    }

    @Override // com.google.android.exoplayer2.BaseRenderer
    protected void onStreamChanged(Format[] formats) throws ExoPlaybackException {
        this.decoder = this.decoderFactory.createDecoder(formats[0]);
    }

    @Override // com.google.android.exoplayer2.BaseRenderer
    protected void onPositionReset(long positionUs, boolean joining) {
        flushPendingMetadata();
        this.inputStreamEnded = false;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public void render(long positionUs, long elapsedRealtimeUs) throws ExoPlaybackException {
        if (!this.inputStreamEnded && this.pendingMetadataCount < 5) {
            this.buffer.clear();
            int result = readSource(this.formatHolder, this.buffer, false);
            if (result == -4) {
                if (this.buffer.isEndOfStream()) {
                    this.inputStreamEnded = true;
                } else if (!this.buffer.isDecodeOnly()) {
                    this.buffer.subsampleOffsetUs = this.formatHolder.format.subsampleOffsetUs;
                    this.buffer.flip();
                    try {
                        int index = (this.pendingMetadataIndex + this.pendingMetadataCount) % 5;
                        this.pendingMetadata[index] = this.decoder.decode(this.buffer);
                        this.pendingMetadataTimestamps[index] = this.buffer.timeUs;
                        this.pendingMetadataCount++;
                    } catch (MetadataDecoderException e) {
                        throw ExoPlaybackException.createForRenderer(e, getIndex());
                    }
                }
            }
        }
        if (this.pendingMetadataCount > 0 && this.pendingMetadataTimestamps[this.pendingMetadataIndex] <= positionUs) {
            invokeRenderer(this.pendingMetadata[this.pendingMetadataIndex]);
            this.pendingMetadata[this.pendingMetadataIndex] = null;
            this.pendingMetadataIndex = (this.pendingMetadataIndex + 1) % 5;
            this.pendingMetadataCount--;
        }
    }

    @Override // com.google.android.exoplayer2.BaseRenderer
    protected void onDisabled() {
        flushPendingMetadata();
        this.decoder = null;
        super.onDisabled();
    }

    @Override // com.google.android.exoplayer2.Renderer
    public boolean isEnded() {
        return this.inputStreamEnded;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public boolean isReady() {
        return true;
    }

    private void invokeRenderer(Metadata metadata) {
        if (this.outputHandler != null) {
            this.outputHandler.obtainMessage(0, metadata).sendToTarget();
        } else {
            invokeRendererInternal(metadata);
        }
    }

    private void flushPendingMetadata() {
        Arrays.fill(this.pendingMetadata, (Object) null);
        this.pendingMetadataIndex = 0;
        this.pendingMetadataCount = 0;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                invokeRendererInternal((Metadata) msg.obj);
                return true;
            default:
                throw new IllegalStateException();
        }
    }

    private void invokeRendererInternal(Metadata metadata) {
        this.output.onMetadata(metadata);
    }
}
