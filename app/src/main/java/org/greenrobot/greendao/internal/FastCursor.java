package org.greenrobot.greendao.internal;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

/* loaded from: classes.dex */
public final class FastCursor implements Cursor {
    private final int count;
    private int position;
    private final CursorWindow window;

    public FastCursor(CursorWindow window) {
        this.window = window;
        this.count = window.getNumRows();
    }

    @Override // android.database.Cursor
    public int getCount() {
        return this.window.getNumRows();
    }

    @Override // android.database.Cursor
    public int getPosition() {
        return this.position;
    }

    @Override // android.database.Cursor
    public boolean move(int offset) {
        return moveToPosition(this.position + offset);
    }

    @Override // android.database.Cursor
    public boolean moveToPosition(int position) {
        if (position < 0 || position >= this.count) {
            return false;
        }
        this.position = position;
        return true;
    }

    @Override // android.database.Cursor
    public boolean moveToFirst() {
        this.position = 0;
        return this.count > 0;
    }

    @Override // android.database.Cursor
    public boolean moveToLast() {
        if (this.count <= 0) {
            return false;
        }
        this.position = this.count - 1;
        return true;
    }

    @Override // android.database.Cursor
    public boolean moveToNext() {
        if (this.position >= this.count - 1) {
            return false;
        }
        this.position++;
        return true;
    }

    @Override // android.database.Cursor
    public boolean moveToPrevious() {
        if (this.position <= 0) {
            return false;
        }
        this.position--;
        return true;
    }

    @Override // android.database.Cursor
    public boolean isFirst() {
        return this.position == 0;
    }

    @Override // android.database.Cursor
    public boolean isLast() {
        return this.position == this.count + (-1);
    }

    @Override // android.database.Cursor
    public boolean isBeforeFirst() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public boolean isAfterLast() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public int getColumnIndex(String columnName) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public String getColumnName(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public String[] getColumnNames() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public int getColumnCount() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public byte[] getBlob(int columnIndex) {
        return this.window.getBlob(this.position, columnIndex);
    }

    @Override // android.database.Cursor
    public String getString(int columnIndex) {
        return this.window.getString(this.position, columnIndex);
    }

    @Override // android.database.Cursor
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public short getShort(int columnIndex) {
        return this.window.getShort(this.position, columnIndex);
    }

    @Override // android.database.Cursor
    public int getInt(int columnIndex) {
        return this.window.getInt(this.position, columnIndex);
    }

    @Override // android.database.Cursor
    public long getLong(int columnIndex) {
        return this.window.getLong(this.position, columnIndex);
    }

    @Override // android.database.Cursor
    public float getFloat(int columnIndex) {
        return this.window.getFloat(this.position, columnIndex);
    }

    @Override // android.database.Cursor
    public double getDouble(int columnIndex) {
        return this.window.getDouble(this.position, columnIndex);
    }

    @Override // android.database.Cursor
    public boolean isNull(int columnIndex) {
        return this.window.isNull(this.position, columnIndex);
    }

    @Override // android.database.Cursor
    public void deactivate() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public boolean requery() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public boolean isClosed() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public void registerContentObserver(ContentObserver observer) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public void unregisterContentObserver(ContentObserver observer) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public void registerDataSetObserver(DataSetObserver observer) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public void unregisterDataSetObserver(DataSetObserver observer) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public void setNotificationUri(ContentResolver cr, Uri uri) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public boolean getWantsAllOnMoveCalls() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public Bundle getExtras() {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public Bundle respond(Bundle extras) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public int getType(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override // android.database.Cursor
    public Uri getNotificationUri() {
        return null;
    }
}
