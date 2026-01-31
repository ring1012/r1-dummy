package com.unisound.vui.data.entity.a.a;

/* loaded from: classes.dex */
public class e implements Comparable<e> {

    /* renamed from: a, reason: collision with root package name */
    private String f401a;
    private String b;
    private Boolean c;
    private String d;
    private Long e;

    public e() {
    }

    public e(String str, String str2, Boolean bool, String str3, Long l) {
        this.f401a = str;
        this.b = str2;
        this.c = bool;
        this.d = str3;
        this.e = l;
    }

    @Override // java.lang.Comparable
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compareTo(e eVar) {
        long jLongValue = e().longValue();
        long jLongValue2 = eVar.e().longValue();
        if (jLongValue > jLongValue2) {
            return -1;
        }
        return jLongValue == jLongValue2 ? 0 : 1;
    }

    public String a() {
        return this.f401a;
    }

    public void a(Boolean bool) {
        this.c = bool;
    }

    public void a(Long l) {
        this.e = l;
    }

    public void a(String str) {
        this.f401a = str;
    }

    public String b() {
        return this.b;
    }

    public void b(String str) {
        this.b = str;
    }

    public Boolean c() {
        return this.c;
    }

    public void c(String str) {
        this.d = str;
    }

    public String d() {
        return this.d;
    }

    public Long e() {
        return this.e;
    }
}
