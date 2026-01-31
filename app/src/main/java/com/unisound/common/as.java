package com.unisound.common;

import android.content.Context;
import android.media.MediaPlayer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class as {

    /* renamed from: a, reason: collision with root package name */
    private MediaPlayer f245a;
    private Context b;
    private MediaPlayer.OnCompletionListener c;
    private int d;
    private String e;

    public as(Context context) {
        this.b = context;
    }

    private void g() throws IllegalStateException, IOException, IllegalArgumentException {
        if (this.e != null) {
            this.f245a = new MediaPlayer();
            this.f245a.setDataSource(new FileInputStream(new File(this.e)).getFD());
            this.f245a.prepare();
        } else if (b() != 0) {
            this.f245a = MediaPlayer.create(this.b, b());
        }
    }

    public String a() {
        return this.e;
    }

    public void a(int i) {
        this.e = null;
        this.d = i;
    }

    public void a(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.c = onCompletionListener;
    }

    public void a(String str) {
        this.d = 0;
        this.e = str;
    }

    public int b() {
        return this.d;
    }

    public MediaPlayer.OnCompletionListener c() {
        return this.c;
    }

    public void d() throws IllegalStateException {
        e();
        try {
            g();
            this.f245a.setOnCompletionListener(c());
            this.f245a.start();
            this.f245a.setLooping(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void e() throws IllegalStateException {
        if (this.f245a != null) {
            this.f245a.stop();
            this.f245a.release();
            this.f245a = null;
        }
    }

    public boolean f() {
        if (this.f245a != null) {
            return this.f245a.isPlaying();
        }
        return false;
    }
}
