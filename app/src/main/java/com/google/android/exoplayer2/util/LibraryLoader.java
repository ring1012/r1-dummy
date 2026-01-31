package com.google.android.exoplayer2.util;

/* loaded from: classes.dex */
public final class LibraryLoader {
    private boolean isAvailable;
    private boolean loadAttempted;
    private String[] nativeLibraries;

    public LibraryLoader(String... libraries) {
        this.nativeLibraries = libraries;
    }

    public synchronized void setLibraries(String... libraries) {
        Assertions.checkState(!this.loadAttempted, "Cannot set libraries after loading");
        this.nativeLibraries = libraries;
    }

    public synchronized boolean isAvailable() {
        boolean z;
        if (this.loadAttempted) {
            z = this.isAvailable;
        } else {
            this.loadAttempted = true;
            try {
                for (String lib : this.nativeLibraries) {
                    System.loadLibrary(lib);
                }
                this.isAvailable = true;
            } catch (UnsatisfiedLinkError e) {
            }
            z = this.isAvailable;
        }
        return z;
    }
}
