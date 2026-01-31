package com.unisound.vui.util;

import com.unisound.vui.util.b;
import com.unisound.vui.util.internal.ObjectUtil;
import java.util.HashMap;
import java.util.Map;
import org.litepal.util.Const;

/* loaded from: classes.dex */
public abstract class c<T extends b<T>> {

    /* renamed from: a, reason: collision with root package name */
    private final Map<String, T> f442a = new HashMap();
    private int b = 1;

    public T a(Class<?> cls, String str) {
        if (cls == null) {
            throw new NullPointerException("firstNameComponent");
        }
        if (str == null) {
            throw new NullPointerException("secondNameComponent");
        }
        return (T) a(cls.getName() + '#' + str);
    }

    public T a(String str) {
        T t;
        if (str == null) {
            throw new NullPointerException(Const.TableSchema.COLUMN_NAME);
        }
        if (str.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        synchronized (this.f442a) {
            t = this.f442a.get(str);
            if (t == null) {
                t = (T) b(this.b, str);
                this.f442a.put(str, t);
                this.b++;
            }
        }
        return t;
    }

    protected abstract T b(int i, String str);

    public boolean b(String str) {
        boolean zContainsKey;
        ObjectUtil.checkNotNull(str, Const.TableSchema.COLUMN_NAME);
        synchronized (this.f442a) {
            zContainsKey = this.f442a.containsKey(str);
        }
        return zContainsKey;
    }

    public T c(String str) {
        T t;
        if (str == null) {
            throw new NullPointerException(Const.TableSchema.COLUMN_NAME);
        }
        if (str.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        synchronized (this.f442a) {
            if (this.f442a.get(str) != null) {
                throw new IllegalArgumentException(String.format("'%s' is already in use", str));
            }
            t = (T) b(this.b, str);
            this.f442a.put(str, t);
            this.b++;
        }
        return t;
    }
}
