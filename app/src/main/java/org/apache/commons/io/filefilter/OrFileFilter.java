package org.apache.commons.io.filefilter;

import cn.yunzhisheng.asr.JniUscClient;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class OrFileFilter extends AbstractFileFilter implements ConditionalFileFilter, Serializable {
    private final List<IOFileFilter> fileFilters;

    public OrFileFilter() {
        this.fileFilters = new ArrayList();
    }

    public OrFileFilter(List<IOFileFilter> fileFilters) {
        if (fileFilters == null) {
            this.fileFilters = new ArrayList();
        } else {
            this.fileFilters = new ArrayList(fileFilters);
        }
    }

    public OrFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
        if (filter1 == null || filter2 == null) {
            throw new IllegalArgumentException("The filters must not be null");
        }
        this.fileFilters = new ArrayList(2);
        addFileFilter(filter1);
        addFileFilter(filter2);
    }

    @Override // org.apache.commons.io.filefilter.ConditionalFileFilter
    public void addFileFilter(IOFileFilter ioFileFilter) {
        this.fileFilters.add(ioFileFilter);
    }

    @Override // org.apache.commons.io.filefilter.ConditionalFileFilter
    public List<IOFileFilter> getFileFilters() {
        return Collections.unmodifiableList(this.fileFilters);
    }

    @Override // org.apache.commons.io.filefilter.ConditionalFileFilter
    public boolean removeFileFilter(IOFileFilter ioFileFilter) {
        return this.fileFilters.remove(ioFileFilter);
    }

    @Override // org.apache.commons.io.filefilter.ConditionalFileFilter
    public void setFileFilters(List<IOFileFilter> fileFilters) {
        this.fileFilters.clear();
        this.fileFilters.addAll(fileFilters);
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FileFilter
    public boolean accept(File file) {
        for (IOFileFilter fileFilter : this.fileFilters) {
            if (fileFilter.accept(file)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File file, String name) {
        for (IOFileFilter fileFilter : this.fileFilters) {
            if (fileFilter.accept(file, name)) {
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
        if (this.fileFilters != null) {
            for (int i = 0; i < this.fileFilters.size(); i++) {
                if (i > 0) {
                    buffer.append(",");
                }
                Object filter = this.fileFilters.get(i);
                buffer.append(filter == null ? JniUscClient.az : filter.toString());
            }
        }
        buffer.append(")");
        return buffer.toString();
    }
}
