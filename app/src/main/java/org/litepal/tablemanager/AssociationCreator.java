package org.litepal.tablemanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import cn.yunzhisheng.common.PinyinConverter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.litepal.exceptions.DatabaseGenerateException;
import org.litepal.tablemanager.model.AssociationsModel;
import org.litepal.tablemanager.model.ColumnModel;
import org.litepal.util.BaseUtility;
import org.litepal.util.Const;
import org.litepal.util.DBUtility;
import org.litepal.util.LogUtil;

/* loaded from: classes.dex */
public abstract class AssociationCreator extends Generator {
    @Override // org.litepal.tablemanager.Generator
    protected abstract void createOrUpgradeTable(SQLiteDatabase sQLiteDatabase, boolean z);

    @Override // org.litepal.tablemanager.Generator
    protected void addOrUpdateAssociation(SQLiteDatabase db, boolean force) {
        addAssociations(getAllAssociations(), db, force);
    }

    protected String generateCreateTableSQL(String tableName, List<ColumnModel> columnModels, boolean autoIncrementId) {
        StringBuilder createTableSQL = new StringBuilder("create table ");
        createTableSQL.append(tableName).append(" (");
        if (autoIncrementId) {
            createTableSQL.append("id integer primary key autoincrement,");
        }
        if (columnModels.size() == 0) {
            createTableSQL.deleteCharAt(createTableSQL.length() - 1);
        }
        boolean needSeparator = false;
        for (ColumnModel columnModel : columnModels) {
            if (!columnModel.isIdColumn()) {
                if (needSeparator) {
                    createTableSQL.append(", ");
                }
                needSeparator = true;
                createTableSQL.append(columnModel.getColumnName()).append(PinyinConverter.PINYIN_SEPARATOR).append(columnModel.getColumnType());
                if (!columnModel.isNullable()) {
                    createTableSQL.append(" not null");
                }
                if (columnModel.isUnique()) {
                    createTableSQL.append(" unique");
                }
                String defaultValue = columnModel.getDefaultValue();
                if (!TextUtils.isEmpty(defaultValue)) {
                    createTableSQL.append(" default ").append(defaultValue);
                }
            }
        }
        createTableSQL.append(")");
        LogUtil.d(Generator.TAG, "create table sql is >> " + ((Object) createTableSQL));
        return createTableSQL.toString();
    }

    protected String generateDropTableSQL(String tableName) {
        return "drop table if exists " + tableName;
    }

    protected String generateAddColumnSQL(String tableName, ColumnModel columnModel) {
        StringBuilder addColumnSQL = new StringBuilder();
        addColumnSQL.append("alter table ").append(tableName);
        addColumnSQL.append(" add column ").append(columnModel.getColumnName());
        addColumnSQL.append(PinyinConverter.PINYIN_SEPARATOR).append(columnModel.getColumnType());
        if (!columnModel.isNullable()) {
            addColumnSQL.append(" not null");
        }
        if (columnModel.isUnique()) {
            addColumnSQL.append(" unique");
        }
        String defaultValue = columnModel.getDefaultValue();
        if (!TextUtils.isEmpty(defaultValue)) {
            addColumnSQL.append(" default ").append(defaultValue);
        } else if (!columnModel.isNullable()) {
            if ("integer".equalsIgnoreCase(columnModel.getColumnType())) {
                defaultValue = "0";
            } else if ("text".equalsIgnoreCase(columnModel.getColumnType())) {
                defaultValue = "''";
            } else if ("real".equalsIgnoreCase(columnModel.getColumnType())) {
                defaultValue = "0.0";
            }
            addColumnSQL.append(" default ").append(defaultValue);
        }
        LogUtil.d(Generator.TAG, "add column sql is >> " + ((Object) addColumnSQL));
        return addColumnSQL.toString();
    }

    protected boolean isForeignKeyColumnFormat(String columnName) {
        return (TextUtils.isEmpty(columnName) || !columnName.toLowerCase().endsWith("_id") || columnName.equalsIgnoreCase("_id")) ? false : true;
    }

    protected void giveTableSchemaACopy(String tableName, int tableType, SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder("select * from ");
        sql.append(Const.TableSchema.TABLE_NAME);
        LogUtil.d(Generator.TAG, "giveTableSchemaACopy SQL is >> " + ((Object) sql));
        Cursor cursor = null;
        try {
            try {
                cursor = db.rawQuery(sql.toString(), null);
                if (isNeedtoGiveACopy(cursor, tableName)) {
                    ContentValues values = new ContentValues();
                    values.put(Const.TableSchema.COLUMN_NAME, BaseUtility.changeCase(tableName));
                    values.put(Const.TableSchema.COLUMN_TYPE, Integer.valueOf(tableType));
                    db.insert(Const.TableSchema.TABLE_NAME, null, values);
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    private boolean isNeedtoGiveACopy(Cursor cursor, String tableName) {
        return (isValueExists(cursor, tableName) || isSpecialTable(tableName)) ? false : true;
    }

    /* JADX WARN: Incorrect condition in loop: B:5:0x0015 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean isValueExists(android.database.Cursor r4, java.lang.String r5) {
        /*
            r3 = this;
            r0 = 0
            boolean r2 = r4.moveToFirst()
            if (r2 == 0) goto L18
        L7:
            java.lang.String r2 = "name"
            int r2 = r4.getColumnIndexOrThrow(r2)
            java.lang.String r1 = r4.getString(r2)
            boolean r2 = r1.equalsIgnoreCase(r5)
            if (r2 == 0) goto L19
            r0 = 1
        L18:
            return r0
        L19:
            boolean r2 = r4.moveToNext()
            if (r2 != 0) goto L7
            goto L18
        */
        throw new UnsupportedOperationException("Method not decompiled: org.litepal.tablemanager.AssociationCreator.isValueExists(android.database.Cursor, java.lang.String):boolean");
    }

    private boolean isSpecialTable(String tableName) {
        return Const.TableSchema.TABLE_NAME.equalsIgnoreCase(tableName);
    }

    private void addAssociations(Collection<AssociationsModel> associatedModels, SQLiteDatabase db, boolean force) {
        for (AssociationsModel associationModel : associatedModels) {
            if (2 == associationModel.getAssociationType() || 1 == associationModel.getAssociationType()) {
                addForeignKeyColumn(associationModel.getTableName(), associationModel.getAssociatedTableName(), associationModel.getTableHoldsForeignKey(), db);
            } else if (3 == associationModel.getAssociationType()) {
                createIntermediateTable(associationModel.getTableName(), associationModel.getAssociatedTableName(), db, force);
            }
        }
    }

    private void createIntermediateTable(String tableName, String associatedTableName, SQLiteDatabase db, boolean force) {
        List<ColumnModel> columnModelList = new ArrayList<>();
        ColumnModel column1 = new ColumnModel();
        column1.setColumnName(String.valueOf(tableName) + "_id");
        column1.setColumnType("integer");
        ColumnModel column2 = new ColumnModel();
        column2.setColumnName(String.valueOf(associatedTableName) + "_id");
        column2.setColumnType("integer");
        columnModelList.add(column1);
        columnModelList.add(column2);
        String intermediateTableName = DBUtility.getIntermediateTableName(tableName, associatedTableName);
        List<String> sqls = new ArrayList<>();
        if (DBUtility.isTableExists(intermediateTableName, db)) {
            if (force) {
                sqls.add(generateDropTableSQL(intermediateTableName));
                sqls.add(generateCreateTableSQL(intermediateTableName, columnModelList, false));
            }
        } else {
            sqls.add(generateCreateTableSQL(intermediateTableName, columnModelList, false));
        }
        execute((String[]) sqls.toArray(new String[0]), db);
        giveTableSchemaACopy(intermediateTableName, 1, db);
    }

    protected void addForeignKeyColumn(String tableName, String associatedTableName, String tableHoldsForeignKey, SQLiteDatabase db) {
        if (DBUtility.isTableExists(tableName, db)) {
            if (DBUtility.isTableExists(associatedTableName, db)) {
                String foreignKeyColumn = null;
                if (tableName.equals(tableHoldsForeignKey)) {
                    foreignKeyColumn = getForeignKeyColumnName(associatedTableName);
                } else if (associatedTableName.equals(tableHoldsForeignKey)) {
                    foreignKeyColumn = getForeignKeyColumnName(tableName);
                }
                if (!DBUtility.isColumnExists(foreignKeyColumn, tableHoldsForeignKey, db)) {
                    ColumnModel columnModel = new ColumnModel();
                    columnModel.setColumnName(foreignKeyColumn);
                    columnModel.setColumnType("integer");
                    String[] sqls = {generateAddColumnSQL(tableHoldsForeignKey, columnModel)};
                    execute(sqls, db);
                    return;
                }
                LogUtil.d(Generator.TAG, "column " + foreignKeyColumn + " is already exist, no need to add one");
                return;
            }
            throw new DatabaseGenerateException(DatabaseGenerateException.TABLE_DOES_NOT_EXIST + associatedTableName);
        }
        throw new DatabaseGenerateException(DatabaseGenerateException.TABLE_DOES_NOT_EXIST + tableName);
    }
}
