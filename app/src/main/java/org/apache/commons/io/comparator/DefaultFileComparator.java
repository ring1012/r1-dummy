package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes.dex */
public class DefaultFileComparator extends AbstractFileComparator implements Serializable {
    public static final Comparator<File> DEFAULT_COMPARATOR = new DefaultFileComparator();
    public static final Comparator<File> DEFAULT_REVERSE = new ReverseComparator(DEFAULT_COMPARATOR);

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ List sort(List x0) {
        return super.sort((List<File>) x0);
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ File[] sort(File[] x0) {
        return super.sort(x0);
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // java.util.Comparator
    public int compare(File file1, File file2) {
        return file1.compareTo(file2);
    }
}
