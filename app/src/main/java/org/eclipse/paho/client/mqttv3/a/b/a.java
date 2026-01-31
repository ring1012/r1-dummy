package org.eclipse.paho.client.mqttv3.a.b;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class a extends InputStream {

    /* renamed from: a, reason: collision with root package name */
    private InputStream f472a;
    private int b = 0;

    public a(InputStream inputStream) {
        this.f472a = inputStream;
    }

    public int a() {
        return this.b;
    }

    public void b() {
        this.b = 0;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int i = this.f472a.read();
        if (i != -1) {
            this.b++;
        }
        return i;
    }
}
