package com.baidu.location;

/* loaded from: classes.dex */
public final class a {

    /* renamed from: a, reason: collision with root package name */
    public final String f52a;
    public final String b;
    public final String c;
    public final String d;
    public final String e;
    public final String f;
    public final String g;
    public final String h;
    public final String i;
    public final String j;

    /* renamed from: com.baidu.location.a$a, reason: collision with other inner class name */
    public static class C0004a {

        /* renamed from: a, reason: collision with root package name */
        private String f53a = null;
        private String b = null;
        private String c = null;
        private String d = null;
        private String e = null;
        private String f = null;
        private String g = null;
        private String h = null;
        private String i = null;
        private String j = null;

        public C0004a a(String str) {
            this.f53a = str;
            return this;
        }

        public a a() {
            StringBuffer stringBuffer = new StringBuffer();
            if (this.f53a != null) {
                stringBuffer.append(this.f53a);
            }
            if (this.c != null) {
                stringBuffer.append(this.c);
            }
            if (this.c != null && this.d != null && !this.c.equals(this.d)) {
                stringBuffer.append(this.d);
            }
            if (this.f != null && (this.d == null || !this.d.equals(this.f))) {
                stringBuffer.append(this.f);
            }
            if (this.g != null) {
                stringBuffer.append(this.g);
            }
            if (this.h != null) {
                stringBuffer.append(this.h);
            }
            if (stringBuffer.length() > 0) {
                this.i = stringBuffer.toString();
            }
            return new a(this);
        }

        public C0004a b(String str) {
            this.j = str;
            return this;
        }

        public C0004a c(String str) {
            this.b = str;
            return this;
        }

        public C0004a d(String str) {
            this.c = str;
            return this;
        }

        public C0004a e(String str) {
            this.d = str;
            return this;
        }

        public C0004a f(String str) {
            this.e = str;
            return this;
        }

        public C0004a g(String str) {
            this.f = str;
            return this;
        }

        public C0004a h(String str) {
            this.g = str;
            return this;
        }

        public C0004a i(String str) {
            this.h = str;
            return this;
        }
    }

    private a(C0004a c0004a) {
        this.f52a = c0004a.f53a;
        this.b = c0004a.b;
        this.c = c0004a.c;
        this.d = c0004a.d;
        this.e = c0004a.e;
        this.f = c0004a.f;
        this.g = c0004a.g;
        this.h = c0004a.h;
        this.i = c0004a.i;
        this.j = c0004a.j;
    }
}
