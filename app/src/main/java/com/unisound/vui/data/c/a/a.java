package com.unisound.vui.data.c.a;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

/* loaded from: classes.dex */
public class a extends AbstractDao<com.unisound.vui.data.entity.a.a.a, Long> {
    public a(DaoConfig daoConfig, com.unisound.vui.data.c.d dVar) {
        super(daoConfig, dVar);
    }

    public static void a(Database database, boolean z) throws SQLException {
        database.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "\"ORIGIN_PUSH_ACTION\" (\"_id\" INTEGER PRIMARY KEY ,\"PUSH_MODEL_ID\" TEXT,\"PUSH_ACTION_ID\" TEXT,\"ACTION\" INTEGER,\"ACTION_MUSIC\" TEXT,\"ACTION_TTS\" TEXT,\"CLASS_NAME\" TEXT);");
    }

    public static void b(Database database, boolean z) throws SQLException {
        database.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "\"ORIGIN_PUSH_ACTION\"");
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
    public Long getKey(com.unisound.vui.data.entity.a.a.a aVar) {
        if (aVar != null) {
            return aVar.a();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Long updateKeyAfterInsert(com.unisound.vui.data.entity.a.a.a aVar, long j) {
        aVar.a(Long.valueOf(j));
        return Long.valueOf(j);
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public void readEntity(Cursor cursor, com.unisound.vui.data.entity.a.a.a aVar, int i) {
        aVar.a(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        aVar.a(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        aVar.b(cursor.isNull(i + 2) ? null : cursor.getString(i + 2));
        aVar.a(cursor.isNull(i + 3) ? null : Integer.valueOf(cursor.getInt(i + 3)));
        aVar.c(cursor.isNull(i + 4) ? null : cursor.getString(i + 4));
        aVar.d(cursor.isNull(i + 5) ? null : cursor.getString(i + 5));
        aVar.e(cursor.isNull(i + 6) ? null : cursor.getString(i + 6));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(SQLiteStatement sQLiteStatement, com.unisound.vui.data.entity.a.a.a aVar) {
        sQLiteStatement.clearBindings();
        Long lA = aVar.a();
        if (lA != null) {
            sQLiteStatement.bindLong(1, lA.longValue());
        }
        String strB = aVar.b();
        if (strB != null) {
            sQLiteStatement.bindString(2, strB);
        }
        String strC = aVar.c();
        if (strC != null) {
            sQLiteStatement.bindString(3, strC);
        }
        if (aVar.d() != null) {
            sQLiteStatement.bindLong(4, r0.intValue());
        }
        String strE = aVar.e();
        if (strE != null) {
            sQLiteStatement.bindString(5, strE);
        }
        String strF = aVar.f();
        if (strF != null) {
            sQLiteStatement.bindString(6, strF);
        }
        String strG = aVar.g();
        if (strG != null) {
            sQLiteStatement.bindString(7, strG);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(DatabaseStatement databaseStatement, com.unisound.vui.data.entity.a.a.a aVar) {
        databaseStatement.clearBindings();
        Long lA = aVar.a();
        if (lA != null) {
            databaseStatement.bindLong(1, lA.longValue());
        }
        String strB = aVar.b();
        if (strB != null) {
            databaseStatement.bindString(2, strB);
        }
        String strC = aVar.c();
        if (strC != null) {
            databaseStatement.bindString(3, strC);
        }
        if (aVar.d() != null) {
            databaseStatement.bindLong(4, r0.intValue());
        }
        String strE = aVar.e();
        if (strE != null) {
            databaseStatement.bindString(5, strE);
        }
        String strF = aVar.f();
        if (strF != null) {
            databaseStatement.bindString(6, strF);
        }
        String strG = aVar.g();
        if (strG != null) {
            databaseStatement.bindString(7, strG);
        }
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public com.unisound.vui.data.entity.a.a.a readEntity(Cursor cursor, int i) {
        return new com.unisound.vui.data.entity.a.a.a(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)), cursor.isNull(i + 1) ? null : cursor.getString(i + 1), cursor.isNull(i + 2) ? null : cursor.getString(i + 2), cursor.isNull(i + 3) ? null : Integer.valueOf(cursor.getInt(i + 3)), cursor.isNull(i + 4) ? null : cursor.getString(i + 4), cursor.isNull(i + 5) ? null : cursor.getString(i + 5), cursor.isNull(i + 6) ? null : cursor.getString(i + 6));
    }

    @Override // org.greenrobot.greendao.AbstractDao
    protected final boolean isEntityUpdateable() {
        return true;
    }
}
