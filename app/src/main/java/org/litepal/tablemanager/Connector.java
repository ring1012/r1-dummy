package org.litepal.tablemanager;

import android.database.sqlite.SQLiteDatabase;
import java.io.IOException;
import org.litepal.exceptions.InvalidAttributesException;
import org.litepal.parser.LitePalAttr;
import org.litepal.parser.LitePalParser;
import org.xml.sax.SAXException;

/* loaded from: classes.dex */
public class Connector {
    private static LitePalAttr mLitePalAttr;
    private static LitePalOpenHelper mLitePalHelper;

    public static synchronized SQLiteDatabase getWritableDatabase() {
        LitePalOpenHelper litePalHelper;
        litePalHelper = buildConnection();
        return litePalHelper.getWritableDatabase();
    }

    public static synchronized SQLiteDatabase getReadableDatabase() {
        LitePalOpenHelper litePalHelper;
        litePalHelper = buildConnection();
        return litePalHelper.getReadableDatabase();
    }

    public static SQLiteDatabase getDatabase() {
        return getWritableDatabase();
    }

    private static LitePalOpenHelper buildConnection() throws SAXException, IOException {
        if (mLitePalAttr == null) {
            LitePalParser.parseLitePalConfiguration();
            mLitePalAttr = LitePalAttr.getInstance();
        }
        if (mLitePalAttr.checkSelfValid()) {
            if (mLitePalHelper == null) {
                mLitePalHelper = new LitePalOpenHelper(mLitePalAttr.getDbName(), mLitePalAttr.getVersion());
            }
            return mLitePalHelper;
        }
        throw new InvalidAttributesException("Uncaught invalid attributes exception happened");
    }
}
