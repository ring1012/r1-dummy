package com.unisound.vui.data.c.a;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

/* loaded from: classes.dex */
public class c extends AbstractDao<com.unisound.vui.data.entity.a.a.c, Long> {
    public c(DaoConfig daoConfig, com.unisound.vui.data.c.d dVar) {
        super(daoConfig, dVar);
    }

    public static void a(Database database, boolean z) throws SQLException {
        database.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "\"ORIGIN_PUSH_COMMAND\" (\"_id\" INTEGER PRIMARY KEY ,\"PUSH_ACTION_ID\" TEXT,\"CONFIRMS\" TEXT,\"CANCELS\" TEXT);");
    }

    public static void b(Database database, boolean z) throws SQLException {
        database.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "\"ORIGIN_PUSH_COMMAND\"");
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
    public Long getKey(com.unisound.vui.data.entity.a.a.c cVar) {
        if (cVar != null) {
            return cVar.a();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Long updateKeyAfterInsert(com.unisound.vui.data.entity.a.a.c cVar, long j) {
        cVar.a(Long.valueOf(j));
        return Long.valueOf(j);
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public void readEntity(Cursor cursor, com.unisound.vui.data.entity.a.a.c cVar, int i) {
        cVar.a(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        cVar.a(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        cVar.b(cursor.isNull(i + 2) ? null : cursor.getString(i + 2));
        cVar.c(cursor.isNull(i + 3) ? null : cursor.getString(i + 3));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(SQLiteStatement sQLiteStatement, com.unisound.vui.data.entity.a.a.c cVar) {
        sQLiteStatement.clearBindings();
        Long lA = cVar.a();
        if (lA != null) {
            sQLiteStatement.bindLong(1, lA.longValue());
        }
        String strB = cVar.b();
        if (strB != null) {
            sQLiteStatement.bindString(2, strB);
        }
        String strC = cVar.c();
        if (strC != null) {
            sQLiteStatement.bindString(3, strC);
        }
        String strD = cVar.d();
        if (strD != null) {
            sQLiteStatement.bindString(4, strD);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(DatabaseStatement databaseStatement, com.unisound.vui.data.entity.a.a.c cVar) {
        databaseStatement.clearBindings();
        Long lA = cVar.a();
        if (lA != null) {
            databaseStatement.bindLong(1, lA.longValue());
        }
        String strB = cVar.b();
        if (strB != null) {
            databaseStatement.bindString(2, strB);
        }
        String strC = cVar.c();
        if (strC != null) {
            databaseStatement.bindString(3, strC);
        }
        String strD = cVar.d();
        if (strD != null) {
            databaseStatement.bindString(4, strD);
        }
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public com.unisound.vui.data.entity.a.a.c readEntity(Cursor cursor, int i) {
        return new com.unisound.vui.data.entity.a.a.c(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)), cursor.isNull(i + 1) ? null : cursor.getString(i + 1), cursor.isNull(i + 2) ? null : cursor.getString(i + 2), cursor.isNull(i + 3) ? null : cursor.getString(i + 3));
    }

    @Override // org.greenrobot.greendao.AbstractDao
    protected final boolean isEntityUpdateable() {
        return true;
    }
}
