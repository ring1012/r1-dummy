package android.support.v4.util;

import cn.yunzhisheng.common.PinyinConverter;

/* loaded from: classes.dex */
public class Pair<F, S> {
    public final F first;
    public final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> p = (Pair) o;
        return objectsEqual(p.first, this.first) && objectsEqual(p.second, this.second);
    }

    private static boolean objectsEqual(Object a2, Object b) {
        return a2 == b || (a2 != null && a2.equals(b));
    }

    public int hashCode() {
        return (this.first == null ? 0 : this.first.hashCode()) ^ (this.second != null ? this.second.hashCode() : 0);
    }

    public String toString() {
        return "Pair{" + String.valueOf(this.first) + PinyinConverter.PINYIN_SEPARATOR + String.valueOf(this.second) + "}";
    }

    public static <A, B> Pair<A, B> create(A a2, B b) {
        return new Pair<>(a2, b);
    }
}
