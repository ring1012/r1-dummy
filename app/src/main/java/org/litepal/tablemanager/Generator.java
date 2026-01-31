package org.litepal.tablemanager;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Collection;
import org.litepal.LitePalBase;
import org.litepal.exceptions.DatabaseGenerateException;
import org.litepal.parser.LitePalAttr;
import org.litepal.tablemanager.model.AssociationsModel;
import org.litepal.tablemanager.model.TableModel;
import org.litepal.util.BaseUtility;

/* loaded from: classes.dex */
public abstract class Generator extends LitePalBase {
    public static final String TAG = "Generator";
    private Collection<AssociationsModel> mAllRelationModels;
    private Collection<TableModel> mTableModels;

    protected abstract void addOrUpdateAssociation(SQLiteDatabase sQLiteDatabase, boolean z);

    protected abstract void createOrUpgradeTable(SQLiteDatabase sQLiteDatabase, boolean z);

    protected Collection<TableModel> getAllTableModels() {
        if (this.mTableModels == null) {
            this.mTableModels = new ArrayList();
        }
        if (!canUseCache()) {
            this.mTableModels.clear();
            for (String className : LitePalAttr.getInstance().getClassNames()) {
                this.mTableModels.add(getTableModel(className));
            }
        }
        return this.mTableModels;
    }

    protected Collection<AssociationsModel> getAllAssociations() {
        if (this.mAllRelationModels == null || this.mAllRelationModels.isEmpty()) {
            this.mAllRelationModels = getAssociations(LitePalAttr.getInstance().getClassNames());
        }
        return this.mAllRelationModels;
    }

    protected void execute(String[] sqls, SQLiteDatabase db) throws SQLException {
        String throwSQL = "";
        if (sqls != null) {
            try {
                for (String sql : sqls) {
                    throwSQL = sql;
                    db.execSQL(BaseUtility.changeCase(sql));
                }
            } catch (SQLException e) {
                throw new DatabaseGenerateException(DatabaseGenerateException.SQL_ERROR + throwSQL);
            }
        }
    }

    private static void addAssociation(SQLiteDatabase db, boolean force) {
        AssociationCreator associationsCreator = new Creator();
        associationsCreator.addOrUpdateAssociation(db, force);
    }

    private static void updateAssociations(SQLiteDatabase db) {
        AssociationUpdater associationUpgrader = new Upgrader();
        associationUpgrader.addOrUpdateAssociation(db, false);
    }

    private static void upgradeTables(SQLiteDatabase db) {
        Upgrader upgrader = new Upgrader();
        upgrader.createOrUpgradeTable(db, false);
    }

    private static void create(SQLiteDatabase db, boolean force) {
        Creator creator = new Creator();
        creator.createOrUpgradeTable(db, force);
    }

    private static void drop(SQLiteDatabase db) {
        Dropper dropper = new Dropper();
        dropper.createOrUpgradeTable(db, false);
    }

    private boolean canUseCache() {
        return this.mTableModels != null && this.mTableModels.size() == LitePalAttr.getInstance().getClassNames().size();
    }

    static void create(SQLiteDatabase db) {
        create(db, true);
        addAssociation(db, true);
    }

    static void upgrade(SQLiteDatabase db) {
        drop(db);
        create(db, false);
        updateAssociations(db);
        upgradeTables(db);
        addAssociation(db, false);
    }
}
