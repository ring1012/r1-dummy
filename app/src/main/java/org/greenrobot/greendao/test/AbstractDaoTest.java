package org.greenrobot.greendao.test;

import java.lang.reflect.Method;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.InternalUnitTestDaoAccess;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScope;

/* loaded from: classes.dex */
public abstract class AbstractDaoTest<D extends AbstractDao<T, K>, T, K> extends DbTest {
    protected D dao;
    protected InternalUnitTestDaoAccess<T, K> daoAccess;
    protected final Class<D> daoClass;
    protected IdentityScope<K, T> identityScopeForDao;
    protected Property pkColumn;

    public AbstractDaoTest(Class<D> daoClass) {
        this(daoClass, true);
    }

    public AbstractDaoTest(Class<D> daoClass, boolean inMemory) {
        super(inMemory);
        this.daoClass = daoClass;
    }

    public void setIdentityScopeBeforeSetUp(IdentityScope<K, T> identityScope) {
        this.identityScopeForDao = identityScope;
    }

    @Override // org.greenrobot.greendao.test.DbTest
    protected void setUp() throws Exception {
        super.setUp();
        try {
            setUpTableForDao();
            this.daoAccess = new InternalUnitTestDaoAccess<>(this.db, this.daoClass, this.identityScopeForDao);
            this.dao = this.daoAccess.getDao();
        } catch (Exception e) {
            throw new RuntimeException("Could not prepare DAO Test", e);
        }
    }

    protected void setUpTableForDao() throws Exception {
        try {
            Method createTableMethod = this.daoClass.getMethod("createTable", Database.class, Boolean.TYPE);
            createTableMethod.invoke(null, this.db, false);
        } catch (NoSuchMethodException e) {
            DaoLog.i("No createTable method");
        }
    }

    protected void clearIdentityScopeIfAny() {
        if (this.identityScopeForDao != null) {
            this.identityScopeForDao.clear();
            DaoLog.d("Identity scope cleared");
        } else {
            DaoLog.d("No identity scope to clear");
        }
    }

    protected void logTableDump() {
        logTableDump(this.dao.getTablename());
    }
}
