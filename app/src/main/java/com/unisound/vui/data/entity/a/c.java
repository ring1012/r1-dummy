package com.unisound.vui.data.entity.a;

/* loaded from: classes.dex */
public class c implements Comparable<c> {

    /* renamed from: a, reason: collision with root package name */
    private Long f405a;
    private String b;
    private Long c;
    private Integer d;

    public c() {
    }

    public c(Long l, String str, Long l2, Integer num) {
        this.f405a = l;
        this.b = str;
        this.c = l2;
        this.d = num;
    }

    @Override // java.lang.Comparable
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compareTo(c cVar) {
        if (c().longValue() > cVar.c().longValue()) {
            return -1;
        }
        return c() == cVar.c() ? 0 : 1;
    }

    public Long a() {
        return this.f405a;
    }

    public void a(Integer num) {
        this.d = num;
    }

    public void a(Long l) {
        this.f405a = l;
    }

    public void a(String str) {
        this.b = str;
    }

    public String b() {
        return this.b;
    }

    public void b(Long l) {
        this.c = l;
    }

    public Long c() {
        return this.c;
    }

    public Integer d() {
        return this.d;
    }
}
