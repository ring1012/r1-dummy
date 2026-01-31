package org.litepal.parser;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import org.litepal.exceptions.InvalidAttributesException;
import org.litepal.util.Const;
import org.litepal.util.SharedUtil;

/* loaded from: classes.dex */
public final class LitePalAttr {
    private static LitePalAttr litePalAttr;
    private String cases;
    private List<String> classNames;
    private String dbName;
    private int version;

    private LitePalAttr() {
    }

    public static LitePalAttr getInstance() {
        if (litePalAttr == null) {
            synchronized (LitePalAttr.class) {
                if (litePalAttr == null) {
                    litePalAttr = new LitePalAttr();
                }
            }
        }
        return litePalAttr;
    }

    public int getVersion() {
        return this.version;
    }

    void setVersion(int version) {
        this.version = version;
    }

    public String getDbName() {
        return this.dbName;
    }

    void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public List<String> getClassNames() {
        if (this.classNames == null) {
            this.classNames = new ArrayList();
            this.classNames.add("org.litepal.model.Table_Schema");
        } else if (this.classNames.isEmpty()) {
            this.classNames.add("org.litepal.model.Table_Schema");
        }
        return this.classNames;
    }

    void addClassName(String className) {
        getClassNames().add(className);
    }

    public String getCases() {
        return this.cases;
    }

    void setCases(String cases) {
        this.cases = cases;
    }

    public boolean checkSelfValid() {
        if (TextUtils.isEmpty(this.dbName)) {
            throw new InvalidAttributesException(InvalidAttributesException.DBNAME_IS_EMPTY_OR_NOT_DEFINED);
        }
        if (!this.dbName.endsWith(Const.LitePal.DB_NAME_SUFFIX)) {
            this.dbName = String.valueOf(this.dbName) + Const.LitePal.DB_NAME_SUFFIX;
        }
        if (this.version < 1) {
            throw new InvalidAttributesException(InvalidAttributesException.VERSION_OF_DATABASE_LESS_THAN_ONE);
        }
        if (this.version < SharedUtil.getLastVersion()) {
            throw new InvalidAttributesException(InvalidAttributesException.VERSION_IS_EARLIER_THAN_CURRENT);
        }
        if (TextUtils.isEmpty(this.cases)) {
            this.cases = Const.LitePal.CASES_LOWER;
        } else if (!this.cases.equals(Const.LitePal.CASES_UPPER) && !this.cases.equals(Const.LitePal.CASES_LOWER) && !this.cases.equals(Const.LitePal.CASES_KEEP)) {
            throw new InvalidAttributesException(String.valueOf(this.cases) + InvalidAttributesException.CASES_VALUE_IS_INVALID);
        }
        return true;
    }
}
