package org.litepal.crud;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.litepal.crud.model.AssociationsInfo;
import org.litepal.exceptions.DataSupportException;
import org.litepal.util.BaseUtility;

/* loaded from: classes.dex */
class UpdateHandler extends DataHandler {
    UpdateHandler(SQLiteDatabase db) {
        this.mDatabase = db;
    }

    int onUpdate(DataSupport baseObj, long id) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        List<Field> supportedFields = getSupportedFields(baseObj.getClassName());
        ContentValues values = new ContentValues();
        putFieldsValue(baseObj, supportedFields, values);
        putFieldsToDefaultValue(baseObj, values);
        if (values.size() > 0) {
            return this.mDatabase.update(baseObj.getTableName(), values, "id = " + id, null);
        }
        return 0;
    }

    int onUpdate(Class<?> modelClass, long id, ContentValues values) {
        if (values.size() > 0) {
            return this.mDatabase.update(getTableName(modelClass), values, "id = " + id, null);
        }
        return 0;
    }

    int onUpdateAll(DataSupport baseObj, String... conditions) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        List<Field> supportedFields = getSupportedFields(baseObj.getClassName());
        ContentValues values = new ContentValues();
        putFieldsValue(baseObj, supportedFields, values);
        putFieldsToDefaultValue(baseObj, values);
        return doUpdateAllAction(baseObj.getTableName(), values, conditions);
    }

    int onUpdateAll(String tableName, ContentValues values, String... conditions) {
        return doUpdateAllAction(tableName, values, conditions);
    }

    private int doUpdateAllAction(String tableName, ContentValues values, String... conditions) {
        BaseUtility.checkConditionsCorrect(conditions);
        if (values.size() > 0) {
            return this.mDatabase.update(tableName, values, getWhereClause(conditions), getWhereArgs(conditions));
        }
        return 0;
    }

    private void putFieldsToDefaultValue(DataSupport baseObj, ContentValues values) throws NoSuchFieldException {
        String fieldName = null;
        try {
            DataSupport emptyModel = getEmptyModel(baseObj);
            Class<?> emptyModelClass = emptyModel.getClass();
            for (String name : baseObj.getFieldsToSetToDefault()) {
                if (!isIdColumn(name)) {
                    fieldName = name;
                    Field field = emptyModelClass.getDeclaredField(fieldName);
                    putContentValues(emptyModel, field, values);
                }
            }
        } catch (NoSuchFieldException e) {
            throw new DataSupportException(DataSupportException.noSuchFieldExceptioin(baseObj.getClassName(), fieldName));
        } catch (Exception e2) {
            throw new DataSupportException(e2.getMessage());
        }
    }

    private int doUpdateAssociations(DataSupport baseObj, long id, ContentValues values) {
        analyzeAssociations(baseObj);
        updateSelfTableForeignKey(baseObj, values);
        int rowsAffected = 0 + updateAssociatedTableForeignKey(baseObj, id);
        return rowsAffected;
    }

    private void analyzeAssociations(DataSupport baseObj) {
        try {
            Collection<AssociationsInfo> associationInfos = getAssociationInfo(baseObj.getClassName());
            analyzeAssociatedModels(baseObj, associationInfos);
        } catch (Exception e) {
            throw new DataSupportException(e.getMessage());
        }
    }

    private void updateSelfTableForeignKey(DataSupport baseObj, ContentValues values) {
        Map<String, Long> associatedModelMap = baseObj.getAssociatedModelsMapWithoutFK();
        for (String associatedTable : associatedModelMap.keySet()) {
            String fkName = getForeignKeyColumnName(associatedTable);
            values.put(fkName, associatedModelMap.get(associatedTable));
        }
    }

    private int updateAssociatedTableForeignKey(DataSupport baseObj, long id) {
        Map<String, Set<Long>> associatedModelMap = baseObj.getAssociatedModelsMapWithFK();
        ContentValues values = new ContentValues();
        for (String associatedTable : associatedModelMap.keySet()) {
            values.clear();
            String fkName = getForeignKeyColumnName(baseObj.getTableName());
            values.put(fkName, Long.valueOf(id));
            Set<Long> ids = associatedModelMap.get(associatedTable);
            if (ids != null && !ids.isEmpty()) {
                return this.mDatabase.update(associatedTable, values, getWhereOfIdsWithOr(ids), null);
            }
        }
        return 0;
    }
}
