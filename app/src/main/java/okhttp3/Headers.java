package okhttp3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

/* loaded from: classes.dex */
public final class Headers {
    private final String[] namesAndValues;

    Headers(Builder builder) {
        this.namesAndValues = (String[]) builder.namesAndValues.toArray(new String[builder.namesAndValues.size()]);
    }

    private Headers(String[] namesAndValues) {
        this.namesAndValues = namesAndValues;
    }

    @Nullable
    public String get(String name) {
        return get(this.namesAndValues, name);
    }

    @Nullable
    public Date getDate(String name) {
        String value = get(name);
        if (value != null) {
            return HttpDate.parse(value);
        }
        return null;
    }

    public int size() {
        return this.namesAndValues.length / 2;
    }

    public String name(int index) {
        return this.namesAndValues[index * 2];
    }

    public String value(int index) {
        return this.namesAndValues[(index * 2) + 1];
    }

    public Set<String> names() {
        TreeSet<String> result = new TreeSet<>((Comparator<? super String>) String.CASE_INSENSITIVE_ORDER);
        int size = size();
        for (int i = 0; i < size; i++) {
            result.add(name(i));
        }
        return Collections.unmodifiableSet(result);
    }

    public List<String> values(String name) {
        List<String> result = null;
        int size = size();
        for (int i = 0; i < size; i++) {
            if (name.equalsIgnoreCase(name(i))) {
                if (result == null) {
                    result = new ArrayList<>(2);
                }
                result.add(value(i));
            }
        }
        if (result != null) {
            return Collections.unmodifiableList(result);
        }
        return Collections.emptyList();
    }

    public Builder newBuilder() {
        Builder result = new Builder();
        Collections.addAll(result.namesAndValues, this.namesAndValues);
        return result;
    }

    public boolean equals(@Nullable Object other) {
        return (other instanceof Headers) && Arrays.equals(((Headers) other).namesAndValues, this.namesAndValues);
    }

    public int hashCode() {
        return Arrays.hashCode(this.namesAndValues);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        int size = size();
        for (int i = 0; i < size; i++) {
            result.append(name(i)).append(": ").append(value(i)).append("\n");
        }
        return result.toString();
    }

    public Map<String, List<String>> toMultimap() {
        Map<String, List<String>> result = new TreeMap<>((Comparator<? super String>) String.CASE_INSENSITIVE_ORDER);
        int size = size();
        for (int i = 0; i < size; i++) {
            String name = name(i).toLowerCase(Locale.US);
            List<String> values = result.get(name);
            if (values == null) {
                values = new ArrayList<>(2);
                result.put(name, values);
            }
            values.add(value(i));
        }
        return result;
    }

    private static String get(String[] namesAndValues, String name) {
        for (int i = namesAndValues.length - 2; i >= 0; i -= 2) {
            if (name.equalsIgnoreCase(namesAndValues[i])) {
                return namesAndValues[i + 1];
            }
        }
        return null;
    }

    public static Headers of(String... namesAndValues) {
        if (namesAndValues == null) {
            throw new NullPointerException("namesAndValues == null");
        }
        if (namesAndValues.length % 2 != 0) {
            throw new IllegalArgumentException("Expected alternating header names and values");
        }
        String[] namesAndValues2 = (String[]) namesAndValues.clone();
        for (int i = 0; i < namesAndValues2.length; i++) {
            if (namesAndValues2[i] == null) {
                throw new IllegalArgumentException("Headers cannot be null");
            }
            namesAndValues2[i] = namesAndValues2[i].trim();
        }
        for (int i2 = 0; i2 < namesAndValues2.length; i2 += 2) {
            String name = namesAndValues2[i2];
            String value = namesAndValues2[i2 + 1];
            if (name.length() == 0 || name.indexOf(0) != -1 || value.indexOf(0) != -1) {
                throw new IllegalArgumentException("Unexpected header: " + name + ": " + value);
            }
        }
        return new Headers(namesAndValues2);
    }

    public static Headers of(Map<String, String> headers) {
        if (headers == null) {
            throw new NullPointerException("headers == null");
        }
        String[] namesAndValues = new String[headers.size() * 2];
        int i = 0;
        for (Map.Entry<String, String> header : headers.entrySet()) {
            if (header.getKey() == null || header.getValue() == null) {
                throw new IllegalArgumentException("Headers cannot be null");
            }
            String name = header.getKey().trim();
            String value = header.getValue().trim();
            if (name.length() == 0 || name.indexOf(0) != -1 || value.indexOf(0) != -1) {
                throw new IllegalArgumentException("Unexpected header: " + name + ": " + value);
            }
            namesAndValues[i] = name;
            namesAndValues[i + 1] = value;
            i += 2;
        }
        return new Headers(namesAndValues);
    }

    public static final class Builder {
        final List<String> namesAndValues = new ArrayList(20);

        Builder addLenient(String line) {
            int index = line.indexOf(":", 1);
            if (index != -1) {
                return addLenient(line.substring(0, index), line.substring(index + 1));
            }
            if (line.startsWith(":")) {
                return addLenient("", line.substring(1));
            }
            return addLenient("", line);
        }

        public Builder add(String line) {
            int index = line.indexOf(":");
            if (index == -1) {
                throw new IllegalArgumentException("Unexpected header: " + line);
            }
            return add(line.substring(0, index).trim(), line.substring(index + 1));
        }

        public Builder add(String name, String value) {
            checkNameAndValue(name, value);
            return addLenient(name, value);
        }

        Builder addLenient(String name, String value) {
            this.namesAndValues.add(name);
            this.namesAndValues.add(value.trim());
            return this;
        }

        public Builder removeAll(String name) {
            int i = 0;
            while (i < this.namesAndValues.size()) {
                if (name.equalsIgnoreCase(this.namesAndValues.get(i))) {
                    this.namesAndValues.remove(i);
                    this.namesAndValues.remove(i);
                    i -= 2;
                }
                i += 2;
            }
            return this;
        }

        public Builder set(String name, String value) {
            checkNameAndValue(name, value);
            removeAll(name);
            addLenient(name, value);
            return this;
        }

        private void checkNameAndValue(String name, String value) {
            if (name == null) {
                throw new NullPointerException("name == null");
            }
            if (name.isEmpty()) {
                throw new IllegalArgumentException("name is empty");
            }
            int length = name.length();
            for (int i = 0; i < length; i++) {
                char c = name.charAt(i);
                if (c <= ' ' || c >= 127) {
                    throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in header name: %s", Integer.valueOf(c), Integer.valueOf(i), name));
                }
            }
            if (value == null) {
                throw new NullPointerException("value for name " + name + " == null");
            }
            int length2 = value.length();
            for (int i2 = 0; i2 < length2; i2++) {
                char c2 = value.charAt(i2);
                if ((c2 <= 31 && c2 != '\t') || c2 >= 127) {
                    throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in %s value: %s", Integer.valueOf(c2), Integer.valueOf(i2), name, value));
                }
            }
        }

        public String get(String name) {
            for (int i = this.namesAndValues.size() - 2; i >= 0; i -= 2) {
                if (name.equalsIgnoreCase(this.namesAndValues.get(i))) {
                    return this.namesAndValues.get(i + 1);
                }
            }
            return null;
        }

        public Headers build() {
            return new Headers(this);
        }
    }
}
