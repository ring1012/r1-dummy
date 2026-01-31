package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;

/* loaded from: classes.dex */
public class ExtensionFileComparator extends AbstractFileComparator implements Serializable {
    private final IOCase caseSensitivity;
    public static final Comparator<File> EXTENSION_COMPARATOR = new ExtensionFileComparator();
    public static final Comparator<File> EXTENSION_REVERSE = new ReverseComparator(EXTENSION_COMPARATOR);
    public static final Comparator<File> EXTENSION_INSENSITIVE_COMPARATOR = new ExtensionFileComparator(IOCase.INSENSITIVE);
    public static final Comparator<File> EXTENSION_INSENSITIVE_REVERSE = new ReverseComparator(EXTENSION_INSENSITIVE_COMPARATOR);
    public static final Comparator<File> EXTENSION_SYSTEM_COMPARATOR = new ExtensionFileComparator(IOCase.SYSTEM);
    public static final Comparator<File> EXTENSION_SYSTEM_REVERSE = new ReverseComparator(EXTENSION_SYSTEM_COMPARATOR);

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ List sort(List x0) {
        return super.sort((List<File>) x0);
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public /* bridge */ /* synthetic */ File[] sort(File[] x0) {
        return super.sort(x0);
    }

    public ExtensionFileComparator() {
        this.caseSensitivity = IOCase.SENSITIVE;
    }

    public ExtensionFileComparator(IOCase caseSensitivity) {
        this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
    }

    @Override // java.util.Comparator
    public int compare(File file1, File file2) {
        String suffix1 = FilenameUtils.getExtension(file1.getName());
        String suffix2 = FilenameUtils.getExtension(file2.getName());
        return this.caseSensitivity.checkCompareTo(suffix1, suffix2);
    }

    @Override // org.apache.commons.io.comparator.AbstractFileComparator
    public String toString() {
        return super.toString() + "[caseSensitivity=" + this.caseSensitivity + "]";
    }
}
