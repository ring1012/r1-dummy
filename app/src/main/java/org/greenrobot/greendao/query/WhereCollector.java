package org.greenrobot.greendao.query;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.WhereCondition;

/* loaded from: classes.dex */
class WhereCollector<T> {
    private final AbstractDao<T, ?> dao;
    private final String tablePrefix;
    private final List<WhereCondition> whereConditions = new ArrayList();

    WhereCollector(AbstractDao<T, ?> dao, String tablePrefix) {
        this.dao = dao;
        this.tablePrefix = tablePrefix;
    }

    void add(WhereCondition cond, WhereCondition... condMore) {
        checkCondition(cond);
        this.whereConditions.add(cond);
        for (WhereCondition whereCondition : condMore) {
            checkCondition(whereCondition);
            this.whereConditions.add(whereCondition);
        }
    }

    WhereCondition combineWhereConditions(String combineOp, WhereCondition cond1, WhereCondition cond2, WhereCondition... condMore) {
        StringBuilder builder = new StringBuilder("(");
        List<Object> combinedValues = new ArrayList<>();
        addCondition(builder, combinedValues, cond1);
        builder.append(combineOp);
        addCondition(builder, combinedValues, cond2);
        for (WhereCondition cond : condMore) {
            builder.append(combineOp);
            addCondition(builder, combinedValues, cond);
        }
        builder.append(')');
        return new WhereCondition.StringCondition(builder.toString(), combinedValues.toArray());
    }

    void addCondition(StringBuilder builder, List<Object> values, WhereCondition condition) {
        checkCondition(condition);
        condition.appendTo(builder, this.tablePrefix);
        condition.appendValuesTo(values);
    }

    void checkCondition(WhereCondition whereCondition) {
        if (whereCondition instanceof WhereCondition.PropertyCondition) {
            checkProperty(((WhereCondition.PropertyCondition) whereCondition).property);
        }
    }

    void checkProperty(Property property) {
        if (this.dao != null) {
            Property[] properties = this.dao.getProperties();
            boolean found = false;
            int length = properties.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Property property2 = properties[i];
                if (property != property2) {
                    i++;
                } else {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new DaoException("Property '" + property.name + "' is not part of " + this.dao);
            }
        }
    }

    void appendWhereClause(StringBuilder builder, String tablePrefixOrNull, List<Object> values) {
        ListIterator<WhereCondition> iter = this.whereConditions.listIterator();
        while (iter.hasNext()) {
            if (iter.hasPrevious()) {
                builder.append(" AND ");
            }
            WhereCondition condition = iter.next();
            condition.appendTo(builder, tablePrefixOrNull);
            condition.appendValuesTo(values);
        }
    }

    boolean isEmpty() {
        return this.whereConditions.isEmpty();
    }
}
