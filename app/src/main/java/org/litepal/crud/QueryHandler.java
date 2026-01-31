package org.litepal.crud;

import android.database.sqlite.SQLiteDatabase;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import java.util.List;
import org.litepal.util.BaseUtility;

/* loaded from: classes.dex */
class QueryHandler extends DataHandler {
    QueryHandler(SQLiteDatabase db) {
        this.mDatabase = db;
    }

    <T> T onFind(Class<T> modelClass, long id, boolean isEager) {
        List<T> dataList = query(modelClass, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null, null, getForeignKeyAssociations(modelClass.getName(), isEager));
        if (dataList.size() > 0) {
            return dataList.get(0);
        }
        return null;
    }

    <T> T onFindFirst(Class<T> modelClass, boolean isEager) {
        List<T> dataList = query(modelClass, null, null, null, null, null, TtmlNode.ATTR_ID, "1", getForeignKeyAssociations(modelClass.getName(), isEager));
        if (dataList.size() > 0) {
            return dataList.get(0);
        }
        return null;
    }

    <T> T onFindLast(Class<T> modelClass, boolean isEager) {
        List<T> dataList = query(modelClass, null, null, null, null, null, "id desc", "1", getForeignKeyAssociations(modelClass.getName(), isEager));
        if (dataList.size() > 0) {
            return dataList.get(0);
        }
        return null;
    }

    <T> List<T> onFindAll(Class<T> modelClass, boolean isEager, long... ids) {
        if (isAffectAllLines(ids)) {
            List<T> dataList = query(modelClass, null, null, null, null, null, TtmlNode.ATTR_ID, null, getForeignKeyAssociations(modelClass.getName(), isEager));
            return dataList;
        }
        List<T> dataList2 = query(modelClass, null, getWhereOfIdsWithOr(ids), null, null, null, TtmlNode.ATTR_ID, null, getForeignKeyAssociations(modelClass.getName(), isEager));
        return dataList2;
    }

    <T> List<T> onFind(Class<T> modelClass, String[] columns, String[] conditions, String orderBy, String limit, boolean isEager) {
        BaseUtility.checkConditionsCorrect(conditions);
        List<T> dataList = query(modelClass, columns, getWhereClause(conditions), getWhereArgs(conditions), null, null, orderBy, limit, getForeignKeyAssociations(modelClass.getName(), isEager));
        return dataList;
    }

    int onCount(String tableName, String[] conditions) {
        return ((Integer) mathQuery(tableName, new String[]{"count(1)"}, conditions, Integer.TYPE)).intValue();
    }

    double onAverage(String tableName, String column, String[] conditions) {
        return ((Double) mathQuery(tableName, new String[]{"avg(" + column + ")"}, conditions, Double.TYPE)).doubleValue();
    }

    <T> T onMax(String str, String str2, String[] strArr, Class<T> cls) {
        return (T) mathQuery(str, new String[]{"max(" + str2 + ")"}, strArr, cls);
    }

    <T> T onMin(String str, String str2, String[] strArr, Class<T> cls) {
        return (T) mathQuery(str, new String[]{"min(" + str2 + ")"}, strArr, cls);
    }

    <T> T onSum(String str, String str2, String[] strArr, Class<T> cls) {
        return (T) mathQuery(str, new String[]{"sum(" + str2 + ")"}, strArr, cls);
    }
}
