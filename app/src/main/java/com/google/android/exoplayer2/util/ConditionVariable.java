package com.google.android.exoplayer2.util;

/* loaded from: classes.dex */
public final class ConditionVariable {
    private boolean isOpen;

    public synchronized boolean open() {
        boolean z = true;
        synchronized (this) {
            if (this.isOpen) {
                z = false;
            } else {
                this.isOpen = true;
                notifyAll();
            }
        }
        return z;
    }

    public synchronized boolean close() {
        boolean wasOpen;
        wasOpen = this.isOpen;
        this.isOpen = false;
        return wasOpen;
    }

    public synchronized void block() throws InterruptedException {
        while (!this.isOpen) {
            wait();
        }
    }
}
