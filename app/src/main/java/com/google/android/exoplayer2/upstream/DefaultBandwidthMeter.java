package com.google.android.exoplayer2.upstream;

import android.os.Handler;
import android.os.SystemClock;
import cn.kuwo.autosdk.bean.Constants;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.SlidingPercentile;

/* loaded from: classes.dex */
public final class DefaultBandwidthMeter implements BandwidthMeter, TransferListener<Object> {
    private static final int BYTES_TRANSFERRED_FOR_ESTIMATE = 524288;
    public static final int DEFAULT_MAX_WEIGHT = 2000;
    private static final int ELAPSED_MILLIS_FOR_ESTIMATE = 2000;
    private long bitrateEstimate;
    private final Handler eventHandler;
    private final BandwidthMeter.EventListener eventListener;
    private long sampleBytesTransferred;
    private long sampleStartTimeMs;
    private final SlidingPercentile slidingPercentile;
    private int streamCount;
    private long totalBytesTransferred;
    private long totalElapsedTimeMs;

    public DefaultBandwidthMeter() {
        this(null, null);
    }

    public DefaultBandwidthMeter(Handler eventHandler, BandwidthMeter.EventListener eventListener) {
        this(eventHandler, eventListener, 2000);
    }

    public DefaultBandwidthMeter(Handler eventHandler, BandwidthMeter.EventListener eventListener, int maxWeight) {
        this.eventHandler = eventHandler;
        this.eventListener = eventListener;
        this.slidingPercentile = new SlidingPercentile(maxWeight);
        this.bitrateEstimate = -1L;
    }

    @Override // com.google.android.exoplayer2.upstream.BandwidthMeter
    public synchronized long getBitrateEstimate() {
        return this.bitrateEstimate;
    }

    @Override // com.google.android.exoplayer2.upstream.TransferListener
    public synchronized void onTransferStart(Object source, DataSpec dataSpec) {
        if (this.streamCount == 0) {
            this.sampleStartTimeMs = SystemClock.elapsedRealtime();
        }
        this.streamCount++;
    }

    @Override // com.google.android.exoplayer2.upstream.TransferListener
    public synchronized void onBytesTransferred(Object source, int bytes) {
        this.sampleBytesTransferred += bytes;
    }

    @Override // com.google.android.exoplayer2.upstream.TransferListener
    public synchronized void onTransferEnd(Object source) {
        Assertions.checkState(this.streamCount > 0);
        long nowMs = SystemClock.elapsedRealtime();
        int sampleElapsedTimeMs = (int) (nowMs - this.sampleStartTimeMs);
        this.totalElapsedTimeMs += sampleElapsedTimeMs;
        this.totalBytesTransferred += this.sampleBytesTransferred;
        if (sampleElapsedTimeMs > 0) {
            float bitsPerSecond = (this.sampleBytesTransferred * Constants.SEARCH_TIMEOUT) / sampleElapsedTimeMs;
            this.slidingPercentile.addSample((int) Math.sqrt(this.sampleBytesTransferred), bitsPerSecond);
            if (this.totalElapsedTimeMs >= 2000 || this.totalBytesTransferred >= 524288) {
                float bitrateEstimateFloat = this.slidingPercentile.getPercentile(0.5f);
                this.bitrateEstimate = Float.isNaN(bitrateEstimateFloat) ? -1L : (long) bitrateEstimateFloat;
            }
        }
        notifyBandwidthSample(sampleElapsedTimeMs, this.sampleBytesTransferred, this.bitrateEstimate);
        int i = this.streamCount - 1;
        this.streamCount = i;
        if (i > 0) {
            this.sampleStartTimeMs = nowMs;
        }
        this.sampleBytesTransferred = 0L;
    }

    private void notifyBandwidthSample(final int elapsedMs, final long bytes, final long bitrate) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() { // from class: com.google.android.exoplayer2.upstream.DefaultBandwidthMeter.1
                @Override // java.lang.Runnable
                public void run() {
                    DefaultBandwidthMeter.this.eventListener.onBandwidthSample(elapsedMs, bytes, bitrate);
                }
            });
        }
    }
}
