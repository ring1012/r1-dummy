package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;

/* loaded from: classes.dex */
public class WildcardFileFilter extends AbstractFileFilter implements Serializable {
    private final IOCase caseSensitivity;
    private final String[] wildcards;

    public WildcardFileFilter(String wildcard) {
        this(wildcard, (IOCase) null);
    }

    public WildcardFileFilter(String wildcard, IOCase caseSensitivity) {
        if (wildcard == null) {
            throw new IllegalArgumentException("The wildcard must not be null");
        }
        this.wildcards = new String[]{wildcard};
        this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
    }

    public WildcardFileFilter(String[] wildcards) {
        this(wildcards, (IOCase) null);
    }

    public WildcardFileFilter(String[] wildcards, IOCase caseSensitivity) {
        if (wildcards == null) {
            throw new IllegalArgumentException("The wildcard array must not be null");
        }
        this.wildcards = new String[wildcards.length];
        System.arraycopy(wildcards, 0, this.wildcards, 0, wildcards.length);
        this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
    }

    public WildcardFileFilter(List<String> wildcards) {
        this(wildcards, (IOCase) null);
    }

    public WildcardFileFilter(List<String> wildcards, IOCase caseSensitivity) {
        if (wildcards == null) {
            throw new IllegalArgumentException("The wildcard list must not be null");
        }
        this.wildcards = (String[]) wildcards.toArray(new String[wildcards.size()]);
        this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        String[] arr$ = this.wildcards;
        for (String wildcard : arr$) {
            if (FilenameUtils.wildcardMatch(name, wildcard, this.caseSensitivity)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        String name = file.getName();
        String[] arr$ = this.wildcards;
        for (String wildcard : arr$) {
            if (FilenameUtils.wildcardMatch(name, wildcard, this.caseSensitivity)) {
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
        if (this.wildcards != null) {
            for (int i = 0; i < this.wildcards.length; i++) {
                if (i > 0) {
                    buffer.append(",");
                }
                buffer.append(this.wildcards[i]);
            }
        }
        buffer.append(")");
        return buffer.toString();
    }
}
