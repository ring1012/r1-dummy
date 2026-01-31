package com.unisound.vui.data.b;

import com.unisound.vui.data.c.c;
import com.unisound.vui.data.c.f;
import com.unisound.vui.data.entity.a.d;
import java.util.List;
import java.util.concurrent.ExecutorService;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

/* loaded from: classes.dex */
public class c {

    /* renamed from: a, reason: collision with root package name */
    private ExecutorService f390a;
    private f b;
    private f c;

    public c(c.a aVar, ExecutorService executorService) {
        this.f390a = executorService;
        this.b = a(aVar);
        this.c = b(aVar);
    }

    private f a(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getReadableDatabase()).newSession().c();
    }

    private f b(c.a aVar) {
        return new com.unisound.vui.data.c.c(aVar.getWritableDatabase()).newSession().c();
    }

    public List<d> a(String str) {
        QueryBuilder<d> queryBuilder = this.b.queryBuilder();
        queryBuilder.whereOr(f.a.b.eq(str), f.a.c.eq(str), new WhereCondition[0]);
        return queryBuilder.list();
    }

    public void a() {
        this.c.deleteAll();
    }

    public void a(d dVar) {
        this.c.insertInTx(dVar);
    }

    public void b(d dVar) {
        this.c.update(dVar);
    }
}
