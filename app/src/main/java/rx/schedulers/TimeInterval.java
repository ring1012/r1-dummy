package rx.schedulers;

/* loaded from: classes.dex */
public class TimeInterval<T> {
    private final long intervalInMilliseconds;
    private final T value;

    public TimeInterval(long intervalInMilliseconds, T value) {
        this.value = value;
        this.intervalInMilliseconds = intervalInMilliseconds;
    }

    public long getIntervalInMilliseconds() {
        return this.intervalInMilliseconds;
    }

    public T getValue() {
        return this.value;
    }

    public int hashCode() {
        int result = ((int) (this.intervalInMilliseconds ^ (this.intervalInMilliseconds >>> 32))) + 31;
        return (result * 31) + (this.value == null ? 0 : this.value.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            TimeInterval<?> other = (TimeInterval) obj;
            if (this.intervalInMilliseconds != other.intervalInMilliseconds) {
                return false;
            }
            return this.value == null ? other.value == null : this.value.equals(other.value);
        }
        return false;
    }

    public String toString() {
        return "TimeInterval [intervalInMilliseconds=" + this.intervalInMilliseconds + ", value=" + this.value + "]";
    }
}
