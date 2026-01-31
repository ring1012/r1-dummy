package org.apache.commons.io;

/* loaded from: classes.dex */
class ThreadMonitor implements Runnable {
    private final Thread thread;
    private final long timeout;

    public static Thread start(long timeout) {
        return start(Thread.currentThread(), timeout);
    }

    public static Thread start(Thread thread, long timeout) {
        if (timeout <= 0) {
            return null;
        }
        ThreadMonitor timout = new ThreadMonitor(thread, timeout);
        Thread monitor = new Thread(timout, ThreadMonitor.class.getSimpleName());
        monitor.setDaemon(true);
        monitor.start();
        return monitor;
    }

    public static void stop(Thread thread) {
        if (thread != null) {
            thread.interrupt();
        }
    }

    private ThreadMonitor(Thread thread, long timeout) {
        this.thread = thread;
        this.timeout = timeout;
    }

    @Override // java.lang.Runnable
    public void run() throws InterruptedException {
        try {
            Thread.sleep(this.timeout);
            this.thread.interrupt();
        } catch (InterruptedException e) {
        }
    }
}
