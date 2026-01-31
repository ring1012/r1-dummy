package com.google.android.exoplayer2.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.Formatter;
import java.util.Locale;

/* loaded from: classes.dex */
public class DefaultTimeBar extends View implements TimeBar {
    private static final int DEFAULT_AD_MARKER_COLOR = -1291845888;
    private static final int DEFAULT_AD_MARKER_WIDTH = 4;
    private static final int DEFAULT_BAR_HEIGHT = 4;
    private static final int DEFAULT_BUFFERED_COLOR = -855638017;
    private static final int DEFAULT_INCREMENT_COUNT = 20;
    private static final int DEFAULT_PLAYED_COLOR = 872415231;
    private static final int DEFAULT_SCRUBBER_DISABLED_SIZE = 0;
    private static final int DEFAULT_SCRUBBER_DRAGGED_SIZE = 16;
    private static final int DEFAULT_SCRUBBER_ENABLED_SIZE = 12;
    private static final int DEFAULT_TOUCH_TARGET_HEIGHT = 26;
    private static final int FINE_SCRUB_RATIO = 3;
    private static final int FINE_SCRUB_Y_THRESHOLD = -50;
    private static final int OPAQUE_COLOR = -16777216;
    private static final long STOP_SCRUBBING_TIMEOUT_MS = 1000;
    private int adBreakCount;
    private long[] adBreakTimesMs;
    private final Paint adMarkerPaint;
    private final int adMarkerWidth;
    private final int barHeight;
    private final Rect bufferedBar;
    private final Paint bufferedPaint;
    private long bufferedPosition;
    private long duration;
    private final int fineScrubYThreshold;
    private final StringBuilder formatBuilder;
    private final Formatter formatter;
    private int keyCountIncrement;
    private long keyTimeIncrement;
    private int lastCoarseScrubXPosition;
    private TimeBar.OnScrubListener listener;
    private int[] locationOnScreen;
    private long position;
    private final Rect progressBar;
    private final Paint progressPaint;
    private long scrubPosition;
    private final Rect scrubberBar;
    private final int scrubberDisabledSize;
    private final int scrubberDraggedSize;
    private final int scrubberEnabledSize;
    private final int scrubberPadding;
    private final Paint scrubberPaint;
    private int scrubberSize;
    private boolean scrubbing;
    private final Rect seekBounds;
    private final Runnable stopScrubbingRunnable;
    private Point touchPosition;
    private final int touchTargetHeight;

    public DefaultTimeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.seekBounds = new Rect();
        this.progressBar = new Rect();
        this.bufferedBar = new Rect();
        this.scrubberBar = new Rect();
        this.progressPaint = new Paint();
        this.bufferedPaint = new Paint();
        this.scrubberPaint = new Paint();
        this.adMarkerPaint = new Paint();
        Resources res = context.getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        this.fineScrubYThreshold = dpToPx(displayMetrics, FINE_SCRUB_Y_THRESHOLD);
        int defaultBarHeight = dpToPx(displayMetrics, 4);
        int defaultTouchTargetHeight = dpToPx(displayMetrics, 26);
        int defaultAdMarkerWidth = dpToPx(displayMetrics, 4);
        int defaultScrubberEnabledSize = dpToPx(displayMetrics, 12);
        int defaultScrubberDisabledSize = dpToPx(displayMetrics, 0);
        int defaultScrubberDraggedSize = dpToPx(displayMetrics, 16);
        if (attrs != null) {
            TypedArray a2 = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DefaultTimeBar, 0, 0);
            try {
                this.barHeight = a2.getDimensionPixelSize(R.styleable.DefaultTimeBar_bar_height, defaultBarHeight);
                this.touchTargetHeight = a2.getDimensionPixelSize(R.styleable.DefaultTimeBar_touch_target_height, defaultTouchTargetHeight);
                this.adMarkerWidth = a2.getDimensionPixelSize(R.styleable.DefaultTimeBar_ad_marker_width, defaultAdMarkerWidth);
                this.scrubberEnabledSize = a2.getDimensionPixelSize(R.styleable.DefaultTimeBar_scrubber_enabled_size, defaultScrubberEnabledSize);
                this.scrubberDisabledSize = a2.getDimensionPixelSize(R.styleable.DefaultTimeBar_scrubber_disabled_size, defaultScrubberDisabledSize);
                this.scrubberDraggedSize = a2.getDimensionPixelSize(R.styleable.DefaultTimeBar_scrubber_dragged_size, defaultScrubberDraggedSize);
                int playedColor = a2.getInt(R.styleable.DefaultTimeBar_played_color, DEFAULT_PLAYED_COLOR);
                int bufferedColor = a2.getInt(R.styleable.DefaultTimeBar_buffered_color, DEFAULT_BUFFERED_COLOR);
                int adMarkerColor = a2.getInt(R.styleable.DefaultTimeBar_ad_marker_color, DEFAULT_AD_MARKER_COLOR);
                this.progressPaint.setColor(playedColor);
                this.scrubberPaint.setColor((-16777216) | playedColor);
                this.bufferedPaint.setColor(bufferedColor);
                this.adMarkerPaint.setColor(adMarkerColor);
            } finally {
                a2.recycle();
            }
        } else {
            this.barHeight = defaultBarHeight;
            this.touchTargetHeight = defaultTouchTargetHeight;
            this.adMarkerWidth = defaultAdMarkerWidth;
            this.scrubberEnabledSize = defaultScrubberEnabledSize;
            this.scrubberDisabledSize = defaultScrubberDisabledSize;
            this.scrubberDraggedSize = defaultScrubberDraggedSize;
            this.scrubberPaint.setColor(-1);
            this.progressPaint.setColor(DEFAULT_PLAYED_COLOR);
            this.bufferedPaint.setColor(DEFAULT_BUFFERED_COLOR);
            this.adMarkerPaint.setColor(DEFAULT_AD_MARKER_COLOR);
        }
        this.formatBuilder = new StringBuilder();
        this.formatter = new Formatter(this.formatBuilder, Locale.getDefault());
        this.stopScrubbingRunnable = new Runnable() { // from class: com.google.android.exoplayer2.ui.DefaultTimeBar.1
            @Override // java.lang.Runnable
            public void run() {
                DefaultTimeBar.this.stopScrubbing(false);
            }
        };
        this.scrubberSize = this.scrubberEnabledSize;
        this.scrubberPadding = (Math.max(this.scrubberDisabledSize, Math.max(this.scrubberEnabledSize, this.scrubberDraggedSize)) + 1) / 2;
        this.duration = C.TIME_UNSET;
        this.keyTimeIncrement = C.TIME_UNSET;
        this.keyCountIncrement = 20;
        setFocusable(true);
        if (Util.SDK_INT >= 16) {
            maybeSetImportantForAccessibilityV16();
        }
    }

    @Override // com.google.android.exoplayer2.ui.TimeBar
    public void setListener(TimeBar.OnScrubListener listener) {
        this.listener = listener;
    }

    @Override // com.google.android.exoplayer2.ui.TimeBar
    public void setKeyTimeIncrement(long time) {
        Assertions.checkArgument(time > 0);
        this.keyCountIncrement = -1;
        this.keyTimeIncrement = time;
    }

    @Override // com.google.android.exoplayer2.ui.TimeBar
    public void setKeyCountIncrement(int count) {
        Assertions.checkArgument(count > 0);
        this.keyCountIncrement = count;
        this.keyTimeIncrement = C.TIME_UNSET;
    }

    @Override // com.google.android.exoplayer2.ui.TimeBar
    public void setPosition(long position) {
        this.position = position;
        setContentDescription(getProgressText());
    }

    @Override // com.google.android.exoplayer2.ui.TimeBar
    public void setBufferedPosition(long bufferedPosition) {
        this.bufferedPosition = bufferedPosition;
    }

    @Override // com.google.android.exoplayer2.ui.TimeBar
    public void setDuration(long duration) {
        this.duration = duration;
        if (this.scrubbing && duration == C.TIME_UNSET) {
            stopScrubbing(true);
        } else {
            updateScrubberState();
        }
    }

    @Override // com.google.android.exoplayer2.ui.TimeBar
    public void setAdBreakTimesMs(@Nullable long[] adBreakTimesMs, int adBreakCount) {
        Assertions.checkArgument(adBreakCount == 0 || adBreakTimesMs != null);
        this.adBreakCount = adBreakCount;
        this.adBreakTimesMs = adBreakTimesMs;
    }

    @Override // android.view.View, com.google.android.exoplayer2.ui.TimeBar
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        updateScrubberState();
        if (this.scrubbing && !enabled) {
            stopScrubbing(true);
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        canvas.save();
        drawTimeBar(canvas);
        drawPlayhead(canvas);
        canvas.restore();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || this.duration <= 0) {
            return false;
        }
        Point touchPosition = resolveRelativeTouchPosition(event);
        int x = touchPosition.x;
        int y = touchPosition.y;
        switch (event.getAction()) {
            case 0:
                if (isInSeekBar(x, y)) {
                    startScrubbing();
                    positionScrubber(x);
                    this.scrubPosition = getScrubberPosition();
                    update();
                    invalidate();
                    return true;
                }
                break;
            case 1:
            case 3:
                if (this.scrubbing) {
                    stopScrubbing(event.getAction() == 3);
                    return true;
                }
                break;
            case 2:
                if (this.scrubbing) {
                    if (y < this.fineScrubYThreshold) {
                        int relativeX = x - this.lastCoarseScrubXPosition;
                        positionScrubber(this.lastCoarseScrubXPosition + (relativeX / 3));
                    } else {
                        this.lastCoarseScrubXPosition = x;
                        positionScrubber(x);
                    }
                    this.scrubPosition = getScrubberPosition();
                    if (this.listener != null) {
                        this.listener.onScrubMove(this, this.scrubPosition);
                    }
                    update();
                    invalidate();
                    return true;
                }
                break;
        }
        return false;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isEnabled()) {
            long positionIncrement = getPositionIncrement();
            switch (keyCode) {
                case 21:
                    positionIncrement = -positionIncrement;
                    break;
                case 22:
                    break;
                case 23:
                case 66:
                    if (this.scrubbing) {
                        removeCallbacks(this.stopScrubbingRunnable);
                        this.stopScrubbingRunnable.run();
                        return true;
                    }
                    break;
            }
            if (scrubIncrementally(positionIncrement)) {
                removeCallbacks(this.stopScrubbingRunnable);
                postDelayed(this.stopScrubbingRunnable, STOP_SCRUBBING_TIMEOUT_MS);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override // android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = right - left;
        int height = bottom - top;
        int barY = height - this.touchTargetHeight;
        int seekLeft = getPaddingLeft();
        int seekRight = width - getPaddingRight();
        int progressY = barY + ((this.touchTargetHeight - this.barHeight) / 2);
        this.seekBounds.set(seekLeft, barY, seekRight, this.touchTargetHeight + barY);
        this.progressBar.set(this.seekBounds.left + this.scrubberPadding, progressY, this.seekBounds.right - this.scrubberPadding, this.barHeight + progressY);
        update();
    }

    @Override // android.view.View
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
    }

    @Override // android.view.View
    @TargetApi(14)
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        if (event.getEventType() == 4) {
            event.getText().add(getProgressText());
        }
        event.setClassName(DefaultTimeBar.class.getName());
    }

    @Override // android.view.View
    @TargetApi(21)
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(DefaultTimeBar.class.getCanonicalName());
        info.setContentDescription(getProgressText());
        if (this.duration > 0) {
            if (Util.SDK_INT >= 21) {
                info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
                info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
            } else if (Util.SDK_INT >= 16) {
                info.addAction(4096);
                info.addAction(8192);
            }
        }
    }

    @Override // android.view.View
    @TargetApi(16)
    public boolean performAccessibilityAction(int action, Bundle args) {
        if (super.performAccessibilityAction(action, args)) {
            return true;
        }
        if (this.duration <= 0) {
            return false;
        }
        if (action == 8192) {
            if (scrubIncrementally(-getPositionIncrement())) {
                stopScrubbing(false);
            }
        } else {
            if (action != 4096) {
                return false;
            }
            if (scrubIncrementally(getPositionIncrement())) {
                stopScrubbing(false);
            }
        }
        sendAccessibilityEvent(4);
        return true;
    }

    @TargetApi(16)
    private void maybeSetImportantForAccessibilityV16() {
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
    }

    private void startScrubbing() {
        this.scrubbing = true;
        updateScrubberState();
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        if (this.listener != null) {
            this.listener.onScrubStart(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopScrubbing(boolean canceled) {
        this.scrubbing = false;
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(false);
        }
        updateScrubberState();
        invalidate();
        if (this.listener != null) {
            this.listener.onScrubStop(this, getScrubberPosition(), canceled);
        }
    }

    private void updateScrubberState() {
        int i;
        if (this.scrubbing) {
            i = this.scrubberDraggedSize;
        } else {
            i = (!isEnabled() || this.duration < 0) ? this.scrubberDisabledSize : this.scrubberEnabledSize;
        }
        this.scrubberSize = i;
    }

    private void update() {
        this.bufferedBar.set(this.progressBar);
        this.scrubberBar.set(this.progressBar);
        long newScrubberTime = this.scrubbing ? this.scrubPosition : this.position;
        if (this.duration > 0) {
            int bufferedPixelWidth = (int) ((this.progressBar.width() * this.bufferedPosition) / this.duration);
            this.bufferedBar.right = this.progressBar.left + bufferedPixelWidth;
            int scrubberPixelPosition = (int) ((this.progressBar.width() * newScrubberTime) / this.duration);
            this.scrubberBar.right = this.progressBar.left + scrubberPixelPosition;
        } else {
            this.bufferedBar.right = this.progressBar.left;
            this.scrubberBar.right = this.progressBar.left;
        }
        invalidate(this.seekBounds);
    }

    private void positionScrubber(float xPosition) {
        this.scrubberBar.right = Util.constrainValue((int) xPosition, this.progressBar.left, this.progressBar.right);
    }

    private Point resolveRelativeTouchPosition(MotionEvent motionEvent) {
        if (this.locationOnScreen == null) {
            this.locationOnScreen = new int[2];
            this.touchPosition = new Point();
        }
        getLocationOnScreen(this.locationOnScreen);
        this.touchPosition.set(((int) motionEvent.getRawX()) - this.locationOnScreen[0], ((int) motionEvent.getRawY()) - this.locationOnScreen[1]);
        return this.touchPosition;
    }

    private long getScrubberPosition() {
        if (this.progressBar.width() <= 0 || this.duration == C.TIME_UNSET) {
            return 0L;
        }
        return (this.scrubberBar.width() * this.duration) / this.progressBar.width();
    }

    private boolean isInSeekBar(float x, float y) {
        return this.seekBounds.contains((int) x, (int) y);
    }

    private void drawTimeBar(Canvas canvas) {
        int progressBarHeight = this.progressBar.height();
        int barTop = this.progressBar.centerY() - (progressBarHeight / 2);
        int barBottom = barTop + progressBarHeight;
        if (this.duration <= 0) {
            canvas.drawRect(this.progressBar.left, barTop, this.progressBar.right, barBottom, this.progressPaint);
            return;
        }
        int bufferedLeft = this.bufferedBar.left;
        int bufferedRight = this.bufferedBar.right;
        int progressLeft = Math.max(Math.max(this.progressBar.left, bufferedRight), this.scrubberBar.right);
        if (progressLeft < this.progressBar.right) {
            canvas.drawRect(progressLeft, barTop, this.progressBar.right, barBottom, this.progressPaint);
        }
        int bufferedLeft2 = Math.max(bufferedLeft, this.scrubberBar.right);
        if (bufferedRight > bufferedLeft2) {
            canvas.drawRect(bufferedLeft2, barTop, bufferedRight, barBottom, this.bufferedPaint);
        }
        if (this.scrubberBar.width() > 0) {
            canvas.drawRect(this.scrubberBar.left, barTop, this.scrubberBar.right, barBottom, this.scrubberPaint);
        }
        int adMarkerOffset = this.adMarkerWidth / 2;
        for (int i = 0; i < this.adBreakCount; i++) {
            long adBreakTimeMs = Util.constrainValue(this.adBreakTimesMs[i], 0L, this.duration);
            int markerPositionOffset = ((int) ((this.progressBar.width() * adBreakTimeMs) / this.duration)) - adMarkerOffset;
            int markerLeft = this.progressBar.left + Math.min(this.progressBar.width() - this.adMarkerWidth, Math.max(0, markerPositionOffset));
            canvas.drawRect(markerLeft, barTop, this.adMarkerWidth + markerLeft, barBottom, this.adMarkerPaint);
        }
    }

    private void drawPlayhead(Canvas canvas) {
        if (this.duration > 0) {
            int playheadRadius = this.scrubberSize / 2;
            int playheadCenter = Util.constrainValue(this.scrubberBar.right, this.scrubberBar.left, this.progressBar.right);
            canvas.drawCircle(playheadCenter, this.scrubberBar.centerY(), playheadRadius, this.scrubberPaint);
        }
    }

    private String getProgressText() {
        return Util.getStringForTime(this.formatBuilder, this.formatter, this.position);
    }

    private long getPositionIncrement() {
        if (this.keyTimeIncrement != C.TIME_UNSET) {
            return this.keyTimeIncrement;
        }
        if (this.duration == C.TIME_UNSET) {
            return 0L;
        }
        return this.duration / this.keyCountIncrement;
    }

    private boolean scrubIncrementally(long positionChange) {
        if (this.duration <= 0) {
            return false;
        }
        long scrubberPosition = getScrubberPosition();
        this.scrubPosition = Util.constrainValue(scrubberPosition + positionChange, 0L, this.duration);
        if (this.scrubPosition == scrubberPosition) {
            return false;
        }
        if (!this.scrubbing) {
            startScrubbing();
        }
        if (this.listener != null) {
            this.listener.onScrubMove(this, this.scrubPosition);
        }
        update();
        return true;
    }

    private static int dpToPx(DisplayMetrics displayMetrics, int dps) {
        return (int) ((dps * displayMetrics.density) + 0.5f);
    }
}
