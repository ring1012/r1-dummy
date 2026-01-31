package org.greenrobot.greendao.database;

import android.database.Cursor;
import android.database.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

/* loaded from: classes.dex */
public class EncryptedDatabase implements Database {
    private final SQLiteDatabase delegate;

    public EncryptedDatabase(SQLiteDatabase delegate) {
        this.delegate = delegate;
    }

    @Override // org.greenrobot.greendao.database.Database
    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return this.delegate.rawQuery(sql, selectionArgs);
    }

    @Override // org.greenrobot.greendao.database.Database
    public void execSQL(String sql) throws SQLException {
        this.delegate.execSQL(sql);
    }

    @Override // org.greenrobot.greendao.database.Database
    public void beginTransaction() {
        this.delegate.beginTransaction();
    }

    @Override // org.greenrobot.greendao.database.Database
    public void endTransaction() {
        this.delegate.endTransaction();
    }

    @Override // org.greenrobot.greendao.database.Database
    public boolean inTransaction() {
        return this.delegate.inTransaction();
    }

    @Override // org.greenrobot.greendao.database.Database
    public void setTransactionSuccessful() {
        this.delegate.setTransactionSuccessful();
    }

    @Override // org.greenrobot.greendao.database.Database
    public void execSQL(String sql, Object[] bindArgs) throws SQLException {
        this.delegate.execSQL(sql, bindArgs);
    }

    @Override // org.greenrobot.greendao.database.Database
    public DatabaseStatement compileStatement(String sql) {
        return new EncryptedDatabaseStatement(this.delegate.compileStatement(sql));
    }

    @Override // org.greenrobot.greendao.database.Database
    public boolean isDbLockedByCurrentThread() {
        return this.delegate.isDbLockedByCurrentThread();
    }

    @Override // org.greenrobot.greendao.database.Database
    public void close() {
        this.delegate.close();
    }

    @Override // org.greenrobot.greendao.database.Database
    public Object getRawDatabase() {
        return this.delegate;
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return this.delegate;
    }
}
