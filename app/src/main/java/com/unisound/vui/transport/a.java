package com.unisound.vui.transport;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
interface a {

    /* renamed from: com.unisound.vui.transport.a$a, reason: collision with other inner class name */
    public static final class C0012a implements a {

        /* renamed from: a, reason: collision with root package name */
        static final Pattern f428a = Pattern.compile("\\W\"type\"\\s*:\\s*(-?\\d+)", 2);
        final c b;

        public C0012a(c cVar) {
            if (cVar == null) {
                throw new NullPointerException("typeMapping");
            }
            this.b = cVar;
        }

        @Override // com.unisound.vui.transport.a
        public Type a(String str) {
            Matcher matcher = f428a.matcher(str);
            if (!matcher.find()) {
                throw new IllegalArgumentException(str);
            }
            return this.b.a(Integer.valueOf(Integer.parseInt(matcher.group(1))));
        }
    }

    Type a(String str);
}
