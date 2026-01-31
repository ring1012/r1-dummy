package com.google.android.exoplayer2.util;

import java.io.IOException;
import java.util.Collections;
import java.util.PriorityQueue;

/* loaded from: classes.dex */
public final class PriorityTaskManager {
    private final Object lock = new Object();
    private final PriorityQueue<Integer> queue = new PriorityQueue<>(10, Collections.reverseOrder());
    private int highestPriority = Integer.MIN_VALUE;

    public static class PriorityTooLowException extends IOException {
        public PriorityTooLowException(int priority, int highestPriority) {
            super("Priority too low [priority=" + priority + ", highest=" + highestPriority + "]");
        }
    }

    public void add(int priority) {
        synchronized (this.lock) {
            this.queue.add(Integer.valueOf(priority));
            this.highestPriority = Math.max(this.highestPriority, priority);
        }
    }

    public void proceed(int priority) throws InterruptedException {
        synchronized (this.lock) {
            while (this.highestPriority != priority) {
                this.lock.wait();
            }
        }
    }

    public boolean proceedNonBlocking(int priority) {
        boolean z;
        synchronized (this.lock) {
            z = this.highestPriority == priority;
        }
        return z;
    }

    public void proceedOrThrow(int priority) throws PriorityTooLowException {
        synchronized (this.lock) {
            if (this.highestPriority != priority) {
                throw new PriorityTooLowException(priority, this.highestPriority);
            }
        }
    }

    public void remove(int priority) {
        synchronized (this.lock) {
            this.queue.remove(Integer.valueOf(priority));
            this.highestPriority = this.queue.isEmpty() ? Integer.MIN_VALUE : this.queue.peek().intValue();
            this.lock.notifyAll();
        }
    }
}
