package nluparser.a;

import java.lang.reflect.Type;
import java.util.Map;

/* loaded from: classes.dex */
abstract class a implements nluparser.a {

    /* renamed from: a, reason: collision with root package name */
    final Map<String, Type> f452a;

    a(Map<String, Type> map) {
        if (map == null) {
            throw new NullPointerException("serviceTypes");
        }
        this.f452a = map;
    }

    protected abstract Type a();

    @Override // nluparser.a
    public final Type a(String str) {
        Type typeA = a();
        if (typeA == null) {
            throw new NullPointerException("default type");
        }
        return a(str, typeA);
    }

    protected abstract Type a(String str, Type type);
}
