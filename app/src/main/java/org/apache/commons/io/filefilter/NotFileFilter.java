package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

/* loaded from: classes.dex */
public class NotFileFilter extends AbstractFileFilter implements Serializable {
    private final IOFileFilter filter;

    public NotFileFilter(IOFileFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The filter must not be null");
        }
        this.filter = filter;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return !this.filter.accept(file);
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File file, String name) {
        return !this.filter.accept(file, name);
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter
    public String toString() {
        return super.toString() + "(" + this.filter.toString() + ")";
    }
}
