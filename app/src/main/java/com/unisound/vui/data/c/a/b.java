package com.unisound.vui.data.c.a;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

/* loaded from: classes.dex */
public class b extends AbstractDao<com.unisound.vui.data.entity.a.a.b, Long> {
    public b(DaoConfig daoConfig, com.unisound.vui.data.c.d dVar) {
        super(daoConfig, dVar);
    }

    public static void a(Database database, boolean z) throws SQLException {
        database.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "\"ORIGIN_PUSH_BASE_INFO\" (\"_id\" INTEGER PRIMARY KEY ,\"PUSH_MODEL_ID\" TEXT,\"PUSH_BASE_INFO_ID\" TEXT,\"NOTIFICATION_TITLE\" TEXT,\"NOTIFICATION_CONTENT\" TEXT,\"NOTIFICATION_ICON\" TEXT,\"TTS\" TEXT);");
    }

    public static void b(Database database, boolean z) throws SQLException {
        database.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "\"ORIGIN_PUSH_BASE_INFO\"");
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
    public Long getKey(com.unisound.vui.data.entity.a.a.b bVar) {
        if (bVar != null) {
            return bVar.a();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Long updateKeyAfterInsert(com.unisound.vui.data.entity.a.a.b bVar, long j) {
        bVar.a(Long.valueOf(j));
        return Long.valueOf(j);
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public void readEntity(Cursor cursor, com.unisound.vui.data.entity.a.a.b bVar, int i) {
        bVar.a(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        bVar.a(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        bVar.b(cursor.isNull(i + 2) ? null : cursor.getString(i + 2));
        bVar.c(cursor.isNull(i + 3) ? null : cursor.getString(i + 3));
        bVar.d(cursor.isNull(i + 4) ? null : cursor.getString(i + 4));
        bVar.e(cursor.isNull(i + 5) ? null : cursor.getString(i + 5));
        bVar.f(cursor.isNull(i + 6) ? null : cursor.getString(i + 6));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(SQLiteStatement sQLiteStatement, com.unisound.vui.data.entity.a.a.b bVar) {
        sQLiteStatement.clearBindings();
        Long lA = bVar.a();
        if (lA != null) {
            sQLiteStatement.bindLong(1, lA.longValue());
        }
        String strB = bVar.b();
        if (strB != null) {
            sQLiteStatement.bindString(2, strB);
        }
        String strC = bVar.c();
        if (strC != null) {
            sQLiteStatement.bindString(3, strC);
        }
        String strD = bVar.d();
        if (strD != null) {
            sQLiteStatement.bindString(4, strD);
        }
        String strE = bVar.e();
        if (strE != null) {
            sQLiteStatement.bindString(5, strE);
        }
        String strF = bVar.f();
        if (strF != null) {
            sQLiteStatement.bindString(6, strF);
        }
        String strG = bVar.g();
        if (strG != null) {
            sQLiteStatement.bindString(7, strG);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(DatabaseStatement databaseStatement, com.unisound.vui.data.entity.a.a.b bVar) {
        databaseStatement.clearBindings();
        Long lA = bVar.a();
        if (lA != null) {
            databaseStatement.bindLong(1, lA.longValue());
        }
        String strB = bVar.b();
        if (strB != null) {
            databaseStatement.bindString(2, strB);
        }
        String strC = bVar.c();
        if (strC != null) {
            databaseStatement.bindString(3, strC);
        }
        String strD = bVar.d();
        if (strD != null) {
            databaseStatement.bindString(4, strD);
        }
        String strE = bVar.e();
        if (strE != null) {
            databaseStatement.bindString(5, strE);
        }
        String strF = bVar.f();
        if (strF != null) {
            databaseStatement.bindString(6, strF);
        }
        String strG = bVar.g();
        if (strG != null) {
            databaseStatement.bindString(7, strG);
        }
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public com.unisound.vui.data.entity.a.a.b readEntity(Cursor cursor, int i) {
        return new com.unisound.vui.data.entity.a.a.b(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)), cursor.isNull(i + 1) ? null : cursor.getString(i + 1), cursor.isNull(i + 2) ? null : cursor.getString(i + 2), cursor.isNull(i + 3) ? null : cursor.getString(i + 3), cursor.isNull(i + 4) ? null : cursor.getString(i + 4), cursor.isNull(i + 5) ? null : cursor.getString(i + 5), cursor.isNull(i + 6) ? null : cursor.getString(i + 6));
    }

    @Override // org.greenrobot.greendao.AbstractDao
    protected final boolean isEntityUpdateable() {
        return true;
    }
}
