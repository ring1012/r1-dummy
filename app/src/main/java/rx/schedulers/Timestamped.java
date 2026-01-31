package rx.schedulers;

/* loaded from: classes.dex */
public final class Timestamped<T> {
    private final long timestampMillis;
    private final T value;

    public Timestamped(long timestampMillis, T value) {
        this.value = value;
        this.timestampMillis = timestampMillis;
    }

    public long getTimestampMillis() {
        return this.timestampMillis;
    }

    public T getValue() {
        return this.value;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof Timestamped)) {
            Timestamped<?> other = (Timestamped) obj;
            if (this.timestampMillis != other.timestampMillis) {
                return false;
            }
            return this.value == null ? other.value == null : this.value.equals(other.value);
        }
        return false;
    }

    public int hashCode() {
        int result = ((int) (this.timestampMillis ^ (this.timestampMillis >>> 32))) + 31;
        return (result * 31) + (this.value == null ? 0 : this.value.hashCode());
    }

    public String toString() {
        return String.format("Timestamped(timestampMillis = %d, value = %s)", Long.valueOf(this.timestampMillis), this.value.toString());
    }
}
