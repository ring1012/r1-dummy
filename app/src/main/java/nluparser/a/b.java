package nluparser.a;

import java.lang.reflect.Type;
import java.util.Map;
import nluparser.a;
import nluparser.a.e;

/* loaded from: classes.dex */
public final class b implements a.InterfaceC0015a {

    static final class a extends e.a {
        a(Map<String, Type> map) {
            super(map);
        }

        @Override // nluparser.a.e.a, nluparser.a.a
        protected Type a() {
            return c.f453a;
        }
    }

    private b() {
    }

    public static b a() {
        return new b();
    }

    @Override // nluparser.a.InterfaceC0015a
    public nluparser.a a(Map<String, Type> map) {
        return new a(map);
    }
}
