package com.baidu.location.a;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class l implements SensorEventListener {

    /* renamed from: a, reason: collision with root package name */
    private static Object f75a = new Object();
    private static l b = null;
    private float[] c;
    private int d = 0;
    private List<Float> e = new ArrayList();
    private List<Float> f = new ArrayList();
    private boolean g = false;
    private boolean h;
    private SensorManager i;

    public l() {
        this.h = false;
        try {
            if (this.i == null) {
                this.i = (SensorManager) com.baidu.location.f.b().getSystemService("sensor");
            }
            if (this.i.getDefaultSensor(6) != null) {
                this.h = true;
            }
        } catch (Exception e) {
            this.h = false;
        }
    }

    public static l a() {
        l lVar;
        synchronized (f75a) {
            if (b == null) {
                b = new l();
            }
            lVar = b;
        }
        return lVar;
    }

    public void b() {
        Sensor defaultSensor;
        if (this.h && !this.g) {
            try {
                this.d = 0;
                this.e.clear();
                this.f.clear();
                if (this.i == null) {
                    this.i = (SensorManager) com.baidu.location.f.b().getSystemService("sensor");
                }
                if (this.i != null && (defaultSensor = this.i.getDefaultSensor(6)) != null) {
                    this.i.registerListener(this, defaultSensor, 2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.g = true;
        }
    }

    public void c() {
        if (this.g) {
            try {
                if (this.i != null) {
                    this.i.unregisterListener(this);
                    this.i = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.g = false;
        }
    }

    public float d() {
        float fFloatValue;
        synchronized (this.f) {
            if (Math.abs(((int) (System.currentTimeMillis() / 1000)) - this.d) > 5 || this.f.size() <= 0) {
                fFloatValue = 0.0f;
            } else {
                try {
                    fFloatValue = this.f.get(this.f.size() - 1).floatValue();
                } catch (Throwable th) {
                    fFloatValue = 0.0f;
                }
            }
        }
        return fFloatValue;
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override // android.hardware.SensorEventListener
    @SuppressLint({"NewApi"})
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case 6:
                if (!this.g) {
                    return;
                }
                this.c = (float[]) sensorEvent.values.clone();
                int iCurrentTimeMillis = (int) (System.currentTimeMillis() / 1000);
                if (iCurrentTimeMillis == this.d) {
                    this.e.add(Float.valueOf(this.c[0]));
                    return;
                }
                this.d = iCurrentTimeMillis;
                if (this.e.size() <= 0) {
                    return;
                }
                int size = this.e.size();
                float fFloatValue = 0.0f;
                Iterator<Float> it = this.e.iterator();
                while (true) {
                    float f = fFloatValue;
                    if (!it.hasNext()) {
                        float f2 = f / size;
                        synchronized (this.f) {
                            try {
                                this.f.add(Float.valueOf(f2));
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.f.clear();
                            }
                            if (this.f.size() >= 4) {
                                this.f.remove(0);
                            }
                        }
                        this.e.clear();
                        return;
                    }
                    fFloatValue = it.next().floatValue() + f;
                }
            default:
                return;
        }
    }
}
