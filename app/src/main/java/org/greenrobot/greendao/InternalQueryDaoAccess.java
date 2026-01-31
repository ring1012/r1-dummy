package org.greenrobot.greendao;

import android.database.Cursor;
import java.util.List;
import org.greenrobot.greendao.internal.TableStatements;

/* loaded from: classes.dex */
public final class InternalQueryDaoAccess<T> {
    private final AbstractDao<T, ?> dao;

    public InternalQueryDaoAccess(AbstractDao<T, ?> abstractDao) {
        this.dao = abstractDao;
    }

    public T loadCurrent(Cursor cursor, int offset, boolean lock) {
        return this.dao.loadCurrent(cursor, offset, lock);
    }

    public List<T> loadAllAndCloseCursor(Cursor cursor) {
        return this.dao.loadAllAndCloseCursor(cursor);
    }

    public T loadUniqueAndCloseCursor(Cursor cursor) {
        return this.dao.loadUniqueAndCloseCursor(cursor);
    }

    public TableStatements getStatements() {
        return this.dao.getStatements();
    }

    public static <T2> TableStatements getStatements(AbstractDao<T2, ?> dao) {
        return dao.getStatements();
    }
}
