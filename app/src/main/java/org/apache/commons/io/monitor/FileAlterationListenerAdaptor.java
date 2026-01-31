package org.apache.commons.io.monitor;

import java.io.File;

/* loaded from: classes.dex */
public class FileAlterationListenerAdaptor implements FileAlterationListener {
    @Override // org.apache.commons.io.monitor.FileAlterationListener
    public void onStart(FileAlterationObserver observer) {
    }

    @Override // org.apache.commons.io.monitor.FileAlterationListener
    public void onDirectoryCreate(File directory) {
    }

    @Override // org.apache.commons.io.monitor.FileAlterationListener
    public void onDirectoryChange(File directory) {
    }

    @Override // org.apache.commons.io.monitor.FileAlterationListener
    public void onDirectoryDelete(File directory) {
    }

    @Override // org.apache.commons.io.monitor.FileAlterationListener
    public void onFileCreate(File file) {
    }

    @Override // org.apache.commons.io.monitor.FileAlterationListener
    public void onFileChange(File file) {
    }

    @Override // org.apache.commons.io.monitor.FileAlterationListener
    public void onFileDelete(File file) {
    }

    @Override // org.apache.commons.io.monitor.FileAlterationListener
    public void onStop(FileAlterationObserver observer) {
    }
}
