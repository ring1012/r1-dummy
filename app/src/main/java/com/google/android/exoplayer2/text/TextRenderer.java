package com.google.android.exoplayer2.text;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class TextRenderer extends BaseRenderer implements Handler.Callback {
    private static final int MSG_UPDATE_OUTPUT = 0;
    private static final int REPLACEMENT_STATE_NONE = 0;
    private static final int REPLACEMENT_STATE_SIGNAL_END_OF_STREAM = 1;
    private static final int REPLACEMENT_STATE_WAIT_END_OF_STREAM = 2;
    private SubtitleDecoder decoder;
    private final SubtitleDecoderFactory decoderFactory;
    private int decoderReplacementState;
    private final FormatHolder formatHolder;
    private boolean inputStreamEnded;
    private SubtitleInputBuffer nextInputBuffer;
    private SubtitleOutputBuffer nextSubtitle;
    private int nextSubtitleEventIndex;
    private final Output output;
    private final Handler outputHandler;
    private boolean outputStreamEnded;
    private Format streamFormat;
    private SubtitleOutputBuffer subtitle;

    public interface Output {
        void onCues(List<Cue> list);
    }

    public TextRenderer(Output output, Looper outputLooper) {
        this(output, outputLooper, SubtitleDecoderFactory.DEFAULT);
    }

    public TextRenderer(Output output, Looper outputLooper, SubtitleDecoderFactory decoderFactory) {
        super(3);
        this.output = (Output) Assertions.checkNotNull(output);
        this.outputHandler = outputLooper == null ? null : new Handler(outputLooper, this);
        this.decoderFactory = decoderFactory;
        this.formatHolder = new FormatHolder();
    }

    @Override // com.google.android.exoplayer2.RendererCapabilities
    public int supportsFormat(Format format) {
        if (this.decoderFactory.supportsFormat(format)) {
            return 3;
        }
        return MimeTypes.isText(format.sampleMimeType) ? 1 : 0;
    }

    @Override // com.google.android.exoplayer2.BaseRenderer
    protected void onStreamChanged(Format[] formats) throws ExoPlaybackException {
        this.streamFormat = formats[0];
        if (this.decoder != null) {
            this.decoderReplacementState = 1;
        } else {
            this.decoder = this.decoderFactory.createDecoder(this.streamFormat);
        }
    }

    @Override // com.google.android.exoplayer2.BaseRenderer
    protected void onPositionReset(long positionUs, boolean joining) {
        clearOutput();
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        if (this.decoderReplacementState != 0) {
            replaceDecoder();
        } else {
            releaseBuffers();
            this.decoder.flush();
        }
    }

    @Override // com.google.android.exoplayer2.Renderer
    public void render(long positionUs, long elapsedRealtimeUs) throws ExoPlaybackException {
        if (!this.outputStreamEnded) {
            if (this.nextSubtitle == null) {
                this.decoder.setPositionUs(positionUs);
                try {
                    this.nextSubtitle = this.decoder.dequeueOutputBuffer();
                } catch (SubtitleDecoderException e) {
                    throw ExoPlaybackException.createForRenderer(e, getIndex());
                }
            }
            if (getState() == 2) {
                boolean textRendererNeedsUpdate = false;
                if (this.subtitle != null) {
                    long subtitleNextEventTimeUs = getNextEventTime();
                    while (subtitleNextEventTimeUs <= positionUs) {
                        this.nextSubtitleEventIndex++;
                        subtitleNextEventTimeUs = getNextEventTime();
                        textRendererNeedsUpdate = true;
                    }
                }
                if (this.nextSubtitle != null) {
                    if (this.nextSubtitle.isEndOfStream()) {
                        if (!textRendererNeedsUpdate && getNextEventTime() == Long.MAX_VALUE) {
                            if (this.decoderReplacementState == 2) {
                                replaceDecoder();
                            } else {
                                releaseBuffers();
                                this.outputStreamEnded = true;
                            }
                        }
                    } else if (this.nextSubtitle.timeUs <= positionUs) {
                        if (this.subtitle != null) {
                            this.subtitle.release();
                        }
                        this.subtitle = this.nextSubtitle;
                        this.nextSubtitle = null;
                        this.nextSubtitleEventIndex = this.subtitle.getNextEventTimeIndex(positionUs);
                        textRendererNeedsUpdate = true;
                    }
                }
                if (textRendererNeedsUpdate) {
                    updateOutput(this.subtitle.getCues(positionUs));
                }
                if (this.decoderReplacementState != 2) {
                    while (!this.inputStreamEnded) {
                        try {
                            if (this.nextInputBuffer == null) {
                                this.nextInputBuffer = this.decoder.dequeueInputBuffer();
                                if (this.nextInputBuffer == null) {
                                    return;
                                }
                            }
                            if (this.decoderReplacementState == 1) {
                                this.nextInputBuffer.setFlags(4);
                                this.decoder.queueInputBuffer(this.nextInputBuffer);
                                this.nextInputBuffer = null;
                                this.decoderReplacementState = 2;
                                return;
                            }
                            int result = readSource(this.formatHolder, this.nextInputBuffer, false);
                            if (result == -4) {
                                if (this.nextInputBuffer.isEndOfStream()) {
                                    this.inputStreamEnded = true;
                                } else {
                                    this.nextInputBuffer.subsampleOffsetUs = this.formatHolder.format.subsampleOffsetUs;
                                    this.nextInputBuffer.flip();
                                }
                                this.decoder.queueInputBuffer(this.nextInputBuffer);
                                this.nextInputBuffer = null;
                            } else if (result == -3) {
                                return;
                            }
                        } catch (SubtitleDecoderException e2) {
                            throw ExoPlaybackException.createForRenderer(e2, getIndex());
                        }
                    }
                }
            }
        }
    }

    @Override // com.google.android.exoplayer2.BaseRenderer
    protected void onDisabled() {
        this.streamFormat = null;
        clearOutput();
        releaseDecoder();
        super.onDisabled();
    }

    @Override // com.google.android.exoplayer2.Renderer
    public boolean isEnded() {
        return this.outputStreamEnded;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public boolean isReady() {
        return true;
    }

    private void releaseBuffers() {
        this.nextInputBuffer = null;
        this.nextSubtitleEventIndex = -1;
        if (this.subtitle != null) {
            this.subtitle.release();
            this.subtitle = null;
        }
        if (this.nextSubtitle != null) {
            this.nextSubtitle.release();
            this.nextSubtitle = null;
        }
    }

    private void releaseDecoder() {
        releaseBuffers();
        this.decoder.release();
        this.decoder = null;
        this.decoderReplacementState = 0;
    }

    private void replaceDecoder() {
        releaseDecoder();
        this.decoder = this.decoderFactory.createDecoder(this.streamFormat);
    }

    private long getNextEventTime() {
        if (this.nextSubtitleEventIndex == -1 || this.nextSubtitleEventIndex >= this.subtitle.getEventTimeCount()) {
            return Long.MAX_VALUE;
        }
        return this.subtitle.getEventTime(this.nextSubtitleEventIndex);
    }

    private void updateOutput(List<Cue> cues) {
        if (this.outputHandler != null) {
            this.outputHandler.obtainMessage(0, cues).sendToTarget();
        } else {
            invokeUpdateOutputInternal(cues);
        }
    }

    private void clearOutput() {
        updateOutput(Collections.emptyList());
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                invokeUpdateOutputInternal((List) msg.obj);
                return true;
            default:
                throw new IllegalStateException();
        }
    }

    private void invokeUpdateOutputInternal(List<Cue> cues) {
        this.output.onCues(cues);
    }
}
