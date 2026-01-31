package okhttp3.internal.http2;

import java.util.Arrays;

/* loaded from: classes.dex */
public final class Settings {
    static final int COUNT = 10;
    static final int DEFAULT_INITIAL_WINDOW_SIZE = 65535;
    static final int ENABLE_PUSH = 2;
    static final int HEADER_TABLE_SIZE = 1;
    static final int INITIAL_WINDOW_SIZE = 7;
    static final int MAX_CONCURRENT_STREAMS = 4;
    static final int MAX_FRAME_SIZE = 5;
    static final int MAX_HEADER_LIST_SIZE = 6;
    private int set;
    private final int[] values = new int[10];

    void clear() {
        this.set = 0;
        Arrays.fill(this.values, 0);
    }

    Settings set(int id, int value) {
        if (id < this.values.length) {
            int bit = 1 << id;
            this.set |= bit;
            this.values[id] = value;
        }
        return this;
    }

    boolean isSet(int id) {
        int bit = 1 << id;
        return (this.set & bit) != 0;
    }

    int get(int id) {
        return this.values[id];
    }

    int size() {
        return Integer.bitCount(this.set);
    }

    int getHeaderTableSize() {
        if ((this.set & 2) != 0) {
            return this.values[1];
        }
        return -1;
    }

    boolean getEnablePush(boolean defaultValue) {
        return ((this.set & 4) != 0 ? this.values[2] : defaultValue ? 1 : 0) == 1;
    }

    int getMaxConcurrentStreams(int defaultValue) {
        if ((this.set & 16) == 0) {
            return defaultValue;
        }
        int defaultValue2 = this.values[4];
        return defaultValue2;
    }

    int getMaxFrameSize(int defaultValue) {
        if ((this.set & 32) == 0) {
            return defaultValue;
        }
        int defaultValue2 = this.values[5];
        return defaultValue2;
    }

    int getMaxHeaderListSize(int defaultValue) {
        if ((this.set & 64) == 0) {
            return defaultValue;
        }
        int defaultValue2 = this.values[6];
        return defaultValue2;
    }

    int getInitialWindowSize() {
        if ((this.set & 128) != 0) {
            return this.values[7];
        }
        return 65535;
    }

    void merge(Settings other) {
        for (int i = 0; i < 10; i++) {
            if (other.isSet(i)) {
                set(i, other.get(i));
            }
        }
    }
}
