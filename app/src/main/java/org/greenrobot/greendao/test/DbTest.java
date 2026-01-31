package org.greenrobot.greendao.test;

import android.app.Application;
import android.app.Instrumentation;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import java.util.Random;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.DbUtils;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;

/* loaded from: classes.dex */
public abstract class DbTest extends AndroidTestCase {
    public static final String DB_NAME = "greendao-unittest-db.temp";
    private Application application;
    protected Database db;
    protected final boolean inMemory;
    protected final Random random;

    public DbTest() {
        this(true);
    }

    public DbTest(boolean inMemory) {
        this.inMemory = inMemory;
        this.random = new Random();
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.db = createDatabase();
    }

    public <T extends Application> T createApplication(Class<T> cls) {
        assertNull("Application already created", this.application);
        try {
            T t = (T) Instrumentation.newApplication(cls, getContext());
            t.onCreate();
            this.application = t;
            return t;
        } catch (Exception e) {
            throw new RuntimeException("Could not create application " + cls, e);
        }
    }

    public void terminateApplication() {
        assertNotNull("Application not yet created", this.application);
        this.application.onTerminate();
        this.application = null;
    }

    public <T extends Application> T getApplication() {
        assertNotNull("Application not yet created", this.application);
        return (T) this.application;
    }

    protected Database createDatabase() {
        SQLiteDatabase sqLiteDatabase;
        if (this.inMemory) {
            sqLiteDatabase = SQLiteDatabase.create(null);
        } else {
            getContext().deleteDatabase(DB_NAME);
            sqLiteDatabase = getContext().openOrCreateDatabase(DB_NAME, 0, null);
        }
        return new StandardDatabase(sqLiteDatabase);
    }

    protected void tearDown() throws Exception {
        if (this.application != null) {
            terminateApplication();
        }
        this.db.close();
        if (!this.inMemory) {
            getContext().deleteDatabase(DB_NAME);
        }
        super.tearDown();
    }

    protected void logTableDump(String tablename) {
        if (this.db instanceof StandardDatabase) {
            DbUtils.logTableDump(((StandardDatabase) this.db).getSQLiteDatabase(), tablename);
        } else {
            DaoLog.w("Table dump unsupported for " + this.db);
        }
    }
}
