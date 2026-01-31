package com.unisound.vui.data.c;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.unisound.common.y;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

/* loaded from: classes.dex */
public class f extends AbstractDao<com.unisound.vui.data.entity.a.d, String> {

    public static class a {

        /* renamed from: a, reason: collision with root package name */
        public static final Property f392a = new Property(0, String.class, TtmlNode.ATTR_ID, true, "ID");
        public static final Property b = new Property(1, String.class, "fromUser", false, "FROM_USER");
        public static final Property c = new Property(2, String.class, "toUser", false, "TO_USER");
        public static final Property d = new Property(3, Integer.class, "msgType", false, "MSG_TYPE");
        public static final Property e = new Property(4, Long.class, "time", false, "TIME");
        public static final Property f = new Property(5, Boolean.class, y.J, false, "SUCCESS");
        public static final Property g = new Property(6, Boolean.class, "read", false, "READ");
        public static final Property h = new Property(7, String.class, "content", false, "CONTENT");
        public static final Property i = new Property(8, String.class, "groupMemberId", false, "GROUP_MEMBER_ID");
        public static final Property j = new Property(9, Boolean.class, "isReceive", false, "IS_RECEIVE");
    }

    public f(DaoConfig daoConfig, d dVar) {
        super(daoConfig, dVar);
    }

    public static void a(Database database, boolean z) {
        database.execSQL("CREATE TABLE " + (z ? "IF NOT EXISTS " : "") + "\"WE_CHAT_MESSAGE\" (\"ID\" TEXT PRIMARY KEY NOT NULL ,\"FROM_USER\" TEXT,\"TO_USER\" TEXT,\"MSG_TYPE\" INTEGER,\"TIME\" INTEGER,\"SUCCESS\" INTEGER,\"READ\" INTEGER,\"CONTENT\" TEXT,\"GROUP_MEMBER_ID\" TEXT,\"IS_RECEIVE\" INTEGER);");
    }

    public static void b(Database database, boolean z) {
        database.execSQL("DROP TABLE " + (z ? "IF EXISTS " : "") + "\"WE_CHAT_MESSAGE\"");
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public String readKey(Cursor cursor, int i) {
        if (cursor.isNull(i + 0)) {
            return null;
        }
        return cursor.getString(i + 0);
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public String getKey(com.unisound.vui.data.entity.a.d dVar) {
        if (dVar != null) {
            return dVar.a();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final String updateKeyAfterInsert(com.unisound.vui.data.entity.a.d dVar, long j) {
        return dVar.a();
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public void readEntity(Cursor cursor, com.unisound.vui.data.entity.a.d dVar, int i) {
        Boolean boolValueOf;
        Boolean boolValueOf2;
        Boolean boolValueOf3 = null;
        dVar.a(cursor.isNull(i + 0) ? null : cursor.getString(i + 0));
        dVar.b(cursor.isNull(i + 1) ? null : cursor.getString(i + 1));
        dVar.c(cursor.isNull(i + 2) ? null : cursor.getString(i + 2));
        dVar.a(cursor.isNull(i + 3) ? null : Integer.valueOf(cursor.getInt(i + 3)));
        dVar.a(cursor.isNull(i + 4) ? null : Long.valueOf(cursor.getLong(i + 4)));
        if (cursor.isNull(i + 5)) {
            boolValueOf = null;
        } else {
            boolValueOf = Boolean.valueOf(cursor.getShort(i + 5) != 0);
        }
        dVar.a(boolValueOf);
        if (cursor.isNull(i + 6)) {
            boolValueOf2 = null;
        } else {
            boolValueOf2 = Boolean.valueOf(cursor.getShort(i + 6) != 0);
        }
        dVar.b(boolValueOf2);
        dVar.d(cursor.isNull(i + 7) ? null : cursor.getString(i + 7));
        dVar.e(cursor.isNull(i + 8) ? null : cursor.getString(i + 8));
        if (!cursor.isNull(i + 9)) {
            boolValueOf3 = Boolean.valueOf(cursor.getShort(i + 9) != 0);
        }
        dVar.c(boolValueOf3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(SQLiteStatement sQLiteStatement, com.unisound.vui.data.entity.a.d dVar) {
        sQLiteStatement.clearBindings();
        String strA = dVar.a();
        if (strA != null) {
            sQLiteStatement.bindString(1, strA);
        }
        String strB = dVar.b();
        if (strB != null) {
            sQLiteStatement.bindString(2, strB);
        }
        String strC = dVar.c();
        if (strC != null) {
            sQLiteStatement.bindString(3, strC);
        }
        if (dVar.d() != null) {
            sQLiteStatement.bindLong(4, r0.intValue());
        }
        Long lE = dVar.e();
        if (lE != null) {
            sQLiteStatement.bindLong(5, lE.longValue());
        }
        Boolean boolF = dVar.f();
        if (boolF != null) {
            sQLiteStatement.bindLong(6, boolF.booleanValue() ? 1L : 0L);
        }
        Boolean boolG = dVar.g();
        if (boolG != null) {
            sQLiteStatement.bindLong(7, boolG.booleanValue() ? 1L : 0L);
        }
        String strH = dVar.h();
        if (strH != null) {
            sQLiteStatement.bindString(8, strH);
        }
        String strI = dVar.i();
        if (strI != null) {
            sQLiteStatement.bindString(9, strI);
        }
        Boolean boolJ = dVar.j();
        if (boolJ != null) {
            sQLiteStatement.bindLong(10, boolJ.booleanValue() ? 1L : 0L);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public final void bindValues(DatabaseStatement databaseStatement, com.unisound.vui.data.entity.a.d dVar) {
        databaseStatement.clearBindings();
        String strA = dVar.a();
        if (strA != null) {
            databaseStatement.bindString(1, strA);
        }
        String strB = dVar.b();
        if (strB != null) {
            databaseStatement.bindString(2, strB);
        }
        String strC = dVar.c();
        if (strC != null) {
            databaseStatement.bindString(3, strC);
        }
        if (dVar.d() != null) {
            databaseStatement.bindLong(4, r0.intValue());
        }
        Long lE = dVar.e();
        if (lE != null) {
            databaseStatement.bindLong(5, lE.longValue());
        }
        Boolean boolF = dVar.f();
        if (boolF != null) {
            databaseStatement.bindLong(6, boolF.booleanValue() ? 1L : 0L);
        }
        Boolean boolG = dVar.g();
        if (boolG != null) {
            databaseStatement.bindLong(7, boolG.booleanValue() ? 1L : 0L);
        }
        String strH = dVar.h();
        if (strH != null) {
            databaseStatement.bindString(8, strH);
        }
        String strI = dVar.i();
        if (strI != null) {
            databaseStatement.bindString(9, strI);
        }
        Boolean boolJ = dVar.j();
        if (boolJ != null) {
            databaseStatement.bindLong(10, boolJ.booleanValue() ? 1L : 0L);
        }
    }

    @Override // org.greenrobot.greendao.AbstractDao
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public com.unisound.vui.data.entity.a.d readEntity(Cursor cursor, int i) {
        Boolean boolValueOf;
        Boolean boolValueOf2;
        Boolean boolValueOf3;
        String string = cursor.isNull(i + 0) ? null : cursor.getString(i + 0);
        String string2 = cursor.isNull(i + 1) ? null : cursor.getString(i + 1);
        String string3 = cursor.isNull(i + 2) ? null : cursor.getString(i + 2);
        Integer numValueOf = cursor.isNull(i + 3) ? null : Integer.valueOf(cursor.getInt(i + 3));
        Long lValueOf = cursor.isNull(i + 4) ? null : Long.valueOf(cursor.getLong(i + 4));
        if (cursor.isNull(i + 5)) {
            boolValueOf = null;
        } else {
            boolValueOf = Boolean.valueOf(cursor.getShort(i + 5) != 0);
        }
        if (cursor.isNull(i + 6)) {
            boolValueOf2 = null;
        } else {
            boolValueOf2 = Boolean.valueOf(cursor.getShort(i + 6) != 0);
        }
        String string4 = cursor.isNull(i + 7) ? null : cursor.getString(i + 7);
        String string5 = cursor.isNull(i + 8) ? null : cursor.getString(i + 8);
        if (cursor.isNull(i + 9)) {
            boolValueOf3 = null;
        } else {
            boolValueOf3 = Boolean.valueOf(cursor.getShort(i + 9) != 0);
        }
        return new com.unisound.vui.data.entity.a.d(string, string2, string3, numValueOf, lValueOf, boolValueOf, boolValueOf2, string4, string5, boolValueOf3);
    }

    @Override // org.greenrobot.greendao.AbstractDao
    protected final boolean isEntityUpdateable() {
        return true;
    }
}
