package com.baidu.location.a;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/* loaded from: classes.dex */
public class m implements SensorEventListener {
    private static m c;

    /* renamed from: a, reason: collision with root package name */
    private float[] f76a;
    private SensorManager b;
    private float d;
    private boolean e = false;
    private boolean f = false;
    private boolean g = false;

    private m() {
    }

    public static synchronized m a() {
        if (c == null) {
            c = new m();
        }
        return c;
    }

    public void a(boolean z) {
        this.e = z;
    }

    public synchronized void b() {
        Sensor defaultSensor;
        if (!this.g && this.e) {
            if (this.b == null) {
                this.b = (SensorManager) com.baidu.location.f.b().getSystemService("sensor");
            }
            if (this.b != null && (defaultSensor = this.b.getDefaultSensor(11)) != null && this.e) {
                this.b.registerListener(this, defaultSensor, 3);
            }
            this.g = true;
        }
    }

    public synchronized void c() {
        if (this.g) {
            if (this.b != null) {
                this.b.unregisterListener(this);
                this.b = null;
            }
            this.g = false;
        }
    }

    public boolean d() {
        return this.e;
    }

    public float e() {
        return this.d;
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override // android.hardware.SensorEventListener
    @SuppressLint({"NewApi"})
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case 11:
                this.f76a = (float[]) sensorEvent.values.clone();
                if (this.f76a != null) {
                    float[] fArr = new float[9];
                    try {
                        SensorManager.getRotationMatrixFromVector(fArr, this.f76a);
                        SensorManager.getOrientation(fArr, new float[3]);
                        this.d = (float) Math.toDegrees(r1[0]);
                        this.d = (float) Math.floor(this.d >= 0.0f ? this.d : this.d + 360.0f);
                        break;
                    } catch (Exception e) {
                        this.d = 0.0f;
                        return;
                    }
                }
                break;
        }
    }
}
