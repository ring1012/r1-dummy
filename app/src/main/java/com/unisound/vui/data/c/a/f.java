package com.unisound.vui.data.c.a;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

/* loaded from: classes.dex */
public class f extends AbstractDao<com.unisound.vui.data.entity.a.a.f, Long> {
    public f(DaoConfig daoConfig, com.unisound.vui.data.c.d dVar) {
        super(daoConfig, dVar);
    }

    public static void a(Database database, boolean z) throws SQLException {
        database.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "\"ORIGIN_PUSH_SETTING\" (\"_id\" INTEGER PRIMARY KEY ,\"PUSH_MODEL_ID\" TEXT,\"SETTING\" INTEGER,\"SETTING_PARA\" TEXT);");
    }

    public static void b(Database database, boolean z) throws SQLException {
        database.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "\"ORIGIN_PUSH_SETTING\"");
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
    public Long getKey(com.unisound.vui.data.entity.a.a.f fVar) {
        if (fVar != null) {
            return fVar.a();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Long updateKeyAfterInsert(com.unisound.vui.data.entity.a.a.f fVar, long j) {
        fVar.a(Long.valueOf(j));
        return Long.valueOf(j);
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public void readEntity(Cursor cursor, com.unisound.vui.data.entity.a.a.f fVar, int i) {
        fVar.a(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        fVar.a(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        fVar.a(cursor.isNull(i + 2) ? null : Integer.valueOf(cursor.getInt(i + 2)));
        fVar.b(cursor.isNull(i + 3) ? null : cursor.getString(i + 3));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(SQLiteStatement sQLiteStatement, com.unisound.vui.data.entity.a.a.f fVar) {
        sQLiteStatement.clearBindings();
        Long lA = fVar.a();
        if (lA != null) {
            sQLiteStatement.bindLong(1, lA.longValue());
        }
        String strB = fVar.b();
        if (strB != null) {
            sQLiteStatement.bindString(2, strB);
        }
        if (fVar.c() != null) {
            sQLiteStatement.bindLong(3, r0.intValue());
        }
        String strD = fVar.d();
        if (strD != null) {
            sQLiteStatement.bindString(4, strD);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(DatabaseStatement databaseStatement, com.unisound.vui.data.entity.a.a.f fVar) {
        databaseStatement.clearBindings();
        Long lA = fVar.a();
        if (lA != null) {
            databaseStatement.bindLong(1, lA.longValue());
        }
        String strB = fVar.b();
        if (strB != null) {
            databaseStatement.bindString(2, strB);
        }
        if (fVar.c() != null) {
            databaseStatement.bindLong(3, r0.intValue());
        }
        String strD = fVar.d();
        if (strD != null) {
            databaseStatement.bindString(4, strD);
        }
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public com.unisound.vui.data.entity.a.a.f readEntity(Cursor cursor, int i) {
        return new com.unisound.vui.data.entity.a.a.f(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)), cursor.isNull(i + 1) ? null : cursor.getString(i + 1), cursor.isNull(i + 2) ? null : Integer.valueOf(cursor.getInt(i + 2)), cursor.isNull(i + 3) ? null : cursor.getString(i + 3));
    }

    @Override // org.greenrobot.greendao.AbstractDao
    protected final boolean isEntityUpdateable() {
        return true;
    }
}
