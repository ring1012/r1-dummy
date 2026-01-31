package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

/* loaded from: classes.dex */
class ReverseComparator extends AbstractFileComparator implements Serializable {
    private final Comparator<File> delegate;

    public ReverseComparator(Comparator<File> delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Delegate comparator is missing");
        }
        this.delegate = delegate;
    }

    @Override // java.util.Comparator
    public int compare(File file1, File file2) {
        return this.delegate.compare(file2, file1);
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public String toString() {
        return super.toString() + "[" + this.delegate.toString() + "]";
    }
}
