package com.unisound.vui.data.c;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

@Deprecated
/* loaded from: classes.dex */
public class a extends AbstractDao<com.unisound.vui.data.entity.a.a, Long> {
    public a(DaoConfig daoConfig, d dVar) {
        super(daoConfig, dVar);
    }

    public static void a(Database database, boolean z) throws SQLException {
        database.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "\"APP\" (\"_id\" INTEGER PRIMARY KEY ,\"PACKAGE_NAME\" TEXT NOT NULL ,\"APP_LABEL\" TEXT,\"APP_PY_LABEL\" TEXT,\"CLASS_NAME\" TEXT,\"NICK_NAME\" TEXT);");
    }

    public static void b(Database database, boolean z) throws SQLException {
        database.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "\"APP\"");
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
    public Long getKey(com.unisound.vui.data.entity.a.a aVar) {
        if (aVar != null) {
            return aVar.a();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Long updateKeyAfterInsert(com.unisound.vui.data.entity.a.a aVar, long j) {
        aVar.a(Long.valueOf(j));
        return Long.valueOf(j);
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public void readEntity(Cursor cursor, com.unisound.vui.data.entity.a.a aVar, int i) {
        aVar.a(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        aVar.a(cursor.getString(i + 1));
        aVar.b(cursor.isNull(i + 2) ? null : cursor.getString(i + 2));
        aVar.c(cursor.isNull(i + 3) ? null : cursor.getString(i + 3));
        aVar.d(cursor.isNull(i + 4) ? null : cursor.getString(i + 4));
        aVar.e(cursor.isNull(i + 5) ? null : cursor.getString(i + 5));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(SQLiteStatement sQLiteStatement, com.unisound.vui.data.entity.a.a aVar) {
        sQLiteStatement.clearBindings();
        Long lA = aVar.a();
        if (lA != null) {
            sQLiteStatement.bindLong(1, lA.longValue());
        }
        sQLiteStatement.bindString(2, aVar.b());
        String strC = aVar.c();
        if (strC != null) {
            sQLiteStatement.bindString(3, strC);
        }
        String strD = aVar.d();
        if (strD != null) {
            sQLiteStatement.bindString(4, strD);
        }
        String strE = aVar.e();
        if (strE != null) {
            sQLiteStatement.bindString(5, strE);
        }
        String strF = aVar.f();
        if (strF != null) {
            sQLiteStatement.bindString(6, strF);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(DatabaseStatement databaseStatement, com.unisound.vui.data.entity.a.a aVar) {
        databaseStatement.clearBindings();
        Long lA = aVar.a();
        if (lA != null) {
            databaseStatement.bindLong(1, lA.longValue());
        }
        databaseStatement.bindString(2, aVar.b());
        String strC = aVar.c();
        if (strC != null) {
            databaseStatement.bindString(3, strC);
        }
        String strD = aVar.d();
        if (strD != null) {
            databaseStatement.bindString(4, strD);
        }
        String strE = aVar.e();
        if (strE != null) {
            databaseStatement.bindString(5, strE);
        }
        String strF = aVar.f();
        if (strF != null) {
            databaseStatement.bindString(6, strF);
        }
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public com.unisound.vui.data.entity.a.a readEntity(Cursor cursor, int i) {
        return new com.unisound.vui.data.entity.a.a(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)), cursor.getString(i + 1), cursor.isNull(i + 2) ? null : cursor.getString(i + 2), cursor.isNull(i + 3) ? null : cursor.getString(i + 3), cursor.isNull(i + 4) ? null : cursor.getString(i + 4), cursor.isNull(i + 5) ? null : cursor.getString(i + 5));
    }

    @Override // org.greenrobot.greendao.AbstractDao
    protected final boolean isEntityUpdateable() {
        return true;
    }
}
