package com.unisound.sdk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class cw {

    /* renamed from: a, reason: collision with root package name */
    public static final String f340a = "wakeup";
    public static final String b = "#JSGF V1.0 utf-8 cn;\ngrammar wakeup;\npublic <wakeup> =( \"<s>\" (\n(<NAME> )\n) \"</s>\");";
    private String c = "wakeup.dat";
    private List<String> d = new ArrayList();
    private boolean e = false;

    public cw() {
        this.d.add("您好魔方");
    }

    public String a() {
        return this.c;
    }

    public void a(String str) {
        this.c = str + "wakeup.dat";
    }

    public void a(boolean z) {
        this.e = z;
    }

    public boolean a(List<String> list) {
        return list == null || !list.toString().equals(this.d.toString());
    }

    public String b() {
        return b;
    }

    public void b(List<String> list) {
        if (a(list)) {
            a(true);
            this.d.clear();
            this.d.addAll(list);
        }
    }

    public String c(List<String> list) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<NAME>").append("\n");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            stringBuffer.append(it.next()).append("\n");
        }
        stringBuffer.append("</NAME>");
        return stringBuffer.toString();
    }

    public boolean c() {
        return this.e;
    }

    public String d() {
        return c(this.d);
    }

    public List<String> e() {
        return this.d;
    }
}
