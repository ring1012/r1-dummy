package com.google.android.exoplayer2.util;

import android.support.annotation.NonNull;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes.dex */
public final class AtomicFile {
    private static final String TAG = "AtomicFile";
    private final File backupName;
    private final File baseName;

    public AtomicFile(File baseName) {
        this.baseName = baseName;
        this.backupName = new File(baseName.getPath() + ".bak");
    }

    public void delete() {
        this.baseName.delete();
        this.backupName.delete();
    }

    public OutputStream startWrite() throws IOException {
        if (this.baseName.exists()) {
            if (!this.backupName.exists()) {
                if (!this.baseName.renameTo(this.backupName)) {
                    Log.w(TAG, "Couldn't rename file " + this.baseName + " to backup file " + this.backupName);
                }
            } else {
                this.baseName.delete();
            }
        }
        try {
            OutputStream str = new AtomicFileOutputStream(this.baseName);
            return str;
        } catch (FileNotFoundException e) {
            File parent = this.baseName.getParentFile();
            if (!parent.mkdirs()) {
                throw new IOException("Couldn't create directory " + this.baseName);
            }
            try {
                OutputStream str2 = new AtomicFileOutputStream(this.baseName);
                return str2;
            } catch (FileNotFoundException e2) {
                throw new IOException("Couldn't create " + this.baseName);
            }
        }
    }

    public void endWrite(OutputStream str) throws IOException {
        str.close();
        this.backupName.delete();
    }

    public InputStream openRead() throws FileNotFoundException {
        restoreBackup();
        return new FileInputStream(this.baseName);
    }

    private void restoreBackup() {
        if (this.backupName.exists()) {
            this.baseName.delete();
            this.backupName.renameTo(this.baseName);
        }
    }

    private static final class AtomicFileOutputStream extends OutputStream {
        private boolean closed = false;
        private final FileOutputStream fileOutputStream;

        public AtomicFileOutputStream(File file) throws FileNotFoundException {
            this.fileOutputStream = new FileOutputStream(file);
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (!this.closed) {
                this.closed = true;
                flush();
                try {
                    this.fileOutputStream.getFD().sync();
                } catch (IOException e) {
                    Log.w(AtomicFile.TAG, "Failed to sync file descriptor:", e);
                }
                this.fileOutputStream.close();
            }
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            this.fileOutputStream.flush();
        }

        @Override // java.io.OutputStream
        public void write(int b) throws IOException {
            this.fileOutputStream.write(b);
        }

        @Override // java.io.OutputStream
        public void write(@NonNull byte[] b) throws IOException {
            this.fileOutputStream.write(b);
        }

        @Override // java.io.OutputStream
        public void write(@NonNull byte[] b, int off, int len) throws IOException {
            this.fileOutputStream.write(b, off, len);
        }
    }
}
