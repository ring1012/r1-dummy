package nluparser.b;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import nluparser.b;
import nluparser.scheme.Mixture;

/* loaded from: classes.dex */
public class b implements b.a {

    static final class a implements nluparser.b<String, Mixture> {

        /* renamed from: a, reason: collision with root package name */
        private final nluparser.a f456a;
        private final Gson b = new Gson();

        public a(nluparser.a aVar) {
            this.f456a = aVar;
        }

        @Override // nluparser.b
        public Mixture a(String str) {
            Type typeA = this.f456a.a(str);
            if (typeA == null) {
                return null;
            }
            return (Mixture) this.b.fromJson(str, typeA);
        }
    }

    private b() {
    }

    public static b a() {
        return new b();
    }

    @Override // nluparser.b.a
    public nluparser.b<String, Mixture> a(nluparser.a aVar) {
        return new a(aVar);
    }
}
