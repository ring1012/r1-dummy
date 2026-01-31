package org.apache.commons.io.monitor;

import java.io.File;
import java.io.Serializable;

/* loaded from: classes.dex */
public class FileEntry implements Serializable {
    static final FileEntry[] EMPTY_ENTRIES = new FileEntry[0];
    private FileEntry[] children;
    private boolean directory;
    private boolean exists;
    private final File file;
    private long lastModified;
    private long length;
    private String name;
    private final FileEntry parent;

    public FileEntry(File file) {
        this((FileEntry) null, file);
    }

    public FileEntry(FileEntry parent, File file) {
        if (file == null) {
            throw new IllegalArgumentException("File is missing");
        }
        this.file = file;
        this.parent = parent;
        this.name = file.getName();
    }

    public boolean refresh(File file) {
        long length = 0;
        boolean origExists = this.exists;
        long origLastModified = this.lastModified;
        boolean origDirectory = this.directory;
        long origLength = this.length;
        this.name = file.getName();
        this.exists = file.exists();
        this.directory = this.exists ? file.isDirectory() : false;
        this.lastModified = this.exists ? file.lastModified() : 0L;
        if (this.exists && !this.directory) {
            length = file.length();
        }
        this.length = length;
        return (this.exists == origExists && this.lastModified == origLastModified && this.directory == origDirectory && this.length == origLength) ? false : true;
    }

    public FileEntry newChildInstance(File file) {
        return new FileEntry(this, file);
    }

    public FileEntry getParent() {
        return this.parent;
    }

    public int getLevel() {
        if (this.parent == null) {
            return 0;
        }
        return this.parent.getLevel() + 1;
    }

    public FileEntry[] getChildren() {
        return this.children != null ? this.children : EMPTY_ENTRIES;
    }

    public void setChildren(FileEntry[] children) {
        this.children = children;
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getLength() {
        return this.length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public boolean isExists() {
        return this.exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public boolean isDirectory() {
        return this.directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }
}
