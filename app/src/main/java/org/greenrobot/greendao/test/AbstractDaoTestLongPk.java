package org.greenrobot.greendao.test;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoLog;

/* loaded from: classes.dex */
public abstract class AbstractDaoTestLongPk<D extends AbstractDao<T, Long>, T> extends AbstractDaoTestSinglePk<D, T, Long> {
    public AbstractDaoTestLongPk(Class<D> daoClass) {
        super(daoClass);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.greenrobot.greendao.test.AbstractDaoTestSinglePk
    public Long createRandomPk() {
        return Long.valueOf(this.random.nextLong());
    }

    public void testAssignPk() {
        if (this.daoAccess.isEntityUpdateable()) {
            T entity1 = createEntity(null);
            if (entity1 != null) {
                T entity2 = createEntity(null);
                this.dao.insert(entity1);
                this.dao.insert(entity2);
                Long pk1 = (Long) this.daoAccess.getKey(entity1);
                assertNotNull(pk1);
                Long pk2 = (Long) this.daoAccess.getKey(entity2);
                assertNotNull(pk2);
                assertFalse(pk1.equals(pk2));
                assertNotNull(this.dao.load(pk1));
                assertNotNull(this.dao.load(pk2));
                return;
            }
            DaoLog.d("Skipping testAssignPk for " + this.daoClass + " (createEntity returned null for null key)");
            return;
        }
        DaoLog.d("Skipping testAssignPk for not updateable " + this.daoClass);
    }
}
