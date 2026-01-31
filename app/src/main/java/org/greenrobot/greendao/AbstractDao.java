package org.greenrobot.greendao;

import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.identityscope.IdentityScope;
import org.greenrobot.greendao.identityscope.IdentityScopeLong;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.internal.TableStatements;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

/* loaded from: classes.dex */
public abstract class AbstractDao<T, K> {
    protected final DaoConfig config;
    protected final Database db;
    protected IdentityScope<K, T> identityScope;
    protected IdentityScopeLong<T> identityScopeLong;
    protected final boolean isStandardSQLite;
    protected final int pkOrdinal;
    protected final AbstractDaoSession session;
    protected TableStatements statements;

    protected abstract void bindValues(SQLiteStatement sQLiteStatement, T t);

    protected abstract void bindValues(DatabaseStatement databaseStatement, T t);

    protected abstract K getKey(T t);

    protected abstract boolean isEntityUpdateable();

    protected abstract T readEntity(Cursor cursor, int i);

    protected abstract void readEntity(Cursor cursor, T t, int i);

    protected abstract K readKey(Cursor cursor, int i);

    protected abstract K updateKeyAfterInsert(T t, long j);

    public AbstractDao(DaoConfig config) {
        this(config, null);
    }

    public AbstractDao(DaoConfig daoConfig, AbstractDaoSession abstractDaoSession) {
        this.config = daoConfig;
        this.session = abstractDaoSession;
        this.db = daoConfig.db;
        this.isStandardSQLite = this.db.getRawDatabase() instanceof SQLiteDatabase;
        this.identityScope = (IdentityScope<K, T>) daoConfig.getIdentityScope();
        if (this.identityScope instanceof IdentityScopeLong) {
            this.identityScopeLong = (IdentityScopeLong) this.identityScope;
        }
        this.statements = daoConfig.statements;
        this.pkOrdinal = daoConfig.pkProperty != null ? daoConfig.pkProperty.ordinal : -1;
    }

    public AbstractDaoSession getSession() {
        return this.session;
    }

    TableStatements getStatements() {
        return this.config.statements;
    }

    public String getTablename() {
        return this.config.tablename;
    }

    public Property[] getProperties() {
        return this.config.properties;
    }

    public Property getPkProperty() {
        return this.config.pkProperty;
    }

    public String[] getAllColumns() {
        return this.config.allColumns;
    }

    public String[] getPkColumns() {
        return this.config.pkColumns;
    }

    public String[] getNonPkColumns() {
        return this.config.nonPkColumns;
    }

    public T load(K key) {
        T entity;
        assertSinglePk();
        if (key == null) {
            return null;
        }
        if (this.identityScope == null || (entity = this.identityScope.get(key)) == null) {
            String sql = this.statements.getSelectByKey();
            String[] keyArray = {key.toString()};
            Cursor cursor = this.db.rawQuery(sql, keyArray);
            return loadUniqueAndCloseCursor(cursor);
        }
        return entity;
    }

    public T loadByRowId(long rowId) {
        String[] idArray = {Long.toString(rowId)};
        Cursor cursor = this.db.rawQuery(this.statements.getSelectByRowId(), idArray);
        return loadUniqueAndCloseCursor(cursor);
    }

    protected T loadUniqueAndCloseCursor(Cursor cursor) {
        try {
            return loadUnique(cursor);
        } finally {
            cursor.close();
        }
    }

    protected T loadUnique(Cursor cursor) {
        boolean available = cursor.moveToFirst();
        if (!available) {
            return null;
        }
        if (!cursor.isLast()) {
            throw new DaoException("Expected unique result, but count was " + cursor.getCount());
        }
        return loadCurrent(cursor, 0, true);
    }

    public List<T> loadAll() {
        Cursor cursor = this.db.rawQuery(this.statements.getSelectAll(), null);
        return loadAllAndCloseCursor(cursor);
    }

    public boolean detach(T entity) {
        if (this.identityScope == null) {
            return false;
        }
        K key = getKeyVerified(entity);
        return this.identityScope.detach(key, entity);
    }

    public void detachAll() {
        if (this.identityScope != null) {
            this.identityScope.clear();
        }
    }

    protected List<T> loadAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }

    public void insertInTx(Iterable<T> entities) {
        insertInTx(entities, isEntityUpdateable());
    }

    public void insertInTx(T... entities) {
        insertInTx(Arrays.asList(entities), isEntityUpdateable());
    }

    public void insertInTx(Iterable<T> entities, boolean setPrimaryKey) {
        DatabaseStatement stmt = this.statements.getInsertStatement();
        executeInsertInTx(stmt, entities, setPrimaryKey);
    }

    public void insertOrReplaceInTx(Iterable<T> entities, boolean setPrimaryKey) {
        DatabaseStatement stmt = this.statements.getInsertOrReplaceStatement();
        executeInsertInTx(stmt, entities, setPrimaryKey);
    }

    public void insertOrReplaceInTx(Iterable<T> entities) {
        insertOrReplaceInTx(entities, isEntityUpdateable());
    }

    public void insertOrReplaceInTx(T... entities) {
        insertOrReplaceInTx(Arrays.asList(entities), isEntityUpdateable());
    }

    private void executeInsertInTx(DatabaseStatement stmt, Iterable<T> entities, boolean setPrimaryKey) {
        this.db.beginTransaction();
        try {
            synchronized (stmt) {
                if (this.identityScope != null) {
                    this.identityScope.lock();
                }
                try {
                    if (this.isStandardSQLite) {
                        SQLiteStatement rawStmt = (SQLiteStatement) stmt.getRawStatement();
                        for (T entity : entities) {
                            bindValues(rawStmt, (SQLiteStatement) entity);
                            if (setPrimaryKey) {
                                long rowId = rawStmt.executeInsert();
                                updateKeyAfterInsertAndAttach(entity, rowId, false);
                            } else {
                                rawStmt.execute();
                            }
                        }
                    } else {
                        for (T entity2 : entities) {
                            bindValues(stmt, (DatabaseStatement) entity2);
                            if (setPrimaryKey) {
                                long rowId2 = stmt.executeInsert();
                                updateKeyAfterInsertAndAttach(entity2, rowId2, false);
                            } else {
                                stmt.execute();
                            }
                        }
                    }
                } finally {
                    if (this.identityScope != null) {
                        this.identityScope.unlock();
                    }
                }
            }
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public long insert(T entity) {
        return executeInsert(entity, this.statements.getInsertStatement(), true);
    }

    public long insertWithoutSettingPk(T entity) {
        return executeInsert(entity, this.statements.getInsertOrReplaceStatement(), false);
    }

    public long insertOrReplace(T entity) {
        return executeInsert(entity, this.statements.getInsertOrReplaceStatement(), true);
    }

    private long executeInsert(T entity, DatabaseStatement stmt, boolean setKeyAndAttach) {
        long rowId;
        if (this.db.isDbLockedByCurrentThread()) {
            rowId = insertInsideTx(entity, stmt);
        } else {
            this.db.beginTransaction();
            try {
                rowId = insertInsideTx(entity, stmt);
                this.db.setTransactionSuccessful();
            } finally {
                this.db.endTransaction();
            }
        }
        if (setKeyAndAttach) {
            updateKeyAfterInsertAndAttach(entity, rowId, true);
        }
        return rowId;
    }

    private long insertInsideTx(T entity, DatabaseStatement stmt) {
        long jExecuteInsert;
        synchronized (stmt) {
            if (this.isStandardSQLite) {
                SQLiteStatement rawStmt = (SQLiteStatement) stmt.getRawStatement();
                bindValues(rawStmt, (SQLiteStatement) entity);
                jExecuteInsert = rawStmt.executeInsert();
            } else {
                bindValues(stmt, (DatabaseStatement) entity);
                jExecuteInsert = stmt.executeInsert();
            }
        }
        return jExecuteInsert;
    }

    protected void updateKeyAfterInsertAndAttach(T entity, long rowId, boolean lock) {
        if (rowId != -1) {
            K key = updateKeyAfterInsert(entity, rowId);
            attachEntity(key, entity, lock);
        } else {
            DaoLog.w("Could not insert row (executeInsert returned -1)");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:39:? A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected java.util.List<T> loadAllFromCursor(android.database.Cursor r7) {
        /*
            r6 = this;
            int r0 = r7.getCount()
            if (r0 != 0) goto Lc
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
        Lb:
            return r1
        Lc:
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>(r0)
            r3 = 0
            r2 = 0
            boolean r4 = r7 instanceof android.database.CrossProcessCursor
            if (r4 == 0) goto L2c
            r4 = r7
            android.database.CrossProcessCursor r4 = (android.database.CrossProcessCursor) r4
            android.database.CursorWindow r3 = r4.getWindow()
            if (r3 == 0) goto L2c
            int r4 = r3.getNumRows()
            if (r4 != r0) goto L55
            org.greenrobot.greendao.internal.FastCursor r7 = new org.greenrobot.greendao.internal.FastCursor
            r7.<init>(r3)
            r2 = 1
        L2c:
            boolean r4 = r7.moveToFirst()
            if (r4 == 0) goto Lb
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r4 = r6.identityScope
            if (r4 == 0) goto L40
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r4 = r6.identityScope
            r4.lock()
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r4 = r6.identityScope
            r4.reserveRoom(r0)
        L40:
            if (r2 != 0) goto L7a
            if (r3 == 0) goto L7a
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r4 = r6.identityScope     // Catch: java.lang.Throwable -> L8a
            if (r4 == 0) goto L7a
            r6.loadAllUnlockOnWindowBounds(r7, r3, r1)     // Catch: java.lang.Throwable -> L8a
        L4b:
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r4 = r6.identityScope
            if (r4 == 0) goto Lb
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r4 = r6.identityScope
            r4.unlock()
            goto Lb
        L55:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Window vs. result size: "
            java.lang.StringBuilder r4 = r4.append(r5)
            int r5 = r3.getNumRows()
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = "/"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r4 = r4.append(r0)
            java.lang.String r4 = r4.toString()
            org.greenrobot.greendao.DaoLog.d(r4)
            goto L2c
        L7a:
            r4 = 0
            r5 = 0
            java.lang.Object r4 = r6.loadCurrent(r7, r4, r5)     // Catch: java.lang.Throwable -> L8a
            r1.add(r4)     // Catch: java.lang.Throwable -> L8a
            boolean r4 = r7.moveToNext()     // Catch: java.lang.Throwable -> L8a
            if (r4 != 0) goto L7a
            goto L4b
        L8a:
            r4 = move-exception
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r5 = r6.identityScope
            if (r5 == 0) goto L94
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r5 = r6.identityScope
            r5.unlock()
        L94:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.greenrobot.greendao.AbstractDao.loadAllFromCursor(android.database.Cursor):java.util.List");
    }

    private void loadAllUnlockOnWindowBounds(Cursor cursor, CursorWindow window, List<T> list) {
        int windowEnd = window.getStartPosition() + window.getNumRows();
        int row = 0;
        while (true) {
            list.add(loadCurrent(cursor, 0, false));
            int row2 = row + 1;
            if (row2 >= windowEnd) {
                CursorWindow window2 = moveToNextUnlocked(cursor);
                if (window2 != null) {
                    windowEnd = window2.getStartPosition() + window2.getNumRows();
                } else {
                    return;
                }
            } else if (!cursor.moveToNext()) {
                return;
            }
            row = row2 + 1;
        }
    }

    private CursorWindow moveToNextUnlocked(Cursor cursor) {
        this.identityScope.unlock();
        try {
            if (cursor.moveToNext()) {
                return ((CrossProcessCursor) cursor).getWindow();
            }
            return null;
        } finally {
            this.identityScope.lock();
        }
    }

    protected final T loadCurrent(Cursor cursor, int offset, boolean lock) {
        if (this.identityScopeLong != null) {
            if (offset != 0 && cursor.isNull(this.pkOrdinal + offset)) {
                return null;
            }
            long key = cursor.getLong(this.pkOrdinal + offset);
            T entity = lock ? this.identityScopeLong.get2(key) : this.identityScopeLong.get2NoLock(key);
            if (entity == null) {
                T entity2 = readEntity(cursor, offset);
                attachEntity(entity2);
                if (lock) {
                    this.identityScopeLong.put2(key, entity2);
                    return entity2;
                }
                this.identityScopeLong.put2NoLock(key, entity2);
                return entity2;
            }
            return entity;
        }
        if (this.identityScope != null) {
            K key2 = readKey(cursor, offset);
            if (offset != 0 && key2 == null) {
                return null;
            }
            T entity3 = lock ? this.identityScope.get(key2) : this.identityScope.getNoLock(key2);
            if (entity3 == null) {
                T entity4 = readEntity(cursor, offset);
                attachEntity(key2, entity4, lock);
                return entity4;
            }
            return entity3;
        }
        if (offset != 0 && readKey(cursor, offset) == null) {
            return null;
        }
        T entity5 = readEntity(cursor, offset);
        attachEntity(entity5);
        return entity5;
    }

    protected final <O> O loadCurrentOther(AbstractDao<O, ?> dao, Cursor cursor, int offset) {
        return dao.loadCurrent(cursor, offset, true);
    }

    public List<T> queryRaw(String where, String... selectionArg) {
        Cursor cursor = this.db.rawQuery(this.statements.getSelectAll() + where, selectionArg);
        return loadAllAndCloseCursor(cursor);
    }

    public Query<T> queryRawCreate(String where, Object... selectionArg) {
        List<Object> argList = Arrays.asList(selectionArg);
        return queryRawCreateListArgs(where, argList);
    }

    public Query<T> queryRawCreateListArgs(String where, Collection<Object> selectionArg) {
        return Query.internalCreate(this, this.statements.getSelectAll() + where, selectionArg.toArray());
    }

    public void deleteAll() throws SQLException {
        this.db.execSQL("DELETE FROM '" + this.config.tablename + "'");
        if (this.identityScope != null) {
            this.identityScope.clear();
        }
    }

    public void delete(T entity) {
        assertSinglePk();
        K key = getKeyVerified(entity);
        deleteByKey(key);
    }

    public void deleteByKey(K key) {
        assertSinglePk();
        DatabaseStatement stmt = this.statements.getDeleteStatement();
        if (this.db.isDbLockedByCurrentThread()) {
            synchronized (stmt) {
                deleteByKeyInsideSynchronized(key, stmt);
            }
        } else {
            this.db.beginTransaction();
            try {
                synchronized (stmt) {
                    deleteByKeyInsideSynchronized(key, stmt);
                }
                this.db.setTransactionSuccessful();
            } finally {
                this.db.endTransaction();
            }
        }
        if (this.identityScope != null) {
            this.identityScope.remove((IdentityScope<K, T>) key);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void deleteByKeyInsideSynchronized(K k, DatabaseStatement stmt) {
        if (k instanceof Long) {
            stmt.bindLong(1, ((Long) k).longValue());
        } else {
            if (k == 0) {
                throw new DaoException("Cannot delete entity, key is null");
            }
            stmt.bindString(1, k.toString());
        }
        stmt.execute();
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0053 A[Catch: all -> 0x003c, TRY_ENTER, TryCatch #2 {all -> 0x003c, blocks: (B:9:0x0021, B:10:0x0025, B:12:0x002b, B:14:0x0038, B:28:0x0053, B:29:0x0057, B:31:0x005d, B:33:0x0066), top: B:48:0x0021, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x006e A[Catch: all -> 0x0047, DONT_GENERATE, Merged into TryCatch #1 {all -> 0x004a, blocks: (B:3:0x000f, B:39:0x0074, B:41:0x007b, B:43:0x007f, B:23:0x0049, B:4:0x0010, B:6:0x0014, B:35:0x006a, B:37:0x006e, B:38:0x0073, B:17:0x003d, B:19:0x0041, B:20:0x0046, B:9:0x0021, B:10:0x0025, B:12:0x002b, B:14:0x0038, B:28:0x0053, B:29:0x0057, B:31:0x005d, B:33:0x0066), top: B:47:0x000f }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void deleteInTxInternal(java.lang.Iterable<T> r8, java.lang.Iterable<K> r9) {
        /*
            r7 = this;
            r7.assertSinglePk()
            org.greenrobot.greendao.internal.TableStatements r5 = r7.statements
            org.greenrobot.greendao.database.DatabaseStatement r4 = r5.getDeleteStatement()
            r2 = 0
            org.greenrobot.greendao.database.Database r5 = r7.db
            r5.beginTransaction()
            monitor-enter(r4)     // Catch: java.lang.Throwable -> L4a
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r5 = r7.identityScope     // Catch: java.lang.Throwable -> L47
            if (r5 == 0) goto L1f
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r5 = r7.identityScope     // Catch: java.lang.Throwable -> L47
            r5.lock()     // Catch: java.lang.Throwable -> L47
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L47
            r3.<init>()     // Catch: java.lang.Throwable -> L47
            r2 = r3
        L1f:
            if (r8 == 0) goto L51
            java.util.Iterator r5 = r8.iterator()     // Catch: java.lang.Throwable -> L3c
        L25:
            boolean r6 = r5.hasNext()     // Catch: java.lang.Throwable -> L3c
            if (r6 == 0) goto L51
            java.lang.Object r0 = r5.next()     // Catch: java.lang.Throwable -> L3c
            java.lang.Object r1 = r7.getKeyVerified(r0)     // Catch: java.lang.Throwable -> L3c
            r7.deleteByKeyInsideSynchronized(r1, r4)     // Catch: java.lang.Throwable -> L3c
            if (r2 == 0) goto L25
            r2.add(r1)     // Catch: java.lang.Throwable -> L3c
            goto L25
        L3c:
            r5 = move-exception
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r6 = r7.identityScope     // Catch: java.lang.Throwable -> L47
            if (r6 == 0) goto L46
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r6 = r7.identityScope     // Catch: java.lang.Throwable -> L47
            r6.unlock()     // Catch: java.lang.Throwable -> L47
        L46:
            throw r5     // Catch: java.lang.Throwable -> L47
        L47:
            r5 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L47
            throw r5     // Catch: java.lang.Throwable -> L4a
        L4a:
            r5 = move-exception
            org.greenrobot.greendao.database.Database r6 = r7.db
            r6.endTransaction()
            throw r5
        L51:
            if (r9 == 0) goto L6a
            java.util.Iterator r5 = r9.iterator()     // Catch: java.lang.Throwable -> L3c
        L57:
            boolean r6 = r5.hasNext()     // Catch: java.lang.Throwable -> L3c
            if (r6 == 0) goto L6a
            java.lang.Object r1 = r5.next()     // Catch: java.lang.Throwable -> L3c
            r7.deleteByKeyInsideSynchronized(r1, r4)     // Catch: java.lang.Throwable -> L3c
            if (r2 == 0) goto L57
            r2.add(r1)     // Catch: java.lang.Throwable -> L3c
            goto L57
        L6a:
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r5 = r7.identityScope     // Catch: java.lang.Throwable -> L47
            if (r5 == 0) goto L73
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r5 = r7.identityScope     // Catch: java.lang.Throwable -> L47
            r5.unlock()     // Catch: java.lang.Throwable -> L47
        L73:
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L47
            org.greenrobot.greendao.database.Database r5 = r7.db     // Catch: java.lang.Throwable -> L4a
            r5.setTransactionSuccessful()     // Catch: java.lang.Throwable -> L4a
            if (r2 == 0) goto L84
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r5 = r7.identityScope     // Catch: java.lang.Throwable -> L4a
            if (r5 == 0) goto L84
            org.greenrobot.greendao.identityscope.IdentityScope<K, T> r5 = r7.identityScope     // Catch: java.lang.Throwable -> L4a
            r5.remove(r2)     // Catch: java.lang.Throwable -> L4a
        L84:
            org.greenrobot.greendao.database.Database r5 = r7.db
            r5.endTransaction()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.greenrobot.greendao.AbstractDao.deleteInTxInternal(java.lang.Iterable, java.lang.Iterable):void");
    }

    public void deleteInTx(Iterable<T> entities) {
        deleteInTxInternal(entities, null);
    }

    public void deleteInTx(T... entities) {
        deleteInTxInternal(Arrays.asList(entities), null);
    }

    public void deleteByKeyInTx(Iterable<K> keys) {
        deleteInTxInternal(null, keys);
    }

    public void deleteByKeyInTx(K... keys) {
        deleteInTxInternal(null, Arrays.asList(keys));
    }

    public void refresh(T entity) {
        assertSinglePk();
        K key = getKeyVerified(entity);
        String sql = this.statements.getSelectByKey();
        String[] keyArray = {key.toString()};
        Cursor cursor = this.db.rawQuery(sql, keyArray);
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                throw new DaoException("Entity does not exist in the database anymore: " + entity.getClass() + " with key " + key);
            }
            if (!cursor.isLast()) {
                throw new DaoException("Expected unique result, but count was " + cursor.getCount());
            }
            readEntity(cursor, entity, 0);
            attachEntity(key, entity, true);
        } finally {
            cursor.close();
        }
    }

    public void update(T entity) {
        assertSinglePk();
        DatabaseStatement stmt = this.statements.getUpdateStatement();
        if (this.db.isDbLockedByCurrentThread()) {
            synchronized (stmt) {
                if (this.isStandardSQLite) {
                    updateInsideSynchronized((AbstractDao<T, K>) entity, (SQLiteStatement) stmt.getRawStatement(), true);
                } else {
                    updateInsideSynchronized((AbstractDao<T, K>) entity, stmt, true);
                }
            }
            return;
        }
        this.db.beginTransaction();
        try {
            synchronized (stmt) {
                updateInsideSynchronized((AbstractDao<T, K>) entity, stmt, true);
            }
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public QueryBuilder<T> queryBuilder() {
        return QueryBuilder.internalCreate(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void updateInsideSynchronized(T entity, DatabaseStatement stmt, boolean lock) {
        bindValues(stmt, (DatabaseStatement) entity);
        int index = this.config.allColumns.length + 1;
        Object key = getKey(entity);
        if (key instanceof Long) {
            stmt.bindLong(index, ((Long) key).longValue());
        } else {
            if (key == null) {
                throw new DaoException("Cannot update entity without key - was it inserted before?");
            }
            stmt.bindString(index, key.toString());
        }
        stmt.execute();
        attachEntity(key, entity, lock);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void updateInsideSynchronized(T entity, SQLiteStatement stmt, boolean lock) {
        bindValues(stmt, (SQLiteStatement) entity);
        int index = this.config.allColumns.length + 1;
        Object key = getKey(entity);
        if (key instanceof Long) {
            stmt.bindLong(index, ((Long) key).longValue());
        } else {
            if (key == null) {
                throw new DaoException("Cannot update entity without key - was it inserted before?");
            }
            stmt.bindString(index, key.toString());
        }
        stmt.execute();
        attachEntity(key, entity, lock);
    }

    protected final void attachEntity(K key, T entity, boolean lock) {
        attachEntity(entity);
        if (this.identityScope != null && key != null) {
            if (lock) {
                this.identityScope.put(key, entity);
            } else {
                this.identityScope.putNoLock(key, entity);
            }
        }
    }

    protected void attachEntity(T entity) {
    }

    public void updateInTx(Iterable<T> entities) {
        DatabaseStatement stmt = this.statements.getUpdateStatement();
        this.db.beginTransaction();
        try {
            synchronized (stmt) {
                if (this.identityScope != null) {
                    this.identityScope.lock();
                }
                try {
                    if (this.isStandardSQLite) {
                        SQLiteStatement rawStmt = (SQLiteStatement) stmt.getRawStatement();
                        for (T entity : entities) {
                            updateInsideSynchronized((AbstractDao<T, K>) entity, rawStmt, false);
                        }
                    } else {
                        for (T entity2 : entities) {
                            updateInsideSynchronized((AbstractDao<T, K>) entity2, stmt, false);
                        }
                    }
                } finally {
                    if (this.identityScope != null) {
                        this.identityScope.unlock();
                    }
                }
            }
            this.db.setTransactionSuccessful();
            try {
                this.db.endTransaction();
            } catch (RuntimeException e) {
                if (0 == 0) {
                    throw e;
                }
                DaoLog.w("Could not end transaction (rethrowing initial exception)", e);
                throw null;
            }
        } catch (RuntimeException e2) {
            try {
                this.db.endTransaction();
            } catch (RuntimeException e3) {
                if (e2 == null) {
                    throw e3;
                }
                DaoLog.w("Could not end transaction (rethrowing initial exception)", e3);
                throw e2;
            }
        } catch (Throwable th) {
            try {
                this.db.endTransaction();
                throw th;
            } catch (RuntimeException e4) {
                if (0 == 0) {
                    throw e4;
                }
                DaoLog.w("Could not end transaction (rethrowing initial exception)", e4);
                throw null;
            }
        }
    }

    public void updateInTx(T... entities) {
        updateInTx(Arrays.asList(entities));
    }

    protected void assertSinglePk() {
        if (this.config.pkColumns.length != 1) {
            throw new DaoException(this + " (" + this.config.tablename + ") does not have a single-column primary key");
        }
    }

    public long count() {
        return this.statements.getCountStatement().simpleQueryForLong();
    }

    protected K getKeyVerified(T entity) {
        K key = getKey(entity);
        if (key == null) {
            if (entity == null) {
                throw new NullPointerException("Entity may not be null");
            }
            throw new DaoException("Entity has no key");
        }
        return key;
    }

    public Database getDatabase() {
        return this.db;
    }
}
