package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

/* loaded from: classes.dex */
public class CanReadFileFilter extends AbstractFileFilter implements Serializable {
    public static final IOFileFilter CAN_READ = new CanReadFileFilter();
    public static final IOFileFilter CANNOT_READ = new NotFileFilter(CAN_READ);
    public static final IOFileFilter READ_ONLY = new AndFileFilter(CAN_READ, CanWriteFileFilter.CANNOT_WRITE);

    protected CanReadFileFilter() {
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return file.canRead();
    }
}
