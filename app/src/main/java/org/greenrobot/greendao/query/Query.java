package org.greenrobot.greendao.query;

import android.database.Cursor;
import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;

/* loaded from: classes.dex */
public class Query<T> extends AbstractQueryWithLimit<T> {
    private final QueryData<T> queryData;

    @Override // org.greenrobot.greendao.query.AbstractQueryWithLimit
    public /* bridge */ /* synthetic */ void setLimit(int i) {
        super.setLimit(i);
    }

    @Override // org.greenrobot.greendao.query.AbstractQueryWithLimit
    public /* bridge */ /* synthetic */ void setOffset(int i) {
        super.setOffset(i);
    }

    private static final class QueryData<T2> extends AbstractQueryData<T2, Query<T2>> {
        private final int limitPosition;
        private final int offsetPosition;

        QueryData(AbstractDao<T2, ?> dao, String sql, String[] initialValues, int limitPosition, int offsetPosition) {
            super(dao, sql, initialValues);
            this.limitPosition = limitPosition;
            this.offsetPosition = offsetPosition;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.greenrobot.greendao.query.AbstractQueryData
        public Query<T2> createQuery() {
            return new Query<>(this, this.dao, this.sql, (String[]) this.initialValues.clone(), this.limitPosition, this.offsetPosition);
        }
    }

    public static <T2> Query<T2> internalCreate(AbstractDao<T2, ?> dao, String sql, Object[] initialValues) {
        return create(dao, sql, initialValues, -1, -1);
    }

    static <T2> Query<T2> create(AbstractDao<T2, ?> dao, String sql, Object[] initialValues, int limitPosition, int offsetPosition) {
        QueryData<T2> queryData = new QueryData<>(dao, sql, toStringArray(initialValues), limitPosition, offsetPosition);
        return queryData.forCurrentThread();
    }

    private Query(QueryData<T> queryData, AbstractDao<T, ?> dao, String sql, String[] initialValues, int limitPosition, int offsetPosition) {
        super(dao, sql, initialValues, limitPosition, offsetPosition);
        this.queryData = queryData;
    }

    public Query<T> forCurrentThread() {
        return (Query) this.queryData.forCurrentThread(this);
    }

    public List<T> list() {
        checkThread();
        Cursor cursor = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
        return this.daoAccess.loadAllAndCloseCursor(cursor);
    }

    public LazyList<T> listLazy() {
        checkThread();
        Cursor cursor = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
        return new LazyList<>(this.daoAccess, cursor, true);
    }

    public LazyList<T> listLazyUncached() {
        checkThread();
        Cursor cursor = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
        return new LazyList<>(this.daoAccess, cursor, false);
    }

    public CloseableListIterator<T> listIterator() {
        return listLazyUncached().listIteratorAutoClose();
    }

    public T unique() {
        checkThread();
        Cursor cursor = this.dao.getDatabase().rawQuery(this.sql, this.parameters);
        return this.daoAccess.loadUniqueAndCloseCursor(cursor);
    }

    public T uniqueOrThrow() {
        T entity = unique();
        if (entity == null) {
            throw new DaoException("No entity found for query");
        }
        return entity;
    }

    @Override // org.greenrobot.greendao.query.AbstractQueryWithLimit, org.greenrobot.greendao.query.AbstractQuery
    public Query<T> setParameter(int index, Object parameter) {
        return (Query) super.setParameter(index, parameter);
    }

    @Override // org.greenrobot.greendao.query.AbstractQueryWithLimit
    public Query<T> setParameter(int index, Date parameter) {
        return (Query) super.setParameter(index, parameter);
    }

    @Override // org.greenrobot.greendao.query.AbstractQueryWithLimit
    public Query<T> setParameter(int index, Boolean parameter) {
        return (Query) super.setParameter(index, parameter);
    }
}
