package org.greenrobot.greendao;

import java.util.HashMap;
import java.util.Map;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

/* loaded from: classes.dex */
public abstract class AbstractDaoMaster {
    protected final Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap = new HashMap();
    protected final Database db;
    protected final int schemaVersion;

    public abstract AbstractDaoSession newSession();

    public abstract AbstractDaoSession newSession(IdentityScopeType identityScopeType);

    public AbstractDaoMaster(Database db, int schemaVersion) {
        this.db = db;
        this.schemaVersion = schemaVersion;
    }

    protected void registerDaoClass(Class<? extends AbstractDao<?, ?>> daoClass) {
        DaoConfig daoConfig = new DaoConfig(this.db, daoClass);
        this.daoConfigMap.put(daoClass, daoConfig);
    }

    public int getSchemaVersion() {
        return this.schemaVersion;
    }

    public Database getDatabase() {
        return this.db;
    }
}
