package com.google.android.exoplayer2.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes.dex */
public interface AudioProcessor {
    public static final ByteBuffer EMPTY_BUFFER = ByteBuffer.allocateDirect(0).order(ByteOrder.nativeOrder());

    boolean configure(int i, int i2, int i3) throws UnhandledFormatException;

    void flush();

    ByteBuffer getOutput();

    int getOutputChannelCount();

    int getOutputEncoding();

    boolean isActive();

    boolean isEnded();

    void queueEndOfStream();

    void queueInput(ByteBuffer byteBuffer);

    void reset();

    public static final class UnhandledFormatException extends Exception {
        public UnhandledFormatException(int sampleRateHz, int channelCount, int encoding) {
            super("Unhandled format: " + sampleRateHz + " Hz, " + channelCount + " channels in encoding " + encoding);
        }
    }
}
