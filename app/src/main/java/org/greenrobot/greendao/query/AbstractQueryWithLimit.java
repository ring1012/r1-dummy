package org.greenrobot.greendao.query;

import java.util.Date;
import org.greenrobot.greendao.AbstractDao;

/* loaded from: classes.dex */
abstract class AbstractQueryWithLimit<T> extends AbstractQuery<T> {
    protected final int limitPosition;
    protected final int offsetPosition;

    protected AbstractQueryWithLimit(AbstractDao<T, ?> dao, String sql, String[] initialValues, int limitPosition, int offsetPosition) {
        super(dao, sql, initialValues);
        this.limitPosition = limitPosition;
        this.offsetPosition = offsetPosition;
    }

    @Override // org.greenrobot.greendao.query.AbstractQuery
    public AbstractQueryWithLimit<T> setParameter(int index, Object parameter) {
        if (index >= 0 && (index == this.limitPosition || index == this.offsetPosition)) {
            throw new IllegalArgumentException("Illegal parameter index: " + index);
        }
        return (AbstractQueryWithLimit) super.setParameter(index, parameter);
    }

    public AbstractQueryWithLimit<T> setParameter(int index, Date parameter) {
        Long converted = parameter != null ? Long.valueOf(parameter.getTime()) : null;
        return setParameter(index, (Object) converted);
    }

    public AbstractQueryWithLimit<T> setParameter(int index, Boolean parameter) {
        Integer converted;
        if (parameter != null) {
            converted = Integer.valueOf(parameter.booleanValue() ? 1 : 0);
        } else {
            converted = null;
        }
        return setParameter(index, (Object) converted);
    }

    public void setLimit(int limit) {
        checkThread();
        if (this.limitPosition == -1) {
            throw new IllegalStateException("Limit must be set with QueryBuilder before it can be used here");
        }
        this.parameters[this.limitPosition] = Integer.toString(limit);
    }

    public void setOffset(int offset) {
        checkThread();
        if (this.offsetPosition == -1) {
            throw new IllegalStateException("Offset must be set with QueryBuilder before it can be used here");
        }
        this.parameters[this.offsetPosition] = Integer.toString(offset);
    }
}
