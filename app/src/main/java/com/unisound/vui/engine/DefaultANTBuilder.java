package com.unisound.vui.engine;

import android.content.Context;
import com.unisound.vui.auth.UNIOSCredentials;
import com.unisound.vui.c;
import com.unisound.vui.e;
import com.unisound.vui.engine.a;

/* loaded from: classes.dex */
public final class DefaultANTBuilder implements a {
    private final Context context;
    private final UNIOSCredentials credentials;

    public static final class Provider implements a.InterfaceC0011a {
        private final Context context;
        private final UNIOSCredentials credentials;

        public Provider(Context context, UNIOSCredentials credentials) {
            if (context == null) {
                throw new NullPointerException("context");
            }
            if (credentials == null) {
                throw new NullPointerException("credentials");
            }
            this.context = context;
            this.credentials = credentials;
        }

        @Override // com.unisound.vui.engine.a.InterfaceC0011a
        public Context context() {
            return this.context;
        }

        @Override // com.unisound.vui.engine.a.InterfaceC0011a
        public a getANTBuilder() {
            return new DefaultANTBuilder(this.context, this.credentials);
        }
    }

    public DefaultANTBuilder(Context context, UNIOSCredentials credentials) {
        this.context = context;
        this.credentials = credentials;
    }

    @Override // com.unisound.vui.engine.a
    public com.unisound.vui.a createASRManager() {
        return new com.unisound.vui.a(this.context, this.credentials.getAccessKey(), this.credentials.getSecretKey());
    }

    @Override // com.unisound.vui.engine.a
    public e createNluManager() {
        return new e(this.context, this.credentials.getAccessKey(), this.credentials.getSecretKey());
    }

    @Override // com.unisound.vui.engine.a
    public c createTTSManager() {
        return new c(this.context, this.credentials.getAccessKey(), this.credentials.getSecretKey());
    }
}
