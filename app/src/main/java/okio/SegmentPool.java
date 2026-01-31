package okio;

import javax.annotation.Nullable;

/* loaded from: classes.dex */
final class SegmentPool {
    static final long MAX_SIZE = 65536;
    static long byteCount;

    @Nullable
    static Segment next;

    private SegmentPool() {
    }

    static Segment take() {
        synchronized (SegmentPool.class) {
            if (next != null) {
                Segment result = next;
                next = result.next;
                result.next = null;
                byteCount -= 8192;
                return result;
            }
            return new Segment();
        }
    }

    static void recycle(Segment segment) {
        if (segment.next != null || segment.prev != null) {
            throw new IllegalArgumentException();
        }
        if (!segment.shared) {
            synchronized (SegmentPool.class) {
                if (byteCount + 8192 <= MAX_SIZE) {
                    byteCount += 8192;
                    segment.next = next;
                    segment.limit = 0;
                    segment.pos = 0;
                    next = segment;
                }
            }
        }
    }
}
