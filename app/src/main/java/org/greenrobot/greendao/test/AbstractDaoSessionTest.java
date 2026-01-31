package org.greenrobot.greendao.test;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;

/* loaded from: classes.dex */
public abstract class AbstractDaoSessionTest<T extends AbstractDaoMaster, S extends AbstractDaoSession> extends DbTest {
    protected T daoMaster;
    private final Class<T> daoMasterClass;
    protected S daoSession;

    public AbstractDaoSessionTest(Class<T> daoMasterClass) {
        this(daoMasterClass, true);
    }

    public AbstractDaoSessionTest(Class<T> daoMasterClass, boolean inMemory) {
        super(inMemory);
        this.daoMasterClass = daoMasterClass;
    }

    @Override // org.greenrobot.greendao.test.DbTest
    protected void setUp() throws Exception {
        super.setUp();
        try {
            this.daoMaster = this.daoMasterClass.getConstructor(Database.class).newInstance(this.db);
            this.daoMasterClass.getMethod("createAllTables", Database.class, Boolean.TYPE).invoke(null, this.db, false);
            this.daoSession = (S) this.daoMaster.newSession();
        } catch (Exception e) {
            throw new RuntimeException("Could not prepare DAO session test", e);
        }
    }
}
