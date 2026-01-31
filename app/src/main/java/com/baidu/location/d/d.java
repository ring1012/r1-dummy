package com.baidu.location.d;

import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class d {

    /* renamed from: a, reason: collision with root package name */
    private static d f104a = null;
    private static final double[] b = {110.389648d, 105.070224d, 96.898845d, 95.617053d, 93.822557d, 91.387165d, 91.387165d, 89.079901d, 87.221341d, 86.259997d, 85.170461d, 85.234525d, 82.863261d, 81.901917d, 79.594685d, 80.395798d, 79.931142d, 77.800174d, 75.252622d, 73.490158d, 73.185732d, 73.874699d, 74.403435d, 74.099003d, 75.460905d, 76.77474d, 78.280852d, 78.15266d, 78.37698d, 78.8897d, 79.690818d, 81.196914d, 81.805762d, 83.888674d, 85.939538d, 87.862215d, 89.031847d, 90.057287d, 91.098741d, 92.156213d, 93.742421d, 95.825333d, 97.956327d, 97.123143d, 98.212679d, 99.206055d, 101.657471d, 102.52267d, 102.23427d, 105.022158d, 106.095662d, 107.858116d, 111.639396d, 109.588503d, 112.280343d, 117.792023d, 118.945559d, 114.203031d, 118.689303d, 123.143466d, 122.726858d, 120.932369d, 123.415857d, 122.374385d, 122.134054d, 121.586108d, 121.17271d, 120.676006d, 120.243409d, 122.790961d, 122.871076d, 121.300884d, 122.134052d, 123.736285d, 126.412019d, 128.559027d, 129.712627d, 131.218707d, 131.987795d, 133.622084d, 135.60888d, 131.378992d, 130.866224d, 128.623088d, 126.091572d, 124.393204d, 122.214164d, 119.65058d, 119.778772d, 118.561044d, 116.510192d, 114.811824d, 119.073776d, 116.446096d, 111.735536d, 110.389648d};
    private static final double[] c = {43.216755d, 42.378597d, 43.172612d, 44.421188d, 45.097508d, 45.565732d, 47.334548d, 48.687188d, 49.62362d, 48.947316d, 48.479092d, 47.334548d, 47.438596d, 46.03394d, 45.201556d, 43.344095d, 42.328663d, 41.395882d, 40.829126d, 39.955382d, 39.258748d, 38.361382d, 38.054394d, 37.168842d, 36.389553d, 35.362313d, 34.311457d, 32.587581d, 31.572149d, 30.781055d, 30.438642d, 29.77743d, 30.09623d, 28.714766d, 27.71114d, 27.581258d, 27.014504d, 27.781984d, 27.510415d, 26.506787d, 26.707513d, 27.959095d, 27.29788d, 23.649404d, 23.62579d, 21.677574d, 20.780213d, 21.323353d, 22.185291d, 22.315173d, 22.515897d, 16.802289d, 13.198973d, 0.693351d, 1.541191d, 10.504055d, 15.591095d, 17.892375d, 19.951383d, 22.187501d, 25.375613d, 25.617568d, 30.627458d, 31.082902d, 31.894166d, 32.503117d, 32.805056d, 34.256784d, 35.155304d, 36.90119d, 37.83411d, 37.940728d, 38.64708d, 38.966937d, 40.979374d, 41.253698d, 42.069802d, 42.48888d, 44.65045d, 44.691252d, 48.620679d, 48.091311d, 49.194151d, 50.032311d, 53.274665d, 53.627577d, 53.892257d, 52.987929d, 52.017425d, 50.230825d, 50.186707d, 47.495779d, 47.341379d, 46.503219d, 45.245983d, 43.216755d};
    private LinkedList<b> d;

    class a {

        /* renamed from: a, reason: collision with root package name */
        double f105a;
        double b;

        a(double d, double d2) {
            this.f105a = d;
            this.b = d2;
        }
    }

    class b {

        /* renamed from: a, reason: collision with root package name */
        a f106a;
        a b;

        b(a aVar, a aVar2) {
            this.f106a = aVar;
            this.b = aVar2;
        }

        double a(a aVar) {
            a aVar2 = d.this.new a(this.b.f105a - this.f106a.f105a, this.b.b - this.f106a.b);
            a aVar3 = d.this.new a(aVar.f105a - this.f106a.f105a, aVar.b - this.f106a.b);
            return (aVar2.f105a * aVar3.b) - (aVar2.b * aVar3.f105a);
        }

        boolean b(a aVar) {
            return d.this.a(a(aVar)) == 0 && aVar.f105a < Math.max(this.f106a.f105a, this.b.f105a) + 1.0E-8d && aVar.f105a > Math.min(this.f106a.f105a, this.b.f105a) - 1.0E-8d && aVar.b < Math.max(this.f106a.b, this.b.b) + 1.0E-8d && aVar.b > Math.min(this.f106a.b, this.b.b) - 1.0E-8d;
        }
    }

    private d() {
        this.d = null;
        int length = b.length;
        this.d = new LinkedList<>();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= length - 1) {
                return;
            }
            this.d.add(new b(new a(b[i2] * 100000.0d, c[i2] * 100000.0d), new a(b[i2 + 1] * 100000.0d, c[i2 + 1] * 100000.0d)));
            i = i2 + 1;
        }
    }

    public static d a() {
        if (f104a == null) {
            f104a = new d();
        }
        return f104a;
    }

    int a(double d) {
        if (d < (-1.0E-8d)) {
            return -1;
        }
        return d > 1.0E-8d ? 1 : 0;
    }

    public boolean a(double d, double d2) {
        try {
            a aVar = new a(100000.0d * d, 100000.0d * d2);
            int i = 0;
            Iterator<b> it = this.d.iterator();
            while (it.hasNext()) {
                b next = it.next();
                if (next.b(aVar)) {
                    return true;
                }
                int iA = a(next.a(aVar));
                int iA2 = a(next.f106a.b - aVar.b);
                int iA3 = a(next.b.b - aVar.b);
                int i2 = (iA <= 0 || iA2 > 0 || iA3 <= 0) ? i : i + 1;
                if (iA < 0 && iA3 <= 0 && iA2 > 0) {
                    i2--;
                }
                i = i2;
            }
            return i != 0;
        } catch (Exception e) {
            return true;
        }
    }
}
