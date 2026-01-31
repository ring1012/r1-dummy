package org.litepal.util;

/* loaded from: classes.dex */
public interface Const {

    public interface LitePal {
        public static final String CASES_KEEP = "keep";
        public static final String CASES_LOWER = "lower";
        public static final String CASES_UPPER = "upper";
        public static final String CONFIGURATION_FILE_NAME = "litepal.xml";
        public static final String DB_NAME_SUFFIX = ".db";
    }

    public interface Model {
        public static final int MANY_TO_MANY = 3;
        public static final int MANY_TO_ONE = 2;
        public static final int ONE_TO_ONE = 1;
    }

    public interface TableSchema {
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";
        public static final int INTERMEDIATE_JOIN_TABLE = 1;
        public static final int NORMAL_TABLE = 0;
        public static final String TABLE_NAME = "table_schema";
    }
}
