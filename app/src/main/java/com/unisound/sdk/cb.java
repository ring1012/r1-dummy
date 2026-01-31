package com.unisound.sdk;

import android.content.Context;
import android.os.Message;
import com.unisound.client.ErrorCode;
import com.unisound.client.TextUnderstanderListener;

/* loaded from: classes.dex */
public class cb extends com.unisound.common.ab {

    /* renamed from: a, reason: collision with root package name */
    private static final int f328a = 100;
    private static final int b = 101;
    private static final int c = 102;
    private TextUnderstanderListener d;
    private cm e;
    private cl f;
    private Context g;
    private ck h = new cc(this);

    protected cb(Context context, String str, String str2) {
        this.g = context;
        this.f = new cl(str, str2);
        this.f.f(com.unisound.c.a.s);
    }

    private void a(String str) {
        if (this.e != null) {
            this.e.c();
        }
        this.e = new cm(this.f);
        this.e.a(str);
        this.e.a(this.h);
        this.e.start();
    }

    protected void cancel() {
        if (this.e != null) {
            this.e.c();
            this.e = null;
        }
    }

    protected Object getOption(int i) {
        switch (i) {
            case 1021:
                return this.f.n();
            case 1022:
                return this.f.t();
            case 1023:
                return this.f.x();
            case 1024:
            case 1025:
            case 1026:
            case 1027:
            case 1028:
            case 1029:
            case 1034:
            case 1035:
            default:
                return null;
            case 1030:
                return this.f.i();
            case 1031:
                return this.f.j();
            case 1032:
                return this.f.m();
            case 1033:
                return this.f.f();
            case 1036:
                return com.unisound.c.a.a(this.f.b());
        }
    }

    @Override // com.unisound.common.ab, android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 100:
                String str = (String) message.obj;
                if (this.d != null) {
                    this.d.onResult(1000, str);
                    break;
                }
                break;
            case 101:
                if (this.d != null) {
                    this.d.onEvent(((Integer) message.obj).intValue());
                    break;
                }
                break;
            case 102:
                if (this.d != null) {
                    int iIntValue = ((Integer) message.obj).intValue();
                    this.d.onError(iIntValue, ErrorCode.toMessage(iIntValue));
                    break;
                }
                break;
        }
    }

    protected int init(String str) {
        try {
            com.unisound.c.a.a(this.g);
            return 0;
        } catch (Exception e) {
            if (this.d != null) {
                sendMessage(102, Integer.valueOf(ErrorCode.GENERAL_NO_READ_PHONE_STATE_PERMISSION));
            }
            e.printStackTrace();
            return 0;
        }
    }

    protected void setListener(TextUnderstanderListener textUnderstanderListener) {
        this.d = textUnderstanderListener;
    }

    protected void setOption(int i, Object obj) throws NumberFormatException {
        switch (i) {
            case 1021:
                try {
                    this.f.k((String) obj);
                    break;
                } catch (Exception e) {
                    com.unisound.common.y.a("set nlu_scenario Error.");
                    return;
                }
            case 1022:
                try {
                    String str = (String) obj;
                    if (str == null || !str.contains(":")) {
                        com.unisound.common.y.a("nlu server set Error.");
                    } else {
                        String[] strArrSplit = str.split(":");
                        String str2 = strArrSplit[0];
                        try {
                            this.f.a(str2, Integer.parseInt(strArrSplit[1]));
                        } catch (NumberFormatException e2) {
                            com.unisound.common.y.a("nlu server set Error.");
                        }
                    }
                    break;
                } catch (Exception e3) {
                    com.unisound.common.y.a("set nlu_server_address Error.");
                    return;
                }
                break;
            case 1023:
                try {
                    this.f.a((cl) obj);
                    break;
                } catch (Exception e4) {
                    com.unisound.common.y.a("set nlu_params Error.");
                    return;
                }
            case 1024:
                try {
                    this.f.d(String.valueOf(obj));
                    break;
                } catch (Exception e5) {
                    com.unisound.common.y.a("set nlu_ver Error.");
                    return;
                }
            case 1025:
                try {
                    this.f.f(String.valueOf(obj));
                    break;
                } catch (Exception e6) {
                    com.unisound.common.y.a("set nlu_appver Error.");
                    return;
                }
            case 1030:
                try {
                    this.f.h((String) obj);
                    break;
                } catch (Exception e7) {
                    com.unisound.common.y.a("set history Error.");
                    return;
                }
            case 1031:
                try {
                    this.f.i((String) obj);
                    break;
                } catch (Exception e8) {
                    com.unisound.common.y.a("set city Error.");
                    return;
                }
            case 1032:
                try {
                    this.f.j((String) obj);
                    break;
                } catch (Exception e9) {
                    com.unisound.common.y.a("set voiceID Error.");
                    return;
                }
            case 1033:
                try {
                    this.f.e((String) obj);
                    break;
                } catch (Exception e10) {
                    com.unisound.common.y.a("set gps Error.");
                    return;
                }
        }
    }

    protected void setText(String str) {
        if (str == null) {
            sendMessage(100, "");
        } else if (str.length() == 0) {
            sendMessage(100, "");
        } else {
            a(str);
        }
    }
}
