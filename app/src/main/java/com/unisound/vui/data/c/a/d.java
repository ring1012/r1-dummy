package com.unisound.vui.data.c.a;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

/* loaded from: classes.dex */
public class d extends AbstractDao<com.unisound.vui.data.entity.a.a.d, Long> {
    public d(DaoConfig daoConfig, com.unisound.vui.data.c.d dVar) {
        super(daoConfig, dVar);
    }

    public static void a(Database database, boolean z) throws SQLException {
        database.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "\"ORIGIN_PUSH_LOCATION\" (\"_id\" INTEGER PRIMARY KEY ,\"PUSH_ACTION_ID\" TEXT,\"LNG\" REAL,\"LAT\" REAL,\"ADDRESS\" TEXT,\"NAME\" TEXT,\"CITY_CODE\" TEXT,\"PROVINCE\" TEXT,\"CITY\" TEXT);");
    }

    public static void b(Database database, boolean z) throws SQLException {
        database.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "\"ORIGIN_PUSH_LOCATION\"");
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public Long readKey(Cursor cursor, int i) {
        if (cursor.isNull(i + 0)) {
            return null;
        }
        return Long.valueOf(cursor.getLong(i + 0));
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public Long getKey(com.unisound.vui.data.entity.a.a.d dVar) {
        if (dVar != null) {
            return dVar.a();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Long updateKeyAfterInsert(com.unisound.vui.data.entity.a.a.d dVar, long j) {
        dVar.a(Long.valueOf(j));
        return Long.valueOf(j);
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public void readEntity(Cursor cursor, com.unisound.vui.data.entity.a.a.d dVar, int i) {
        dVar.a(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        dVar.a(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        dVar.a(cursor.isNull(i + 2) ? null : Double.valueOf(cursor.getDouble(i + 2)));
        dVar.b(cursor.isNull(i + 3) ? null : Double.valueOf(cursor.getDouble(i + 3)));
        dVar.b(cursor.isNull(i + 4) ? null : cursor.getString(i + 4));
        dVar.c(cursor.isNull(i + 5) ? null : cursor.getString(i + 5));
        dVar.d(cursor.isNull(i + 6) ? null : cursor.getString(i + 6));
        dVar.e(cursor.isNull(i + 7) ? null : cursor.getString(i + 7));
        dVar.f(cursor.isNull(i + 8) ? null : cursor.getString(i + 8));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(SQLiteStatement sQLiteStatement, com.unisound.vui.data.entity.a.a.d dVar) {
        sQLiteStatement.clearBindings();
        Long lA = dVar.a();
        if (lA != null) {
            sQLiteStatement.bindLong(1, lA.longValue());
        }
        String strB = dVar.b();
        if (strB != null) {
            sQLiteStatement.bindString(2, strB);
        }
        Double dC = dVar.c();
        if (dC != null) {
            sQLiteStatement.bindDouble(3, dC.doubleValue());
        }
        Double d = dVar.d();
        if (d != null) {
            sQLiteStatement.bindDouble(4, d.doubleValue());
        }
        String strE = dVar.e();
        if (strE != null) {
            sQLiteStatement.bindString(5, strE);
        }
        String strF = dVar.f();
        if (strF != null) {
            sQLiteStatement.bindString(6, strF);
        }
        String strG = dVar.g();
        if (strG != null) {
            sQLiteStatement.bindString(7, strG);
        }
        String strH = dVar.h();
        if (strH != null) {
            sQLiteStatement.bindString(8, strH);
        }
        String strI = dVar.i();
        if (strI != null) {
            sQLiteStatement.bindString(9, strI);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(DatabaseStatement databaseStatement, com.unisound.vui.data.entity.a.a.d dVar) {
        databaseStatement.clearBindings();
        Long lA = dVar.a();
        if (lA != null) {
            databaseStatement.bindLong(1, lA.longValue());
        }
        String strB = dVar.b();
        if (strB != null) {
            databaseStatement.bindString(2, strB);
        }
        Double dC = dVar.c();
        if (dC != null) {
            databaseStatement.bindDouble(3, dC.doubleValue());
        }
        Double d = dVar.d();
        if (d != null) {
            databaseStatement.bindDouble(4, d.doubleValue());
        }
        String strE = dVar.e();
        if (strE != null) {
            databaseStatement.bindString(5, strE);
        }
        String strF = dVar.f();
        if (strF != null) {
            databaseStatement.bindString(6, strF);
        }
        String strG = dVar.g();
        if (strG != null) {
            databaseStatement.bindString(7, strG);
        }
        String strH = dVar.h();
        if (strH != null) {
            databaseStatement.bindString(8, strH);
        }
        String strI = dVar.i();
        if (strI != null) {
            databaseStatement.bindString(9, strI);
        }
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public com.unisound.vui.data.entity.a.a.d readEntity(Cursor cursor, int i) {
        return new com.unisound.vui.data.entity.a.a.d(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)), cursor.isNull(i + 1) ? null : cursor.getString(i + 1), cursor.isNull(i + 2) ? null : Double.valueOf(cursor.getDouble(i + 2)), cursor.isNull(i + 3) ? null : Double.valueOf(cursor.getDouble(i + 3)), cursor.isNull(i + 4) ? null : cursor.getString(i + 4), cursor.isNull(i + 5) ? null : cursor.getString(i + 5), cursor.isNull(i + 6) ? null : cursor.getString(i + 6), cursor.isNull(i + 7) ? null : cursor.getString(i + 7), cursor.isNull(i + 8) ? null : cursor.getString(i + 8));
    }

    @Override // org.greenrobot.greendao.AbstractDao
    protected final boolean isEntityUpdateable() {
        return true;
    }
}
