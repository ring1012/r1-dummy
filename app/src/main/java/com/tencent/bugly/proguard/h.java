package com.tencent.bugly.proguard;

import cn.yunzhisheng.asr.JniUscClient;
import java.util.List;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class h {

    /* renamed from: a, reason: collision with root package name */
    private StringBuilder f189a;
    private int b;

    private void a(String str) {
        for (int i = 0; i < this.b; i++) {
            this.f189a.append('\t');
        }
        if (str != null) {
            this.f189a.append(str).append(": ");
        }
    }

    public h(StringBuilder sb, int i) {
        this.b = 0;
        this.f189a = sb;
        this.b = i;
    }

    public final h a(boolean z, String str) {
        a(str);
        this.f189a.append(z ? 'T' : 'F').append('\n');
        return this;
    }

    public final h a(byte b, String str) {
        a(str);
        this.f189a.append((int) b).append('\n');
        return this;
    }

    public final h a(short s, String str) {
        a(str);
        this.f189a.append((int) s).append('\n');
        return this;
    }

    public final h a(int i, String str) {
        a(str);
        this.f189a.append(i).append('\n');
        return this;
    }

    public final h a(long j, String str) {
        a(str);
        this.f189a.append(j).append('\n');
        return this;
    }

    public final h a(String str, String str2) {
        a(str2);
        if (str == null) {
            this.f189a.append("null\n");
        } else {
            this.f189a.append(str).append('\n');
        }
        return this;
    }

    public final h a(byte[] bArr, String str) {
        a(str);
        if (bArr == null) {
            this.f189a.append("null\n");
        } else if (bArr.length == 0) {
            this.f189a.append(bArr.length).append(", []\n");
        } else {
            this.f189a.append(bArr.length).append(", [\n");
            h hVar = new h(this.f189a, this.b + 1);
            for (byte b : bArr) {
                hVar.a(null);
                hVar.f189a.append((int) b).append('\n');
            }
            a(null);
            this.f189a.append(']').append('\n');
        }
        return this;
    }

    public final <K, V> h a(Map<K, V> map, String str) {
        a(str);
        if (map == null) {
            this.f189a.append("null\n");
        } else if (map.isEmpty()) {
            this.f189a.append(map.size()).append(", {}\n");
        } else {
            this.f189a.append(map.size()).append(", {\n");
            h hVar = new h(this.f189a, this.b + 1);
            h hVar2 = new h(this.f189a, this.b + 2);
            for (Map.Entry<K, V> entry : map.entrySet()) {
                hVar.a(null);
                hVar.f189a.append('(').append('\n');
                hVar2.a((h) entry.getKey(), (String) null);
                hVar2.a((h) entry.getValue(), (String) null);
                hVar.a(null);
                hVar.f189a.append(')').append('\n');
            }
            a(null);
            this.f189a.append('}').append('\n');
        }
        return this;
    }

    private <T> h a(T[] tArr, String str) {
        a(str);
        if (tArr == null) {
            this.f189a.append("null\n");
        } else if (tArr.length == 0) {
            this.f189a.append(tArr.length).append(", []\n");
        } else {
            this.f189a.append(tArr.length).append(", [\n");
            h hVar = new h(this.f189a, this.b + 1);
            for (T t : tArr) {
                hVar.a((h) t, (String) null);
            }
            a(null);
            this.f189a.append(']').append('\n');
        }
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> h a(T t, String str) {
        int i = 0;
        if (t == 0) {
            this.f189a.append("null\n");
        } else if (t instanceof Byte) {
            byte bByteValue = ((Byte) t).byteValue();
            a(str);
            this.f189a.append((int) bByteValue).append('\n');
        } else if (t instanceof Boolean) {
            boolean zBooleanValue = ((Boolean) t).booleanValue();
            a(str);
            this.f189a.append(zBooleanValue ? 'T' : 'F').append('\n');
        } else if (t instanceof Short) {
            short sShortValue = ((Short) t).shortValue();
            a(str);
            this.f189a.append((int) sShortValue).append('\n');
        } else if (t instanceof Integer) {
            int iIntValue = ((Integer) t).intValue();
            a(str);
            this.f189a.append(iIntValue).append('\n');
        } else if (t instanceof Long) {
            long jLongValue = ((Long) t).longValue();
            a(str);
            this.f189a.append(jLongValue).append('\n');
        } else if (t instanceof Float) {
            float fFloatValue = ((Float) t).floatValue();
            a(str);
            this.f189a.append(fFloatValue).append('\n');
        } else if (t instanceof Double) {
            double dDoubleValue = ((Double) t).doubleValue();
            a(str);
            this.f189a.append(dDoubleValue).append('\n');
        } else if (t instanceof String) {
            a((String) t, str);
        } else if (t instanceof Map) {
            a((Map) t, str);
        } else if (t instanceof List) {
            List list = (List) t;
            if (list == null) {
                a(str);
                this.f189a.append("null\t");
            } else {
                a(list.toArray(), str);
            }
        } else if (t instanceof k) {
            a((k) t, str);
        } else if (t instanceof byte[]) {
            a((byte[]) t, str);
        } else if (t instanceof boolean[]) {
            a((h) t, str);
        } else if (t instanceof short[]) {
            short[] sArr = (short[]) t;
            a(str);
            if (sArr == null) {
                this.f189a.append("null\n");
            } else if (sArr.length == 0) {
                this.f189a.append(sArr.length).append(", []\n");
            } else {
                this.f189a.append(sArr.length).append(", [\n");
                h hVar = new h(this.f189a, this.b + 1);
                int length = sArr.length;
                while (i < length) {
                    short s = sArr[i];
                    hVar.a(null);
                    hVar.f189a.append((int) s).append('\n');
                    i++;
                }
                a(null);
                this.f189a.append(']').append('\n');
            }
        } else if (t instanceof int[]) {
            int[] iArr = (int[]) t;
            a(str);
            if (iArr == null) {
                this.f189a.append("null\n");
            } else if (iArr.length == 0) {
                this.f189a.append(iArr.length).append(", []\n");
            } else {
                this.f189a.append(iArr.length).append(", [\n");
                h hVar2 = new h(this.f189a, this.b + 1);
                int length2 = iArr.length;
                while (i < length2) {
                    int i2 = iArr[i];
                    hVar2.a(null);
                    hVar2.f189a.append(i2).append('\n');
                    i++;
                }
                a(null);
                this.f189a.append(']').append('\n');
            }
        } else if (t instanceof long[]) {
            long[] jArr = (long[]) t;
            a(str);
            if (jArr == null) {
                this.f189a.append("null\n");
            } else if (jArr.length == 0) {
                this.f189a.append(jArr.length).append(", []\n");
            } else {
                this.f189a.append(jArr.length).append(", [\n");
                h hVar3 = new h(this.f189a, this.b + 1);
                int length3 = jArr.length;
                while (i < length3) {
                    long j = jArr[i];
                    hVar3.a(null);
                    hVar3.f189a.append(j).append('\n');
                    i++;
                }
                a(null);
                this.f189a.append(']').append('\n');
            }
        } else if (t instanceof float[]) {
            float[] fArr = (float[]) t;
            a(str);
            if (fArr == null) {
                this.f189a.append("null\n");
            } else if (fArr.length == 0) {
                this.f189a.append(fArr.length).append(", []\n");
            } else {
                this.f189a.append(fArr.length).append(", [\n");
                h hVar4 = new h(this.f189a, this.b + 1);
                int length4 = fArr.length;
                while (i < length4) {
                    float f = fArr[i];
                    hVar4.a(null);
                    hVar4.f189a.append(f).append('\n');
                    i++;
                }
                a(null);
                this.f189a.append(']').append('\n');
            }
        } else if (t instanceof double[]) {
            double[] dArr = (double[]) t;
            a(str);
            if (dArr == null) {
                this.f189a.append("null\n");
            } else if (dArr.length == 0) {
                this.f189a.append(dArr.length).append(", []\n");
            } else {
                this.f189a.append(dArr.length).append(", [\n");
                h hVar5 = new h(this.f189a, this.b + 1);
                int length5 = dArr.length;
                while (i < length5) {
                    double d = dArr[i];
                    hVar5.a(null);
                    hVar5.f189a.append(d).append('\n');
                    i++;
                }
                a(null);
                this.f189a.append(']').append('\n');
            }
        } else if (t.getClass().isArray()) {
            a((Object[]) t, str);
        } else {
            throw new b("write object error: unsupport type.");
        }
        return this;
    }

    public final h a(k kVar, String str) {
        a(str);
        this.f189a.append('{').append('\n');
        if (kVar == null) {
            this.f189a.append('\t').append(JniUscClient.az);
        } else {
            kVar.a(this.f189a, this.b + 1);
        }
        a(null);
        this.f189a.append('}').append('\n');
        return this;
    }
}
