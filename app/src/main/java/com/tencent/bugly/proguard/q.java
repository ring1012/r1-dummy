package com.tencent.bugly.proguard;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.util.Iterator;
import java.util.List;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class q extends SQLiteOpenHelper {

    /* renamed from: a, reason: collision with root package name */
    public static String f199a = "bugly_db";
    private static int b = 13;
    private Context c;
    private List<com.tencent.bugly.a> d;

    /* JADX WARN: Illegal instructions before constructor call */
    public q(Context context, List<com.tencent.bugly.a> list) {
        StringBuilder sbAppend = new StringBuilder().append(f199a).append("_");
        com.tencent.bugly.crashreport.common.info.a.a(context).getClass();
        super(context, sbAppend.toString(), (SQLiteDatabase.CursorFactory) null, b);
        this.c = context;
        this.d = list;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final synchronized void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.setLength(0);
            sb.append(" CREATE TABLE IF NOT EXISTS t_ui").append(" ( _id").append(" INTEGER PRIMARY KEY").append(" , _tm").append(" int").append(" , _ut").append(" int").append(" , _tp").append(" int").append(" , _dt").append(" blob").append(" , _pc").append(" text").append(" ) ");
            x.c(sb.toString(), new Object[0]);
            sQLiteDatabase.execSQL(sb.toString(), new String[0]);
            sb.setLength(0);
            sb.append(" CREATE TABLE IF NOT EXISTS t_lr").append(" ( _id").append(" INTEGER PRIMARY KEY").append(" , _tp").append(" int").append(" , _tm").append(" int").append(" , _pc").append(" text").append(" , _th").append(" text").append(" , _dt").append(" blob").append(" ) ");
            x.c(sb.toString(), new Object[0]);
            sQLiteDatabase.execSQL(sb.toString(), new String[0]);
            sb.setLength(0);
            sb.append(" CREATE TABLE IF NOT EXISTS t_pf").append(" ( _id").append(" integer").append(" , _tp").append(" text").append(" , _tm").append(" int").append(" , _dt").append(" blob").append(",primary key(_id").append(",_tp").append(" )) ");
            x.c(sb.toString(), new Object[0]);
            sQLiteDatabase.execSQL(sb.toString(), new String[0]);
            sb.setLength(0);
            sb.append(" CREATE TABLE IF NOT EXISTS t_cr").append(" ( _id").append(" INTEGER PRIMARY KEY").append(" , _tm").append(" int").append(" , _s1").append(" text").append(" , _up").append(" int").append(" , _me").append(" int").append(" , _uc").append(" int").append(" , _dt").append(" blob").append(" ) ");
            x.c(sb.toString(), new Object[0]);
            sQLiteDatabase.execSQL(sb.toString(), new String[0]);
            sb.setLength(0);
            sb.append(" CREATE TABLE IF NOT EXISTS dl_1002").append(" (_id").append(" integer primary key autoincrement, _dUrl").append(" varchar(100), _sFile").append(" varchar(100), _sLen").append(" INTEGER, _tLen").append(" INTEGER, _MD5").append(" varchar(100), _DLTIME").append(" INTEGER)");
            x.c(sb.toString(), new Object[0]);
            sQLiteDatabase.execSQL(sb.toString(), new String[0]);
            sb.setLength(0);
            sb.append("CREATE TABLE IF NOT EXISTS ge_1002").append(" (_id").append(" integer primary key autoincrement, _time").append(" INTEGER, _datas").append(" blob)");
            x.c(sb.toString(), new Object[0]);
            sQLiteDatabase.execSQL(sb.toString(), new String[0]);
            sb.setLength(0);
            sb.append(" CREATE TABLE IF NOT EXISTS st_1002").append(" ( _id").append(" integer").append(" , _tp").append(" text").append(" , _tm").append(" int").append(" , _dt").append(" blob").append(",primary key(_id").append(",_tp").append(" )) ");
            x.c(sb.toString(), new Object[0]);
            sQLiteDatabase.execSQL(sb.toString(), new String[0]);
        } catch (Throwable th) {
            if (!x.b(th)) {
                th.printStackTrace();
            }
        }
        if (this.d != null) {
            Iterator<com.tencent.bugly.a> it = this.d.iterator();
            while (it.hasNext()) {
                try {
                    it.next().onDbCreate(sQLiteDatabase);
                } catch (Throwable th2) {
                    if (!x.b(th2)) {
                        th2.printStackTrace();
                    }
                }
            }
        }
    }

    private synchronized boolean a(SQLiteDatabase sQLiteDatabase) {
        boolean z = true;
        synchronized (this) {
            try {
                for (String str : new String[]{"t_lr", "t_ui", "t_pf"}) {
                    sQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + str, new String[0]);
                }
            } catch (Throwable th) {
                if (!x.b(th)) {
                    th.printStackTrace();
                }
                z = false;
            }
        }
        return z;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final synchronized void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        x.d("[Database] Upgrade %d to %d , drop tables!", Integer.valueOf(i), Integer.valueOf(i2));
        if (this.d != null) {
            Iterator<com.tencent.bugly.a> it = this.d.iterator();
            while (it.hasNext()) {
                try {
                    it.next().onDbUpgrade(sQLiteDatabase, i, i2);
                } catch (Throwable th) {
                    if (!x.b(th)) {
                        th.printStackTrace();
                    }
                }
            }
        }
        if (a(sQLiteDatabase)) {
            onCreate(sQLiteDatabase);
        } else {
            x.d("[Database] Failed to drop, delete db.", new Object[0]);
            File databasePath = this.c.getDatabasePath(f199a);
            if (databasePath != null && databasePath.canWrite()) {
                databasePath.delete();
            }
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    @TargetApi(11)
    public final synchronized void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (com.tencent.bugly.crashreport.common.info.b.c() >= 11) {
            x.d("[Database] Downgrade %d to %d drop tables.", Integer.valueOf(i), Integer.valueOf(i2));
            if (this.d != null) {
                Iterator<com.tencent.bugly.a> it = this.d.iterator();
                while (it.hasNext()) {
                    try {
                        it.next().onDbDowngrade(sQLiteDatabase, i, i2);
                    } catch (Throwable th) {
                        if (!x.b(th)) {
                            th.printStackTrace();
                        }
                    }
                }
            }
            if (a(sQLiteDatabase)) {
                onCreate(sQLiteDatabase);
            } else {
                x.d("[Database] Failed to drop, delete db.", new Object[0]);
                File databasePath = this.c.getDatabasePath(f199a);
                if (databasePath != null && databasePath.canWrite()) {
                    databasePath.delete();
                }
            }
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final synchronized SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase readableDatabase;
        int i = 0;
        synchronized (this) {
            readableDatabase = null;
            while (readableDatabase == null && i < 5) {
                i++;
                try {
                    readableDatabase = super.getReadableDatabase();
                } catch (Throwable th) {
                    x.d("[Database] Try to get db(count: %d).", Integer.valueOf(i));
                    if (i == 5) {
                        x.e("[Database] Failed to get db.", new Object[0]);
                    }
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return readableDatabase;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final synchronized SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase writableDatabase;
        int i = 0;
        synchronized (this) {
            writableDatabase = null;
            while (writableDatabase == null && i < 5) {
                i++;
                try {
                    writableDatabase = super.getWritableDatabase();
                } catch (Throwable th) {
                    x.d("[Database] Try to get db(count: %d).", Integer.valueOf(i));
                    if (i == 5) {
                        x.e("[Database] Failed to get db.", new Object[0]);
                    }
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (writableDatabase == null) {
                x.d("[Database] db error delay error record 1min.", new Object[0]);
            }
        }
        return writableDatabase;
    }
}
