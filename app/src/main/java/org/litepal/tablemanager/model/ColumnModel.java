package org.litepal.tablemanager.model;

import android.text.TextUtils;
import com.google.android.exoplayer2.text.ttml.TtmlNode;

/* loaded from: classes.dex */
public class ColumnModel {
    private String columnName;
    private String columnType;
    private boolean isNullable = true;
    private boolean isUnique = false;
    private String defaultValue = "";

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return this.columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public boolean isNullable() {
        return this.isNullable;
    }

    public void setIsNullable(boolean isNullable) {
        this.isNullable = isNullable;
    }

    public boolean isUnique() {
        return this.isUnique;
    }

    public void setIsUnique(boolean isUnique) {
        this.isUnique = isUnique;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        if ("text".equalsIgnoreCase(this.columnType)) {
            if (!TextUtils.isEmpty(defaultValue)) {
                this.defaultValue = "'" + defaultValue + "'";
                return;
            }
            return;
        }
        this.defaultValue = defaultValue;
    }

    public boolean isIdColumn() {
        return "_id".equalsIgnoreCase(this.columnName) || TtmlNode.ATTR_ID.equalsIgnoreCase(this.columnName);
    }
}
