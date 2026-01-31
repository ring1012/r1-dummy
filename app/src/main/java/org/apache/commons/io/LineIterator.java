package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: classes.dex */
public class LineIterator implements Iterator<String> {
    private final BufferedReader bufferedReader;
    private String cachedLine;
    private boolean finished = false;

    public LineIterator(Reader reader) throws IllegalArgumentException {
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be null");
        }
        if (reader instanceof BufferedReader) {
            this.bufferedReader = (BufferedReader) reader;
        } else {
            this.bufferedReader = new BufferedReader(reader);
        }
    }

    @Override // java.util.Iterator
    public boolean hasNext() throws IOException {
        boolean z = true;
        if (this.cachedLine != null) {
            return true;
        }
        if (this.finished) {
            return false;
        }
        while (true) {
            try {
                String line = this.bufferedReader.readLine();
                if (line == null) {
                    this.finished = true;
                    z = false;
                    break;
                }
                if (isValidLine(line)) {
                    this.cachedLine = line;
                    break;
                }
            } catch (IOException ioe) {
                close();
                throw new IllegalStateException(ioe);
            }
        }
        return z;
    }

    protected boolean isValidLine(String line) {
        return true;
    }

    @Override // java.util.Iterator
    public String next() {
        return nextLine();
    }

    public String nextLine() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more lines");
        }
        String currentLine = this.cachedLine;
        this.cachedLine = null;
        return currentLine;
    }

    public void close() {
        this.finished = true;
        IOUtils.closeQuietly((Reader) this.bufferedReader);
        this.cachedLine = null;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Remove unsupported on LineIterator");
    }

    public static void closeQuietly(LineIterator iterator) {
        if (iterator != null) {
            iterator.close();
        }
    }
}
