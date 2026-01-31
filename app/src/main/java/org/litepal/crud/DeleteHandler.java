package org.litepal.crud;

import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.litepal.crud.model.AssociationsInfo;
import org.litepal.exceptions.DataSupportException;
import org.litepal.util.BaseUtility;
import org.litepal.util.DBUtility;

/* loaded from: classes.dex */
public class DeleteHandler extends DataHandler {
    private List<String> foreignKeyTableToDelete;

    DeleteHandler(SQLiteDatabase db) {
        this.mDatabase = db;
    }

    int onDelete(DataSupport baseObj) {
        if (!baseObj.isSaved()) {
            return 0;
        }
        Collection<AssociationsInfo> associationInfos = analyzeAssociations(baseObj);
        int rowsAffected = deleteCascade(baseObj);
        int rowsAffected2 = rowsAffected + this.mDatabase.delete(baseObj.getTableName(), "id = " + baseObj.getBaseObjId(), null);
        clearAssociatedModelSaveState(baseObj, associationInfos);
        return rowsAffected2;
    }

    int onDelete(Class<?> modelClass, long id) {
        analyzeAssociations(modelClass);
        int rowsAffected = deleteCascade(modelClass, id);
        int rowsAffected2 = rowsAffected + this.mDatabase.delete(getTableName(modelClass), "id = " + id, null);
        getForeignKeyTableToDelete().clear();
        return rowsAffected2;
    }

    int onDeleteAll(String tableName, String... conditions) {
        BaseUtility.checkConditionsCorrect(conditions);
        return this.mDatabase.delete(tableName, getWhereClause(conditions), getWhereArgs(conditions));
    }

    int onDeleteAll(Class<?> modelClass, String... conditions) {
        BaseUtility.checkConditionsCorrect(conditions);
        analyzeAssociations(modelClass);
        int rowsAffected = deleteAllCascade(modelClass, conditions);
        int rowsAffected2 = rowsAffected + this.mDatabase.delete(getTableName(modelClass), getWhereClause(conditions), getWhereArgs(conditions));
        getForeignKeyTableToDelete().clear();
        return rowsAffected2;
    }

    private void analyzeAssociations(Class<?> modelClass) {
        Collection<AssociationsInfo> associationInfos = getAssociationInfo(modelClass.getName());
        for (AssociationsInfo associationInfo : associationInfos) {
            String associatedTableName = DBUtility.getTableNameByClassName(associationInfo.getAssociatedClassName());
            if (associationInfo.getAssociationType() == 2 || associationInfo.getAssociationType() == 1) {
                String classHoldsForeignKey = associationInfo.getClassHoldsForeignKey();
                if (!modelClass.getName().equals(classHoldsForeignKey)) {
                    getForeignKeyTableToDelete().add(associatedTableName);
                }
            } else if (associationInfo.getAssociationType() == 3) {
                String joinTableName = DBUtility.getIntermediateTableName(getTableName(modelClass), associatedTableName);
                getForeignKeyTableToDelete().add(BaseUtility.changeCase(joinTableName));
            }
        }
    }

    private int deleteCascade(Class<?> modelClass, long id) {
        int rowsAffected = 0;
        for (String associatedTableName : getForeignKeyTableToDelete()) {
            String fkName = getForeignKeyColumnName(getTableName(modelClass));
            rowsAffected += this.mDatabase.delete(associatedTableName, String.valueOf(fkName) + " = " + id, null);
        }
        return rowsAffected;
    }

    private int deleteAllCascade(Class<?> modelClass, String... conditions) {
        int rowsAffected = 0;
        for (String associatedTableName : getForeignKeyTableToDelete()) {
            String tableName = getTableName(modelClass);
            String fkName = getForeignKeyColumnName(tableName);
            StringBuilder whereClause = new StringBuilder();
            whereClause.append(fkName).append(" in (select id from ");
            whereClause.append(tableName);
            if (conditions != null && conditions.length > 0) {
                whereClause.append(" where ").append(buildConditionString(conditions));
            }
            whereClause.append(")");
            rowsAffected += this.mDatabase.delete(associatedTableName, BaseUtility.changeCase(whereClause.toString()), null);
        }
        return rowsAffected;
    }

    private String buildConditionString(String... conditions) {
        int argCount = conditions.length - 1;
        String whereClause = conditions[0];
        for (int i = 0; i < argCount; i++) {
            whereClause = whereClause.replaceFirst("\\?", "'" + conditions[i + 1] + "'");
        }
        return whereClause;
    }

    private Collection<AssociationsInfo> analyzeAssociations(DataSupport baseObj) {
        try {
            Collection<AssociationsInfo> associationInfos = getAssociationInfo(baseObj.getClassName());
            analyzeAssociatedModels(baseObj, associationInfos);
            return associationInfos;
        } catch (Exception e) {
            throw new DataSupportException(e.getMessage());
        }
    }

    private void clearAssociatedModelSaveState(DataSupport baseObj, Collection<AssociationsInfo> associationInfos) {
        DataSupport model;
        try {
            for (AssociationsInfo associationInfo : associationInfos) {
                if (associationInfo.getAssociationType() == 2 && !baseObj.getClassName().equals(associationInfo.getClassHoldsForeignKey())) {
                    Collection<DataSupport> associatedModels = getAssociatedModels(baseObj, associationInfo);
                    if (associatedModels != null && !associatedModels.isEmpty()) {
                        for (DataSupport model2 : associatedModels) {
                            if (model2 != null) {
                                model2.clearSavedState();
                            }
                        }
                    }
                } else if (associationInfo.getAssociationType() == 1 && (model = getAssociatedModel(baseObj, associationInfo)) != null) {
                    model.clearSavedState();
                }
            }
        } catch (Exception e) {
            throw new DataSupportException(e.getMessage());
        }
    }

    private int deleteCascade(DataSupport baseObj) {
        int rowsAffected = deleteAssociatedForeignKeyRows(baseObj);
        return rowsAffected + deleteAssociatedJoinTableRows(baseObj);
    }

    private int deleteAssociatedForeignKeyRows(DataSupport baseObj) {
        int rowsAffected = 0;
        Map<String, Set<Long>> associatedModelMap = baseObj.getAssociatedModelsMapWithFK();
        for (String associatedTableName : associatedModelMap.keySet()) {
            String fkName = getForeignKeyColumnName(baseObj.getTableName());
            rowsAffected += this.mDatabase.delete(associatedTableName, String.valueOf(fkName) + " = " + baseObj.getBaseObjId(), null);
        }
        return rowsAffected;
    }

    private int deleteAssociatedJoinTableRows(DataSupport baseObj) {
        int rowsAffected = 0;
        Set<String> associatedTableNames = baseObj.getAssociatedModelsMapForJoinTable().keySet();
        for (String associatedTableName : associatedTableNames) {
            String joinTableName = DBUtility.getIntermediateTableName(baseObj.getTableName(), associatedTableName);
            String fkName = getForeignKeyColumnName(baseObj.getTableName());
            rowsAffected += this.mDatabase.delete(joinTableName, String.valueOf(fkName) + " = " + baseObj.getBaseObjId(), null);
        }
        return rowsAffected;
    }

    private List<String> getForeignKeyTableToDelete() {
        if (this.foreignKeyTableToDelete == null) {
            this.foreignKeyTableToDelete = new ArrayList();
        }
        return this.foreignKeyTableToDelete;
    }
}
