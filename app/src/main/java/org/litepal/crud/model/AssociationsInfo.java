package org.litepal.crud.model;

import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class AssociationsInfo {
    private Field associateOtherModelFromSelf;
    private Field associateSelfFromOtherModel;
    private String associatedClassName;
    private int associationType;
    private String classHoldsForeignKey;
    private String selfClassName;

    public String getSelfClassName() {
        return this.selfClassName;
    }

    public void setSelfClassName(String selfClassName) {
        this.selfClassName = selfClassName;
    }

    public String getAssociatedClassName() {
        return this.associatedClassName;
    }

    public void setAssociatedClassName(String associatedClassName) {
        this.associatedClassName = associatedClassName;
    }

    public String getClassHoldsForeignKey() {
        return this.classHoldsForeignKey;
    }

    public void setClassHoldsForeignKey(String classHoldsForeignKey) {
        this.classHoldsForeignKey = classHoldsForeignKey;
    }

    public Field getAssociateOtherModelFromSelf() {
        return this.associateOtherModelFromSelf;
    }

    public void setAssociateOtherModelFromSelf(Field associateOtherModelFromSelf) {
        this.associateOtherModelFromSelf = associateOtherModelFromSelf;
    }

    public Field getAssociateSelfFromOtherModel() {
        return this.associateSelfFromOtherModel;
    }

    public void setAssociateSelfFromOtherModel(Field associateSelfFromOtherModel) {
        this.associateSelfFromOtherModel = associateSelfFromOtherModel;
    }

    public int getAssociationType() {
        return this.associationType;
    }

    public void setAssociationType(int associationType) {
        this.associationType = associationType;
    }

    public boolean equals(Object o) {
        if (o instanceof AssociationsInfo) {
            AssociationsInfo other = (AssociationsInfo) o;
            if (o != null && other != null && other.getAssociationType() == this.associationType && other.getClassHoldsForeignKey().equals(this.classHoldsForeignKey)) {
                if (other.getSelfClassName().equals(this.selfClassName) && other.getAssociatedClassName().equals(this.associatedClassName)) {
                    return true;
                }
                if (other.getSelfClassName().equals(this.associatedClassName) && other.getAssociatedClassName().equals(this.selfClassName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
