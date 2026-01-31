package org.greenrobot.greendao.query;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;

/* loaded from: classes.dex */
public interface WhereCondition {
    void appendTo(StringBuilder sb, String str);

    void appendValuesTo(List<Object> list);

    public static abstract class AbstractCondition implements WhereCondition {
        protected final boolean hasSingleValue;
        protected final Object value;
        protected final Object[] values;

        public AbstractCondition() {
            this.hasSingleValue = false;
            this.value = null;
            this.values = null;
        }

        public AbstractCondition(Object value) {
            this.value = value;
            this.hasSingleValue = true;
            this.values = null;
        }

        public AbstractCondition(Object[] values) {
            this.value = null;
            this.hasSingleValue = false;
            this.values = values;
        }

        @Override // org.greenrobot.greendao.query.WhereCondition
        public void appendValuesTo(List<Object> valuesTarget) {
            if (this.hasSingleValue) {
                valuesTarget.add(this.value);
                return;
            }
            if (this.values != null) {
                for (Object value : this.values) {
                    valuesTarget.add(value);
                }
            }
        }
    }

    public static class PropertyCondition extends AbstractCondition {
        public final String op;
        public final Property property;

        private static Object checkValueForType(Property property, Object value) {
            if (value != null && value.getClass().isArray()) {
                throw new DaoException("Illegal value: found array, but simple object required");
            }
            Class<?> type = property.type;
            if (type == Date.class) {
                if (value instanceof Date) {
                    return Long.valueOf(((Date) value).getTime());
                }
                if (!(value instanceof Long)) {
                    throw new DaoException("Illegal date value: expected java.util.Date or Long for value " + value);
                }
                return value;
            }
            if (property.type == Boolean.TYPE || property.type == Boolean.class) {
                if (value instanceof Boolean) {
                    return Integer.valueOf(((Boolean) value).booleanValue() ? 1 : 0);
                }
                if (value instanceof Number) {
                    int intValue = ((Number) value).intValue();
                    if (intValue != 0 && intValue != 1) {
                        throw new DaoException("Illegal boolean value: numbers must be 0 or 1, but was " + value);
                    }
                    return value;
                }
                if (value instanceof String) {
                    String stringValue = (String) value;
                    if ("TRUE".equalsIgnoreCase(stringValue)) {
                        return 1;
                    }
                    if ("FALSE".equalsIgnoreCase(stringValue)) {
                        return 0;
                    }
                    throw new DaoException("Illegal boolean value: Strings must be \"TRUE\" or \"FALSE\" (case insensitive), but was " + value);
                }
                return value;
            }
            return value;
        }

        private static Object[] checkValuesForType(Property property, Object[] values) {
            for (int i = 0; i < values.length; i++) {
                values[i] = checkValueForType(property, values[i]);
            }
            return values;
        }

        public PropertyCondition(Property property, String op) {
            this.property = property;
            this.op = op;
        }

        public PropertyCondition(Property property, String op, Object value) {
            super(checkValueForType(property, value));
            this.property = property;
            this.op = op;
        }

        public PropertyCondition(Property property, String op, Object[] values) {
            super(checkValuesForType(property, values));
            this.property = property;
            this.op = op;
        }

        @Override // org.greenrobot.greendao.query.WhereCondition
        public void appendTo(StringBuilder builder, String tableAlias) {
            SqlUtils.appendProperty(builder, tableAlias, this.property).append(this.op);
        }
    }

    public static class StringCondition extends AbstractCondition {
        protected final String string;

        public StringCondition(String string) {
            this.string = string;
        }

        public StringCondition(String string, Object value) {
            super(value);
            this.string = string;
        }

        public StringCondition(String string, Object... values) {
            super(values);
            this.string = string;
        }

        @Override // org.greenrobot.greendao.query.WhereCondition
        public void appendTo(StringBuilder builder, String tableAlias) {
            builder.append(this.string);
        }
    }
}
