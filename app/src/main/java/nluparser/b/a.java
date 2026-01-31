package nluparser.b;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import nluparser.b;
import nluparser.scheme.NLU;

/* loaded from: classes.dex */
public class a implements b.a {

    /* renamed from: nluparser.b.a$a, reason: collision with other inner class name */
    static final class C0016a implements nluparser.b<String, NLU> {

        /* renamed from: a, reason: collision with root package name */
        private final nluparser.a f455a;
        private final Gson b = new Gson();

        public C0016a(nluparser.a aVar) {
            this.f455a = aVar;
        }

        @Override // nluparser.b
        public NLU a(String str) {
            Type typeA = this.f455a.a(str);
            if (typeA == null) {
                return null;
            }
            return (NLU) this.b.fromJson(str, typeA);
        }
    }

    private a() {
    }

    public static a a() {
        return new a();
    }

    @Override // nluparser.b.a
    public nluparser.b<String, NLU> a(nluparser.a aVar) {
        return new C0016a(aVar);
    }
}
