package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

/* loaded from: classes.dex */
public class SizeFileFilter extends AbstractFileFilter implements Serializable {
    private final boolean acceptLarger;
    private final long size;

    public SizeFileFilter(long size) {
        this(size, true);
    }

    public SizeFileFilter(long size, boolean acceptLarger) {
        if (size < 0) {
            throw new IllegalArgumentException("The size must be non-negative");
        }
        this.size = size;
        this.acceptLarger = acceptLarger;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        boolean smaller = file.length() < this.size;
        return this.acceptLarger ? !smaller : smaller;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter
    public String toString() {
        String condition = this.acceptLarger ? ">=" : "<";
        return super.toString() + "(" + condition + this.size + ")";
    }
}
