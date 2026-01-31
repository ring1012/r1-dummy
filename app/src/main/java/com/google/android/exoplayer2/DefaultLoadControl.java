package com.google.android.exoplayer2;

import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.util.PriorityTaskManager;
import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
public final class DefaultLoadControl implements LoadControl {
    private static final int ABOVE_HIGH_WATERMARK = 0;
    private static final int BELOW_LOW_WATERMARK = 2;
    private static final int BETWEEN_WATERMARKS = 1;
    public static final int DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = 5000;
    public static final int DEFAULT_BUFFER_FOR_PLAYBACK_MS = 2500;
    public static final int DEFAULT_MAX_BUFFER_MS = 30000;
    public static final int DEFAULT_MIN_BUFFER_MS = 15000;
    private final DefaultAllocator allocator;
    private final long bufferForPlaybackAfterRebufferUs;
    private final long bufferForPlaybackUs;
    private boolean isBuffering;
    private final long maxBufferUs;
    private final long minBufferUs;
    private final PriorityTaskManager priorityTaskManager;
    private int targetBufferSize;

    public DefaultLoadControl() {
        this(new DefaultAllocator(true, 65536));
    }

    public DefaultLoadControl(DefaultAllocator allocator) {
        this(allocator, 15000, 30000, 2500L, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
    }

    public DefaultLoadControl(DefaultAllocator allocator, int minBufferMs, int maxBufferMs, long bufferForPlaybackMs, long bufferForPlaybackAfterRebufferMs) {
        this(allocator, minBufferMs, maxBufferMs, bufferForPlaybackMs, bufferForPlaybackAfterRebufferMs, null);
    }

    public DefaultLoadControl(DefaultAllocator allocator, int minBufferMs, int maxBufferMs, long bufferForPlaybackMs, long bufferForPlaybackAfterRebufferMs, PriorityTaskManager priorityTaskManager) {
        this.allocator = allocator;
        this.minBufferUs = minBufferMs * 1000;
        this.maxBufferUs = maxBufferMs * 1000;
        this.bufferForPlaybackUs = bufferForPlaybackMs * 1000;
        this.bufferForPlaybackAfterRebufferUs = bufferForPlaybackAfterRebufferMs * 1000;
        this.priorityTaskManager = priorityTaskManager;
    }

    @Override // com.google.android.exoplayer2.LoadControl
    public void onPrepared() {
        reset(false);
    }

    @Override // com.google.android.exoplayer2.LoadControl
    public void onTracksSelected(Renderer[] renderers, TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        this.targetBufferSize = 0;
        for (int i = 0; i < renderers.length; i++) {
            if (trackSelections.get(i) != null) {
                this.targetBufferSize += Util.getDefaultBufferSize(renderers[i].getTrackType());
            }
        }
        this.allocator.setTargetBufferSize(this.targetBufferSize);
    }

    @Override // com.google.android.exoplayer2.LoadControl
    public void onStopped() {
        reset(true);
    }

    @Override // com.google.android.exoplayer2.LoadControl
    public void onReleased() {
        reset(true);
    }

    @Override // com.google.android.exoplayer2.LoadControl
    public Allocator getAllocator() {
        return this.allocator;
    }

    @Override // com.google.android.exoplayer2.LoadControl
    public boolean shouldStartPlayback(long bufferedDurationUs, boolean rebuffering) {
        long minBufferDurationUs = rebuffering ? this.bufferForPlaybackAfterRebufferUs : this.bufferForPlaybackUs;
        return minBufferDurationUs <= 0 || bufferedDurationUs >= minBufferDurationUs;
    }

    @Override // com.google.android.exoplayer2.LoadControl
    public boolean shouldContinueLoading(long bufferedDurationUs) {
        boolean z = true;
        int bufferTimeState = getBufferTimeState(bufferedDurationUs);
        boolean targetBufferSizeReached = this.allocator.getTotalBytesAllocated() >= this.targetBufferSize;
        boolean wasBuffering = this.isBuffering;
        if (bufferTimeState != 2 && (bufferTimeState != 1 || !this.isBuffering || targetBufferSizeReached)) {
            z = false;
        }
        this.isBuffering = z;
        if (this.priorityTaskManager != null && this.isBuffering != wasBuffering) {
            if (this.isBuffering) {
                this.priorityTaskManager.add(0);
            } else {
                this.priorityTaskManager.remove(0);
            }
        }
        return this.isBuffering;
    }

    private int getBufferTimeState(long bufferedDurationUs) {
        if (bufferedDurationUs > this.maxBufferUs) {
            return 0;
        }
        return bufferedDurationUs < this.minBufferUs ? 2 : 1;
    }

    private void reset(boolean resetAllocator) {
        this.targetBufferSize = 0;
        if (this.priorityTaskManager != null && this.isBuffering) {
            this.priorityTaskManager.remove(0);
        }
        this.isBuffering = false;
        if (resetAllocator) {
            this.allocator.reset();
        }
    }
}
