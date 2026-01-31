package org.litepal.crud;

import java.util.List;
import org.litepal.tablemanager.Connector;
import org.litepal.util.BaseUtility;

/* loaded from: classes.dex */
public class ClusterQuery {
    String[] mColumns;
    String[] mConditions;
    String mLimit;
    String mOffset;
    String mOrderBy;

    ClusterQuery() {
    }

    public ClusterQuery select(String... columns) {
        this.mColumns = columns;
        return this;
    }

    public ClusterQuery where(String... conditions) {
        this.mConditions = conditions;
        return this;
    }

    public ClusterQuery order(String column) {
        this.mOrderBy = column;
        return this;
    }

    public ClusterQuery limit(int value) {
        this.mLimit = String.valueOf(value);
        return this;
    }

    public ClusterQuery offset(int value) {
        this.mOffset = String.valueOf(value);
        return this;
    }

    public <T> List<T> find(Class<T> modelClass) {
        return find(modelClass, false);
    }

    public synchronized <T> List<T> find(Class<T> modelClass, boolean isEager) {
        QueryHandler queryHandler;
        String limit;
        queryHandler = new QueryHandler(Connector.getDatabase());
        if (this.mOffset == null) {
            limit = this.mLimit;
        } else {
            if (this.mLimit == null) {
                this.mLimit = "0";
            }
            limit = String.valueOf(this.mOffset) + "," + this.mLimit;
        }
        return queryHandler.onFind(modelClass, this.mColumns, this.mConditions, this.mOrderBy, limit, isEager);
    }

    public synchronized int count(Class<?> modelClass) {
        return count(BaseUtility.changeCase(modelClass.getSimpleName()));
    }

    public synchronized int count(String tableName) {
        QueryHandler queryHandler;
        queryHandler = new QueryHandler(Connector.getDatabase());
        return queryHandler.onCount(tableName, this.mConditions);
    }

    public synchronized double average(Class<?> modelClass, String column) {
        return average(BaseUtility.changeCase(modelClass.getSimpleName()), column);
    }

    public synchronized double average(String tableName, String column) {
        QueryHandler queryHandler;
        queryHandler = new QueryHandler(Connector.getDatabase());
        return queryHandler.onAverage(tableName, column, this.mConditions);
    }

    public synchronized <T> T max(Class<?> cls, String str, Class<T> cls2) {
        return (T) max(BaseUtility.changeCase(cls.getSimpleName()), str, cls2);
    }

    public synchronized <T> T max(String str, String str2, Class<T> cls) {
        return (T) new QueryHandler(Connector.getDatabase()).onMax(str, str2, this.mConditions, cls);
    }

    public synchronized <T> T min(Class<?> cls, String str, Class<T> cls2) {
        return (T) min(BaseUtility.changeCase(cls.getSimpleName()), str, cls2);
    }

    public synchronized <T> T min(String str, String str2, Class<T> cls) {
        return (T) new QueryHandler(Connector.getDatabase()).onMin(str, str2, this.mConditions, cls);
    }

    public synchronized <T> T sum(Class<?> cls, String str, Class<T> cls2) {
        return (T) sum(BaseUtility.changeCase(cls.getSimpleName()), str, cls2);
    }

    public synchronized <T> T sum(String str, String str2, Class<T> cls) {
        return (T) new QueryHandler(Connector.getDatabase()).onSum(str, str2, this.mConditions, cls);
    }
}
