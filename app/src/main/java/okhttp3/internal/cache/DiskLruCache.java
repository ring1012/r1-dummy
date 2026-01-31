package okhttp3.internal.cache;

import cn.yunzhisheng.common.PinyinConverter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.platform.Platform;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;
import org.apache.commons.io.FilenameUtils;

/* loaded from: classes.dex */
public final class DiskLruCache implements Closeable, Flushable {
    static final /* synthetic */ boolean $assertionsDisabled;
    static final long ANY_SEQUENCE_NUMBER = -1;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final Pattern LEGAL_KEY_PATTERN;
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    boolean closed;
    final File directory;
    private final Executor executor;
    final FileSystem fileSystem;
    boolean hasJournalErrors;
    boolean initialized;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    BufferedSink journalWriter;
    private long maxSize;
    boolean mostRecentRebuildFailed;
    boolean mostRecentTrimFailed;
    int redundantOpCount;
    final int valueCount;
    private long size = 0;
    final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<>(0, 0.75f, true);
    private long nextSequenceNumber = 0;
    private final Runnable cleanupRunnable = new Runnable() { // from class: okhttp3.internal.cache.DiskLruCache.1
        @Override // java.lang.Runnable
        public void run() {
            synchronized (DiskLruCache.this) {
                if (!((DiskLruCache.this.initialized ? false : true) | DiskLruCache.this.closed)) {
                    try {
                        DiskLruCache.this.trimToSize();
                    } catch (IOException e) {
                        DiskLruCache.this.mostRecentTrimFailed = true;
                    }
                    try {
                        if (DiskLruCache.this.journalRebuildRequired()) {
                            DiskLruCache.this.rebuildJournal();
                            DiskLruCache.this.redundantOpCount = 0;
                        }
                    } catch (IOException e2) {
                        DiskLruCache.this.mostRecentRebuildFailed = true;
                        DiskLruCache.this.journalWriter = Okio.buffer(Okio.blackhole());
                    }
                }
            }
        }
    };

    static {
        $assertionsDisabled = !DiskLruCache.class.desiredAssertionStatus();
        LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,120}");
    }

    DiskLruCache(FileSystem fileSystem, File directory, int appVersion, int valueCount, long maxSize, Executor executor) {
        this.fileSystem = fileSystem;
        this.directory = directory;
        this.appVersion = appVersion;
        this.journalFile = new File(directory, JOURNAL_FILE);
        this.journalFileTmp = new File(directory, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(directory, JOURNAL_FILE_BACKUP);
        this.valueCount = valueCount;
        this.maxSize = maxSize;
        this.executor = executor;
    }

    public synchronized void initialize() throws IOException {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        if (!this.initialized) {
            if (this.fileSystem.exists(this.journalFileBackup)) {
                if (this.fileSystem.exists(this.journalFile)) {
                    this.fileSystem.delete(this.journalFileBackup);
                } else {
                    this.fileSystem.rename(this.journalFileBackup, this.journalFile);
                }
            }
            if (this.fileSystem.exists(this.journalFile)) {
                try {
                    readJournal();
                    processJournal();
                    this.initialized = true;
                } catch (IOException journalIsCorrupt) {
                    Platform.get().log(5, "DiskLruCache " + this.directory + " is corrupt: " + journalIsCorrupt.getMessage() + ", removing", journalIsCorrupt);
                    try {
                        delete();
                        this.closed = false;
                    } catch (Throwable th) {
                        this.closed = false;
                        throw th;
                    }
                }
            } else {
                rebuildJournal();
                this.initialized = true;
            }
        }
    }

    public static DiskLruCache create(FileSystem fileSystem, File directory, int appVersion, int valueCount, long maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (valueCount <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        }
        Executor executor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory("OkHttp DiskLruCache", true));
        return new DiskLruCache(fileSystem, directory, appVersion, valueCount, maxSize, executor);
    }

    private void readJournal() throws IOException {
        BufferedSource source = Okio.buffer(this.fileSystem.source(this.journalFile));
        try {
            String magic = source.readUtf8LineStrict();
            String version = source.readUtf8LineStrict();
            String appVersionString = source.readUtf8LineStrict();
            String valueCountString = source.readUtf8LineStrict();
            String blank = source.readUtf8LineStrict();
            if (!MAGIC.equals(magic) || !"1".equals(version) || !Integer.toString(this.appVersion).equals(appVersionString) || !Integer.toString(this.valueCount).equals(valueCountString) || !"".equals(blank)) {
                throw new IOException("unexpected journal header: [" + magic + ", " + version + ", " + valueCountString + ", " + blank + "]");
            }
            int lineCount = 0;
            while (true) {
                try {
                    readJournalLine(source.readUtf8LineStrict());
                    lineCount++;
                } catch (EOFException e) {
                    this.redundantOpCount = lineCount - this.lruEntries.size();
                    if (!source.exhausted()) {
                        rebuildJournal();
                    } else {
                        this.journalWriter = newJournalWriter();
                    }
                    Util.closeQuietly(source);
                    return;
                }
            }
        } catch (Throwable th) {
            Util.closeQuietly(source);
            throw th;
        }
    }

    private BufferedSink newJournalWriter() throws FileNotFoundException {
        Sink fileSink = this.fileSystem.appendingSink(this.journalFile);
        Sink faultHidingSink = new FaultHidingSink(fileSink) { // from class: okhttp3.internal.cache.DiskLruCache.2
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !DiskLruCache.class.desiredAssertionStatus();
            }

            @Override // okhttp3.internal.cache.FaultHidingSink
            protected void onException(IOException e) {
                if (!$assertionsDisabled && !Thread.holdsLock(DiskLruCache.this)) {
                    throw new AssertionError();
                }
                DiskLruCache.this.hasJournalErrors = true;
            }
        };
        return Okio.buffer(faultHidingSink);
    }

    private void readJournalLine(String line) throws IOException {
        String key;
        int firstSpace = line.indexOf(32);
        if (firstSpace == -1) {
            throw new IOException("unexpected journal line: " + line);
        }
        int keyBegin = firstSpace + 1;
        int secondSpace = line.indexOf(32, keyBegin);
        if (secondSpace == -1) {
            key = line.substring(keyBegin);
            if (firstSpace == REMOVE.length() && line.startsWith(REMOVE)) {
                this.lruEntries.remove(key);
                return;
            }
        } else {
            key = line.substring(keyBegin, secondSpace);
        }
        Entry entry = this.lruEntries.get(key);
        if (entry == null) {
            entry = new Entry(key);
            this.lruEntries.put(key, entry);
        }
        if (secondSpace != -1 && firstSpace == CLEAN.length() && line.startsWith(CLEAN)) {
            String[] parts = line.substring(secondSpace + 1).split(PinyinConverter.PINYIN_SEPARATOR);
            entry.readable = true;
            entry.currentEditor = null;
            entry.setLengths(parts);
            return;
        }
        if (secondSpace == -1 && firstSpace == DIRTY.length() && line.startsWith(DIRTY)) {
            entry.currentEditor = new Editor(entry);
        } else if (secondSpace != -1 || firstSpace != READ.length() || !line.startsWith(READ)) {
            throw new IOException("unexpected journal line: " + line);
        }
    }

    private void processJournal() throws IOException {
        this.fileSystem.delete(this.journalFileTmp);
        Iterator<Entry> i = this.lruEntries.values().iterator();
        while (i.hasNext()) {
            Entry entry = i.next();
            if (entry.currentEditor == null) {
                for (int t = 0; t < this.valueCount; t++) {
                    this.size += entry.lengths[t];
                }
            } else {
                entry.currentEditor = null;
                for (int t2 = 0; t2 < this.valueCount; t2++) {
                    this.fileSystem.delete(entry.cleanFiles[t2]);
                    this.fileSystem.delete(entry.dirtyFiles[t2]);
                }
                i.remove();
            }
        }
    }

    synchronized void rebuildJournal() throws IOException {
        if (this.journalWriter != null) {
            this.journalWriter.close();
        }
        BufferedSink writer = Okio.buffer(this.fileSystem.sink(this.journalFileTmp));
        try {
            writer.writeUtf8(MAGIC).writeByte(10);
            writer.writeUtf8("1").writeByte(10);
            writer.writeDecimalLong(this.appVersion).writeByte(10);
            writer.writeDecimalLong(this.valueCount).writeByte(10);
            writer.writeByte(10);
            for (Entry entry : this.lruEntries.values()) {
                if (entry.currentEditor != null) {
                    writer.writeUtf8(DIRTY).writeByte(32);
                    writer.writeUtf8(entry.key);
                    writer.writeByte(10);
                } else {
                    writer.writeUtf8(CLEAN).writeByte(32);
                    writer.writeUtf8(entry.key);
                    entry.writeLengths(writer);
                    writer.writeByte(10);
                }
            }
            writer.close();
            if (this.fileSystem.exists(this.journalFile)) {
                this.fileSystem.rename(this.journalFile, this.journalFileBackup);
            }
            this.fileSystem.rename(this.journalFileTmp, this.journalFile);
            this.fileSystem.delete(this.journalFileBackup);
            this.journalWriter = newJournalWriter();
            this.hasJournalErrors = false;
            this.mostRecentRebuildFailed = false;
        } catch (Throwable th) {
            writer.close();
            throw th;
        }
    }

    public synchronized Snapshot get(String key) throws IOException {
        Snapshot snapshot;
        initialize();
        checkNotClosed();
        validateKey(key);
        Entry entry = this.lruEntries.get(key);
        if (entry == null || !entry.readable || (snapshot = entry.snapshot()) == null) {
            snapshot = null;
        } else {
            this.redundantOpCount++;
            this.journalWriter.writeUtf8(READ).writeByte(32).writeUtf8(key).writeByte(10);
            if (journalRebuildRequired()) {
                this.executor.execute(this.cleanupRunnable);
            }
        }
        return snapshot;
    }

    @Nullable
    public Editor edit(String key) throws IOException {
        return edit(key, -1L);
    }

    synchronized Editor edit(String key, long expectedSequenceNumber) throws IOException {
        Editor editor = null;
        synchronized (this) {
            initialize();
            checkNotClosed();
            validateKey(key);
            Entry entry = this.lruEntries.get(key);
            if ((expectedSequenceNumber == -1 || (entry != null && entry.sequenceNumber == expectedSequenceNumber)) && (entry == null || entry.currentEditor == null)) {
                if (this.mostRecentTrimFailed || this.mostRecentRebuildFailed) {
                    this.executor.execute(this.cleanupRunnable);
                } else {
                    this.journalWriter.writeUtf8(DIRTY).writeByte(32).writeUtf8(key).writeByte(10);
                    this.journalWriter.flush();
                    if (!this.hasJournalErrors) {
                        if (entry == null) {
                            entry = new Entry(key);
                            this.lruEntries.put(key, entry);
                        }
                        editor = new Editor(entry);
                        entry.currentEditor = editor;
                    }
                }
            }
        }
        return editor;
    }

    public File getDirectory() {
        return this.directory;
    }

    public synchronized long getMaxSize() {
        return this.maxSize;
    }

    public synchronized void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
        if (this.initialized) {
            this.executor.execute(this.cleanupRunnable);
        }
    }

    public synchronized long size() throws IOException {
        initialize();
        return this.size;
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0056 A[Catch: all -> 0x000d, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0007, B:6:0x000c, B:11:0x0012, B:14:0x0017, B:16:0x001b, B:18:0x0021, B:19:0x003c, B:20:0x003d, B:22:0x0049, B:27:0x0052, B:29:0x0056, B:31:0x005c, B:33:0x0064, B:34:0x0081, B:35:0x0084, B:36:0x008a, B:38:0x0098, B:40:0x00bd, B:41:0x00c6, B:43:0x00d3, B:45:0x00d9, B:46:0x00e2), top: B:48:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0098 A[Catch: all -> 0x000d, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0007, B:6:0x000c, B:11:0x0012, B:14:0x0017, B:16:0x001b, B:18:0x0021, B:19:0x003c, B:20:0x003d, B:22:0x0049, B:27:0x0052, B:29:0x0056, B:31:0x005c, B:33:0x0064, B:34:0x0081, B:35:0x0084, B:36:0x008a, B:38:0x0098, B:40:0x00bd, B:41:0x00c6, B:43:0x00d3, B:45:0x00d9, B:46:0x00e2), top: B:48:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00d9 A[Catch: all -> 0x000d, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0007, B:6:0x000c, B:11:0x0012, B:14:0x0017, B:16:0x001b, B:18:0x0021, B:19:0x003c, B:20:0x003d, B:22:0x0049, B:27:0x0052, B:29:0x0056, B:31:0x005c, B:33:0x0064, B:34:0x0081, B:35:0x0084, B:36:0x008a, B:38:0x0098, B:40:0x00bd, B:41:0x00c6, B:43:0x00d3, B:45:0x00d9, B:46:0x00e2), top: B:48:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00e2 A[Catch: all -> 0x000d, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0007, B:6:0x000c, B:11:0x0012, B:14:0x0017, B:16:0x001b, B:18:0x0021, B:19:0x003c, B:20:0x003d, B:22:0x0049, B:27:0x0052, B:29:0x0056, B:31:0x005c, B:33:0x0064, B:34:0x0081, B:35:0x0084, B:36:0x008a, B:38:0x0098, B:40:0x00bd, B:41:0x00c6, B:43:0x00d3, B:45:0x00d9, B:46:0x00e2), top: B:48:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    synchronized void completeEdit(okhttp3.internal.cache.DiskLruCache.Editor r13, boolean r14) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 261
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache.completeEdit(okhttp3.internal.cache.DiskLruCache$Editor, boolean):void");
    }

    boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }

    public synchronized boolean remove(String key) throws IOException {
        boolean removed = false;
        synchronized (this) {
            initialize();
            checkNotClosed();
            validateKey(key);
            Entry entry = this.lruEntries.get(key);
            if (entry != null && (removed = removeEntry(entry)) && this.size <= this.maxSize) {
                this.mostRecentTrimFailed = false;
            }
        }
        return removed;
    }

    boolean removeEntry(Entry entry) throws IOException {
        if (entry.currentEditor != null) {
            entry.currentEditor.detach();
        }
        for (int i = 0; i < this.valueCount; i++) {
            this.fileSystem.delete(entry.cleanFiles[i]);
            this.size -= entry.lengths[i];
            entry.lengths[i] = 0;
        }
        this.redundantOpCount++;
        this.journalWriter.writeUtf8(REMOVE).writeByte(32).writeUtf8(entry.key).writeByte(10);
        this.lruEntries.remove(entry.key);
        if (journalRebuildRequired()) {
            this.executor.execute(this.cleanupRunnable);
            return true;
        }
        return true;
    }

    public synchronized boolean isClosed() {
        return this.closed;
    }

    private synchronized void checkNotClosed() {
        if (isClosed()) {
            throw new IllegalStateException("cache is closed");
        }
    }

    @Override // java.io.Flushable
    public synchronized void flush() throws IOException {
        if (this.initialized) {
            checkNotClosed();
            trimToSize();
            this.journalWriter.flush();
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (!this.initialized || this.closed) {
            this.closed = true;
        } else {
            for (Entry entry : (Entry[]) this.lruEntries.values().toArray(new Entry[this.lruEntries.size()])) {
                if (entry.currentEditor != null) {
                    entry.currentEditor.abort();
                }
            }
            trimToSize();
            this.journalWriter.close();
            this.journalWriter = null;
            this.closed = true;
        }
    }

    void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            Entry toEvict = this.lruEntries.values().iterator().next();
            removeEntry(toEvict);
        }
        this.mostRecentTrimFailed = false;
    }

    public void delete() throws IOException {
        close();
        this.fileSystem.deleteContents(this.directory);
    }

    public synchronized void evictAll() throws IOException {
        synchronized (this) {
            initialize();
            for (Entry entry : (Entry[]) this.lruEntries.values().toArray(new Entry[this.lruEntries.size()])) {
                removeEntry(entry);
            }
            this.mostRecentTrimFailed = false;
        }
    }

    private void validateKey(String key) {
        Matcher matcher = LEGAL_KEY_PATTERN.matcher(key);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + key + "\"");
        }
    }

    public synchronized Iterator<Snapshot> snapshots() throws IOException {
        initialize();
        return new Iterator<Snapshot>() { // from class: okhttp3.internal.cache.DiskLruCache.3
            final Iterator<Entry> delegate;
            Snapshot nextSnapshot;
            Snapshot removeSnapshot;

            {
                this.delegate = new ArrayList(DiskLruCache.this.lruEntries.values()).iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                boolean z = true;
                if (this.nextSnapshot == null) {
                    synchronized (DiskLruCache.this) {
                        if (!DiskLruCache.this.closed) {
                            while (true) {
                                if (!this.delegate.hasNext()) {
                                    z = false;
                                    break;
                                }
                                Entry entry = this.delegate.next();
                                Snapshot snapshot = entry.snapshot();
                                if (snapshot != null) {
                                    this.nextSnapshot = snapshot;
                                    break;
                                }
                            }
                        } else {
                            z = false;
                        }
                    }
                }
                return z;
            }

            @Override // java.util.Iterator
            public Snapshot next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                this.removeSnapshot = this.nextSnapshot;
                this.nextSnapshot = null;
                return this.removeSnapshot;
            }

            @Override // java.util.Iterator
            public void remove() {
                if (this.removeSnapshot == null) {
                    throw new IllegalStateException("remove() before next()");
                }
                try {
                    DiskLruCache.this.remove(this.removeSnapshot.key);
                } catch (IOException e) {
                } finally {
                    this.removeSnapshot = null;
                }
            }
        };
    }

    public final class Snapshot implements Closeable {
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        private final Source[] sources;

        Snapshot(String key, long sequenceNumber, Source[] sources, long[] lengths) {
            this.key = key;
            this.sequenceNumber = sequenceNumber;
            this.sources = sources;
            this.lengths = lengths;
        }

        public String key() {
            return this.key;
        }

        @Nullable
        public Editor edit() throws IOException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        public Source getSource(int index) {
            return this.sources[index];
        }

        public long getLength(int index) {
            return this.lengths[index];
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            for (Source in : this.sources) {
                Util.closeQuietly(in);
            }
        }
    }

    public final class Editor {
        private boolean done;
        final Entry entry;
        final boolean[] written;

        Editor(Entry entry) {
            this.entry = entry;
            this.written = entry.readable ? null : new boolean[DiskLruCache.this.valueCount];
        }

        void detach() {
            if (this.entry.currentEditor == this) {
                for (int i = 0; i < DiskLruCache.this.valueCount; i++) {
                    try {
                        DiskLruCache.this.fileSystem.delete(this.entry.dirtyFiles[i]);
                    } catch (IOException e) {
                    }
                }
                this.entry.currentEditor = null;
            }
        }

        public Source newSource(int index) {
            Source source = null;
            synchronized (DiskLruCache.this) {
                if (this.done) {
                    throw new IllegalStateException();
                }
                if (this.entry.readable && this.entry.currentEditor == this) {
                    try {
                        source = DiskLruCache.this.fileSystem.source(this.entry.cleanFiles[index]);
                    } catch (FileNotFoundException e) {
                    }
                }
                return source;
            }
        }

        public Sink newSink(int index) {
            Sink sinkBlackhole;
            synchronized (DiskLruCache.this) {
                if (this.done) {
                    throw new IllegalStateException();
                }
                if (this.entry.currentEditor != this) {
                    sinkBlackhole = Okio.blackhole();
                } else {
                    if (!this.entry.readable) {
                        this.written[index] = true;
                    }
                    File dirtyFile = this.entry.dirtyFiles[index];
                    try {
                        Sink sink = DiskLruCache.this.fileSystem.sink(dirtyFile);
                        sinkBlackhole = new FaultHidingSink(sink) { // from class: okhttp3.internal.cache.DiskLruCache.Editor.1
                            @Override // okhttp3.internal.cache.FaultHidingSink
                            protected void onException(IOException e) {
                                synchronized (DiskLruCache.this) {
                                    Editor.this.detach();
                                }
                            }
                        };
                    } catch (FileNotFoundException e) {
                        sinkBlackhole = Okio.blackhole();
                    }
                }
                return sinkBlackhole;
            }
        }

        public void commit() throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.done) {
                    throw new IllegalStateException();
                }
                if (this.entry.currentEditor == this) {
                    DiskLruCache.this.completeEdit(this, true);
                }
                this.done = true;
            }
        }

        public void abort() throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.done) {
                    throw new IllegalStateException();
                }
                if (this.entry.currentEditor == this) {
                    DiskLruCache.this.completeEdit(this, false);
                }
                this.done = true;
            }
        }

        public void abortUnlessCommitted() {
            synchronized (DiskLruCache.this) {
                if (!this.done && this.entry.currentEditor == this) {
                    try {
                        DiskLruCache.this.completeEdit(this, false);
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    private final class Entry {
        final File[] cleanFiles;
        Editor currentEditor;
        final File[] dirtyFiles;
        final String key;
        final long[] lengths;
        boolean readable;
        long sequenceNumber;

        Entry(String key) {
            this.key = key;
            this.lengths = new long[DiskLruCache.this.valueCount];
            this.cleanFiles = new File[DiskLruCache.this.valueCount];
            this.dirtyFiles = new File[DiskLruCache.this.valueCount];
            StringBuilder fileBuilder = new StringBuilder(key).append(FilenameUtils.EXTENSION_SEPARATOR);
            int truncateTo = fileBuilder.length();
            for (int i = 0; i < DiskLruCache.this.valueCount; i++) {
                fileBuilder.append(i);
                this.cleanFiles[i] = new File(DiskLruCache.this.directory, fileBuilder.toString());
                fileBuilder.append(".tmp");
                this.dirtyFiles[i] = new File(DiskLruCache.this.directory, fileBuilder.toString());
                fileBuilder.setLength(truncateTo);
            }
        }

        void setLengths(String[] strings) throws IOException {
            if (strings.length != DiskLruCache.this.valueCount) {
                throw invalidLengths(strings);
            }
            for (int i = 0; i < strings.length; i++) {
                try {
                    this.lengths[i] = Long.parseLong(strings[i]);
                } catch (NumberFormatException e) {
                    throw invalidLengths(strings);
                }
            }
        }

        void writeLengths(BufferedSink writer) throws IOException {
            for (long length : this.lengths) {
                writer.writeByte(32).writeDecimalLong(length);
            }
        }

        private IOException invalidLengths(String[] strings) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(strings));
        }

        Snapshot snapshot() {
            if (!Thread.holdsLock(DiskLruCache.this)) {
                throw new AssertionError();
            }
            Source[] sources = new Source[DiskLruCache.this.valueCount];
            long[] lengths = (long[]) this.lengths.clone();
            for (int i = 0; i < DiskLruCache.this.valueCount; i++) {
                try {
                    sources[i] = DiskLruCache.this.fileSystem.source(this.cleanFiles[i]);
                } catch (FileNotFoundException e) {
                    for (int i2 = 0; i2 < DiskLruCache.this.valueCount && sources[i2] != null; i2++) {
                        Util.closeQuietly(sources[i2]);
                    }
                    try {
                        DiskLruCache.this.removeEntry(this);
                    } catch (IOException e2) {
                    }
                    return null;
                }
            }
            return DiskLruCache.this.new Snapshot(this.key, this.sequenceNumber, sources, lengths);
        }
    }
}
