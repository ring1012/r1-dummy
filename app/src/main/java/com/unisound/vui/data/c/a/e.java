package com.unisound.vui.data.c.a;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

/* loaded from: classes.dex */
public class e extends AbstractDao<com.unisound.vui.data.entity.a.a.e, Void> {
    public e(DaoConfig daoConfig, com.unisound.vui.data.c.d dVar) {
        super(daoConfig, dVar);
    }

    public static void a(Database database, boolean z) throws SQLException {
        database.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "\"ORIGIN_PUSH_MODEL\" (\"ID\" TEXT NOT NULL ,\"PUSH_MODEL_ID\" TEXT,\"READ\" INTEGER,\"TIME\" TEXT,\"PUSH_TIME_STAMP\" INTEGER);");
    }

    public static void b(Database database, boolean z) throws SQLException {
        database.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "\"ORIGIN_PUSH_MODEL\"");
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public Void readKey(Cursor cursor, int i) {
        return null;
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public Void getKey(com.unisound.vui.data.entity.a.a.e eVar) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Void updateKeyAfterInsert(com.unisound.vui.data.entity.a.a.e eVar, long j) {
        return null;
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public void readEntity(Cursor cursor, com.unisound.vui.data.entity.a.a.e eVar, int i) {
        Boolean boolValueOf;
        eVar.a(cursor.getString(i + 0));
        eVar.b(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        if (cursor.isNull(i + 2)) {
            boolValueOf = null;
        } else {
            boolValueOf = Boolean.valueOf(cursor.getShort(i + 2) != 0);
        }
        eVar.a(boolValueOf);
        eVar.c(cursor.isNull(i + 3) ? null : cursor.getString(i + 3));
        eVar.a(cursor.isNull(i + 4) ? null : Long.valueOf(cursor.getLong(i + 4)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(SQLiteStatement sQLiteStatement, com.unisound.vui.data.entity.a.a.e eVar) {
        sQLiteStatement.clearBindings();
        sQLiteStatement.bindString(1, eVar.a());
        String strB = eVar.b();
        if (strB != null) {
            sQLiteStatement.bindString(2, strB);
        }
        Boolean boolC = eVar.c();
        if (boolC != null) {
            sQLiteStatement.bindLong(3, boolC.booleanValue() ? 1L : 0L);
        }
        String strD = eVar.d();
        if (strD != null) {
            sQLiteStatement.bindString(4, strD);
        }
        Long lE = eVar.e();
        if (lE != null) {
            sQLiteStatement.bindLong(5, lE.longValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(DatabaseStatement databaseStatement, com.unisound.vui.data.entity.a.a.e eVar) {
        databaseStatement.clearBindings();
        databaseStatement.bindString(1, eVar.a());
        String strB = eVar.b();
        if (strB != null) {
            databaseStatement.bindString(2, strB);
        }
        Boolean boolC = eVar.c();
        if (boolC != null) {
            databaseStatement.bindLong(3, boolC.booleanValue() ? 1L : 0L);
        }
        String strD = eVar.d();
        if (strD != null) {
            databaseStatement.bindString(4, strD);
        }
        Long lE = eVar.e();
        if (lE != null) {
            databaseStatement.bindLong(5, lE.longValue());
        }
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public com.unisound.vui.data.entity.a.a.e readEntity(Cursor cursor, int i) {
        Boolean boolValueOf;
        String string = cursor.getString(i + 0);
        String string2 = cursor.isNull(i + 1) ? null : cursor.getString(i + 1);
        if (cursor.isNull(i + 2)) {
            boolValueOf = null;
        } else {
            boolValueOf = Boolean.valueOf(cursor.getShort(i + 2) != 0);
        }
        return new com.unisound.vui.data.entity.a.a.e(string, string2, boolValueOf, cursor.isNull(i + 3) ? null : cursor.getString(i + 3), cursor.isNull(i + 4) ? null : Long.valueOf(cursor.getLong(i + 4)));
    }

    @Override // org.greenrobot.greendao.AbstractDao
    protected final boolean isEntityUpdateable() {
        return true;
    }
}
