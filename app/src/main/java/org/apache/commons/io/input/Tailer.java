package org.apache.commons.io.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class Tailer implements Runnable {
    private static final int DEFAULT_BUFSIZE = 4096;
    private static final int DEFAULT_DELAY_MILLIS = 1000;
    private static final String RAF_MODE = "r";
    private final long delayMillis;
    private final boolean end;
    private final File file;
    private final byte[] inbuf;
    private final TailerListener listener;
    private final boolean reOpen;
    private volatile boolean run;

    public Tailer(File file, TailerListener listener) {
        this(file, listener, 1000L);
    }

    public Tailer(File file, TailerListener listener, long delayMillis) {
        this(file, listener, delayMillis, false);
    }

    public Tailer(File file, TailerListener listener, long delayMillis, boolean end) {
        this(file, listener, delayMillis, end, 4096);
    }

    public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen) {
        this(file, listener, delayMillis, end, reOpen, 4096);
    }

    public Tailer(File file, TailerListener listener, long delayMillis, boolean end, int bufSize) {
        this(file, listener, delayMillis, end, false, bufSize);
    }

    public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
        this.run = true;
        this.file = file;
        this.delayMillis = delayMillis;
        this.end = end;
        this.inbuf = new byte[bufSize];
        this.listener = listener;
        listener.init(this);
        this.reOpen = reOpen;
    }

    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, int bufSize) {
        Tailer tailer = new Tailer(file, listener, delayMillis, end, bufSize);
        Thread thread = new Thread(tailer);
        thread.setDaemon(true);
        thread.start();
        return tailer;
    }

    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
        Tailer tailer = new Tailer(file, listener, delayMillis, end, reOpen, bufSize);
        Thread thread = new Thread(tailer);
        thread.setDaemon(true);
        thread.start();
        return tailer;
    }

    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end) {
        return create(file, listener, delayMillis, end, 4096);
    }

    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen) {
        return create(file, listener, delayMillis, end, reOpen, 4096);
    }

    public static Tailer create(File file, TailerListener listener, long delayMillis) {
        return create(file, listener, delayMillis, false);
    }

    public static Tailer create(File file, TailerListener listener) {
        return create(file, listener, 1000L, false);
    }

    public File getFile() {
        return this.file;
    }

    public long getDelay() {
        return this.delayMillis;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        RandomAccessFile reader;
        RandomAccessFile reader2;
        RandomAccessFile reader3 = null;
        long last = 0;
        long position = 0;
        while (true) {
            try {
                reader = reader3;
                if (!this.run || reader != null) {
                    break;
                }
                try {
                    reader3 = new RandomAccessFile(this.file, RAF_MODE);
                } catch (FileNotFoundException e) {
                    this.listener.fileNotFound();
                    reader3 = reader;
                }
                if (reader3 == null) {
                    try {
                        try {
                            try {
                                Thread.sleep(this.delayMillis);
                            } catch (Exception e2) {
                                e = e2;
                                this.listener.handle(e);
                                IOUtils.closeQuietly(reader3);
                                return;
                            }
                        } catch (Throwable th) {
                            th = th;
                            IOUtils.closeQuietly(reader3);
                            throw th;
                        }
                    } catch (InterruptedException e3) {
                    }
                } else {
                    position = this.end ? this.file.length() : 0L;
                    last = System.currentTimeMillis();
                    reader3.seek(position);
                }
            } catch (Exception e4) {
                e = e4;
                reader3 = reader;
            } catch (Throwable th2) {
                th = th2;
                reader3 = reader;
            }
        }
        while (this.run) {
            boolean newer = FileUtils.isFileNewer(this.file, last);
            long length = this.file.length();
            if (length < position) {
                this.listener.fileRotated();
                RandomAccessFile save = reader;
                try {
                    reader3 = new RandomAccessFile(this.file, RAF_MODE);
                    position = 0;
                    try {
                        IOUtils.closeQuietly(save);
                        reader = reader3;
                    } catch (FileNotFoundException e5) {
                        this.listener.fileNotFound();
                        reader = reader3;
                    }
                } catch (FileNotFoundException e6) {
                    reader3 = reader;
                }
            } else {
                if (length > position) {
                    position = readLines(reader);
                    last = System.currentTimeMillis();
                } else if (newer) {
                    reader.seek(0L);
                    position = readLines(reader);
                    last = System.currentTimeMillis();
                }
                if (this.reOpen) {
                    IOUtils.closeQuietly(reader);
                }
                try {
                    Thread.sleep(this.delayMillis);
                } catch (InterruptedException e7) {
                }
                if (this.run && this.reOpen) {
                    reader2 = new RandomAccessFile(this.file, RAF_MODE);
                    reader2.seek(position);
                } else {
                    reader2 = reader;
                }
                reader = reader2;
            }
        }
        IOUtils.closeQuietly(reader);
    }

    public void stop() {
        this.run = false;
    }

    private long readLines(RandomAccessFile reader) throws IOException {
        int num;
        StringBuilder sb = new StringBuilder();
        long pos = reader.getFilePointer();
        long rePos = pos;
        boolean seenCR = false;
        while (this.run && (num = reader.read(this.inbuf)) != -1) {
            for (int i = 0; i < num; i++) {
                byte ch = this.inbuf[i];
                switch (ch) {
                    case 10:
                        seenCR = false;
                        this.listener.handle(sb.toString());
                        sb.setLength(0);
                        rePos = i + pos + 1;
                        break;
                    case 11:
                    case 12:
                    default:
                        if (seenCR) {
                            seenCR = false;
                            this.listener.handle(sb.toString());
                            sb.setLength(0);
                            rePos = i + pos + 1;
                        }
                        sb.append((char) ch);
                        break;
                    case 13:
                        if (seenCR) {
                            sb.append('\r');
                        }
                        seenCR = true;
                        break;
                }
            }
            pos = reader.getFilePointer();
        }
        reader.seek(rePos);
        return rePos;
    }
}
