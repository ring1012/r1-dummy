package cn.yunzhisheng.asr;

import android.support.v4.internal.view.SupportMenu;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.unisound.common.j;
import com.unisound.common.y;
import com.unisound.sdk.cr;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class VAD {

    /* renamed from: a, reason: collision with root package name */
    public static final int f25a = -1;
    public static final int b = 0;
    public static final int c = 1;
    public static final int d = 2;
    public static final int e = 3;
    public static int f = 0;
    private static final int l = 0;
    private static final int m = 1;
    private static final int n = -1001;
    private double C;
    protected long i;
    private a p;
    private cr q;
    private ByteArrayOutputStream o = new ByteArrayOutputStream(CacheDataSink.DEFAULT_BUFFER_SIZE);
    public List<byte[]> g = new LinkedList();
    private boolean r = false;
    private boolean s = false;
    public boolean h = false;
    public boolean j = true;
    public boolean k = false;
    private boolean t = false;
    private boolean u = false;
    private byte[] v = {99};
    private boolean w = false;
    private int x = 0;
    private ArrayList<byte[]> y = new ArrayList<>();
    private boolean z = false;
    private ArrayList<byte[]> A = new ArrayList<>();
    private boolean B = false;

    public VAD(a aVar, cr crVar) {
        this.i = 0L;
        this.p = aVar;
        this.q = crVar;
        this.i = create();
        if (this.i == 0) {
            y.a("jni VAD create fail!");
            return;
        }
        if (this.p.c.a()) {
            this.p.n(true);
            this.p.S();
            if (this.p.c != null) {
                this.p.c.a(String.valueOf(System.currentTimeMillis()));
            }
        }
        if (this.p.av()) {
            this.p.af();
        }
        this.p.l(false);
        a(this.p.n());
        init(this.i);
    }

    private double a(double d2) {
        return d2 / 32.0d;
    }

    private int a(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = 0;
        int i4 = 0;
        while (i4 < i - 1) {
            int i5 = i4 + 1;
            byte b2 = bArr[i4];
            i4 = i5 + 1;
            byte b3 = bArr[i5];
            int i6 = i3 + 1;
            bArr2[i3] = b2;
            int i7 = i6 + 1;
            bArr2[i6] = b3;
            int i8 = i7 + 1;
            bArr2[i7] = b2;
            i3 = i8 + 1;
            bArr2[i8] = b3;
        }
        return i3;
    }

    private String b(int i, String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("timeout", i);
            jSONObject.put("afterTimeoutVoice", str);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jSONObject.toString();
    }

    private void b(int i) {
        cr crVar = this.q;
        if (crVar != null) {
            crVar.b(i);
        }
    }

    private void b(boolean z) {
        if (z != this.s && a()) {
            this.s = z;
            if (this.s) {
                setTime(this.i, -1001, 1);
            } else {
                setTime(this.i, -1001, 0);
            }
        }
    }

    private void c(int i) {
        a(i);
        if (this.p.b() && this.p.z()) {
            j.a(false, this.p.c());
        }
        cr crVar = this.q;
        if (crVar != null) {
            crVar.a(this);
            this.p.v(true);
        }
        if (i != 2) {
            y.c("VAD >>", "TimeOut");
        }
    }

    private void d(int i) {
        this.C += i;
    }

    private synchronized void d(byte[] bArr) {
        synchronized (this) {
            this.g.add(bArr);
            int size = this.g.size() - 1;
            int i = 0;
            while (true) {
                if (size < 0) {
                    size = 0;
                    break;
                }
                int length = this.g.get(size).length + i;
                if (length >= this.p.ad) {
                    break;
                }
                size--;
                i = length;
            }
            for (int i2 = 0; i2 < size; i2++) {
                byte[] bArrRemove = this.g.remove(0);
                a(false, bArrRemove, 0, bArrRemove.length);
            }
        }
    }

    private void f() {
        cr crVar = this.q;
        if (crVar != null) {
            crVar.n();
        }
    }

    private void g() {
        cr crVar = this.q;
        if (crVar != null) {
            crVar.o();
        }
    }

    private void h() {
        this.y.clear();
        this.z = false;
    }

    private void i() {
        this.A.clear();
        this.B = false;
    }

    private void j() {
        this.C = 0.0d;
    }

    private double k() {
        return this.C;
    }

    public int a(int i, String str) {
        if (this.i == 0) {
            return -1;
        }
        if (str == null || str.length() == 0) {
            return 0;
        }
        return nativeSetOption(this.i, i, str);
    }

    public int a(b bVar) {
        if (bVar.c()) {
            return a(bVar.b, bVar.toString());
        }
        return 0;
    }

    public int a(byte[] bArr, int i) {
        if (a()) {
            return checkPitchOffset(this.i, bArr, i);
        }
        return 0;
    }

    public synchronized int a(byte[] bArr, int i, int i2) {
        byte[][] bArrA;
        int length;
        byte[] bArr2;
        byte[] bArr3;
        int iB;
        if (bArr.length == 1 && (bArr[0] == 99 || bArr[0] == 100)) {
            iB = 0;
        } else {
            if (this.p.b()) {
                j.a(bArr, this.p.c());
            }
            this.p.c.b(bArr);
            if (y.l) {
                y.d("VAD::write size =", Integer.valueOf(bArr.length), " first byte ", Byte.valueOf(bArr[0]));
            }
            if (bArr.length == 1 && (bArr[0] == 100 || bArr[0] == 99)) {
                a(true, bArr, 0, i2);
                iB = 0;
            } else if (i2 <= 0) {
                iB = 0;
            } else {
                if (this.p.c == null || !this.p.c.a()) {
                    if (this.p.av()) {
                        bArrA = this.p.c.a(bArr);
                    } else {
                        byte[][] bArr4 = (byte[][]) Array.newInstance((Class<?>) Byte.TYPE, 2, bArr.length);
                        bArr4[1] = bArr;
                        bArr4[0] = bArr;
                        bArrA = bArr4;
                    }
                } else if (this.p.c.s() && this.p.c.t()) {
                    byte[][] bArr5 = (byte[][]) Array.newInstance((Class<?>) Byte.TYPE, 2, bArr.length);
                    bArr5[1] = bArr;
                    bArr5[0] = bArr;
                    bArrA = bArr5;
                } else {
                    bArrA = this.p.c.a(bArr);
                }
                if (this.p.ag) {
                    if (this.p.c.i() == 0) {
                        if (this.p.c.o()) {
                            int length2 = bArrA[1].length * 2;
                            byte[] bArr6 = new byte[length2];
                            int iA = a(bArrA[0], bArrA[0].length, bArr6, length2);
                            byte[] bArr7 = new byte[length2];
                            a(bArrA[0], bArrA[0].length, bArr7, length2);
                            length = iA;
                            bArr2 = bArr6;
                            bArr3 = bArr7;
                        } else {
                            int length3 = bArrA[1].length * 2;
                            byte[] bArr8 = new byte[length3];
                            int iA2 = a(bArrA[1], bArrA[1].length, bArr8, length3);
                            byte[] bArr9 = new byte[length3];
                            a(bArrA[0], bArrA[0].length, bArr9, length3);
                            length = iA2;
                            bArr2 = bArr8;
                            bArr3 = bArr9;
                        }
                    } else if (this.p.c.o()) {
                        int length4 = bArrA[0].length * 2;
                        byte[] bArr10 = new byte[length4];
                        int iA3 = a(bArrA[1], bArrA[1].length, bArr10, length4);
                        byte[] bArr11 = new byte[length4];
                        a(bArrA[1], bArrA[1].length, bArr11, length4);
                        length = iA3;
                        bArr2 = bArr10;
                        bArr3 = bArr11;
                    } else {
                        int length5 = bArrA[0].length * 2;
                        byte[] bArr12 = new byte[length5];
                        int iA4 = a(bArrA[0], bArrA[0].length, bArr12, length5);
                        byte[] bArr13 = new byte[length5];
                        a(bArrA[1], bArrA[1].length, bArr13, length5);
                        length = iA4;
                        bArr2 = bArr12;
                        bArr3 = bArr13;
                    }
                } else if (this.p.c.i() == 0) {
                    if (this.p.c.o()) {
                        byte[] bArrCopyOfRange = Arrays.copyOfRange(bArrA[0], 0, bArrA[0].length);
                        byte[] bArrCopyOfRange2 = Arrays.copyOfRange(bArrA[0], 0, bArrA[0].length);
                        length = bArrCopyOfRange.length;
                        bArr2 = bArrCopyOfRange;
                        bArr3 = bArrCopyOfRange2;
                    } else {
                        byte[] bArrCopyOfRange3 = Arrays.copyOfRange(bArrA[1], 0, bArrA[1].length);
                        byte[] bArrCopyOfRange4 = Arrays.copyOfRange(bArrA[0], 0, bArrA[0].length);
                        length = bArrCopyOfRange3.length;
                        bArr2 = bArrCopyOfRange3;
                        bArr3 = bArrCopyOfRange4;
                    }
                } else if (this.p.c.o()) {
                    byte[] bArrCopyOfRange5 = Arrays.copyOfRange(bArrA[1], 0, bArrA[1].length);
                    byte[] bArrCopyOfRange6 = Arrays.copyOfRange(bArrA[1], 0, bArrA[1].length);
                    length = bArrCopyOfRange5.length;
                    bArr2 = bArrCopyOfRange5;
                    bArr3 = bArrCopyOfRange6;
                } else {
                    byte[] bArrCopyOfRange7 = Arrays.copyOfRange(bArrA[0], 0, bArrA[0].length);
                    byte[] bArrCopyOfRange8 = Arrays.copyOfRange(bArrA[1], 0, bArrA[1].length);
                    length = bArrCopyOfRange7.length;
                    bArr2 = bArrCopyOfRange7;
                    bArr3 = bArrCopyOfRange8;
                }
                if (!this.p.D()) {
                    this.p.e(bArr3.length);
                }
                this.p.c.c(bArr3);
                this.p.c.d(bArr2);
                if (this.p.A() && this.p.B()) {
                    this.x += bArr.length;
                    if (this.x >= this.p.aj()) {
                        this.w = true;
                    }
                }
                if (!this.j || (this.p.A() && !this.w && this.p.B())) {
                    a(true, bArr3, 0, bArr3.length);
                    b(c(bArr3, bArr3.length));
                    iB = 0;
                } else {
                    if (!this.p.m() || this.r) {
                        iB = b(bArr2, length);
                        if (iB != 0) {
                            if (iB == 1) {
                                if (!this.p.A() || this.u) {
                                    this.B = true;
                                } else {
                                    this.z = true;
                                }
                                if (this.p.b() && this.p.z()) {
                                    j.b(false, this.p.c());
                                }
                                y.c("VAD >>", "ASR_VAD_BACK_END ", "param = ", Boolean.valueOf(this.u));
                                g();
                            } else if (iB == 2) {
                                this.p.c(b(0, ""));
                                c(2);
                                y.c("VAD >>", "ASR_VAD_MAX_SIL1");
                            } else if (iB == 3) {
                                if (this.p.A()) {
                                    if (this.p.B()) {
                                        this.u = true;
                                    }
                                    if (this.u) {
                                        i();
                                    } else {
                                        h();
                                    }
                                } else {
                                    i();
                                }
                                if (this.p.b() && this.p.z()) {
                                    j.a(true, this.p.c());
                                }
                                this.h = true;
                                y.c("VAD >>", "ASR_VAD_FRONT_END ", "param = ", Boolean.valueOf(this.u));
                                f();
                            }
                        }
                        if (!this.h && this.p.Y && this.p.at()) {
                            if (!this.p.A() || !this.p.B()) {
                                d(bArr3);
                            }
                        } else if (this.p.Y && !this.t && this.p.at()) {
                            this.g.add(bArr3);
                            this.t = this.h;
                        } else {
                            a(true, bArr3, 0, length);
                        }
                        y.f("VAD done        1");
                        b(c());
                        if (this.p.A() && this.p.B()) {
                            d(bArr.length);
                        }
                        if (!this.p.A() || this.u || this.p.c.a()) {
                            if (c(bArr2)) {
                                this.p.c(b(1, String.valueOf(a(k()))));
                                c(iB);
                                j();
                                i();
                            }
                        } else if (b(bArr2)) {
                            this.p.c(b(2, String.valueOf(a(k()))));
                            c(iB);
                            j();
                            h();
                        }
                    } else {
                        a(bArr3);
                        b(c());
                        iB = 0;
                    }
                    if (y.l) {
                        y.d("AF_VAD::read size =", Integer.valueOf(bArr.length), " first byte ", Byte.valueOf(bArr[0]));
                    }
                }
            }
        }
        return iB;
    }

    public synchronized void a(int i) {
        this.h = false;
        this.r = false;
        this.t = false;
        this.g.clear();
        this.o.reset();
        if (this.p.A()) {
            this.u = false;
        }
        if (a() && i == 2) {
            reset(this.i);
        }
    }

    public void a(int i, int i2) {
        if (a()) {
            setTime(this.i, i / 10, i2 / 10);
        }
    }

    public void a(boolean z) {
        this.j = z;
    }

    public void a(boolean z, byte[] bArr, int i, int i2) {
        if (y.l) {
            y.d("VAD::onVadData size =", Integer.valueOf(bArr.length), " first byte ", Byte.valueOf(bArr[0]), "enabled = ", Boolean.valueOf(z));
        }
        cr crVar = this.q;
        if (z && this.p.d() != null && !this.p.d().equals("") && (bArr.length != 1 || (bArr[0] != 100 && bArr[0] != 99))) {
            j.a(bArr, this.p.d());
        }
        if (crVar != null) {
            crVar.b(z, bArr, i, i2);
        }
    }

    protected synchronized void a(byte[] bArr) {
        this.o.write(bArr, 0, bArr.length);
        if (this.o.size() >= this.p.ae) {
            byte[] byteArray = this.o.toByteArray();
            this.o.reset();
            int iCheckPitchOffset = checkPitchOffset(this.i, byteArray, byteArray.length);
            if (iCheckPitchOffset > 0) {
                byte[] bArr2 = new byte[iCheckPitchOffset];
                System.arraycopy(byteArray, 0, bArr2, 0, iCheckPitchOffset);
                a(false, bArr2, 0, bArr2.length);
                this.o.write(byteArray, iCheckPitchOffset, byteArray.length - iCheckPitchOffset);
                byteArray = this.o.toByteArray();
                this.o.reset();
            }
            f = iCheckPitchOffset;
            if (byteArray.length > 0) {
                a(true, byteArray, 0, byteArray.length);
                b(byteArray, byteArray.length);
            }
            this.r = true;
            this.h = true;
        }
    }

    public boolean a() {
        return this.i != 0;
    }

    public int b(byte[] bArr, int i) {
        if (this.i == 0) {
            return 0;
        }
        return isVADTimeout(this.i, bArr, i);
    }

    public void b() {
        y.b("frontSil = ", Integer.valueOf(this.p.ab), " backSil= ", Integer.valueOf(this.p.ac));
        a(this.p.ab, this.p.ac);
        if (this.p.h()) {
            y.b("mParams.isFarFeildEnabled() = ", Boolean.valueOf(this.p.h()));
            b(this.p.h());
        }
        if (this.p.y.f27a != null && !this.p.y.f27a.equals("")) {
            y.b("mParams.MINBACKENG = ", this.p.y.f27a);
            a(this.p.y);
        }
        if (this.p.z.f27a != null && !this.p.z.f27a.equals("")) {
            y.b("mParams.MINBACKENGH = ", this.p.z.f27a);
            a(this.p.z);
        }
        if (this.p.A.f27a != null && !this.p.A.f27a.equals("")) {
            y.b("mParams.PITCHTH = ", this.p.A.f27a);
            a(this.p.A);
        }
        if (this.p.B.f27a != null && !this.p.B.f27a.equals("")) {
            y.b("mParams.PITCHSTNUMTH = ", this.p.B.f27a);
            a(this.p.B);
        }
        if (this.p.C.f27a != null && !this.p.C.f27a.equals("")) {
            y.b("mParams.PITCHENDNUMTH = ", this.p.C.f27a);
            a(this.p.C);
        }
        if (this.p.D.f27a != null && !this.p.D.f27a.equals("")) {
            y.b("mParams.LOWHIGHTH = ", this.p.D.f27a);
            a(this.p.D);
        }
        if (this.p.E.f27a != null && !this.p.E.f27a.equals("")) {
            y.b("mParams.MINSIGLEN = ", this.p.E.f27a);
            a(this.p.E);
        }
        if (this.p.F.f27a != null && !this.p.F.f27a.equals("") && !this.p.F.f27a.equals((this.p.ac / 10) + "")) {
            y.b("mParams.MAXSILLEN = ", this.p.F.f27a);
            a(this.p.F);
        }
        if (this.p.G.f27a != null && !this.p.G.f27a.equals("")) {
            y.b("mParams.SINGLEMAX = ", this.p.G.f27a);
            a(this.p.G);
        }
        if (this.p.H.f27a != null && !this.p.H.f27a.equals("")) {
            y.b("mParams.NOISE2YTH = ", this.p.H.f27a);
            a(this.p.H);
        }
        if (this.p.I.f27a != null && !this.p.I.f27a.equals("")) {
            y.b("mParams.NOISE2YTHVOWEL = ", this.p.I.f27a);
            a(this.p.I);
        }
        if (this.p.J.f27a != null && !this.p.J.f27a.equals("")) {
            y.b("mParams.VOICEPROBTH = ", this.p.J.f27a);
            a(this.p.J);
        }
        if (this.p.K.f27a != null && !this.p.K.f27a.equals("")) {
            y.b("mParams.USEPEAK = ", this.p.K.f27a);
            a(this.p.K);
        }
        if (this.p.L.f27a != null && !this.p.L.f27a.equals("")) {
            y.b("mParams.NOISE2YST = ", this.p.L.f27a);
            a(this.p.L);
        }
        if (this.p.M.f27a != null && !this.p.M.f27a.equals("")) {
            y.b("mParams.PITCHLASTTH = ", this.p.M.f27a);
            a(this.p.M);
        }
        if (this.p.N.f27a != null && !this.p.N.f27a.equals("")) {
            y.b("mParams.DETECTMUSIC = ", this.p.N.f27a);
            a(this.p.N);
        }
        if (this.p.O.f27a != null && !this.p.O.f27a.equals("")) {
            y.b("mParams.MUSICTH = ", this.p.O.f27a);
            a(this.p.O);
        }
        j();
    }

    protected boolean b(byte[] bArr) {
        if (this.z) {
            this.y.add(bArr);
            int iG = this.p.G();
            int length = 0;
            for (int size = this.y.size() - 1; size >= 0; size--) {
                length += this.y.get(size).length;
                if (length >= iG) {
                    return true;
                }
            }
        }
        return false;
    }

    public int c() {
        if (a()) {
            return getVolume(this.i);
        }
        return 0;
    }

    protected int c(byte[] bArr, int i) {
        float fAbs = 0.0f;
        for (int i2 = 0; i2 < i; i2 += 2) {
            int i3 = (bArr[i2] & 255) + ((bArr[i2 + 1] & 255) << 8);
            if (i3 >= 32768) {
                i3 = SupportMenu.USER_MASK - i3;
            }
            fAbs += Math.abs(i3);
        }
        int iLog10 = (int) (((10.0d * Math.log10(((2.0f * fAbs) / i) + 1.0f)) - 20.0d) * 5.0d);
        if (iLog10 < 0) {
            iLog10 = 0;
        }
        if (iLog10 > 100) {
            return 100;
        }
        return iLog10;
    }

    protected boolean c(byte[] bArr) {
        if (this.B) {
            this.A.add(bArr);
            int iH = this.p.H();
            int length = 0;
            for (int size = this.A.size() - 1; size >= 0; size--) {
                length += this.A.get(size).length;
                if (length >= iH) {
                    return true;
                }
            }
        }
        return false;
    }

    protected native int checkPitchOffset(long j, byte[] bArr, int i);

    protected native long create();

    public synchronized void d() {
        if (a()) {
            try {
                this.o.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            destory(this.i);
            this.i = 0L;
        }
    }

    protected native void destory(long j);

    public synchronized void e() {
        if (this.o.size() > 0) {
            a(this.h, this.o.toByteArray(), 0, this.o.size());
            this.o.reset();
        }
        int size = this.g.size();
        for (int i = 0; i < size; i++) {
            byte[] bArrRemove = this.g.remove(0);
            a(this.h, bArrRemove, 0, bArrRemove.length);
        }
        b(0);
    }

    protected native int getVolume(long j);

    protected native void init(long j);

    protected native int isVADTimeout(long j, byte[] bArr, int i);

    protected native int nativeSetOption(long j, int i, String str);

    protected native void reset(long j);

    protected native void setTime(long j, int i, int i2);
}
