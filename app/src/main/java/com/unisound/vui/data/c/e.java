package com.unisound.vui.data.c;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

/* loaded from: classes.dex */
public class e extends AbstractDao<com.unisound.vui.data.entity.a.c, Long> {
    public e(DaoConfig daoConfig, d dVar) {
        super(daoConfig, dVar);
    }

    public static void a(Database database, boolean z) {
        database.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "\"POI\" (\"_id\" INTEGER PRIMARY KEY ,\"ADDRESS\" TEXT NOT NULL ,\"POI_TIME_STAMP\" INTEGER,\"USAGE_COUNTER\" INTEGER);");
    }

    public static void b(Database database, boolean z) {
        database.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "\"POI\"");
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
    public Long getKey(com.unisound.vui.data.entity.a.c cVar) {
        if (cVar != null) {
            return cVar.a();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Long updateKeyAfterInsert(com.unisound.vui.data.entity.a.c cVar, long j) {
        cVar.a(Long.valueOf(j));
        return Long.valueOf(j);
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public void readEntity(Cursor cursor, com.unisound.vui.data.entity.a.c cVar, int i) {
        cVar.a(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        cVar.a(cursor.getString(i + 1));
        cVar.b(cursor.isNull(i + 2) ? null : Long.valueOf(cursor.getLong(i + 2)));
        cVar.a(cursor.isNull(i + 3) ? null : Integer.valueOf(cursor.getInt(i + 3)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(SQLiteStatement sQLiteStatement, com.unisound.vui.data.entity.a.c cVar) {
        sQLiteStatement.clearBindings();
        Long lA = cVar.a();
        if (lA != null) {
            sQLiteStatement.bindLong(1, lA.longValue());
        }
        sQLiteStatement.bindString(2, cVar.b());
        Long lC = cVar.c();
        if (lC != null) {
            sQLiteStatement.bindLong(3, lC.longValue());
        }
        if (cVar.d() != null) {
            sQLiteStatement.bindLong(4, r0.intValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(DatabaseStatement databaseStatement, com.unisound.vui.data.entity.a.c cVar) {
        databaseStatement.clearBindings();
        Long lA = cVar.a();
        if (lA != null) {
            databaseStatement.bindLong(1, lA.longValue());
        }
        databaseStatement.bindString(2, cVar.b());
        Long lC = cVar.c();
        if (lC != null) {
            databaseStatement.bindLong(3, lC.longValue());
        }
        if (cVar.d() != null) {
            databaseStatement.bindLong(4, r0.intValue());
        }
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public com.unisound.vui.data.entity.a.c readEntity(Cursor cursor, int i) {
        return new com.unisound.vui.data.entity.a.c(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)), cursor.getString(i + 1), cursor.isNull(i + 2) ? null : Long.valueOf(cursor.getLong(i + 2)), cursor.isNull(i + 3) ? null : Integer.valueOf(cursor.getInt(i + 3)));
    }

    @Override // org.greenrobot.greendao.AbstractDao
    protected final boolean isEntityUpdateable() {
        return true;
    }
}
