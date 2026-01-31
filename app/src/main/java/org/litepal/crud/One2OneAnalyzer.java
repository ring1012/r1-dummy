package org.litepal.crud;

import java.lang.reflect.InvocationTargetException;
import org.litepal.crud.model.AssociationsInfo;
import org.litepal.util.DBUtility;

/* loaded from: classes.dex */
public class One2OneAnalyzer extends AssociationsAnalyzer {
    void analyze(DataSupport baseObj, AssociationsInfo associationInfo) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        DataSupport associatedModel = getAssociatedModel(baseObj, associationInfo);
        if (associatedModel != null) {
            buildBidirectionalAssociations(baseObj, associatedModel, associationInfo);
            dealAssociatedModel(baseObj, associatedModel, associationInfo);
        } else {
            String tableName = DBUtility.getTableNameByClassName(associationInfo.getAssociatedClassName());
            baseObj.addAssociatedTableNameToClearFK(tableName);
        }
    }

    private void dealAssociatedModel(DataSupport baseObj, DataSupport associatedModel, AssociationsInfo associationInfo) {
        if (associationInfo.getAssociateSelfFromOtherModel() != null) {
            bidirectionalCondition(baseObj, associatedModel);
        } else {
            unidirectionalCondition(baseObj, associatedModel);
        }
    }

    private void bidirectionalCondition(DataSupport baseObj, DataSupport associatedModel) {
        if (associatedModel.isSaved()) {
            baseObj.addAssociatedModelWithFK(associatedModel.getTableName(), associatedModel.getBaseObjId());
            baseObj.addAssociatedModelWithoutFK(associatedModel.getTableName(), associatedModel.getBaseObjId());
        }
    }

    private void unidirectionalCondition(DataSupport baseObj, DataSupport associatedModel) {
        dealsAssociationsOnTheSideWithoutFK(baseObj, associatedModel);
    }
}
