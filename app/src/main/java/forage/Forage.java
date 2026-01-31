package forage;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public final class Forage {
    private final Map<Key, String> MAP = new HashMap(8);
    private final Class<?> clazz;
    private final FieldFilter fieldFilter;

    static class Key {
        final Object key;

        Key(Object key) {
            this.key = key;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Key key = (Key) o;
            if (this.key != null) {
                if (this.key.equals(key.key)) {
                    return true;
                }
            } else if (key.key == null) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            if (this.key != null) {
                return this.key.hashCode();
            }
            return 0;
        }
    }

    private Forage(Class<?> clazz, FieldFilter fieldFilter) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        this.clazz = clazz;
        if (fieldFilter == null) {
            this.fieldFilter = FieldFilter.DEFAULT;
        } else {
            this.fieldFilter = fieldFilter;
        }
    }

    public static Forage getInstance(Class<?> clazz, FieldFilter fieldFilter) {
        return new Forage(clazz, fieldFilter);
    }

    public void clean() {
        this.MAP.clear();
    }

    public String queryFieldName(Object value) throws SecurityException {
        if (this.MAP.containsKey(new Key(value))) {
            return this.MAP.get(new Key(value));
        }
        String string = value.toString();
        for (Field field : this.clazz.getFields()) {
            if (this.fieldFilter.accept(field.getName(), field.getModifiers(), field.getType())) {
                try {
                    if (field.get(null).equals(value)) {
                        string = field.getName();
                        this.MAP.put(new Key(value), string);
                        return string;
                    }
                } catch (IllegalAccessException e) {
                    return string;
                }
            }
        }
        return string;
    }
}
