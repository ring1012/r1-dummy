package com.unisound.common;

/* loaded from: classes.dex */
final class z implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f274a;

    z(String str) {
        this.f274a = str;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x00cc A[Catch: all -> 0x00a0, DONT_GENERATE, EDGE_INSN: B:38:0x00cc->B:32:0x00cc BREAK  A[LOOP:0: B:12:0x0025->B:20:0x003e], TRY_ENTER, TRY_LEAVE, TryCatch #0 {, blocks: (B:5:0x0007, B:7:0x0017, B:9:0x001d, B:12:0x0025, B:14:0x0028, B:16:0x002e, B:18:0x0036, B:21:0x0042, B:23:0x008d, B:30:0x00a3, B:26:0x009c, B:32:0x00cc), top: B:34:0x0007, inners: #1 }] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() {
        /*
            r13 = this;
            r0 = 0
            java.lang.Object r11 = com.unisound.common.y.b()
            monitor-enter(r11)
            r1 = 1
            com.unisound.common.y.a(r1)     // Catch: java.lang.Throwable -> La0
            java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> La0
            java.lang.String r2 = r13.f274a     // Catch: java.lang.Throwable -> La0
            r1.<init>(r2)     // Catch: java.lang.Throwable -> La0
            boolean r2 = r1.exists()     // Catch: java.lang.Throwable -> La0
            if (r2 == 0) goto Lcc
            boolean r2 = r1.isDirectory()     // Catch: java.lang.Throwable -> La0
            if (r2 == 0) goto Lcc
            java.io.File[] r12 = r1.listFiles()     // Catch: java.lang.Throwable -> La0
            int r1 = r12.length     // Catch: java.lang.Throwable -> La0
            if (r1 <= 0) goto Lcc
            r10 = r0
        L25:
            int r0 = r12.length     // Catch: java.lang.Throwable -> La0
            if (r10 >= r0) goto Lcc
            boolean r0 = com.unisound.common.y.c()     // Catch: java.lang.Throwable -> La0
            if (r0 == 0) goto L3e
            r0 = r12[r10]     // Catch: java.lang.Throwable -> La0
            java.lang.String r0 = com.unisound.common.y.a(r0)     // Catch: java.lang.Throwable -> La0
            if (r0 == 0) goto L3e
            java.lang.String r1 = ""
            boolean r1 = r0.equals(r1)     // Catch: java.lang.Throwable -> La0
            if (r1 == 0) goto L42
        L3e:
            int r0 = r10 + 1
            r10 = r0
            goto L25
        L42:
            org.json.JSONObject r9 = new org.json.JSONObject     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            r9.<init>(r0)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            java.lang.String r0 = "appKey"
            java.lang.String r1 = r9.getString(r0)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            java.lang.String r0 = "imei"
            java.lang.String r2 = r9.getString(r0)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            java.lang.String r0 = "sessionID"
            java.lang.String r3 = r9.getString(r0)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            java.lang.String r0 = "timeStamp"
            java.lang.String r4 = r9.getString(r0)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            java.lang.String r0 = "packageName"
            java.lang.String r5 = r9.getString(r0)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            java.lang.String r0 = "status"
            int r6 = r9.getInt(r0)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            java.lang.String r0 = "errString"
            java.lang.String r7 = r9.getString(r0)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            java.lang.String r0 = "javaSDKLog"
            java.lang.String r8 = r9.getString(r0)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            java.lang.String r0 = "cSDKLog"
            java.lang.String r9 = r9.getString(r0)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            com.unisound.common.x r0 = new com.unisound.common.x     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            com.unisound.common.w r1 = new com.unisound.common.w     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            r1.<init>()     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            int r0 = r1.a(r0)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            if (r0 != 0) goto La3
            r0 = 0
            com.unisound.common.y.a(r0)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            r0 = r12[r10]     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            r0.delete()     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            r0 = 1
            com.unisound.common.y.a(r0)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            goto L3e
        L9b:
            r0 = move-exception
            r0.printStackTrace()     // Catch: java.lang.Throwable -> La0
            goto L3e
        La0:
            r0 = move-exception
            monitor-exit(r11)     // Catch: java.lang.Throwable -> La0
            throw r0
        La3:
            com.unisound.common.y.d()     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            r0.<init>()     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            java.lang.String r1 = "postLogError "
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            int r1 = com.unisound.common.y.e()     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            java.lang.String r1 = " times"
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            com.unisound.common.y.a(r0)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            r0 = 0
            com.unisound.common.y.a(r0)     // Catch: java.lang.Exception -> L9b java.lang.Throwable -> La0
            goto L3e
        Lcc:
            monitor-exit(r11)     // Catch: java.lang.Throwable -> La0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.common.z.run():void");
    }
}
