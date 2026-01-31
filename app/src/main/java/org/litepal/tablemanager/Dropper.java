package org.litepal.tablemanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.litepal.tablemanager.model.TableModel;
import org.litepal.util.BaseUtility;
import org.litepal.util.Const;
import org.litepal.util.LogUtil;

/* loaded from: classes.dex */
public class Dropper extends AssociationUpdater {
    private Collection<TableModel> mTableModels;

    @Override // org.litepal.tablemanager.AssociationUpdater, org.litepal.tablemanager.Creator, org.litepal.tablemanager.AssociationCreator, org.litepal.tablemanager.Generator
    protected void createOrUpgradeTable(SQLiteDatabase db, boolean force) {
        this.mTableModels = getAllTableModels();
        this.mDb = db;
        dropTables();
    }

    private void dropTables() {
        List<String> tableNamesToDrop = findTablesToDrop();
        dropTables(tableNamesToDrop, this.mDb);
        clearCopyInTableSchema(tableNamesToDrop);
    }

    private List<String> findTablesToDrop() {
        List<String> dropTableNames = new ArrayList<>();
        Cursor cursor = null;
        try {
            try {
                cursor = this.mDb.query(Const.TableSchema.TABLE_NAME, null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String tableName = cursor.getString(cursor.getColumnIndexOrThrow(Const.TableSchema.COLUMN_NAME));
                        int tableType = cursor.getInt(cursor.getColumnIndexOrThrow(Const.TableSchema.COLUMN_TYPE));
                        if (shouldDropThisTable(tableName, tableType)) {
                            LogUtil.d(AssociationUpdater.TAG, "need to drop " + tableName);
                            dropTableNames.add(tableName);
                        }
                    } while (cursor.moveToNext());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (cursor != null) {
                    cursor.close();
                }
            }
            return dropTableNames;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private List<String> pickTableNamesFromTableModels() {
        List<String> tableNames = new ArrayList<>();
        for (TableModel tableModel : this.mTableModels) {
            tableNames.add(tableModel.getTableName());
        }
        return tableNames;
    }

    private boolean shouldDropThisTable(String tableName, int tableType) {
        return !BaseUtility.containsIgnoreCases(pickTableNamesFromTableModels(), tableName) && tableType == 0;
    }
}
