package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* loaded from: classes.dex */
public class MapDeserializer implements ObjectDeserializer {
    public static final MapDeserializer instance = new MapDeserializer();

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        JSONLexer lexer = defaultJSONParser.getLexer();
        if (lexer.token() == 8) {
            lexer.nextToken(16);
            return null;
        }
        Map<Object, Object> mapCreateMap = createMap(type);
        ParseContext context = defaultJSONParser.getContext();
        try {
            defaultJSONParser.setContext(context, mapCreateMap, obj);
            return (T) deserialze(defaultJSONParser, type, obj, mapCreateMap);
        } finally {
            defaultJSONParser.setContext(context);
        }
    }

    protected Object deserialze(DefaultJSONParser parser, Type type, Object fieldName, Map map) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type keyType = parameterizedType.getActualTypeArguments()[0];
            Type valueType = parameterizedType.getActualTypeArguments()[1];
            if (String.class == keyType) {
                return parseMap(parser, map, valueType, fieldName);
            }
            return parseMap(parser, map, keyType, valueType, fieldName);
        }
        return parser.parseObject(map, fieldName);
    }

    /* JADX WARN: Code restructure failed: missing block: B:93:?, code lost:
    
        return r13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.Map parseMap(com.alibaba.fastjson.parser.DefaultJSONParser r12, java.util.Map<java.lang.String, java.lang.Object> r13, java.lang.reflect.Type r14, java.lang.Object r15) {
        /*
            Method dump skipped, instructions count: 453
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.MapDeserializer.parseMap(com.alibaba.fastjson.parser.DefaultJSONParser, java.util.Map, java.lang.reflect.Type, java.lang.Object):java.util.Map");
    }

    public static Object parseMap(DefaultJSONParser parser, Map<Object, Object> map, Type keyType, Type valueType, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() != 12 && lexer.token() != 16) {
            throw new JSONException("syntax error, expect {, actual " + lexer.tokenName());
        }
        ObjectDeserializer keyDeserializer = parser.getConfig().getDeserializer(keyType);
        ObjectDeserializer valueDeserializer = parser.getConfig().getDeserializer(valueType);
        lexer.nextToken(keyDeserializer.getFastMatchToken());
        ParseContext context = parser.getContext();
        while (lexer.token() != 13) {
            try {
                if (lexer.token() == 4 && lexer.isRef()) {
                    Object object = null;
                    lexer.nextTokenWithColon(4);
                    if (lexer.token() != 4) {
                        throw new JSONException("illegal ref, " + JSONToken.name(lexer.token()));
                    }
                    String ref = lexer.stringVal();
                    if ("..".equals(ref)) {
                        ParseContext parentContext = context.getParentContext();
                        object = parentContext.getObject();
                    } else if ("$".equals(ref)) {
                        ParseContext rootContext = context;
                        while (rootContext.getParentContext() != null) {
                            rootContext = rootContext.getParentContext();
                        }
                        object = rootContext.getObject();
                    } else {
                        parser.addResolveTask(new DefaultJSONParser.ResolveTask(context, ref));
                        parser.setResolveStatus(1);
                    }
                    lexer.nextToken(13);
                    if (lexer.token() != 13) {
                        throw new JSONException("illegal ref");
                    }
                    lexer.nextToken(16);
                    parser.setContext(context);
                    return object;
                }
                if (map.size() == 0 && lexer.token() == 4 && JSON.DEFAULT_TYPE_KEY.equals(lexer.stringVal())) {
                    lexer.nextTokenWithColon(4);
                    lexer.nextToken(16);
                    if (lexer.token() == 13) {
                        lexer.nextToken();
                        return map;
                    }
                    lexer.nextToken(keyDeserializer.getFastMatchToken());
                }
                Object key = keyDeserializer.deserialze(parser, keyType, null);
                if (lexer.token() != 17) {
                    throw new JSONException("syntax error, expect :, actual " + lexer.token());
                }
                lexer.nextToken(valueDeserializer.getFastMatchToken());
                Object value = valueDeserializer.deserialze(parser, valueType, key);
                map.put(key, value);
                if (lexer.token() == 16) {
                    lexer.nextToken(keyDeserializer.getFastMatchToken());
                }
            } finally {
                parser.setContext(context);
            }
        }
        lexer.nextToken(16);
        return map;
    }

    protected Map<Object, Object> createMap(Type type) {
        if (type == Properties.class) {
            return new Properties();
        }
        if (type == Hashtable.class) {
            return new Hashtable();
        }
        if (type == IdentityHashMap.class) {
            return new IdentityHashMap();
        }
        if (type == SortedMap.class || type == TreeMap.class) {
            return new TreeMap();
        }
        if (type == ConcurrentMap.class || type == ConcurrentHashMap.class) {
            return new ConcurrentHashMap();
        }
        if (type == Map.class || type == HashMap.class) {
            return new HashMap();
        }
        if (type == LinkedHashMap.class) {
            return new LinkedHashMap();
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return createMap(parameterizedType.getRawType());
        }
        Class<?> clazz = (Class) type;
        if (clazz.isInterface()) {
            throw new JSONException("unsupport type " + type);
        }
        try {
            return (Map) clazz.newInstance();
        } catch (Exception e) {
            throw new JSONException("unsupport type " + type, e);
        }
    }

    @Override // com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 12;
    }
}
