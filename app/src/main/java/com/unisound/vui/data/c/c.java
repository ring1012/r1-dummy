package com.unisound.vui.data.c;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.unisound.vui.data.c.a.g;
import com.unisound.vui.util.LogMgr;
import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

/* loaded from: classes.dex */
public class c extends AbstractDaoMaster {

    public static class a extends b {
        public a(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory) {
            super(context, str, cursorFactory);
        }

        @Override // org.greenrobot.greendao.database.DatabaseOpenHelper
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            LogMgr.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            if (oldVersion < newVersion) {
                com.unisound.vui.data.e.a.a().a(db, com.unisound.vui.data.c.a.class, com.unisound.vui.data.c.b.class, e.class, f.class, com.unisound.vui.data.c.a.e.class, com.unisound.vui.data.c.a.f.class, com.unisound.vui.data.c.a.b.class, com.unisound.vui.data.c.a.a.class, g.class, com.unisound.vui.data.c.a.d.class, com.unisound.vui.data.c.a.c.class);
            }
        }
    }

    public static abstract class b extends DatabaseOpenHelper {
        public b(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory) {
            super(context, str, cursorFactory, 1);
        }

        @Override // org.greenrobot.greendao.database.DatabaseOpenHelper
        public void onCreate(Database db) throws SQLException {
            LogMgr.i("greenDAO", "Creating tables for schema version 1");
            c.a(db, false);
        }
    }

    public c(SQLiteDatabase sQLiteDatabase) {
        this(new StandardDatabase(sQLiteDatabase));
    }

    public c(Database database) {
        super(database, 1);
        registerDaoClass(com.unisound.vui.data.c.a.class);
        registerDaoClass(com.unisound.vui.data.c.b.class);
        registerDaoClass(e.class);
        registerDaoClass(f.class);
        registerDaoClass(com.unisound.vui.data.c.a.e.class);
        registerDaoClass(com.unisound.vui.data.c.a.f.class);
        registerDaoClass(com.unisound.vui.data.c.a.b.class);
        registerDaoClass(com.unisound.vui.data.c.a.a.class);
        registerDaoClass(g.class);
        registerDaoClass(com.unisound.vui.data.c.a.d.class);
        registerDaoClass(com.unisound.vui.data.c.a.c.class);
    }

    public static void a(Database database, boolean z) throws SQLException {
        com.unisound.vui.data.c.a.a(database, z);
        com.unisound.vui.data.c.b.a(database, z);
        e.a(database, z);
        f.a(database, z);
        com.unisound.vui.data.c.a.e.a(database, z);
        com.unisound.vui.data.c.a.f.a(database, z);
        com.unisound.vui.data.c.a.b.a(database, z);
        com.unisound.vui.data.c.a.a.a(database, z);
        g.a(database, z);
        com.unisound.vui.data.c.a.d.a(database, z);
        com.unisound.vui.data.c.a.c.a(database, z);
    }

    public static void b(Database database, boolean z) throws SQLException {
        com.unisound.vui.data.c.a.b(database, z);
        com.unisound.vui.data.c.b.b(database, z);
        e.b(database, z);
        f.b(database, z);
        com.unisound.vui.data.c.a.e.b(database, z);
        com.unisound.vui.data.c.a.f.b(database, z);
        com.unisound.vui.data.c.a.b.b(database, z);
        com.unisound.vui.data.c.a.a.b(database, z);
        g.b(database, z);
        com.unisound.vui.data.c.a.d.b(database, z);
        com.unisound.vui.data.c.a.c.b(database, z);
    }

    @Override // org.greenrobot.greendao.AbstractDaoMaster
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public d newSession() {
        return new d(this.db, IdentityScopeType.Session, this.daoConfigMap);
    }

    @Override // org.greenrobot.greendao.AbstractDaoMaster
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public d newSession(IdentityScopeType identityScopeType) {
        return new d(this.db, identityScopeType, this.daoConfigMap);
    }
}
