package org.litepal.tablemanager.typechange;

/* loaded from: classes.dex */
public class BlobOrm extends OrmChange {
    @Override // org.litepal.tablemanager.typechange.OrmChange
    public String object2Relation(String fieldType) {
        if (fieldType == null || !(fieldType.equals("byte") || fieldType.equals("java.lang.Byte"))) {
            return null;
        }
        return "blob";
    }
}
