package org.litepal.tablemanager.model;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class TableModel {
    private String className;
    private List<ColumnModel> columnModels = new ArrayList();
    private String tableName;

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void addColumnModel(ColumnModel columnModel) {
        this.columnModels.add(columnModel);
    }

    public List<ColumnModel> getColumnModels() {
        return this.columnModels;
    }

    public ColumnModel getColumnModelByName(String columnName) {
        for (ColumnModel columnModel : this.columnModels) {
            if (columnModel.getColumnName().equalsIgnoreCase(columnName)) {
                return columnModel;
            }
        }
        return null;
    }

    public void removeColumnModelByName(String columnName) {
        if (!TextUtils.isEmpty(columnName)) {
            int indexToRemove = -1;
            int i = 0;
            while (true) {
                if (i >= this.columnModels.size()) {
                    break;
                }
                ColumnModel columnModel = this.columnModels.get(i);
                if (!columnName.equalsIgnoreCase(columnModel.getColumnName())) {
                    i++;
                } else {
                    indexToRemove = i;
                    break;
                }
            }
            if (indexToRemove != -1) {
                this.columnModels.remove(indexToRemove);
            }
        }
    }

    public boolean containsColumn(String columnName) {
        for (int i = 0; i < this.columnModels.size(); i++) {
            ColumnModel columnModel = this.columnModels.get(i);
            if (columnName.equalsIgnoreCase(columnModel.getColumnName())) {
                return true;
            }
        }
        return false;
    }
}
