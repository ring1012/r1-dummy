package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.FileUtils;

/* loaded from: classes.dex */
public class SizeFileComparator extends AbstractFileComparator implements Serializable {
    public static final Comparator<File> SIZE_COMPARATOR = new SizeFileComparator();
    public static final Comparator<File> SIZE_REVERSE = new ReverseComparator(SIZE_COMPARATOR);
    public static final Comparator<File> SIZE_SUMDIR_COMPARATOR = new SizeFileComparator(true);
    public static final Comparator<File> SIZE_SUMDIR_REVERSE = new ReverseComparator(SIZE_SUMDIR_COMPARATOR);
    private final boolean sumDirectoryContents;

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ List sort(List x0) {
        return super.sort((List<File>) x0);
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ File[] sort(File[] x0) {
        return super.sort(x0);
    }

    public SizeFileComparator() {
        this.sumDirectoryContents = false;
    }

    public SizeFileComparator(boolean sumDirectoryContents) {
        this.sumDirectoryContents = sumDirectoryContents;
    }

    @Override // java.util.Comparator
    public int compare(File file1, File file2) {
        long size1;
        long size2;
        if (file1.isDirectory()) {
            size1 = (this.sumDirectoryContents && file1.exists()) ? FileUtils.sizeOfDirectory(file1) : 0L;
        } else {
            size1 = file1.length();
        }
        if (file2.isDirectory()) {
            size2 = (this.sumDirectoryContents && file2.exists()) ? FileUtils.sizeOfDirectory(file2) : 0L;
        } else {
            size2 = file2.length();
        }
        long result = size1 - size2;
        if (result < 0) {
            return -1;
        }
        if (result > 0) {
            return 1;
        }
        return 0;
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public String toString() {
        return super.toString() + "[sumDirectoryContents=" + this.sumDirectoryContents + "]";
    }
}
