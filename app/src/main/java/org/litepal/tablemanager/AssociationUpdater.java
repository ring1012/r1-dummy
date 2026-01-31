package org.litepal.tablemanager;

import android.database.sqlite.SQLiteDatabase;
import cn.yunzhisheng.common.PinyinConverter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.litepal.parser.LitePalAttr;
import org.litepal.tablemanager.model.AssociationsModel;
import org.litepal.tablemanager.model.ColumnModel;
import org.litepal.tablemanager.model.TableModel;
import org.litepal.util.BaseUtility;
import org.litepal.util.Const;
import org.litepal.util.DBUtility;
import org.litepal.util.LogUtil;

/* loaded from: classes.dex */
public abstract class AssociationUpdater extends Creator {
    public static final String TAG = "AssociationUpdater";
    private Collection<AssociationsModel> mAssociationModels;
    protected SQLiteDatabase mDb;

    @Override // org.litepal.tablemanager.Creator, org.litepal.tablemanager.AssociationCreator, org.litepal.tablemanager.Generator
    protected abstract void createOrUpgradeTable(SQLiteDatabase sQLiteDatabase, boolean z);

    @Override // org.litepal.tablemanager.AssociationCreator, org.litepal.tablemanager.Generator
    protected void addOrUpdateAssociation(SQLiteDatabase db, boolean force) {
        this.mAssociationModels = getAllAssociations();
        this.mDb = db;
        removeAssociations();
    }

    protected List<String> getForeignKeyColumns(TableModel tableModel) {
        List<String> foreignKeyColumns = new ArrayList<>();
        List<ColumnModel> columnModelList = getTableModelFromDB(tableModel.getTableName()).getColumnModels();
        for (ColumnModel columnModel : columnModelList) {
            String columnName = columnModel.getColumnName();
            if (isForeignKeyColumnFormat(columnModel.getColumnName()) && !tableModel.containsColumn(columnName)) {
                LogUtil.d(TAG, "getForeignKeyColumnNames >> foreign key column is " + columnName);
                foreignKeyColumns.add(columnName);
            }
        }
        return foreignKeyColumns;
    }

    protected boolean isForeignKeyColumn(TableModel tableModel, String columnName) {
        return BaseUtility.containsIgnoreCases(getForeignKeyColumns(tableModel), columnName);
    }

    protected TableModel getTableModelFromDB(String tableName) {
        return DBUtility.findPragmaTableInfo(tableName, this.mDb);
    }

    protected void dropTables(List<String> dropTableNames, SQLiteDatabase db) {
        if (dropTableNames != null && !dropTableNames.isEmpty()) {
            String[] dropTableSQLS = new String[dropTableNames.size()];
            for (int i = 0; i < dropTableSQLS.length; i++) {
                dropTableSQLS[i] = generateDropTableSQL(dropTableNames.get(i));
            }
            execute(dropTableSQLS, db);
        }
    }

    protected void removeColumns(Collection<String> removeColumnNames, String tableName) {
        if (removeColumnNames != null && !removeColumnNames.isEmpty()) {
            execute(getRemoveColumnSQLs(removeColumnNames, tableName), this.mDb);
        }
    }

    protected void clearCopyInTableSchema(List<String> tableNames) {
        if (tableNames != null && !tableNames.isEmpty()) {
            StringBuilder deleteData = new StringBuilder("delete from ");
            deleteData.append(Const.TableSchema.TABLE_NAME).append(" where");
            boolean needOr = false;
            for (String tableName : tableNames) {
                if (needOr) {
                    deleteData.append(" or ");
                }
                needOr = true;
                deleteData.append(" lower(").append(Const.TableSchema.COLUMN_NAME).append(") ");
                deleteData.append("=").append(" lower('").append(tableName).append("')");
            }
            LogUtil.d(TAG, "clear table schema value sql is " + ((Object) deleteData));
            String[] sqls = {deleteData.toString()};
            execute(sqls, this.mDb);
        }
    }

    private void removeAssociations() {
        removeForeignKeyColumns();
        removeIntermediateTables();
    }

    private void removeForeignKeyColumns() {
        for (String className : LitePalAttr.getInstance().getClassNames()) {
            TableModel tableModel = getTableModel(className);
            removeColumns(findForeignKeyToRemove(tableModel), tableModel.getTableName());
        }
    }

    private void removeIntermediateTables() {
        List<String> tableNamesToDrop = findIntermediateTablesToDrop();
        dropTables(tableNamesToDrop, this.mDb);
        clearCopyInTableSchema(tableNamesToDrop);
    }

    private List<String> findForeignKeyToRemove(TableModel tableModel) {
        List<String> removeRelations = new ArrayList<>();
        List<String> foreignKeyColumns = getForeignKeyColumns(tableModel);
        String selfTableName = tableModel.getTableName();
        for (String foreignKeyColumn : foreignKeyColumns) {
            String associatedTableName = DBUtility.getTableNameByForeignColumn(foreignKeyColumn);
            if (shouldDropForeignKey(selfTableName, associatedTableName)) {
                removeRelations.add(foreignKeyColumn);
            }
        }
        LogUtil.d(TAG, "findForeignKeyToRemove >> " + tableModel.getTableName() + PinyinConverter.PINYIN_SEPARATOR + removeRelations);
        return removeRelations;
    }

    private List<String> findIntermediateTablesToDrop() {
        List<String> intermediateTables = new ArrayList<>();
        for (String tableName : DBUtility.findAllTableNames(this.mDb)) {
            if (DBUtility.isIntermediateTable(tableName, this.mDb)) {
                boolean dropIntermediateTable = true;
                for (AssociationsModel associationModel : this.mAssociationModels) {
                    if (associationModel.getAssociationType() == 3) {
                        String intermediateTableName = DBUtility.getIntermediateTableName(associationModel.getTableName(), associationModel.getAssociatedTableName());
                        if (tableName.equalsIgnoreCase(intermediateTableName)) {
                            dropIntermediateTable = false;
                        }
                    }
                }
                if (dropIntermediateTable) {
                    intermediateTables.add(tableName);
                }
            }
        }
        LogUtil.d(TAG, "findIntermediateTablesToDrop >> " + intermediateTables);
        return intermediateTables;
    }

    protected String generateAlterToTempTableSQL(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("alter table ").append(tableName).append(" rename to ").append(getTempTableName(tableName));
        return sql.toString();
    }

    private String generateCreateNewTableSQL(Collection<String> removeColumnNames, TableModel tableModel) {
        for (String removeColumnName : removeColumnNames) {
            tableModel.removeColumnModelByName(removeColumnName);
        }
        return generateCreateTableSQL(tableModel);
    }

    protected String generateDataMigrationSQL(TableModel tableModel) {
        String tableName = tableModel.getTableName();
        List<ColumnModel> columnModels = tableModel.getColumnModels();
        if (!columnModels.isEmpty()) {
            StringBuilder sql = new StringBuilder();
            sql.append("insert into ").append(tableName).append("(");
            boolean needComma = false;
            for (ColumnModel columnModel : columnModels) {
                if (needComma) {
                    sql.append(", ");
                }
                needComma = true;
                sql.append(columnModel.getColumnName());
            }
            sql.append(") ");
            sql.append("select ");
            boolean needComma2 = false;
            for (ColumnModel columnModel2 : columnModels) {
                ColumnModel columnModel3 = columnModel2;
                if (needComma2) {
                    sql.append(", ");
                }
                needComma2 = true;
                sql.append(columnModel3.getColumnName());
            }
            sql.append(" from ").append(getTempTableName(tableName));
            return sql.toString();
        }
        return null;
    }

    protected String generateDropTempTableSQL(String tableName) {
        return generateDropTableSQL(getTempTableName(tableName));
    }

    protected String getTempTableName(String tableName) {
        return String.valueOf(tableName) + "_temp";
    }

    private String[] getRemoveColumnSQLs(Collection<String> removeColumnNames, String tableName) {
        TableModel tableModelFromDB = getTableModelFromDB(tableName);
        String alterToTempTableSQL = generateAlterToTempTableSQL(tableName);
        LogUtil.d(TAG, "generateRemoveColumnSQL >> " + alterToTempTableSQL);
        String createNewTableSQL = generateCreateNewTableSQL(removeColumnNames, tableModelFromDB);
        LogUtil.d(TAG, "generateRemoveColumnSQL >> " + createNewTableSQL);
        String dataMigrationSQL = generateDataMigrationSQL(tableModelFromDB);
        LogUtil.d(TAG, "generateRemoveColumnSQL >> " + dataMigrationSQL);
        String dropTempTableSQL = generateDropTempTableSQL(tableName);
        LogUtil.d(TAG, "generateRemoveColumnSQL >> " + dropTempTableSQL);
        String[] sqls = {alterToTempTableSQL, createNewTableSQL, dataMigrationSQL, dropTempTableSQL};
        return sqls;
    }

    private boolean shouldDropForeignKey(String selfTableName, String associatedTableName) {
        for (AssociationsModel associationModel : this.mAssociationModels) {
            if (associationModel.getAssociationType() == 1) {
                if (!selfTableName.equalsIgnoreCase(associationModel.getTableHoldsForeignKey())) {
                    continue;
                } else if (associationModel.getTableName().equalsIgnoreCase(selfTableName)) {
                    if (isRelationCorrect(associationModel, selfTableName, associatedTableName)) {
                        return false;
                    }
                } else if (associationModel.getAssociatedTableName().equalsIgnoreCase(selfTableName) && isRelationCorrect(associationModel, associatedTableName, selfTableName)) {
                    return false;
                }
            } else if (associationModel.getAssociationType() == 2 && isRelationCorrect(associationModel, associatedTableName, selfTableName)) {
                return false;
            }
        }
        return true;
    }

    private boolean isRelationCorrect(AssociationsModel associationModel, String tableName1, String tableName2) {
        return associationModel.getTableName().equalsIgnoreCase(tableName1) && associationModel.getAssociatedTableName().equalsIgnoreCase(tableName2);
    }
}
