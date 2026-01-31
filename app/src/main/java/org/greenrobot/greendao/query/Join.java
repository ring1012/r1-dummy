package org.greenrobot.greendao.query;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;

/* loaded from: classes.dex */
public class Join<SRC, DST> {
    final AbstractDao<DST, ?> daoDestination;
    final Property joinPropertyDestination;
    final Property joinPropertySource;
    final String sourceTablePrefix;
    final String tablePrefix;
    final WhereCollector<DST> whereCollector;

    public Join(String sourceTablePrefix, Property sourceJoinProperty, AbstractDao<DST, ?> daoDestination, Property destinationJoinProperty, String joinTablePrefix) {
        this.sourceTablePrefix = sourceTablePrefix;
        this.joinPropertySource = sourceJoinProperty;
        this.daoDestination = daoDestination;
        this.joinPropertyDestination = destinationJoinProperty;
        this.tablePrefix = joinTablePrefix;
        this.whereCollector = new WhereCollector<>(daoDestination, joinTablePrefix);
    }

    public Join<SRC, DST> where(WhereCondition cond, WhereCondition... condMore) {
        this.whereCollector.add(cond, condMore);
        return this;
    }

    public Join<SRC, DST> whereOr(WhereCondition cond1, WhereCondition cond2, WhereCondition... condMore) {
        this.whereCollector.add(or(cond1, cond2, condMore), new WhereCondition[0]);
        return this;
    }

    public WhereCondition or(WhereCondition cond1, WhereCondition cond2, WhereCondition... condMore) {
        return this.whereCollector.combineWhereConditions(" OR ", cond1, cond2, condMore);
    }

    public WhereCondition and(WhereCondition cond1, WhereCondition cond2, WhereCondition... condMore) {
        return this.whereCollector.combineWhereConditions(" AND ", cond1, cond2, condMore);
    }

    public String getTablePrefix() {
        return this.tablePrefix;
    }
}
