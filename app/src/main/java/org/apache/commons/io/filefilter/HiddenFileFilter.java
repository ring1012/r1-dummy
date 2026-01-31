package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

/* loaded from: classes.dex */
public class HiddenFileFilter extends AbstractFileFilter implements Serializable {
    public static final IOFileFilter HIDDEN = new HiddenFileFilter();
    public static final IOFileFilter VISIBLE = new NotFileFilter(HIDDEN);

    protected HiddenFileFilter() {
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        return file.isHidden();
    }
}
