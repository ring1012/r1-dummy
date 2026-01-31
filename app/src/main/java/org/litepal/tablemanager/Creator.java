package org.litepal.tablemanager;

import android.database.sqlite.SQLiteDatabase;
import org.litepal.tablemanager.model.TableModel;
import org.litepal.util.DBUtility;

/* loaded from: classes.dex */
class Creator extends AssociationCreator {
    public static final String TAG = "Creator";

    Creator() {
    }

    @Override // org.litepal.tablemanager.AssociationCreator, org.litepal.tablemanager.Generator
    protected void createOrUpgradeTable(SQLiteDatabase db, boolean force) {
        for (TableModel tableModel : getAllTableModels()) {
            createOrUpgradeTable(tableModel, db, force);
        }
    }

    protected void createOrUpgradeTable(TableModel tableModel, SQLiteDatabase db, boolean force) {
        execute(getCreateTableSQLs(tableModel, db, force), db);
        giveTableSchemaACopy(tableModel.getTableName(), 0, db);
    }

    protected String[] getCreateTableSQLs(TableModel tableModel, SQLiteDatabase db, boolean force) {
        if (force) {
            return new String[]{generateDropTableSQL(tableModel), generateCreateTableSQL(tableModel)};
        }
        if (DBUtility.isTableExists(tableModel.getTableName(), db)) {
            return null;
        }
        return new String[]{generateCreateTableSQL(tableModel)};
    }

    protected String generateDropTableSQL(TableModel tableModel) {
        return generateDropTableSQL(tableModel.getTableName());
    }

    protected String generateCreateTableSQL(TableModel tableModel) {
        return generateCreateTableSQL(tableModel.getTableName(), tableModel.getColumnModels(), true);
    }
}
