package org.greenrobot.greendao.query;

import android.database.Cursor;
import java.util.Date;
import org.greenrobot.greendao.AbstractDao;

/* loaded from: classes.dex */
public class CursorQuery<T> extends AbstractQueryWithLimit<T> {
    private final QueryData<T> queryData;

    @Override // org.greenrobot.greendao.query.AbstractQueryWithLimit
    public /* bridge */ /* synthetic */ void setLimit(int i) {
        super.setLimit(i);
    }

    @Override // org.greenrobot.greendao.query.AbstractQueryWithLimit
    public /* bridge */ /* synthetic */ void setOffset(int i) {
        super.setOffset(i);
    }

    @Override // org.greenrobot.greendao.query.AbstractQueryWithLimit
    public /* bridge */ /* synthetic */ AbstractQueryWithLimit setParameter(int i, Boolean bool) {
        return super.setParameter(i, bool);
    }

    @Override // org.greenrobot.greendao.query.AbstractQueryWithLimit, org.greenrobot.greendao.query.AbstractQuery
    public /* bridge */ /* synthetic */ AbstractQueryWithLimit setParameter(int i, Object obj) {
        return super.setParameter(i, obj);
    }

    @Override // org.greenrobot.greendao.query.AbstractQueryWithLimit
    public /* bridge */ /* synthetic */ AbstractQueryWithLimit setParameter(int i, Date date) {
        return super.setParameter(i, date);
    }

    private static final class QueryData<T2> extends AbstractQueryData<T2, CursorQuery<T2>> {
        private final int limitPosition;
        private final int offsetPosition;

        QueryData(AbstractDao dao, String sql, String[] initialValues, int limitPosition, int offsetPosition) {
            super(dao, sql, initialValues);
            this.limitPosition = limitPosition;
            this.offsetPosition = offsetPosition;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.greenrobot.greendao.query.AbstractQueryData
        public CursorQuery<T2> createQuery() {
            return new CursorQuery<>(this, this.dao, this.sql, (String[]) this.initialValues.clone(), this.limitPosition, this.offsetPosition);
        }
    }

    public static <T2> CursorQuery<T2> internalCreate(AbstractDao<T2, ?> dao, String sql, Object[] initialValues) {
        return create(dao, sql, initialValues, -1, -1);
    }

    static <T2> CursorQuery<T2> create(AbstractDao<T2, ?> dao, String sql, Object[] initialValues, int limitPosition, int offsetPosition) {
        QueryData<T2> queryData = new QueryData<>(dao, sql, toStringArray(initialValues), limitPosition, offsetPosition);
        return queryData.forCurrentThread();
    }

    private CursorQuery(QueryData<T> queryData, AbstractDao<T, ?> dao, String sql, String[] initialValues, int limitPosition, int offsetPosition) {
        super(dao, sql, initialValues, limitPosition, offsetPosition);
        this.queryData = queryData;
    }

    public CursorQuery forCurrentThread() {
        return this.queryData.forCurrentThread(this);
    }

    public Cursor query() {
        checkThread();
        return this.dao.getDatabase().rawQuery(this.sql, this.parameters);
    }
}
