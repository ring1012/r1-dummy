package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes.dex */
public class CompositeFileComparator extends AbstractFileComparator implements Serializable {
    private static final Comparator<?>[] NO_COMPARATORS = new Comparator[0];
    private final Comparator<File>[] delegates;

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ List sort(List x0) {
        return super.sort((List<File>) x0);
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ File[] sort(File[] x0) {
        return super.sort(x0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CompositeFileComparator(Comparator<File>... delegates) {
        if (delegates == null) {
            this.delegates = NO_COMPARATORS;
        } else {
            this.delegates = new Comparator[delegates.length];
            System.arraycopy(delegates, 0, this.delegates, 0, delegates.length);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CompositeFileComparator(Iterable<Comparator<File>> delegates) {
        if (delegates == null) {
            this.delegates = NO_COMPARATORS;
            return;
        }
        List<Comparator<File>> list = new ArrayList<>();
        for (Comparator<File> comparator : delegates) {
            list.add(comparator);
        }
        this.delegates = (Comparator[]) list.toArray(new Comparator[list.size()]);
    }

    @Override // java.util.Comparator
    public int compare(File file1, File file2) {
        int result = 0;
        Comparator<File>[] arr$ = this.delegates;
        for (Comparator<File> delegate : arr$) {
            result = delegate.compare(file1, file2);
            if (result != 0) {
                break;
            }
        }
        return result;
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append('{');
        for (int i = 0; i < this.delegates.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(this.delegates[i]);
        }
        builder.append('}');
        return builder.toString();
    }
}
