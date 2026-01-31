package com.unisound.vui.data.e;

import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;
import cn.yunzhisheng.common.PinyinConverter;
import com.unisound.vui.data.c.c;
import com.unisound.vui.util.LogMgr;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;

/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    private static a f395a;

    public static a a() {
        if (f395a == null) {
            f395a = new a();
        }
        return f395a;
    }

    private String a(Class<?> cls) throws Exception {
        if (String.class.equals(cls)) {
            return "TEXT";
        }
        if (Long.class.equals(cls) || Integer.class.equals(cls) || Long.TYPE.equals(cls)) {
            return "INTEGER";
        }
        if (Boolean.class.equals(cls)) {
            return "BOOLEAN";
        }
        throw new Exception("MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT PARAMETERS".concat(" - Class: ").concat(cls.toString()));
    }

    private static List<String> a(Database database, String str) {
        Cursor cursorRawQuery = null;
        ArrayList arrayList = new ArrayList();
        try {
            try {
                cursorRawQuery = database.rawQuery("SELECT * FROM " + str + " limit 1", null);
                if (cursorRawQuery != null) {
                    arrayList = new ArrayList(Arrays.asList(cursorRawQuery.getColumnNames()));
                }
            } catch (Exception e) {
                LogMgr.v(str, e.getMessage());
                e.printStackTrace();
                if (cursorRawQuery != null) {
                    cursorRawQuery.close();
                }
            }
            return arrayList;
        } finally {
            if (cursorRawQuery != null) {
                cursorRawQuery.close();
            }
        }
    }

    private void b(Database database, Class<? extends AbstractDao<?, ?>>... clsArr) throws SQLException {
        for (Class<? extends AbstractDao<?, ?>> cls : clsArr) {
            DaoConfig daoConfig = new DaoConfig(database, cls);
            String str = "";
            String str2 = daoConfig.tablename;
            String strConcat = daoConfig.tablename.concat("_TEMP");
            ArrayList arrayList = new ArrayList();
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE ").append(strConcat).append(" (");
            for (int i = 0; i < daoConfig.properties.length; i++) {
                String str3 = daoConfig.properties[i].columnName;
                if (a(database, str2).contains(str3)) {
                    arrayList.add(str3);
                    String strA = null;
                    try {
                        strA = a(daoConfig.properties[i].type);
                    } catch (Exception e) {
                    }
                    sb.append(str).append(str3).append(PinyinConverter.PINYIN_SEPARATOR).append(strA);
                    if (daoConfig.properties[i].primaryKey) {
                        sb.append(" PRIMARY KEY");
                    }
                    str = ",";
                }
            }
            sb.append(");");
            database.execSQL(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("INSERT INTO ").append(strConcat).append(" (");
            sb2.append(TextUtils.join(",", arrayList));
            sb2.append(") SELECT ");
            sb2.append(TextUtils.join(",", arrayList));
            sb2.append(" FROM ").append(str2).append(";");
            database.execSQL(sb2.toString());
        }
    }

    private void c(Database database, Class<? extends AbstractDao<?, ?>>... clsArr) throws SQLException {
        for (Class<? extends AbstractDao<?, ?>> cls : clsArr) {
            DaoConfig daoConfig = new DaoConfig(database, cls);
            String str = daoConfig.tablename;
            String strConcat = daoConfig.tablename.concat("_TEMP");
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < daoConfig.properties.length; i++) {
                String str2 = daoConfig.properties[i].columnName;
                if (a(database, strConcat).contains(str2)) {
                    arrayList.add(str2);
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO ").append(str).append(" (");
            sb.append(TextUtils.join(",", arrayList));
            sb.append(") SELECT ");
            sb.append(TextUtils.join(",", arrayList));
            sb.append(" FROM ").append(strConcat).append(";");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("DROP TABLE ").append(strConcat);
            database.execSQL(sb.toString());
            database.execSQL(sb2.toString());
        }
    }

    public void a(Database database, Class<? extends AbstractDao<?, ?>>... clsArr) {
        b(database, clsArr);
        c.b(database, true);
        c.a(database, false);
        c(database, clsArr);
    }
}
