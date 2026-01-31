package com.unisound.vui.data.c;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

/* loaded from: classes.dex */
public class b extends AbstractDao<com.unisound.vui.data.entity.a.b, Long> {
    public b(DaoConfig daoConfig, d dVar) {
        super(daoConfig, dVar);
    }

    public static void a(Database database, boolean z) {
        database.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "\"CONTACT\" (\"_id\" INTEGER PRIMARY KEY ,\"NAME\" TEXT NOT NULL ,\"PY\" TEXT,\"NUMBER\" TEXT,\"NAME_TIME_STAMP\" INTEGER,\"NUMBER_TIME_STAMP\" INTEGER,\"USAGE_COUNTER\" INTEGER);");
    }

    public static void b(Database database, boolean z) {
        database.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "\"CONTACT\"");
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
    public Long getKey(com.unisound.vui.data.entity.a.b bVar) {
        if (bVar != null) {
            return bVar.a();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final Long updateKeyAfterInsert(com.unisound.vui.data.entity.a.b bVar, long j) {
        bVar.a(Long.valueOf(j));
        return Long.valueOf(j);
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public void readEntity(Cursor cursor, com.unisound.vui.data.entity.a.b bVar, int i) {
        bVar.a(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)));
        bVar.a(cursor.getString(i + 1));
        bVar.b(cursor.isNull(i + 2) ? null : cursor.getString(i + 2));
        bVar.c(cursor.isNull(i + 3) ? null : cursor.getString(i + 3));
        bVar.b(cursor.isNull(i + 4) ? null : Long.valueOf(cursor.getLong(i + 4)));
        bVar.c(cursor.isNull(i + 5) ? null : Long.valueOf(cursor.getLong(i + 5)));
        bVar.a(cursor.isNull(i + 6) ? null : Integer.valueOf(cursor.getInt(i + 6)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(SQLiteStatement sQLiteStatement, com.unisound.vui.data.entity.a.b bVar) {
        sQLiteStatement.clearBindings();
        Long lA = bVar.a();
        if (lA != null) {
            sQLiteStatement.bindLong(1, lA.longValue());
        }
        sQLiteStatement.bindString(2, bVar.b());
        String strC = bVar.c();
        if (strC != null) {
            sQLiteStatement.bindString(3, strC);
        }
        String strD = bVar.d();
        if (strD != null) {
            sQLiteStatement.bindString(4, strD);
        }
        Long lE = bVar.e();
        if (lE != null) {
            sQLiteStatement.bindLong(5, lE.longValue());
        }
        Long lF = bVar.f();
        if (lF != null) {
            sQLiteStatement.bindLong(6, lF.longValue());
        }
        if (bVar.g() != null) {
            sQLiteStatement.bindLong(7, r0.intValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(DatabaseStatement databaseStatement, com.unisound.vui.data.entity.a.b bVar) {
        databaseStatement.clearBindings();
        Long lA = bVar.a();
        if (lA != null) {
            databaseStatement.bindLong(1, lA.longValue());
        }
        databaseStatement.bindString(2, bVar.b());
        String strC = bVar.c();
        if (strC != null) {
            databaseStatement.bindString(3, strC);
        }
        String strD = bVar.d();
        if (strD != null) {
            databaseStatement.bindString(4, strD);
        }
        Long lE = bVar.e();
        if (lE != null) {
            databaseStatement.bindLong(5, lE.longValue());
        }
        Long lF = bVar.f();
        if (lF != null) {
            databaseStatement.bindLong(6, lF.longValue());
        }
        if (bVar.g() != null) {
            databaseStatement.bindLong(7, r0.intValue());
        }
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public com.unisound.vui.data.entity.a.b readEntity(Cursor cursor, int i) {
        return new com.unisound.vui.data.entity.a.b(cursor.isNull(i + 0) ? null : Long.valueOf(cursor.getLong(i + 0)), cursor.getString(i + 1), cursor.isNull(i + 2) ? null : cursor.getString(i + 2), cursor.isNull(i + 3) ? null : cursor.getString(i + 3), cursor.isNull(i + 4) ? null : Long.valueOf(cursor.getLong(i + 4)), cursor.isNull(i + 5) ? null : Long.valueOf(cursor.getLong(i + 5)), cursor.isNull(i + 6) ? null : Integer.valueOf(cursor.getInt(i + 6)));
    }

    @Override // org.greenrobot.greendao.AbstractDao
    protected final boolean isEntityUpdateable() {
        return true;
    }
}
