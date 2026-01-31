package com.unisound.vui.data.entity.a;

import java.io.Serializable;

/* loaded from: classes.dex */
public class b implements Serializable, Comparable<b> {

    /* renamed from: a, reason: collision with root package name */
    private Long f404a;
    private String b;
    private String c;
    private String d;
    private Long e;
    private Long f;
    private Integer g;

    public b() {
    }

    public b(Long l, String str, String str2, String str3, Long l2, Long l3, Integer num) {
        this.f404a = l;
        this.b = str;
        this.c = str2;
        this.d = str3;
        this.e = l2;
        this.f = l3;
        this.g = num;
    }

    private int b(b bVar) {
        if (f().longValue() > bVar.f().longValue()) {
            return -1;
        }
        return f().longValue() < bVar.f().longValue() ? 1 : 0;
    }

    @Override // java.lang.Comparable
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compareTo(b bVar) {
        if (e().longValue() > bVar.e().longValue()) {
            return -1;
        }
        if (e().longValue() < bVar.e().longValue()) {
            return 1;
        }
        return b(bVar);
    }

    public Long a() {
        return this.f404a;
    }

    public void a(Integer num) {
        this.g = num;
    }

    public void a(Long l) {
        this.f404a = l;
    }

    public void a(String str) {
        this.b = str;
    }

    public String b() {
        return this.b;
    }

    public void b(Long l) {
        this.e = l;
    }

    public void b(String str) {
        this.c = str;
    }

    public String c() {
        return this.c;
    }

    public void c(Long l) {
        this.f = l;
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

    public boolean equals(Object another) {
        if (another instanceof b) {
            return b().equals(((b) another).b()) && d().equals(((b) another).d());
        }
        return false;
    }

    public Long f() {
        return this.f;
    }

    public Integer g() {
        return this.g;
    }

    public String toString() {
        return "Contact{name='" + this.b + "', number='" + this.d + "'}";
    }
}
