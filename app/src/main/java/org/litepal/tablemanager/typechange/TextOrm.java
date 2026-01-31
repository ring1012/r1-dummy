package org.litepal.tablemanager.typechange;

/* loaded from: classes.dex */
public class TextOrm extends OrmChange {
    @Override // org.litepal.tablemanager.typechange.OrmChange
    public String object2Relation(String fieldType) {
        if (fieldType != null && (fieldType.equals("char") || fieldType.equals("java.lang.Character") || fieldType.equals("java.lang.String"))) {
            return "text";
        }
        return null;
    }
}
