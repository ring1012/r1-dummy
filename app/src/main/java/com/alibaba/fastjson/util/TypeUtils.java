package com.alibaba.fastjson.util;

import cn.yunzhisheng.asr.JniUscClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tencent.bugly.Bugly;
import com.unisound.vui.handler.session.memo.utils.MemoConstants;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessControlException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* loaded from: classes.dex */
public class TypeUtils {
    public static boolean compatibleWithJavaBean = false;
    private static boolean setAccessibleEnable = true;
    private static ConcurrentMap<String, Class<?>> mappings = new ConcurrentHashMap();

    static {
        addBaseClassMappings();
    }

    public static final String castToString(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public static final Byte castToByte(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return Byte.valueOf(((Number) value).byteValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || JniUscClient.az.equals(strVal)) {
                return null;
            }
            return Byte.valueOf(Byte.parseByte(strVal));
        }
        throw new JSONException("can not cast to byte, value : " + value);
    }

    public static final Character castToChar(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Character) {
            return (Character) value;
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }
            if (strVal.length() != 1) {
                throw new JSONException("can not cast to byte, value : " + value);
            }
            return Character.valueOf(strVal.charAt(0));
        }
        throw new JSONException("can not cast to byte, value : " + value);
    }

    public static final Short castToShort(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return Short.valueOf(((Number) value).shortValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || JniUscClient.az.equals(strVal)) {
                return null;
            }
            return Short.valueOf(Short.parseShort(strVal));
        }
        throw new JSONException("can not cast to short, value : " + value);
    }

    public static final BigDecimal castToBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        }
        String strVal = value.toString();
        if (strVal.length() == 0) {
            return null;
        }
        return new BigDecimal(strVal);
    }

    public static final BigInteger castToBigInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        if ((value instanceof Float) || (value instanceof Double)) {
            return BigInteger.valueOf(((Number) value).longValue());
        }
        String strVal = value.toString();
        if (strVal.length() == 0) {
            return null;
        }
        return new BigInteger(strVal);
    }

    public static final Float castToFloat(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return Float.valueOf(((Number) value).floatValue());
        }
        if (value instanceof String) {
            String strVal = value.toString();
            if (strVal.length() == 0 || JniUscClient.az.equals(strVal)) {
                return null;
            }
            return Float.valueOf(Float.parseFloat(strVal));
        }
        throw new JSONException("can not cast to float, value : " + value);
    }

    public static final Double castToDouble(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return Double.valueOf(((Number) value).doubleValue());
        }
        if (value instanceof String) {
            String strVal = value.toString();
            if (strVal.length() == 0 || JniUscClient.az.equals(strVal)) {
                return null;
            }
            return Double.valueOf(Double.parseDouble(strVal));
        }
        throw new JSONException("can not cast to double, value : " + value);
    }

    public static final Date castToDate(Object value) throws NumberFormatException {
        String format;
        if (value == null) {
            return null;
        }
        if (value instanceof Calendar) {
            return ((Calendar) value).getTime();
        }
        if (value instanceof Date) {
            return (Date) value;
        }
        long longValue = -1;
        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.indexOf(45) != -1) {
                if (strVal.length() == JSON.DEFFAULT_DATE_FORMAT.length()) {
                    format = JSON.DEFFAULT_DATE_FORMAT;
                } else if (strVal.length() == 10) {
                    format = MemoConstants.DATE_FORMATE_YMD;
                } else if (strVal.length() == MemoConstants.DATE_FORMATE_YMDHMS.length()) {
                    format = MemoConstants.DATE_FORMATE_YMDHMS;
                } else {
                    format = "yyyy-MM-dd HH:mm:ss.SSS";
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                try {
                    return dateFormat.parse(strVal);
                } catch (ParseException e) {
                    throw new JSONException("can not cast to Date, value : " + strVal);
                }
            }
            if (strVal.length() == 0) {
                return null;
            }
            longValue = Long.parseLong(strVal);
        }
        if (longValue < 0) {
            throw new JSONException("can not cast to Date, value : " + value);
        }
        return new Date(longValue);
    }

    public static final java.sql.Date castToSqlDate(Object value) throws NumberFormatException {
        if (value == null) {
            return null;
        }
        if (value instanceof Calendar) {
            return new java.sql.Date(((Calendar) value).getTimeInMillis());
        }
        if (value instanceof java.sql.Date) {
            return (java.sql.Date) value;
        }
        if (value instanceof Date) {
            return new java.sql.Date(((Date) value).getTime());
        }
        long longValue = 0;
        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }
            longValue = Long.parseLong(strVal);
        }
        if (longValue <= 0) {
            throw new JSONException("can not cast to Date, value : " + value);
        }
        return new java.sql.Date(longValue);
    }

    public static final Timestamp castToTimestamp(Object value) throws NumberFormatException {
        if (value == null) {
            return null;
        }
        if (value instanceof Calendar) {
            return new Timestamp(((Calendar) value).getTimeInMillis());
        }
        if (value instanceof Timestamp) {
            return (Timestamp) value;
        }
        if (value instanceof Date) {
            return new Timestamp(((Date) value).getTime());
        }
        long longValue = 0;
        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }
            longValue = Long.parseLong(strVal);
        }
        if (longValue <= 0) {
            throw new JSONException("can not cast to Date, value : " + value);
        }
        return new Timestamp(longValue);
    }

    public static final Long castToLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return Long.valueOf(((Number) value).longValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || JniUscClient.az.equals(strVal)) {
                return null;
            }
            try {
                return Long.valueOf(Long.parseLong(strVal));
            } catch (NumberFormatException e) {
                JSONScanner dateParser = new JSONScanner(strVal);
                Calendar calendar = null;
                if (dateParser.scanISO8601DateIfMatch(false)) {
                    calendar = dateParser.getCalendar();
                }
                dateParser.close();
                if (calendar != null) {
                    return Long.valueOf(calendar.getTimeInMillis());
                }
            }
        }
        throw new JSONException("can not cast to long, value : " + value);
    }

    public static final Integer castToInt(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return Integer.valueOf(((Number) value).intValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() != 0 && !JniUscClient.az.equals(strVal)) {
                return Integer.valueOf(Integer.parseInt(strVal));
            }
            return null;
        }
        throw new JSONException("can not cast to int, value : " + value);
    }

    public static final byte[] castToBytes(Object value) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }
        if (value instanceof String) {
            return Base64.decodeFast((String) value);
        }
        throw new JSONException("can not cast to int, value : " + value);
    }

    public static final Boolean castToBoolean(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            return Boolean.valueOf(((Number) value).intValue() == 1);
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }
            if ("true".equalsIgnoreCase(strVal)) {
                return Boolean.TRUE;
            }
            if (Bugly.SDK_IS_DEV.equalsIgnoreCase(strVal)) {
                return Boolean.FALSE;
            }
            if ("1".equals(strVal)) {
                return Boolean.TRUE;
            }
            if ("0".equals(strVal)) {
                return Boolean.FALSE;
            }
            if (JniUscClient.az.equals(strVal)) {
                return null;
            }
        }
        throw new JSONException("can not cast to int, value : " + value);
    }

    public static final <T> T castToJavaBean(Object obj, Class<T> cls) {
        return (T) cast(obj, (Class) cls, ParserConfig.getGlobalInstance());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> T cast(Object obj, Class<T> cls, ParserConfig parserConfig) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        Calendar calendar;
        if (obj == 0) {
            return null;
        }
        if (cls == null) {
            throw new IllegalArgumentException("clazz is null");
        }
        if (cls != obj.getClass()) {
            if (obj instanceof Map) {
                if (cls != Map.class) {
                    Map map = (Map) obj;
                    if (cls != Object.class || map.containsKey(JSON.DEFAULT_TYPE_KEY)) {
                        return (T) castToJavaBean((Map) obj, cls, parserConfig);
                    }
                    return obj;
                }
                return obj;
            }
            if (cls.isArray()) {
                if (obj instanceof Collection) {
                    Collection collection = (Collection) obj;
                    int i = 0;
                    T t = (T) Array.newInstance(cls.getComponentType(), collection.size());
                    Iterator it = collection.iterator();
                    while (it.hasNext()) {
                        Array.set(t, i, cast(it.next(), (Class) cls.getComponentType(), parserConfig));
                        i++;
                    }
                    return t;
                }
                if (cls == byte[].class) {
                    return (T) castToBytes(obj);
                }
            }
            if (!cls.isAssignableFrom(obj.getClass())) {
                if (cls == Boolean.TYPE || cls == Boolean.class) {
                    return (T) castToBoolean(obj);
                }
                if (cls == Byte.TYPE || cls == Byte.class) {
                    return (T) castToByte(obj);
                }
                if (cls == Short.TYPE || cls == Short.class) {
                    return (T) castToShort(obj);
                }
                if (cls == Integer.TYPE || cls == Integer.class) {
                    return (T) castToInt(obj);
                }
                if (cls == Long.TYPE || cls == Long.class) {
                    return (T) castToLong(obj);
                }
                if (cls == Float.TYPE || cls == Float.class) {
                    return (T) castToFloat(obj);
                }
                if (cls == Double.TYPE || cls == Double.class) {
                    return (T) castToDouble(obj);
                }
                if (cls == String.class) {
                    return (T) castToString(obj);
                }
                if (cls == BigDecimal.class) {
                    return (T) castToBigDecimal(obj);
                }
                if (cls == BigInteger.class) {
                    return (T) castToBigInteger(obj);
                }
                if (cls == Date.class) {
                    return (T) castToDate(obj);
                }
                if (cls == java.sql.Date.class) {
                    return (T) castToSqlDate(obj);
                }
                if (cls == Timestamp.class) {
                    return (T) castToTimestamp(obj);
                }
                if (cls.isEnum()) {
                    return (T) castToEnum(obj, cls, parserConfig);
                }
                if (Calendar.class.isAssignableFrom(cls)) {
                    Date dateCastToDate = castToDate(obj);
                    if (cls == Calendar.class) {
                        calendar = Calendar.getInstance();
                    } else {
                        try {
                            calendar = (Calendar) cls.newInstance();
                        } catch (Exception e) {
                            throw new JSONException("can not cast to : " + cls.getName(), e);
                        }
                    }
                    calendar.setTime(dateCastToDate);
                    return (T) calendar;
                }
                if ((obj instanceof String) && ((String) obj).length() == 0) {
                    return null;
                }
                throw new JSONException("can not cast to : " + cls.getName());
            }
            return obj;
        }
        return obj;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [T, java.lang.Enum] */
    public static final <T> T castToEnum(Object obj, Class<T> cls, ParserConfig parserConfig) {
        try {
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.length() == 0) {
                    return null;
                }
                return (T) Enum.valueOf(cls, str);
            }
            if (obj instanceof Number) {
                int iIntValue = ((Number) obj).intValue();
                for (Object obj2 : (Object[]) cls.getMethod("values", new Class[0]).invoke(null, new Object[0])) {
                    ?? r0 = (T) ((Enum) obj2);
                    if (r0.ordinal() == iIntValue) {
                        return r0;
                    }
                }
            }
            throw new JSONException("can not cast to : " + cls.getName());
        } catch (Exception e) {
            throw new JSONException("can not cast to : " + cls.getName(), e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> T cast(Object obj, Type type, ParserConfig parserConfig) {
        if (obj == 0) {
            return null;
        }
        if (type instanceof Class) {
            return (T) cast(obj, (Class) type, parserConfig);
        }
        if (type instanceof ParameterizedType) {
            return (T) cast(obj, (ParameterizedType) type, parserConfig);
        }
        if ((obj instanceof String) && ((String) obj).length() == 0) {
            return null;
        }
        if (type instanceof TypeVariable) {
            return obj;
        }
        throw new JSONException("can not cast to : " + type);
    }

    /* JADX WARN: Type inference failed for: r11v0, types: [T, java.util.HashMap, java.util.Map] */
    public static final <T> T cast(Object obj, ParameterizedType parameterizedType, ParserConfig parserConfig) {
        T t;
        Type rawType = parameterizedType.getRawType();
        if (rawType == Set.class || rawType == HashSet.class || rawType == TreeSet.class || rawType == List.class || rawType == ArrayList.class) {
            Type type = parameterizedType.getActualTypeArguments()[0];
            if (obj instanceof Iterable) {
                if (rawType == Set.class || rawType == HashSet.class) {
                    t = (T) new HashSet();
                } else if (rawType == TreeSet.class) {
                    t = (T) new TreeSet();
                } else {
                    t = (T) new ArrayList();
                }
                Iterator<T> it = ((Iterable) obj).iterator();
                while (it.hasNext()) {
                    ((Collection) t).add(cast(it.next(), type, parserConfig));
                }
                return t;
            }
        }
        if (rawType == Map.class || rawType == HashMap.class) {
            Type type2 = parameterizedType.getActualTypeArguments()[0];
            Type type3 = parameterizedType.getActualTypeArguments()[1];
            if (obj instanceof Map) {
                ?? r11 = (T) new HashMap();
                for (Map.Entry entry : ((Map) obj).entrySet()) {
                    r11.put(cast(entry.getKey(), type2, parserConfig), cast(entry.getValue(), type3, parserConfig));
                }
                return r11;
            }
        }
        if ((obj instanceof String) && ((String) obj).length() == 0) {
            return null;
        }
        if (parameterizedType.getActualTypeArguments().length == 1 && (parameterizedType.getActualTypeArguments()[0] instanceof WildcardType)) {
            return (T) cast(obj, rawType, parserConfig);
        }
        throw new JSONException("can not cast to : " + parameterizedType);
    }

    public static final <T> T castToJavaBean(Map<String, Object> map, Class<T> cls, ParserConfig parserConfig) throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, SecurityException, IllegalArgumentException, InvocationTargetException {
        JSONObject jSONObject;
        int iIntValue;
        try {
            if (cls == StackTraceElement.class) {
                String str = (String) map.get("className");
                String str2 = (String) map.get("methodName");
                String str3 = (String) map.get("fileName");
                Number number = (Number) map.get("lineNumber");
                if (number == null) {
                    iIntValue = 0;
                } else {
                    iIntValue = number.intValue();
                }
                return (T) new StackTraceElement(str, str2, str3, iIntValue);
            }
            Object obj = map.get(JSON.DEFAULT_TYPE_KEY);
            if (obj instanceof String) {
                String str4 = (String) obj;
                Class<?> clsLoadClass = loadClass(str4);
                if (clsLoadClass == null) {
                    throw new ClassNotFoundException(str4 + " not found");
                }
                if (!clsLoadClass.equals(cls)) {
                    return (T) castToJavaBean(map, clsLoadClass, parserConfig);
                }
            }
            if (cls.isInterface()) {
                if (map instanceof JSONObject) {
                    jSONObject = (JSONObject) map;
                } else {
                    jSONObject = new JSONObject(map);
                }
                return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{cls}, jSONObject);
            }
            if (parserConfig == null) {
                parserConfig = ParserConfig.getGlobalInstance();
            }
            Map<String, FieldDeserializer> fieldDeserializers = parserConfig.getFieldDeserializers(cls);
            Constructor<T> declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
            if (!declaredConstructor.isAccessible()) {
                declaredConstructor.setAccessible(true);
            }
            T tNewInstance = declaredConstructor.newInstance(new Object[0]);
            for (Map.Entry<String, FieldDeserializer> entry : fieldDeserializers.entrySet()) {
                String key = entry.getKey();
                FieldDeserializer value = entry.getValue();
                if (map.containsKey(key)) {
                    Object obj2 = map.get(key);
                    Method method = value.getMethod();
                    if (method != null) {
                        method.invoke(tNewInstance, cast(obj2, method.getGenericParameterTypes()[0], parserConfig));
                    } else {
                        Field field = value.getField();
                        field.set(tNewInstance, cast(obj2, field.getGenericType(), parserConfig));
                    }
                }
            }
            return tNewInstance;
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public static void addClassMapping(String className, Class<?> clazz) {
        if (className == null) {
            className = clazz.getName();
        }
        mappings.put(className, clazz);
    }

    public static void addBaseClassMappings() {
        mappings.put("byte", Byte.TYPE);
        mappings.put("short", Short.TYPE);
        mappings.put("int", Integer.TYPE);
        mappings.put("long", Long.TYPE);
        mappings.put("float", Float.TYPE);
        mappings.put("double", Double.TYPE);
        mappings.put("boolean", Boolean.TYPE);
        mappings.put("char", Character.TYPE);
        mappings.put("[byte", byte[].class);
        mappings.put("[short", short[].class);
        mappings.put("[int", int[].class);
        mappings.put("[long", long[].class);
        mappings.put("[float", float[].class);
        mappings.put("[double", double[].class);
        mappings.put("[boolean", boolean[].class);
        mappings.put("[char", char[].class);
        mappings.put(HashMap.class.getName(), HashMap.class);
    }

    public static void clearClassMapping() {
        mappings.clear();
        addBaseClassMappings();
    }

    public static Class<?> loadClass(String className) {
        if (className == null || className.length() == 0) {
            return null;
        }
        Class<?> clazz = mappings.get(className);
        if (clazz == null) {
            if (className.charAt(0) == '[') {
                Class<?> componentType = loadClass(className.substring(1));
                return Array.newInstance(componentType, 0).getClass();
            }
            if (className.startsWith("L") && className.endsWith(";")) {
                String newClassName = className.substring(1, className.length() - 1);
                return loadClass(newClassName);
            }
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                if (classLoader != null) {
                    clazz = classLoader.loadClass(className);
                    addClassMapping(className, clazz);
                    return clazz;
                }
            } catch (Throwable th) {
            }
            try {
                clazz = Class.forName(className);
                addClassMapping(className, clazz);
                return clazz;
            } catch (Throwable th2) {
                return clazz;
            }
        }
        return clazz;
    }

    public static List<FieldInfo> computeGetters(Class<?> clazz, Map<String, String> aliasMap) {
        return computeGetters(clazz, aliasMap, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00ab A[PHI: r6 r7
  0x00ab: PHI (r6v4 'ordinal' int) = (r6v3 'ordinal' int), (r6v10 'ordinal' int) binds: [B:21:0x0070, B:25:0x008c] A[DONT_GENERATE, DONT_INLINE]
  0x00ab: PHI (r7v4 'serialzeFeatures' int) = (r7v3 'serialzeFeatures' int), (r7v10 'serialzeFeatures' int) binds: [B:21:0x0070, B:25:0x008c] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0128 A[PHI: r3 r6 r7
  0x0128: PHI (r3v22 'propertyName' java.lang.String) = 
  (r3v21 'propertyName' java.lang.String)
  (r3v21 'propertyName' java.lang.String)
  (r3v21 'propertyName' java.lang.String)
  (r3v26 'propertyName' java.lang.String)
  (r3v28 'propertyName' java.lang.String)
 binds: [B:45:0x00f0, B:47:0x00fa, B:51:0x0116, B:53:0x011c, B:55:0x0126] A[DONT_GENERATE, DONT_INLINE]
  0x0128: PHI (r6v8 'ordinal' int) = (r6v4 'ordinal' int), (r6v4 'ordinal' int), (r6v9 'ordinal' int), (r6v9 'ordinal' int), (r6v9 'ordinal' int) binds: [B:45:0x00f0, B:47:0x00fa, B:51:0x0116, B:53:0x011c, B:55:0x0126] A[DONT_GENERATE, DONT_INLINE]
  0x0128: PHI (r7v8 'serialzeFeatures' int) = 
  (r7v4 'serialzeFeatures' int)
  (r7v4 'serialzeFeatures' int)
  (r7v9 'serialzeFeatures' int)
  (r7v9 'serialzeFeatures' int)
  (r7v9 'serialzeFeatures' int)
 binds: [B:45:0x00f0, B:47:0x00fa, B:51:0x0116, B:53:0x011c, B:55:0x0126] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01b3 A[PHI: r3 r6 r7
  0x01b3: PHI (r3v9 'propertyName' java.lang.String) = 
  (r3v8 'propertyName' java.lang.String)
  (r3v8 'propertyName' java.lang.String)
  (r3v8 'propertyName' java.lang.String)
  (r3v13 'propertyName' java.lang.String)
  (r3v15 'propertyName' java.lang.String)
 binds: [B:72:0x017b, B:74:0x0185, B:78:0x01a1, B:80:0x01a7, B:82:0x01b1] A[DONT_GENERATE, DONT_INLINE]
  0x01b3: PHI (r6v6 'ordinal' int) = (r6v5 'ordinal' int), (r6v5 'ordinal' int), (r6v7 'ordinal' int), (r6v7 'ordinal' int), (r6v7 'ordinal' int) binds: [B:72:0x017b, B:74:0x0185, B:78:0x01a1, B:80:0x01a7, B:82:0x01b1] A[DONT_GENERATE, DONT_INLINE]
  0x01b3: PHI (r7v6 'serialzeFeatures' int) = 
  (r7v5 'serialzeFeatures' int)
  (r7v5 'serialzeFeatures' int)
  (r7v7 'serialzeFeatures' int)
  (r7v7 'serialzeFeatures' int)
  (r7v7 'serialzeFeatures' int)
 binds: [B:72:0x017b, B:74:0x0185, B:78:0x01a1, B:80:0x01a7, B:82:0x01b1] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.List<com.alibaba.fastjson.util.FieldInfo> computeGetters(java.lang.Class<?> r29, java.util.Map<java.lang.String, java.lang.String> r30, boolean r31) throws java.lang.SecurityException {
        /*
            Method dump skipped, instructions count: 882
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.TypeUtils.computeGetters(java.lang.Class, java.util.Map, boolean):java.util.List");
    }

    public static JSONField getSupperMethodAnnotation(Class<?> clazz, Method method) throws SecurityException {
        JSONField annotation;
        Class<?>[] arr$ = clazz.getInterfaces();
        for (Class<?> interfaceClass : arr$) {
            Method[] arr$2 = interfaceClass.getMethods();
            for (Method interfaceMethod : arr$2) {
                if (interfaceMethod.getName().equals(method.getName()) && interfaceMethod.getParameterTypes().length == method.getParameterTypes().length) {
                    boolean match = true;
                    int i = 0;
                    while (true) {
                        if (i >= interfaceMethod.getParameterTypes().length) {
                            break;
                        }
                        if (interfaceMethod.getParameterTypes()[i].equals(method.getParameterTypes()[i])) {
                            i++;
                        } else {
                            match = false;
                            break;
                        }
                    }
                    if (match && (annotation = (JSONField) interfaceMethod.getAnnotation(JSONField.class)) != null) {
                        return annotation;
                    }
                }
            }
        }
        return null;
    }

    private static boolean isJSONTypeIgnore(Class<?> clazz, String propertyName) {
        JSONType jsonType = (JSONType) clazz.getAnnotation(JSONType.class);
        if (jsonType != null && jsonType.ignores() != null) {
            String[] arr$ = jsonType.ignores();
            for (String item : arr$) {
                if (propertyName.equalsIgnoreCase(item)) {
                    return true;
                }
            }
        }
        return (clazz.getSuperclass() == Object.class || clazz.getSuperclass() == null || !isJSONTypeIgnore(clazz.getSuperclass(), propertyName)) ? false : true;
    }

    public static boolean isGenericParamType(Type type) {
        if (type instanceof ParameterizedType) {
            return true;
        }
        if (type instanceof Class) {
            return isGenericParamType(((Class) type).getGenericSuperclass());
        }
        return false;
    }

    public static Type getGenericParamType(Type type) {
        if (!(type instanceof ParameterizedType) && (type instanceof Class)) {
            return getGenericParamType(((Class) type).getGenericSuperclass());
        }
        return type;
    }

    public static Type unwrap(Type type) {
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            if (componentType == Byte.TYPE) {
                return byte[].class;
            }
            if (componentType == Character.TYPE) {
                return char[].class;
            }
            return type;
        }
        return type;
    }

    public static Class<?> getClass(Type type) {
        if (type.getClass() == Class.class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        }
        return Object.class;
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        Field[] arr$ = clazz.getDeclaredFields();
        for (Field field : arr$) {
            if (fieldName.equals(field.getName())) {
                return field;
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            return getField(superClass, fieldName);
        }
        return null;
    }

    public static int getSerializeFeatures(Class<?> clazz) {
        JSONType annotation = (JSONType) clazz.getAnnotation(JSONType.class);
        if (annotation == null) {
            return 0;
        }
        return SerializerFeature.of(annotation.serialzeFeatures());
    }

    public static int getParserFeatures(Class<?> clazz) {
        JSONType annotation = (JSONType) clazz.getAnnotation(JSONType.class);
        if (annotation == null) {
            return 0;
        }
        return Feature.of(annotation.parseFeatures());
    }

    public static String decapitalize(String name) {
        if (name != null && name.length() != 0) {
            if (name.length() <= 1 || !Character.isUpperCase(name.charAt(1)) || !Character.isUpperCase(name.charAt(0))) {
                char[] chars = name.toCharArray();
                chars[0] = Character.toLowerCase(chars[0]);
                return new String(chars);
            }
            return name;
        }
        return name;
    }

    static void setAccessible(AccessibleObject obj) {
        if (setAccessibleEnable && !obj.isAccessible()) {
            try {
                obj.setAccessible(true);
            } catch (AccessControlException e) {
                setAccessibleEnable = false;
            }
        }
    }
}
