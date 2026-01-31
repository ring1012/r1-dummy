package org.eclipse.paho.client.mqttv3.b;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttPersistable;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.a.m;
import org.eclipse.paho.client.mqttv3.a.p;

/* loaded from: classes.dex */
public class b implements MqttClientPersistence {

    /* renamed from: a, reason: collision with root package name */
    private static final String f505a = ".msg";
    private static final String b = ".bup";
    private static final String c = ".lck";
    private static final FilenameFilter g = new c();
    private File d;
    private File e;
    private m f;

    public b() {
        this(System.getProperty("user.dir"));
    }

    public b(String str) {
        this.e = null;
        this.f = null;
        this.d = new File(str);
    }

    private void a() throws MqttPersistenceException {
        if (this.e == null) {
            throw new MqttPersistenceException();
        }
    }

    private void a(File file) throws MqttPersistenceException {
        File[] fileArrListFiles = file.listFiles(new d(this));
        if (fileArrListFiles == null) {
            throw new MqttPersistenceException();
        }
        for (int i = 0; i < fileArrListFiles.length; i++) {
            File file2 = new File(file, fileArrListFiles[i].getName().substring(0, fileArrListFiles[i].getName().length() - b.length()));
            if (!fileArrListFiles[i].renameTo(file2)) {
                file2.delete();
                fileArrListFiles[i].renameTo(file2);
            }
        }
    }

    private boolean a(char c2) {
        return Character.isJavaIdentifierPart(c2) || c2 == '-';
    }

    private File[] b() throws MqttPersistenceException {
        a();
        File[] fileArrListFiles = this.e.listFiles(g);
        if (fileArrListFiles == null) {
            throw new MqttPersistenceException();
        }
        return fileArrListFiles;
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public void clear() throws MqttPersistenceException {
        a();
        for (File file : b()) {
            file.delete();
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public void close() {
        synchronized (this) {
            if (this.f != null) {
                this.f.a();
            }
            if (b().length == 0) {
                this.e.delete();
            }
            this.e = null;
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public boolean containsKey(String str) throws MqttPersistenceException {
        a();
        return new File(this.e, new StringBuffer(String.valueOf(str)).append(f505a).toString()).exists();
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public MqttPersistable get(String str) throws IOException, MqttPersistenceException {
        a();
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(this.e, new StringBuffer(String.valueOf(str)).append(f505a).toString()));
            int iAvailable = fileInputStream.available();
            byte[] bArr = new byte[iAvailable];
            for (int i = 0; i < iAvailable; i += fileInputStream.read(bArr, i, iAvailable - i)) {
            }
            fileInputStream.close();
            return new p(str, bArr, 0, bArr.length, null, 0, 0);
        } catch (IOException e) {
            throw new MqttPersistenceException(e);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public Enumeration keys() throws MqttPersistenceException {
        a();
        File[] fileArrB = b();
        Vector vector = new Vector(fileArrB.length);
        for (File file : fileArrB) {
            String name = file.getName();
            vector.addElement(name.substring(0, name.length() - f505a.length()));
        }
        return vector.elements();
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public void open(String str, String str2) throws MqttPersistenceException {
        if (this.d.exists() && !this.d.isDirectory()) {
            throw new MqttPersistenceException();
        }
        if (!this.d.exists() && !this.d.mkdirs()) {
            throw new MqttPersistenceException();
        }
        if (!this.d.canWrite()) {
            throw new MqttPersistenceException();
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (a(cCharAt)) {
                stringBuffer.append(cCharAt);
            }
        }
        stringBuffer.append("-");
        for (int i2 = 0; i2 < str2.length(); i2++) {
            char cCharAt2 = str2.charAt(i2);
            if (a(cCharAt2)) {
                stringBuffer.append(cCharAt2);
            }
        }
        synchronized (this) {
            if (this.e == null) {
                this.e = new File(this.d, stringBuffer.toString());
                if (!this.e.exists()) {
                    this.e.mkdir();
                }
            }
            try {
                this.f = new m(this.e, c);
                a(this.e);
            } catch (Exception e) {
                throw new MqttPersistenceException(32200);
            }
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public void put(String str, MqttPersistable mqttPersistable) throws MqttPersistenceException {
        a();
        File file = new File(this.e, new StringBuffer(String.valueOf(str)).append(f505a).toString());
        File file2 = new File(this.e, new StringBuffer(String.valueOf(str)).append(f505a).append(b).toString());
        if (file.exists() && !file.renameTo(file2)) {
            file2.delete();
            file.renameTo(file2);
        }
        try {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(mqttPersistable.getHeaderBytes(), mqttPersistable.getHeaderOffset(), mqttPersistable.getHeaderLength());
                if (mqttPersistable.getPayloadBytes() != null) {
                    fileOutputStream.write(mqttPersistable.getPayloadBytes(), mqttPersistable.getPayloadOffset(), mqttPersistable.getPayloadLength());
                }
                fileOutputStream.getFD().sync();
                fileOutputStream.close();
                if (file2.exists()) {
                    file2.delete();
                }
            } catch (IOException e) {
                throw new MqttPersistenceException(e);
            }
        } finally {
            if (file2.exists() && !file2.renameTo(file)) {
                file.delete();
                file2.renameTo(file);
            }
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public void remove(String str) throws MqttPersistenceException {
        a();
        File file = new File(this.e, new StringBuffer(String.valueOf(str)).append(f505a).toString());
        if (file.exists()) {
            file.delete();
        }
    }
}
