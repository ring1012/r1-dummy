package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.IOCase;

/* loaded from: classes.dex */
public class SuffixFileFilter extends AbstractFileFilter implements Serializable {
    private final IOCase caseSensitivity;
    private final String[] suffixes;

    public SuffixFileFilter(String suffix) {
        this(suffix, IOCase.SENSITIVE);
    }

    public SuffixFileFilter(String suffix, IOCase caseSensitivity) {
        if (suffix == null) {
            throw new IllegalArgumentException("The suffix must not be null");
        }
        this.suffixes = new String[]{suffix};
        this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
    }

    public SuffixFileFilter(String[] suffixes) {
        this(suffixes, IOCase.SENSITIVE);
    }

    public SuffixFileFilter(String[] suffixes, IOCase caseSensitivity) {
        if (suffixes == null) {
            throw new IllegalArgumentException("The array of suffixes must not be null");
        }
        this.suffixes = new String[suffixes.length];
        System.arraycopy(suffixes, 0, this.suffixes, 0, suffixes.length);
        this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
    }

    public SuffixFileFilter(List<String> suffixes) {
        this(suffixes, IOCase.SENSITIVE);
    }

    public SuffixFileFilter(List<String> suffixes, IOCase caseSensitivity) {
        if (suffixes == null) {
            throw new IllegalArgumentException("The list of suffixes must not be null");
        }
        this.suffixes = (String[]) suffixes.toArray(new String[suffixes.size()]);
        this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        String name = file.getName();
        String[] arr$ = this.suffixes;
        for (String suffix : arr$) {
            if (this.caseSensitivity.checkEndsWith(name, suffix)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File file, String name) {
        String[] arr$ = this.suffixes;
        for (String suffix : arr$) {
            if (this.caseSensitivity.checkEndsWith(name, suffix)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        if (this.suffixes != null) {
            for (int i = 0; i < this.suffixes.length; i++) {
                if (i > 0) {
                    buffer.append(",");
                }
                buffer.append(this.suffixes[i]);
            }
        }
        buffer.append(")");
        return buffer.toString();
    }
}
