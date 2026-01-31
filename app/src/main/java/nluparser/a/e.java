package nluparser.a;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nluparser.a;

/* loaded from: classes.dex */
public final class e implements a.InterfaceC0015a {

    static class a extends nluparser.a.a {
        static final Pattern b = Pattern.compile("\\W\"service\"\\s*:\\s*\"([^\",]+)\"", 2);
        static final Pattern c = Pattern.compile("\\W\"code\"\\s*:\\s*\"([^\",]+)\"", 2);

        a(Map<String, Type> map) {
            super(map);
        }

        @Override // nluparser.a.a
        protected Type a() {
            return d.f454a;
        }

        @Override // nluparser.a.a
        protected Type a(String str, Type type) {
            Matcher matcher = b.matcher(str);
            if (!matcher.find()) {
                return type;
            }
            return this.f452a.get(matcher.group(1));
        }
    }

    private e() {
    }

    public static e a() {
        return new e();
    }

    @Override // nluparser.a.InterfaceC0015a
    public nluparser.a a(Map<String, Type> map) {
        return new a(map);
    }
}
