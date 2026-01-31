package butterknife;

import java.util.AbstractList;
import java.util.RandomAccess;

/* loaded from: classes.dex */
final class ImmutableList<T> extends AbstractList<T> implements RandomAccess {
    private final T[] views;

    ImmutableList(T[] views) {
        this.views = views;
    }

    @Override // java.util.AbstractList, java.util.List
    public T get(int index) {
        return this.views[index];
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.views.length;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean contains(Object o) {
        for (T view : this.views) {
            if (view == o) {
                return true;
            }
        }
        return false;
    }
}
