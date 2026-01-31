package org.greenrobot.greendao.database;

import net.sqlcipher.database.SQLiteStatement;

/* loaded from: classes.dex */
public class EncryptedDatabaseStatement implements DatabaseStatement {
    private final SQLiteStatement delegate;

    public EncryptedDatabaseStatement(SQLiteStatement delegate) {
        this.delegate = delegate;
    }

    @Override // org.greenrobot.greendao.database.DatabaseStatement
    public void execute() {
        this.delegate.execute();
    }

    @Override // org.greenrobot.greendao.database.DatabaseStatement
    public long simpleQueryForLong() {
        return this.delegate.simpleQueryForLong();
    }

    @Override // org.greenrobot.greendao.database.DatabaseStatement
    public void bindNull(int index) {
        this.delegate.bindNull(index);
    }

    @Override // org.greenrobot.greendao.database.DatabaseStatement
    public long executeInsert() {
        return this.delegate.executeInsert();
    }

    @Override // org.greenrobot.greendao.database.DatabaseStatement
    public void bindString(int index, String value) {
        this.delegate.bindString(index, value);
    }

    @Override // org.greenrobot.greendao.database.DatabaseStatement
    public void bindBlob(int index, byte[] value) {
        this.delegate.bindBlob(index, value);
    }

    @Override // org.greenrobot.greendao.database.DatabaseStatement
    public void bindLong(int index, long value) {
        this.delegate.bindLong(index, value);
    }

    @Override // org.greenrobot.greendao.database.DatabaseStatement
    public void clearBindings() {
        this.delegate.clearBindings();
    }

    @Override // org.greenrobot.greendao.database.DatabaseStatement
    public void bindDouble(int index, double value) {
        this.delegate.bindDouble(index, value);
    }

    @Override // org.greenrobot.greendao.database.DatabaseStatement
    public void close() {
        this.delegate.close();
    }

    @Override // org.greenrobot.greendao.database.DatabaseStatement
    public Object getRawStatement() {
        return this.delegate;
    }
}
