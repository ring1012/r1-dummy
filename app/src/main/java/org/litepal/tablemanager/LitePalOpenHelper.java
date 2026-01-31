package org.litepal.tablemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.litepal.LitePalApplication;
import org.litepal.util.SharedUtil;

/* loaded from: classes.dex */
class LitePalOpenHelper extends SQLiteOpenHelper {
    public static final String TAG = "LitePalHelper";

    LitePalOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    LitePalOpenHelper(String dbName, int version) {
        this(LitePalApplication.getContext(), dbName, null, version);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase db) {
        Generator.create(db);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Generator.upgrade(db);
        SharedUtil.updateVersion(newVersion);
    }
}
