package org.litepal.crud;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.litepal.crud.model.AssociationsInfo;
import org.litepal.exceptions.DataSupportException;

/* loaded from: classes.dex */
class SaveHandler extends DataHandler {
    boolean ignoreAssociations = false;

    SaveHandler(SQLiteDatabase db) {
        this.mDatabase = db;
    }

    void onSave(DataSupport baseObj) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        String className = baseObj.getClassName();
        List<Field> supportedFields = getSupportedFields(className);
        Collection<AssociationsInfo> associationInfos = getAssociationInfo(className);
        if (!baseObj.isSaved()) {
            if (!this.ignoreAssociations) {
                analyzeAssociatedModels(baseObj, associationInfos);
            }
            doSaveAction(baseObj, supportedFields);
            if (!this.ignoreAssociations) {
                analyzeAssociatedModels(baseObj, associationInfos);
                return;
            }
            return;
        }
        if (!this.ignoreAssociations) {
            analyzeAssociatedModels(baseObj, associationInfos);
        }
        doUpdateAction(baseObj, supportedFields);
    }

    void onSaveFast(DataSupport baseObj) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        this.ignoreAssociations = true;
        onSave(baseObj);
    }

    <T extends DataSupport> void onSaveAll(Collection<T> collection) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (collection != null && collection.size() > 0) {
            DataSupport[] array = (DataSupport[]) collection.toArray(new DataSupport[0]);
            DataSupport firstObj = array[0];
            String className = firstObj.getClassName();
            List<Field> supportedFields = getSupportedFields(className);
            Collection<AssociationsInfo> associationInfos = getAssociationInfo(className);
            for (DataSupport baseObj : array) {
                if (!baseObj.isSaved()) {
                    analyzeAssociatedModels(baseObj, associationInfos);
                    doSaveAction(baseObj, supportedFields);
                    analyzeAssociatedModels(baseObj, associationInfos);
                } else {
                    analyzeAssociatedModels(baseObj, associationInfos);
                    doUpdateAction(baseObj, supportedFields);
                }
                baseObj.clearAssociatedData();
            }
        }
    }

    private void doSaveAction(DataSupport baseObj, List<Field> supportedFields) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ContentValues values = new ContentValues();
        beforeSave(baseObj, supportedFields, values);
        long id = saving(baseObj, values);
        afterSave(baseObj, supportedFields, id);
    }

    private void beforeSave(DataSupport baseObj, List<Field> supportedFields, ContentValues values) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        putFieldsValue(baseObj, supportedFields, values);
        if (!this.ignoreAssociations) {
            putForeignKeyValue(values, baseObj);
        }
    }

    private long saving(DataSupport baseObj, ContentValues values) {
        return this.mDatabase.insert(baseObj.getTableName(), null, values);
    }

    private void afterSave(DataSupport baseObj, List<Field> supportedFields, long id) {
        throwIfSaveFailed(id);
        assignIdValue(baseObj, getIdField(supportedFields), id);
        if (!this.ignoreAssociations) {
            updateAssociatedTableWithFK(baseObj);
            insertIntermediateJoinTableValue(baseObj, false);
        }
    }

    private void doUpdateAction(DataSupport baseObj, List<Field> supportedFields) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ContentValues values = new ContentValues();
        beforeUpdate(baseObj, supportedFields, values);
        updating(baseObj, values);
        afterUpdate(baseObj);
    }

    private void beforeUpdate(DataSupport baseObj, List<Field> supportedFields, ContentValues values) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        putFieldsValue(baseObj, supportedFields, values);
        if (!this.ignoreAssociations) {
            putForeignKeyValue(values, baseObj);
            for (String fkName : baseObj.getListToClearSelfFK()) {
                values.putNull(fkName);
            }
        }
    }

    private void updating(DataSupport baseObj, ContentValues values) {
        this.mDatabase.update(baseObj.getTableName(), values, "id = ?", new String[]{String.valueOf(baseObj.getBaseObjId())});
    }

    private void afterUpdate(DataSupport baseObj) {
        if (!this.ignoreAssociations) {
            updateAssociatedTableWithFK(baseObj);
            insertIntermediateJoinTableValue(baseObj, true);
            clearFKValueInAssociatedTable(baseObj);
        }
    }

    private Field getIdField(List<Field> supportedFields) {
        for (Field field : supportedFields) {
            if (isIdColumn(field.getName())) {
                return field;
            }
        }
        return null;
    }

    private void throwIfSaveFailed(long id) {
        if (id == -1) {
            throw new DataSupportException(DataSupportException.SAVE_FAILED);
        }
    }

    private void assignIdValue(DataSupport baseObj, Field idField, long id) {
        try {
            giveBaseObjIdValue(baseObj, id);
            if (idField != null) {
                giveModelIdValue(baseObj, idField.getName(), idField.getType(), id);
            }
        } catch (Exception e) {
            throw new DataSupportException(e.getMessage());
        }
    }

    private void giveModelIdValue(DataSupport baseObj, String idName, Class<?> idType, long id) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        Object objValueOf;
        if (shouldGiveModelIdValue(idName, idType, id)) {
            if (idType == Integer.TYPE || idType == Integer.class) {
                objValueOf = Integer.valueOf((int) id);
            } else if (idType == Long.TYPE || idType == Long.class) {
                objValueOf = Long.valueOf(id);
            } else {
                throw new DataSupportException(DataSupportException.ID_TYPE_INVALID_EXCEPTION);
            }
            DynamicExecutor.setField(baseObj, idName, objValueOf, baseObj.getClass());
        }
    }

    private void putForeignKeyValue(ContentValues values, DataSupport baseObj) {
        Map<String, Long> associatedModelMap = baseObj.getAssociatedModelsMapWithoutFK();
        for (String associatedTableName : associatedModelMap.keySet()) {
            values.put(getForeignKeyColumnName(associatedTableName), associatedModelMap.get(associatedTableName));
        }
    }

    private void updateAssociatedTableWithFK(DataSupport baseObj) {
        Map<String, Set<Long>> associatedModelMap = baseObj.getAssociatedModelsMapWithFK();
        ContentValues values = new ContentValues();
        for (String associatedTableName : associatedModelMap.keySet()) {
            values.clear();
            String fkName = getForeignKeyColumnName(baseObj.getTableName());
            values.put(fkName, Long.valueOf(baseObj.getBaseObjId()));
            Set<Long> ids = associatedModelMap.get(associatedTableName);
            if (ids != null && !ids.isEmpty()) {
                this.mDatabase.update(associatedTableName, values, getWhereOfIdsWithOr(ids), null);
            }
        }
    }

    private void clearFKValueInAssociatedTable(DataSupport baseObj) {
        List<String> associatedTableNames = baseObj.getListToClearAssociatedFK();
        for (String associatedTableName : associatedTableNames) {
            String fkColumnName = getForeignKeyColumnName(baseObj.getTableName());
            ContentValues values = new ContentValues();
            values.putNull(fkColumnName);
            String whereClause = String.valueOf(fkColumnName) + " = " + baseObj.getBaseObjId();
            this.mDatabase.update(associatedTableName, values, whereClause, null);
        }
    }

    private void insertIntermediateJoinTableValue(DataSupport baseObj, boolean isUpdate) {
        Map<String, Set<Long>> associatedIdsM2M = baseObj.getAssociatedModelsMapForJoinTable();
        ContentValues values = new ContentValues();
        for (String associatedTableName : associatedIdsM2M.keySet()) {
            String joinTableName = getIntermediateTableName(baseObj, associatedTableName);
            if (isUpdate) {
                this.mDatabase.delete(joinTableName, getWhereForJoinTableToDelete(baseObj), new String[]{String.valueOf(baseObj.getBaseObjId())});
            }
            Set<Long> associatedIdsM2MSet = associatedIdsM2M.get(associatedTableName);
            Iterator<Long> it = associatedIdsM2MSet.iterator();
            while (it.hasNext()) {
                long associatedId = it.next().longValue();
                values.clear();
                values.put(getForeignKeyColumnName(baseObj.getTableName()), Long.valueOf(baseObj.getBaseObjId()));
                values.put(getForeignKeyColumnName(associatedTableName), Long.valueOf(associatedId));
                this.mDatabase.insert(joinTableName, null, values);
            }
        }
    }

    private String getWhereForJoinTableToDelete(DataSupport baseObj) {
        return getForeignKeyColumnName(baseObj.getTableName()) + " = ?";
    }

    private boolean shouldGiveModelIdValue(String idName, Class<?> idType, long id) {
        return (idName == null || idType == null || id <= 0) ? false : true;
    }
}
