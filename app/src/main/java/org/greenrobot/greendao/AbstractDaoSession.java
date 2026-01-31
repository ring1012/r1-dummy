package org.greenrobot.greendao;

import android.database.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

/* loaded from: classes.dex */
public class AbstractDaoSession {
    private final Database db;
    private final Map<Class<?>, AbstractDao<?, ?>> entityToDao = new HashMap();

    public AbstractDaoSession(Database db) {
        this.db = db;
    }

    protected <T> void registerDao(Class<T> entityClass, AbstractDao<T, ?> dao) {
        this.entityToDao.put(entityClass, dao);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> long insert(T entity) {
        return getDao(entity.getClass()).insert(entity);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> long insertOrReplace(T entity) {
        return getDao(entity.getClass()).insertOrReplace(entity);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> void refresh(T entity) {
        getDao(entity.getClass()).refresh(entity);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> void update(T entity) {
        getDao(entity.getClass()).update(entity);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> void delete(T entity) {
        getDao(entity.getClass()).delete(entity);
    }

    public <T> void deleteAll(Class<T> entityClass) throws SQLException {
        getDao(entityClass).deleteAll();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T, K> T load(Class<T> cls, K k) {
        return (T) getDao(cls).load(k);
    }

    public <T, K> List<T> loadAll(Class<T> cls) {
        return (List<T>) getDao(cls).loadAll();
    }

    public <T, K> List<T> queryRaw(Class<T> cls, String str, String... strArr) {
        return (List<T>) getDao(cls).queryRaw(str, strArr);
    }

    public <T> QueryBuilder<T> queryBuilder(Class<T> cls) {
        return (QueryBuilder<T>) getDao(cls).queryBuilder();
    }

    public AbstractDao<?, ?> getDao(Class<? extends Object> entityClass) {
        AbstractDao<?, ?> dao = this.entityToDao.get(entityClass);
        if (dao == null) {
            throw new DaoException("No DAO registered for " + entityClass);
        }
        return dao;
    }

    public void runInTx(Runnable runnable) {
        this.db.beginTransaction();
        try {
            runnable.run();
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public <V> V callInTx(Callable<V> callable) throws Exception {
        this.db.beginTransaction();
        try {
            V result = callable.call();
            this.db.setTransactionSuccessful();
            return result;
        } finally {
            this.db.endTransaction();
        }
    }

    public <V> V callInTxNoException(Callable<V> callable) {
        this.db.beginTransaction();
        try {
            try {
                V result = callable.call();
                this.db.setTransactionSuccessful();
                return result;
            } catch (Exception e) {
                throw new DaoException("Callable failed", e);
            }
        } finally {
            this.db.endTransaction();
        }
    }

    public Database getDatabase() {
        return this.db;
    }

    public Collection<AbstractDao<?, ?>> getAllDaos() {
        return Collections.unmodifiableCollection(this.entityToDao.values());
    }

    public AsyncSession startAsyncSession() {
        return new AsyncSession(this);
    }
}
