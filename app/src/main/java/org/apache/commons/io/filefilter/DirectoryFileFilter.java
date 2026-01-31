package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

/* loaded from: classes.dex */
public class DirectoryFileFilter extends AbstractFileFilter implements Serializable {
    public static final IOFileFilter DIRECTORY = new DirectoryFileFilter();
    public static final IOFileFilter INSTANCE = DIRECTORY;

    protected DirectoryFileFilter() {
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return file.isDirectory();
    }
}
