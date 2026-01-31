package org.greenrobot.greendao;

import java.util.Collection;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.query.WhereCondition;

/* loaded from: classes.dex */
public class Property {
    public final String columnName;
    public final String name;
    public final int ordinal;
    public final boolean primaryKey;
    public final Class<?> type;

    public Property(int ordinal, Class<?> type, String name, boolean primaryKey, String columnName) {
        this.ordinal = ordinal;
        this.type = type;
        this.name = name;
        this.primaryKey = primaryKey;
        this.columnName = columnName;
    }

    public WhereCondition eq(Object value) {
        return new WhereCondition.PropertyCondition(this, "=?", value);
    }

    public WhereCondition notEq(Object value) {
        return new WhereCondition.PropertyCondition(this, "<>?", value);
    }

    public WhereCondition like(String value) {
        return new WhereCondition.PropertyCondition(this, " LIKE ?", value);
    }

    public WhereCondition between(Object value1, Object value2) {
        Object[] values = {value1, value2};
        return new WhereCondition.PropertyCondition(this, " BETWEEN ? AND ?", values);
    }

    public WhereCondition in(Object... inValues) {
        StringBuilder condition = new StringBuilder(" IN (");
        SqlUtils.appendPlaceholders(condition, inValues.length).append(')');
        return new WhereCondition.PropertyCondition(this, condition.toString(), inValues);
    }

    public WhereCondition in(Collection<?> inValues) {
        return in(inValues.toArray());
    }

    public WhereCondition notIn(Object... notInValues) {
        StringBuilder condition = new StringBuilder(" NOT IN (");
        SqlUtils.appendPlaceholders(condition, notInValues.length).append(')');
        return new WhereCondition.PropertyCondition(this, condition.toString(), notInValues);
    }

    public WhereCondition notIn(Collection<?> notInValues) {
        return notIn(notInValues.toArray());
    }

    public WhereCondition gt(Object value) {
        return new WhereCondition.PropertyCondition(this, ">?", value);
    }

    public WhereCondition lt(Object value) {
        return new WhereCondition.PropertyCondition(this, "<?", value);
    }

    public WhereCondition ge(Object value) {
        return new WhereCondition.PropertyCondition(this, ">=?", value);
    }

    public WhereCondition le(Object value) {
        return new WhereCondition.PropertyCondition(this, "<=?", value);
    }

    public WhereCondition isNull() {
        return new WhereCondition.PropertyCondition(this, " IS NULL");
    }

    public WhereCondition isNotNull() {
        return new WhereCondition.PropertyCondition(this, " IS NOT NULL");
    }
}
