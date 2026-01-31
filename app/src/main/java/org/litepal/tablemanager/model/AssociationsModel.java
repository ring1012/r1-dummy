package org.litepal.tablemanager.model;

/* loaded from: classes.dex */
public class AssociationsModel {
    private String associatedTableName;
    private int associationType;
    private String tableHoldsForeignKey;
    private String tableName;

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAssociatedTableName() {
        return this.associatedTableName;
    }

    public void setAssociatedTableName(String associatedTableName) {
        this.associatedTableName = associatedTableName;
    }

    public String getTableHoldsForeignKey() {
        return this.tableHoldsForeignKey;
    }

    public void setTableHoldsForeignKey(String tableHoldsForeignKey) {
        this.tableHoldsForeignKey = tableHoldsForeignKey;
    }

    public int getAssociationType() {
        return this.associationType;
    }

    public void setAssociationType(int associationType) {
        this.associationType = associationType;
    }

    public boolean equals(Object o) {
        if (o instanceof AssociationsModel) {
            AssociationsModel association = (AssociationsModel) o;
            if (association.getTableName() != null && association.getAssociatedTableName() != null && association.getAssociationType() == this.associationType && association.getTableHoldsForeignKey().equals(this.tableHoldsForeignKey)) {
                if (association.getTableName().equals(this.tableName) && association.getAssociatedTableName().equals(this.associatedTableName) && association.getTableHoldsForeignKey().equals(this.tableHoldsForeignKey)) {
                    return true;
                }
                if (association.getTableName().equals(this.associatedTableName) && association.getAssociatedTableName().equals(this.tableName) && association.getTableHoldsForeignKey().equals(this.tableHoldsForeignKey)) {
                    return true;
                }
            }
        }
        return false;
    }
}
