package org.litepal.tablemanager.typechange;

/* loaded from: classes.dex */
public class DateOrm extends OrmChange {
    @Override // org.litepal.tablemanager.typechange.OrmChange
    public String object2Relation(String fieldType) {
        if (fieldType == null || !fieldType.equals("java.util.Date")) {
            return null;
        }
        return "integer";
    }
}
