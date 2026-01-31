package com.unisound.vui.data.c;

import com.unisound.vui.data.c.a.g;
import java.util.Map;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

/* loaded from: classes.dex */
public class d extends AbstractDaoSession {

    /* renamed from: a, reason: collision with root package name */
    private final DaoConfig f391a;
    private final DaoConfig b;
    private final DaoConfig c;
    private final DaoConfig d;
    private final DaoConfig e;
    private final DaoConfig f;
    private final DaoConfig g;
    private final DaoConfig h;
    private final DaoConfig i;
    private final DaoConfig j;
    private final DaoConfig k;
    private final a l;
    private final b m;
    private final e n;
    private final f o;
    private final com.unisound.vui.data.c.a.e p;
    private final com.unisound.vui.data.c.a.f q;
    private final com.unisound.vui.data.c.a.b r;
    private final com.unisound.vui.data.c.a.a s;
    private final g t;
    private final com.unisound.vui.data.c.a.d u;
    private final com.unisound.vui.data.c.a.c v;

    public d(Database database, IdentityScopeType identityScopeType, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> map) {
        super(database);
        this.f391a = map.get(a.class).clone();
        this.f391a.initIdentityScope(identityScopeType);
        this.b = map.get(b.class).clone();
        this.b.initIdentityScope(identityScopeType);
        this.c = map.get(e.class).clone();
        this.c.initIdentityScope(identityScopeType);
        this.d = map.get(f.class).clone();
        this.d.initIdentityScope(identityScopeType);
        this.e = map.get(com.unisound.vui.data.c.a.e.class).clone();
        this.e.initIdentityScope(identityScopeType);
        this.f = map.get(com.unisound.vui.data.c.a.f.class).clone();
        this.f.initIdentityScope(identityScopeType);
        this.g = map.get(com.unisound.vui.data.c.a.b.class).clone();
        this.g.initIdentityScope(identityScopeType);
        this.h = map.get(com.unisound.vui.data.c.a.a.class).clone();
        this.h.initIdentityScope(identityScopeType);
        this.i = map.get(g.class).clone();
        this.i.initIdentityScope(identityScopeType);
        this.j = map.get(com.unisound.vui.data.c.a.d.class).clone();
        this.j.initIdentityScope(identityScopeType);
        this.k = map.get(com.unisound.vui.data.c.a.c.class).clone();
        this.k.initIdentityScope(identityScopeType);
        this.l = new a(this.f391a, this);
        this.m = new b(this.b, this);
        this.n = new e(this.c, this);
        this.o = new f(this.d, this);
        this.p = new com.unisound.vui.data.c.a.e(this.e, this);
        this.q = new com.unisound.vui.data.c.a.f(this.f, this);
        this.r = new com.unisound.vui.data.c.a.b(this.g, this);
        this.s = new com.unisound.vui.data.c.a.a(this.h, this);
        this.t = new g(this.i, this);
        this.u = new com.unisound.vui.data.c.a.d(this.j, this);
        this.v = new com.unisound.vui.data.c.a.c(this.k, this);
        registerDao(com.unisound.vui.data.entity.a.a.class, this.l);
        registerDao(com.unisound.vui.data.entity.a.b.class, this.m);
        registerDao(com.unisound.vui.data.entity.a.c.class, this.n);
        registerDao(com.unisound.vui.data.entity.a.d.class, this.o);
        registerDao(com.unisound.vui.data.entity.a.a.e.class, this.p);
        registerDao(com.unisound.vui.data.entity.a.a.f.class, this.q);
        registerDao(com.unisound.vui.data.entity.a.a.b.class, this.r);
        registerDao(com.unisound.vui.data.entity.a.a.a.class, this.s);
        registerDao(com.unisound.vui.data.entity.a.a.g.class, this.t);
        registerDao(com.unisound.vui.data.entity.a.a.d.class, this.u);
        registerDao(com.unisound.vui.data.entity.a.a.c.class, this.v);
    }

    public b a() {
        return this.m;
    }

    public e b() {
        return this.n;
    }

    public f c() {
        return this.o;
    }

    public com.unisound.vui.data.c.a.e d() {
        return this.p;
    }

    public com.unisound.vui.data.c.a.f e() {
        return this.q;
    }

    public com.unisound.vui.data.c.a.b f() {
        return this.r;
    }

    public com.unisound.vui.data.c.a.a g() {
        return this.s;
    }

    public g h() {
        return this.t;
    }

    public com.unisound.vui.data.c.a.d i() {
        return this.u;
    }

    public com.unisound.vui.data.c.a.c j() {
        return this.v;
    }
}
