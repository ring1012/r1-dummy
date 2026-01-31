package org.litepal.tablemanager.typechange;

/* loaded from: classes.dex */
public class DecimalOrm extends OrmChange {
    @Override // org.litepal.tablemanager.typechange.OrmChange
    public String object2Relation(String fieldType) {
        if (fieldType != null && (fieldType.equals("float") || fieldType.equals("java.lang.Float") || fieldType.equals("double") || fieldType.equals("java.lang.Double"))) {
            return "real";
        }
        return null;
    }
}
