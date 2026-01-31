package org.litepal.crud;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.litepal.exceptions.DataSupportException;
import org.litepal.tablemanager.Connector;
import org.litepal.util.BaseUtility;
import org.litepal.util.DBUtility;

/* loaded from: classes.dex */
public class DataSupport {
    private Map<String, Set<Long>> associatedModelsMapForJoinTable;
    private Map<String, Set<Long>> associatedModelsMapWithFK;
    private Map<String, Long> associatedModelsMapWithoutFK;
    private long baseObjId;
    private List<String> fieldsToSetToDefault;
    private List<String> listToClearAssociatedFK;
    private List<String> listToClearSelfFK;

    public static synchronized ClusterQuery select(String... columns) {
        ClusterQuery cQuery;
        cQuery = new ClusterQuery();
        cQuery.mColumns = columns;
        return cQuery;
    }

    public static synchronized ClusterQuery where(String... conditions) {
        ClusterQuery cQuery;
        cQuery = new ClusterQuery();
        cQuery.mConditions = conditions;
        return cQuery;
    }

    public static synchronized ClusterQuery order(String column) {
        ClusterQuery cQuery;
        cQuery = new ClusterQuery();
        cQuery.mOrderBy = column;
        return cQuery;
    }

    public static synchronized ClusterQuery limit(int value) {
        ClusterQuery cQuery;
        cQuery = new ClusterQuery();
        cQuery.mLimit = String.valueOf(value);
        return cQuery;
    }

    public static synchronized ClusterQuery offset(int value) {
        ClusterQuery cQuery;
        cQuery = new ClusterQuery();
        cQuery.mOffset = String.valueOf(value);
        return cQuery;
    }

    public static synchronized int count(Class<?> modelClass) {
        return count(BaseUtility.changeCase(DBUtility.getTableNameByClassName(modelClass.getName())));
    }

    public static synchronized int count(String tableName) {
        ClusterQuery cQuery;
        cQuery = new ClusterQuery();
        return cQuery.count(tableName);
    }

    public static synchronized double average(Class<?> modelClass, String column) {
        return average(BaseUtility.changeCase(DBUtility.getTableNameByClassName(modelClass.getName())), column);
    }

    public static synchronized double average(String tableName, String column) {
        ClusterQuery cQuery;
        cQuery = new ClusterQuery();
        return cQuery.average(tableName, column);
    }

    public static synchronized <T> T max(Class<?> cls, String str, Class<T> cls2) {
        return (T) max(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), str, cls2);
    }

    public static synchronized <T> T max(String str, String str2, Class<T> cls) {
        return (T) new ClusterQuery().max(str, str2, cls);
    }

    public static synchronized <T> T min(Class<?> cls, String str, Class<T> cls2) {
        return (T) min(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), str, cls2);
    }

    public static synchronized <T> T min(String str, String str2, Class<T> cls) {
        return (T) new ClusterQuery().min(str, str2, cls);
    }

    public static synchronized <T> T sum(Class<?> cls, String str, Class<T> cls2) {
        return (T) sum(BaseUtility.changeCase(DBUtility.getTableNameByClassName(cls.getName())), str, cls2);
    }

    public static synchronized <T> T sum(String str, String str2, Class<T> cls) {
        return (T) new ClusterQuery().sum(str, str2, cls);
    }

    public static synchronized <T> T find(Class<T> cls, long j) {
        return (T) find(cls, j, false);
    }

    public static synchronized <T> T find(Class<T> cls, long j, boolean z) {
        return (T) new QueryHandler(Connector.getDatabase()).onFind(cls, j, z);
    }

    public static synchronized <T> T findFirst(Class<T> cls) {
        return (T) findFirst(cls, false);
    }

    public static synchronized <T> T findFirst(Class<T> cls, boolean z) {
        return (T) new QueryHandler(Connector.getDatabase()).onFindFirst(cls, z);
    }

    public static synchronized <T> T findLast(Class<T> cls) {
        return (T) findLast(cls, false);
    }

    public static synchronized <T> T findLast(Class<T> cls, boolean z) {
        return (T) new QueryHandler(Connector.getDatabase()).onFindLast(cls, z);
    }

    public static synchronized <T> List<T> findAll(Class<T> modelClass, long... ids) {
        return findAll(modelClass, false, ids);
    }

    public static synchronized <T> List<T> findAll(Class<T> modelClass, boolean isEager, long... ids) {
        QueryHandler queryHandler;
        queryHandler = new QueryHandler(Connector.getDatabase());
        return queryHandler.onFindAll(modelClass, isEager, ids);
    }

    public static synchronized Cursor findBySQL(String... sql) {
        String[] selectionArgs;
        Cursor cursorRawQuery = null;
        synchronized (DataSupport.class) {
            BaseUtility.checkConditionsCorrect(sql);
            if (sql != null && sql.length > 0) {
                if (sql.length == 1) {
                    selectionArgs = null;
                } else {
                    selectionArgs = new String[sql.length - 1];
                    System.arraycopy(sql, 1, selectionArgs, 0, sql.length - 1);
                }
                cursorRawQuery = Connector.getDatabase().rawQuery(sql[0], selectionArgs);
            }
        }
        return cursorRawQuery;
    }

    public static synchronized int delete(Class<?> modelClass, long id) {
        int rowsAffected;
        SQLiteDatabase db = Connector.getDatabase();
        db.beginTransaction();
        try {
            DeleteHandler deleteHandler = new DeleteHandler(db);
            rowsAffected = deleteHandler.onDelete(modelClass, id);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return rowsAffected;
    }

    public static synchronized int deleteAll(Class<?> modelClass, String... conditions) {
        DeleteHandler deleteHandler;
        deleteHandler = new DeleteHandler(Connector.getDatabase());
        return deleteHandler.onDeleteAll(modelClass, conditions);
    }

    public static synchronized int deleteAll(String tableName, String... conditions) {
        DeleteHandler deleteHandler;
        deleteHandler = new DeleteHandler(Connector.getDatabase());
        return deleteHandler.onDeleteAll(tableName, conditions);
    }

    public static synchronized int update(Class<?> modelClass, ContentValues values, long id) {
        UpdateHandler updateHandler;
        updateHandler = new UpdateHandler(Connector.getDatabase());
        return updateHandler.onUpdate(modelClass, id, values);
    }

    public static synchronized int updateAll(Class<?> modelClass, ContentValues values, String... conditions) {
        return updateAll(BaseUtility.changeCase(DBUtility.getTableNameByClassName(modelClass.getName())), values, conditions);
    }

    public static synchronized int updateAll(String tableName, ContentValues values, String... conditions) {
        UpdateHandler updateHandler;
        updateHandler = new UpdateHandler(Connector.getDatabase());
        return updateHandler.onUpdateAll(tableName, values, conditions);
    }

    public static synchronized <T extends DataSupport> void saveAll(Collection<T> collection) {
        SQLiteDatabase db = Connector.getDatabase();
        db.beginTransaction();
        try {
            try {
                SaveHandler saveHandler = new SaveHandler(db);
                saveHandler.onSaveAll(collection);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                throw new DataSupportException(e.getMessage());
            }
        } finally {
            db.endTransaction();
        }
    }

    public static <T extends DataSupport> void markAsDeleted(Collection<T> collection) {
        for (T t : collection) {
            t.clearSavedState();
        }
    }

    public synchronized int delete() {
        int rowsAffected;
        SQLiteDatabase db = Connector.getDatabase();
        db.beginTransaction();
        try {
            DeleteHandler deleteHandler = new DeleteHandler(db);
            rowsAffected = deleteHandler.onDelete(this);
            this.baseObjId = 0L;
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return rowsAffected;
    }

    public synchronized int update(long id) {
        int rowsAffected;
        try {
            UpdateHandler updateHandler = new UpdateHandler(Connector.getDatabase());
            rowsAffected = updateHandler.onUpdate(this, id);
            getFieldsToSetToDefault().clear();
        } catch (Exception e) {
            throw new DataSupportException(e.getMessage());
        }
        return rowsAffected;
    }

    public synchronized int updateAll(String... conditions) {
        int rowsAffected;
        try {
            UpdateHandler updateHandler = new UpdateHandler(Connector.getDatabase());
            rowsAffected = updateHandler.onUpdateAll(this, conditions);
            getFieldsToSetToDefault().clear();
        } catch (Exception e) {
            throw new DataSupportException(e.getMessage());
        }
        return rowsAffected;
    }

    public synchronized boolean save() {
        boolean z;
        try {
            saveThrows();
            z = true;
        } catch (Exception e) {
            e.printStackTrace();
            z = false;
        }
        return z;
    }

    public synchronized void saveThrows() {
        SQLiteDatabase db = Connector.getDatabase();
        db.beginTransaction();
        try {
            try {
                SaveHandler saveHandler = new SaveHandler(db);
                saveHandler.onSave(this);
                clearAssociatedData();
                db.setTransactionSuccessful();
            } catch (Exception e) {
                throw new DataSupportException(e.getMessage());
            }
        } finally {
            db.endTransaction();
        }
    }

    public synchronized boolean saveFast() {
        boolean z;
        SQLiteDatabase db = Connector.getDatabase();
        db.beginTransaction();
        try {
            try {
                SaveHandler saveHandler = new SaveHandler(db);
                saveHandler.onSaveFast(this);
                db.setTransactionSuccessful();
                z = true;
            } catch (Exception e) {
                e.printStackTrace();
                db.endTransaction();
                z = false;
            }
        } finally {
        }
        return z;
    }

    public boolean isSaved() {
        return this.baseObjId > 0;
    }

    public void clearSavedState() {
        this.baseObjId = 0L;
    }

    public void setToDefault(String fieldName) {
        getFieldsToSetToDefault().add(fieldName);
    }

    public void assignBaseObjId(int baseObjId) {
        this.baseObjId = baseObjId;
    }

    protected DataSupport() {
    }

    protected long getBaseObjId() {
        return this.baseObjId;
    }

    protected String getClassName() {
        return getClass().getName();
    }

    protected String getTableName() {
        return BaseUtility.changeCase(DBUtility.getTableNameByClassName(getClassName()));
    }

    List<String> getFieldsToSetToDefault() {
        if (this.fieldsToSetToDefault == null) {
            this.fieldsToSetToDefault = new ArrayList();
        }
        return this.fieldsToSetToDefault;
    }

    void addAssociatedModelWithFK(String associatedTableName, long associatedId) {
        Set<Long> associatedIdsWithFKSet = getAssociatedModelsMapWithFK().get(associatedTableName);
        if (associatedIdsWithFKSet == null) {
            Set<Long> associatedIdsWithFKSet2 = new HashSet<>();
            associatedIdsWithFKSet2.add(Long.valueOf(associatedId));
            this.associatedModelsMapWithFK.put(associatedTableName, associatedIdsWithFKSet2);
            return;
        }
        associatedIdsWithFKSet.add(Long.valueOf(associatedId));
    }

    Map<String, Set<Long>> getAssociatedModelsMapWithFK() {
        if (this.associatedModelsMapWithFK == null) {
            this.associatedModelsMapWithFK = new HashMap();
        }
        return this.associatedModelsMapWithFK;
    }

    void addAssociatedModelForJoinTable(String associatedModelName, long associatedId) {
        Set<Long> associatedIdsM2MSet = getAssociatedModelsMapForJoinTable().get(associatedModelName);
        if (associatedIdsM2MSet == null) {
            Set<Long> associatedIdsM2MSet2 = new HashSet<>();
            associatedIdsM2MSet2.add(Long.valueOf(associatedId));
            this.associatedModelsMapForJoinTable.put(associatedModelName, associatedIdsM2MSet2);
            return;
        }
        associatedIdsM2MSet.add(Long.valueOf(associatedId));
    }

    void addEmptyModelForJoinTable(String associatedModelName) {
        Set<Long> associatedIdsM2MSet = getAssociatedModelsMapForJoinTable().get(associatedModelName);
        if (associatedIdsM2MSet == null) {
            Set<Long> associatedIdsM2MSet2 = new HashSet<>();
            this.associatedModelsMapForJoinTable.put(associatedModelName, associatedIdsM2MSet2);
        }
    }

    Map<String, Set<Long>> getAssociatedModelsMapForJoinTable() {
        if (this.associatedModelsMapForJoinTable == null) {
            this.associatedModelsMapForJoinTable = new HashMap();
        }
        return this.associatedModelsMapForJoinTable;
    }

    void addAssociatedModelWithoutFK(String associatedTableName, long associatedId) {
        getAssociatedModelsMapWithoutFK().put(associatedTableName, Long.valueOf(associatedId));
    }

    Map<String, Long> getAssociatedModelsMapWithoutFK() {
        if (this.associatedModelsMapWithoutFK == null) {
            this.associatedModelsMapWithoutFK = new HashMap();
        }
        return this.associatedModelsMapWithoutFK;
    }

    void addFKNameToClearSelf(String foreignKeyName) {
        List<String> list = getListToClearSelfFK();
        if (!list.contains(foreignKeyName)) {
            list.add(foreignKeyName);
        }
    }

    List<String> getListToClearSelfFK() {
        if (this.listToClearSelfFK == null) {
            this.listToClearSelfFK = new ArrayList();
        }
        return this.listToClearSelfFK;
    }

    void addAssociatedTableNameToClearFK(String associatedTableName) {
        List<String> list = getListToClearAssociatedFK();
        if (!list.contains(associatedTableName)) {
            list.add(associatedTableName);
        }
    }

    List<String> getListToClearAssociatedFK() {
        if (this.listToClearAssociatedFK == null) {
            this.listToClearAssociatedFK = new ArrayList();
        }
        return this.listToClearAssociatedFK;
    }

    void clearAssociatedData() {
        clearIdOfModelWithFK();
        clearIdOfModelWithoutFK();
        clearIdOfModelForJoinTable();
        clearFKNameList();
    }

    private void clearIdOfModelWithFK() {
        for (String associatedModelName : getAssociatedModelsMapWithFK().keySet()) {
            this.associatedModelsMapWithFK.get(associatedModelName).clear();
        }
        this.associatedModelsMapWithFK.clear();
    }

    private void clearIdOfModelWithoutFK() {
        getAssociatedModelsMapWithoutFK().clear();
    }

    private void clearIdOfModelForJoinTable() {
        for (String associatedModelName : getAssociatedModelsMapForJoinTable().keySet()) {
            this.associatedModelsMapForJoinTable.get(associatedModelName).clear();
        }
        this.associatedModelsMapForJoinTable.clear();
    }

    private void clearFKNameList() {
        getListToClearSelfFK().clear();
        getListToClearAssociatedFK().clear();
    }
}
