package org.litepal.crud;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import org.litepal.crud.model.AssociationsInfo;
import org.litepal.tablemanager.Connector;
import org.litepal.util.BaseUtility;
import org.litepal.util.DBUtility;

/* loaded from: classes.dex */
public class Many2ManyAnalyzer extends AssociationsAnalyzer {
    void analyze(DataSupport baseObj, AssociationsInfo associationInfo) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Collection<DataSupport> associatedModels = getAssociatedModels(baseObj, associationInfo);
        declareAssociations(baseObj, associationInfo);
        if (associatedModels != null) {
            for (DataSupport associatedModel : associatedModels) {
                Collection<DataSupport> tempCollection = getReverseAssociatedModels(associatedModel, associationInfo);
                Collection<DataSupport> reverseAssociatedModels = checkAssociatedModelCollection(tempCollection, associationInfo.getAssociateSelfFromOtherModel());
                addNewModelForAssociatedModel(reverseAssociatedModels, baseObj);
                setReverseAssociatedModels(associatedModel, associationInfo, reverseAssociatedModels);
                dealAssociatedModel(baseObj, associatedModel);
            }
        }
    }

    private void declareAssociations(DataSupport baseObj, AssociationsInfo associationInfo) {
        baseObj.addEmptyModelForJoinTable(getAssociatedTableName(associationInfo));
    }

    private void addNewModelForAssociatedModel(Collection<DataSupport> associatedModelCollection, DataSupport baseObj) {
        if (!associatedModelCollection.contains(baseObj)) {
            associatedModelCollection.add(baseObj);
        }
    }

    private void dealAssociatedModel(DataSupport baseObj, DataSupport associatedModel) {
        if (associatedModel.isSaved()) {
            baseObj.addAssociatedModelForJoinTable(associatedModel.getTableName(), associatedModel.getBaseObjId());
        }
    }

    private String getAssociatedTableName(AssociationsInfo associationInfo) {
        return BaseUtility.changeCase(DBUtility.getTableNameByClassName(associationInfo.getAssociatedClassName()));
    }

    @Deprecated
    private boolean isDataExists(DataSupport baseObj, DataSupport associatedModel) {
        boolean z = true;
        SQLiteDatabase db = Connector.getDatabase();
        Cursor cursor = null;
        try {
            try {
                cursor = db.query(getJoinTableName(baseObj, associatedModel), null, getSelection(baseObj, associatedModel), getSelectionArgs(baseObj, associatedModel), null, null, null);
                boolean exists = cursor.getCount() > 0;
                cursor.close();
                z = exists;
            } catch (Exception e) {
                e.printStackTrace();
                cursor.close();
            }
            return z;
        } catch (Throwable th) {
            cursor.close();
            throw th;
        }
    }

    private String getSelection(DataSupport baseObj, DataSupport associatedModel) {
        return getForeignKeyColumnName(baseObj.getTableName()) + " = ? and " + getForeignKeyColumnName(associatedModel.getTableName()) + " = ?";
    }

    private String[] getSelectionArgs(DataSupport baseObj, DataSupport associatedModel) {
        return new String[]{String.valueOf(baseObj.getBaseObjId()), String.valueOf(associatedModel.getBaseObjId())};
    }

    private String getJoinTableName(DataSupport baseObj, DataSupport associatedModel) {
        return getIntermediateTableName(baseObj, associatedModel.getTableName());
    }
}
