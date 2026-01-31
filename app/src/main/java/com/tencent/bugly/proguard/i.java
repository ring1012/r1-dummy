package com.tencent.bugly.proguard;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class i {

    /* renamed from: a, reason: collision with root package name */
    private ByteBuffer f190a;
    private String b = "GBK";

    /* compiled from: BUGLY */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        public byte f191a;
        public int b;
    }

    public i() {
    }

    public i(byte[] bArr) {
        this.f190a = ByteBuffer.wrap(bArr);
    }

    public i(byte[] bArr, int i) {
        this.f190a = ByteBuffer.wrap(bArr);
        this.f190a.position(4);
    }

    public final void a(byte[] bArr) {
        if (this.f190a != null) {
            this.f190a.clear();
        }
        this.f190a = ByteBuffer.wrap(bArr);
    }

    private static int a(a aVar, ByteBuffer byteBuffer) {
        byte b = byteBuffer.get();
        aVar.f191a = (byte) (b & 15);
        aVar.b = (b & 240) >> 4;
        if (aVar.b != 15) {
            return 1;
        }
        aVar.b = byteBuffer.get();
        return 2;
    }

    private boolean a(int i) {
        try {
            a aVar = new a();
            while (true) {
                int iA = a(aVar, this.f190a.duplicate());
                if (i <= aVar.b || aVar.f191a == 11) {
                    break;
                }
                this.f190a.position(iA + this.f190a.position());
                a(aVar.f191a);
            }
            return i == aVar.b;
        } catch (g e) {
            return false;
        } catch (BufferUnderflowException e2) {
            return false;
        }
    }

    private void a() {
        a aVar = new a();
        do {
            a(aVar, this.f190a);
            a(aVar.f191a);
        } while (aVar.f191a != 11);
    }

    private void a(byte b) {
        int i = 0;
        switch (b) {
            case 0:
                this.f190a.position(this.f190a.position() + 1);
                return;
            case 1:
                this.f190a.position(2 + this.f190a.position());
                return;
            case 2:
                this.f190a.position(this.f190a.position() + 4);
                return;
            case 3:
                this.f190a.position(this.f190a.position() + 8);
                return;
            case 4:
                this.f190a.position(this.f190a.position() + 4);
                return;
            case 5:
                this.f190a.position(this.f190a.position() + 8);
                return;
            case 6:
                int i2 = this.f190a.get();
                if (i2 < 0) {
                    i2 += 256;
                }
                this.f190a.position(i2 + this.f190a.position());
                return;
            case 7:
                this.f190a.position(this.f190a.getInt() + this.f190a.position());
                return;
            case 8:
                int iA = a(0, 0, true);
                while (i < (iA << 1)) {
                    a aVar = new a();
                    a(aVar, this.f190a);
                    a(aVar.f191a);
                    i++;
                }
                return;
            case 9:
                int iA2 = a(0, 0, true);
                while (i < iA2) {
                    a aVar2 = new a();
                    a(aVar2, this.f190a);
                    a(aVar2.f191a);
                    i++;
                }
                return;
            case 10:
                a();
                return;
            case 11:
            case 12:
                return;
            case 13:
                a aVar3 = new a();
                a(aVar3, this.f190a);
                if (aVar3.f191a != 0) {
                    throw new g("skipField with invalid type, type value: " + ((int) b) + ", " + ((int) aVar3.f191a));
                }
                this.f190a.position(a(0, 0, true) + this.f190a.position());
                return;
            default:
                throw new g("invalid type.");
        }
    }

    public final boolean a(int i, boolean z) {
        return a((byte) 0, i, z) != 0;
    }

    public final byte a(byte b, int i, boolean z) {
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 0:
                    return this.f190a.get();
                case 12:
                    return (byte) 0;
                default:
                    throw new g("type mismatch.");
            }
        }
        if (z) {
            throw new g("require field not exist.");
        }
        return b;
    }

    public final short a(short s, int i, boolean z) {
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 0:
                    return this.f190a.get();
                case 1:
                    return this.f190a.getShort();
                case 12:
                    return (short) 0;
                default:
                    throw new g("type mismatch.");
            }
        }
        if (z) {
            throw new g("require field not exist.");
        }
        return s;
    }

    public final int a(int i, int i2, boolean z) {
        if (a(i2)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 0:
                    return this.f190a.get();
                case 1:
                    return this.f190a.getShort();
                case 2:
                    return this.f190a.getInt();
                case 12:
                    return 0;
                default:
                    throw new g("type mismatch.");
            }
        }
        if (z) {
            throw new g("require field not exist.");
        }
        return i;
    }

    public final long a(long j, int i, boolean z) {
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 0:
                    return this.f190a.get();
                case 1:
                    return this.f190a.getShort();
                case 2:
                    return this.f190a.getInt();
                case 3:
                    return this.f190a.getLong();
                case 12:
                    return 0L;
                default:
                    throw new g("type mismatch.");
            }
        }
        if (z) {
            throw new g("require field not exist.");
        }
        return j;
    }

    private float a(float f, int i, boolean z) {
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 4:
                    return this.f190a.getFloat();
                case 12:
                    return 0.0f;
                default:
                    throw new g("type mismatch.");
            }
        }
        if (z) {
            throw new g("require field not exist.");
        }
        return f;
    }

    private double a(double d, int i, boolean z) {
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 4:
                    return this.f190a.getFloat();
                case 5:
                    return this.f190a.getDouble();
                case 12:
                    return 0.0d;
                default:
                    throw new g("type mismatch.");
            }
        }
        if (z) {
            throw new g("require field not exist.");
        }
        return d;
    }

    public final String b(int i, boolean z) {
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 6:
                    int i2 = this.f190a.get();
                    if (i2 < 0) {
                        i2 += 256;
                    }
                    byte[] bArr = new byte[i2];
                    this.f190a.get(bArr);
                    try {
                        return new String(bArr, this.b);
                    } catch (UnsupportedEncodingException e) {
                        return new String(bArr);
                    }
                case 7:
                    int i3 = this.f190a.getInt();
                    if (i3 > 104857600 || i3 < 0) {
                        throw new g("String too long: " + i3);
                    }
                    byte[] bArr2 = new byte[i3];
                    this.f190a.get(bArr2);
                    try {
                        return new String(bArr2, this.b);
                    } catch (UnsupportedEncodingException e2) {
                        return new String(bArr2);
                    }
                default:
                    throw new g("type mismatch.");
            }
        }
        if (!z) {
            return null;
        }
        throw new g("require field not exist.");
    }

    public final <K, V> HashMap<K, V> a(Map<K, V> map, int i, boolean z) {
        return (HashMap) a(new HashMap(), map, i, z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <K, V> Map<K, V> a(Map<K, V> map, Map<K, V> map2, int i, boolean z) {
        if (map2 == null || map2.isEmpty()) {
            return new HashMap();
        }
        Map.Entry<K, V> next = map2.entrySet().iterator().next();
        K key = next.getKey();
        V value = next.getValue();
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 8:
                    int iA = a(0, 0, true);
                    if (iA < 0) {
                        throw new g("size invalid: " + iA);
                    }
                    for (int i2 = 0; i2 < iA; i2++) {
                        map.put(a((i) key, 0, true), a((i) value, 1, true));
                    }
                    return map;
                default:
                    throw new g("type mismatch.");
            }
        }
        if (z) {
            throw new g("require field not exist.");
        }
        return map;
    }

    private boolean[] d(int i, boolean z) {
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 9:
                    int iA = a(0, 0, true);
                    if (iA < 0) {
                        throw new g("size invalid: " + iA);
                    }
                    boolean[] zArr = new boolean[iA];
                    for (int i2 = 0; i2 < iA; i2++) {
                        zArr[i2] = a((byte) 0, 0, true) != 0;
                    }
                    return zArr;
                default:
                    throw new g("type mismatch.");
            }
        }
        if (!z) {
            return null;
        }
        throw new g("require field not exist.");
    }

    public final byte[] c(int i, boolean z) {
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 9:
                    int iA = a(0, 0, true);
                    if (iA < 0) {
                        throw new g("size invalid: " + iA);
                    }
                    byte[] bArr = new byte[iA];
                    for (int i2 = 0; i2 < iA; i2++) {
                        bArr[i2] = a(bArr[0], 0, true);
                    }
                    return bArr;
                case 13:
                    a aVar2 = new a();
                    a(aVar2, this.f190a);
                    if (aVar2.f191a != 0) {
                        throw new g("type mismatch, tag: " + i + ", type: " + ((int) aVar.f191a) + ", " + ((int) aVar2.f191a));
                    }
                    int iA2 = a(0, 0, true);
                    if (iA2 < 0) {
                        throw new g("invalid size, tag: " + i + ", type: " + ((int) aVar.f191a) + ", " + ((int) aVar2.f191a) + ", size: " + iA2);
                    }
                    byte[] bArr2 = new byte[iA2];
                    this.f190a.get(bArr2);
                    return bArr2;
                default:
                    throw new g("type mismatch.");
            }
        }
        if (!z) {
            return null;
        }
        throw new g("require field not exist.");
    }

    private short[] e(int i, boolean z) {
        short[] sArr = null;
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 9:
                    int iA = a(0, 0, true);
                    if (iA < 0) {
                        throw new g("size invalid: " + iA);
                    }
                    sArr = new short[iA];
                    for (int i2 = 0; i2 < iA; i2++) {
                        sArr[i2] = a(sArr[0], 0, true);
                    }
                    break;
                default:
                    throw new g("type mismatch.");
            }
        } else if (z) {
            throw new g("require field not exist.");
        }
        return sArr;
    }

    private int[] f(int i, boolean z) {
        int[] iArr = null;
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 9:
                    int iA = a(0, 0, true);
                    if (iA < 0) {
                        throw new g("size invalid: " + iA);
                    }
                    iArr = new int[iA];
                    for (int i2 = 0; i2 < iA; i2++) {
                        iArr[i2] = a(iArr[0], 0, true);
                    }
                    break;
                default:
                    throw new g("type mismatch.");
            }
        } else if (z) {
            throw new g("require field not exist.");
        }
        return iArr;
    }

    private long[] g(int i, boolean z) {
        long[] jArr = null;
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 9:
                    int iA = a(0, 0, true);
                    if (iA < 0) {
                        throw new g("size invalid: " + iA);
                    }
                    jArr = new long[iA];
                    for (int i2 = 0; i2 < iA; i2++) {
                        jArr[i2] = a(jArr[0], 0, true);
                    }
                    break;
                default:
                    throw new g("type mismatch.");
            }
        } else if (z) {
            throw new g("require field not exist.");
        }
        return jArr;
    }

    private float[] h(int i, boolean z) {
        float[] fArr = null;
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 9:
                    int iA = a(0, 0, true);
                    if (iA < 0) {
                        throw new g("size invalid: " + iA);
                    }
                    fArr = new float[iA];
                    for (int i2 = 0; i2 < iA; i2++) {
                        fArr[i2] = a(fArr[0], 0, true);
                    }
                    break;
                default:
                    throw new g("type mismatch.");
            }
        } else if (z) {
            throw new g("require field not exist.");
        }
        return fArr;
    }

    private double[] i(int i, boolean z) {
        double[] dArr = null;
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 9:
                    int iA = a(0, 0, true);
                    if (iA < 0) {
                        throw new g("size invalid: " + iA);
                    }
                    dArr = new double[iA];
                    for (int i2 = 0; i2 < iA; i2++) {
                        dArr[i2] = a(dArr[0], 0, true);
                    }
                    break;
                default:
                    throw new g("type mismatch.");
            }
        } else if (z) {
            throw new g("require field not exist.");
        }
        return dArr;
    }

    private <T> T[] a(T[] tArr, int i, boolean z) {
        if (tArr == null || tArr.length == 0) {
            throw new g("unable to get type of key and value.");
        }
        return (T[]) b(tArr[0], i, z);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> T[] b(T t, int i, boolean z) {
        if (a(i)) {
            a aVar = new a();
            a(aVar, this.f190a);
            switch (aVar.f191a) {
                case 9:
                    int iA = a(0, 0, true);
                    if (iA < 0) {
                        throw new g("size invalid: " + iA);
                    }
                    T[] tArr = (T[]) ((Object[]) Array.newInstance(t.getClass(), iA));
                    for (int i2 = 0; i2 < iA; i2++) {
                        tArr[i2] = a((i) t, 0, true);
                    }
                    return tArr;
                default:
                    throw new g("type mismatch.");
            }
        }
        if (z) {
            throw new g("require field not exist.");
        }
        return null;
    }

    public final k a(k kVar, int i, boolean z) {
        k kVar2 = null;
        if (a(i)) {
            try {
                kVar2 = (k) kVar.getClass().newInstance();
                a aVar = new a();
                a(aVar, this.f190a);
                if (aVar.f191a != 10) {
                    throw new g("type mismatch.");
                }
                kVar2.a(this);
                a();
            } catch (Exception e) {
                throw new g(e.getMessage());
            }
        } else if (z) {
            throw new g("require field not exist.");
        }
        return kVar2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <T> Object a(T t, int i, boolean z) {
        if (t instanceof Byte) {
            return Byte.valueOf(a((byte) 0, i, z));
        }
        if (t instanceof Boolean) {
            return Boolean.valueOf(a((byte) 0, i, z) != 0);
        }
        if (t instanceof Short) {
            return Short.valueOf(a((short) 0, i, z));
        }
        if (t instanceof Integer) {
            return Integer.valueOf(a(0, i, z));
        }
        if (t instanceof Long) {
            return Long.valueOf(a(0L, i, z));
        }
        if (t instanceof Float) {
            return Float.valueOf(a(0.0f, i, z));
        }
        if (t instanceof Double) {
            return Double.valueOf(a(0.0d, i, z));
        }
        if (t instanceof String) {
            return String.valueOf(b(i, z));
        }
        if (t instanceof Map) {
            return (HashMap) a(new HashMap(), (Map) t, i, z);
        }
        if (t instanceof List) {
            List list = (List) t;
            if (list == null || list.isEmpty()) {
                return new ArrayList();
            }
            Object[] objArrB = b(list.get(0), i, z);
            if (objArrB == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (Object obj : objArrB) {
                arrayList.add(obj);
            }
            return arrayList;
        }
        if (t instanceof k) {
            return a((k) t, i, z);
        }
        if (t.getClass().isArray()) {
            if ((t instanceof byte[]) || (t instanceof Byte[])) {
                return c(i, z);
            }
            if (t instanceof boolean[]) {
                return d(i, z);
            }
            if (t instanceof short[]) {
                return e(i, z);
            }
            if (t instanceof int[]) {
                return f(i, z);
            }
            if (t instanceof long[]) {
                return g(i, z);
            }
            if (t instanceof float[]) {
                return h(i, z);
            }
            if (t instanceof double[]) {
                return i(i, z);
            }
            return a((Object[]) t, i, z);
        }
        throw new g("read object error: unsupport type.");
    }

    public final int a(String str) {
        this.b = str;
        return 0;
    }
}
