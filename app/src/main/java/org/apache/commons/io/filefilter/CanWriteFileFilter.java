package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

/* loaded from: classes.dex */
public class CanWriteFileFilter extends AbstractFileFilter implements Serializable {
    public static final IOFileFilter CAN_WRITE = new CanWriteFileFilter();
    public static final IOFileFilter CANNOT_WRITE = new NotFileFilter(CAN_WRITE);

    protected CanWriteFileFilter() {
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return file.canWrite();
    }
}
