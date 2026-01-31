package org.litepal.exceptions;

/* loaded from: classes.dex */
public class InvalidAttributesException extends RuntimeException {
    public static final String CASES_VALUE_IS_INVALID = " is an invalid value for <cases></cases>";
    public static final String DBNAME_IS_EMPTY_OR_NOT_DEFINED = "dbname is empty or not defined in litepal.xml file";
    public static final String VERSION_IS_EARLIER_THAN_CURRENT = "the version in litepal.xml is earlier than the current version";
    public static final String VERSION_OF_DATABASE_LESS_THAN_ONE = "the version of database can not be less than 1";
    private static final long serialVersionUID = 1;

    public InvalidAttributesException(String errorMessage) {
        super(errorMessage);
    }
}
