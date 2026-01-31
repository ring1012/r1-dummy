package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.IOCase;

/* loaded from: classes.dex */
public class PathFileComparator extends AbstractFileComparator implements Serializable {
    private final IOCase caseSensitivity;
    public static final Comparator<File> PATH_COMPARATOR = new PathFileComparator();
    public static final Comparator<File> PATH_REVERSE = new ReverseComparator(PATH_COMPARATOR);
    public static final Comparator<File> PATH_INSENSITIVE_COMPARATOR = new PathFileComparator(IOCase.INSENSITIVE);
    public static final Comparator<File> PATH_INSENSITIVE_REVERSE = new ReverseComparator(PATH_INSENSITIVE_COMPARATOR);
    public static final Comparator<File> PATH_SYSTEM_COMPARATOR = new PathFileComparator(IOCase.SYSTEM);
    public static final Comparator<File> PATH_SYSTEM_REVERSE = new ReverseComparator(PATH_SYSTEM_COMPARATOR);

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ List sort(List x0) {
        return super.sort((List<File>) x0);
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ File[] sort(File[] x0) {
        return super.sort(x0);
    }

    public PathFileComparator() {
        this.caseSensitivity = IOCase.SENSITIVE;
    }

    public PathFileComparator(IOCase caseSensitivity) {
        this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
    }

    @Override // java.util.Comparator
    public int compare(File file1, File file2) {
        return this.caseSensitivity.checkCompareTo(file1.getPath(), file2.getPath());
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public String toString() {
        return super.toString() + "[caseSensitivity=" + this.caseSensitivity + "]";
    }
}
