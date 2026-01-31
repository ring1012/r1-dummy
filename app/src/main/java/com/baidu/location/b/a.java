package com.baidu.location.b;

import com.unisound.vui.priority.PriorityMap;
import java.util.Locale;

/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    public int f86a;
    public int b;
    public int c;
    public int d;
    public int e;
    public int f;
    public long g;
    public int h;
    public char i;
    public String j;
    private boolean k;

    public a() {
        this.f86a = -1;
        this.b = -1;
        this.c = -1;
        this.d = -1;
        this.e = PriorityMap.PRIORITY_MAX;
        this.f = PriorityMap.PRIORITY_MAX;
        this.g = 0L;
        this.h = -1;
        this.i = '0';
        this.j = null;
        this.k = false;
        this.g = System.currentTimeMillis();
    }

    public a(int i, int i2, int i3, int i4, int i5, char c) {
        this.f86a = -1;
        this.b = -1;
        this.c = -1;
        this.d = -1;
        this.e = PriorityMap.PRIORITY_MAX;
        this.f = PriorityMap.PRIORITY_MAX;
        this.g = 0L;
        this.h = -1;
        this.i = '0';
        this.j = null;
        this.k = false;
        this.f86a = i;
        this.b = i2;
        this.c = i3;
        this.d = i4;
        this.h = i5;
        this.i = c;
        this.g = System.currentTimeMillis();
    }

    public a(a aVar) {
        this(aVar.f86a, aVar.b, aVar.c, aVar.d, aVar.h, aVar.i);
        this.g = aVar.g;
    }

    public boolean a() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        return jCurrentTimeMillis - this.g > 0 && jCurrentTimeMillis - this.g < 3000;
    }

    public boolean a(a aVar) {
        return this.f86a == aVar.f86a && this.b == aVar.b && this.d == aVar.d && this.c == aVar.c;
    }

    public boolean b() {
        return this.f86a > -1 && this.b > 0;
    }

    public boolean c() {
        return this.f86a == -1 && this.b == -1 && this.d == -1 && this.c == -1;
    }

    public boolean d() {
        return this.f86a > -1 && this.b > -1 && this.d == -1 && this.c == -1;
    }

    public boolean e() {
        return this.f86a > -1 && this.b > -1 && this.d > -1 && this.c > -1;
    }

    public void f() {
        this.k = true;
    }

    public String g() {
        StringBuffer stringBuffer = new StringBuffer(128);
        stringBuffer.append("&nw=");
        stringBuffer.append(this.i);
        stringBuffer.append(String.format(Locale.CHINA, "&cl=%d|%d|%d|%d&cl_s=%d", Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.f86a), Integer.valueOf(this.b), Integer.valueOf(this.h)));
        if (this.k) {
            stringBuffer.append("&newcl=1");
        }
        return stringBuffer.toString();
    }

    public String h() {
        StringBuffer stringBuffer = new StringBuffer(128);
        stringBuffer.append("&nw2=");
        stringBuffer.append(this.i);
        stringBuffer.append(String.format(Locale.CHINA, "&cl2=%d|%d|%d|%d&cl_s2=%d", Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.f86a), Integer.valueOf(this.b), Integer.valueOf(this.h)));
        return stringBuffer.toString();
    }
}
