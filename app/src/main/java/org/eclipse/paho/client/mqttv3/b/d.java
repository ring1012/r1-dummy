package org.eclipse.paho.client.mqttv3.b;

import java.io.File;
import java.io.FileFilter;

/* loaded from: classes.dex */
class d implements FileFilter {

    /* renamed from: a, reason: collision with root package name */
    final b f506a;

    d(b bVar) {
        this.f506a = bVar;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return file.getName().endsWith(".bup");
    }
}
