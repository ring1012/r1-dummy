package cn.yunzhisheng.asrfix;

import cn.yunzhisheng.common.PinyinConverter;
import com.unisound.b.g;
import com.unisound.client.ErrorCode;
import com.unisound.common.y;
import com.unisound.sdk.u;
import com.unisound.sdk.w;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes.dex */
public class JniAsrFix {
    private static JniAsrFix D = null;
    private static List<String> E = null;
    private static final int F = 0;
    private static final int G = -1;
    private static final int H = -2;
    private static final int I = -3;
    private static final int J = 100;
    private static final int K = 16;
    private static final int L = 500;
    private static final int M = 502;
    private static ArrayList<Integer> R = null;

    /* renamed from: a, reason: collision with root package name */
    public static final int f28a = 0;
    public static final int b = 1;
    public static final int c = 2;
    public static final int d = 3;
    public static final int e = -1;
    public static final int f = -2;
    public static final int g = -3;
    public static final int h = -4;
    public static final int i = -5;
    public static final int j = -6;
    public static final int k = -7;
    public static final int l = -8;
    public static final int m = -9;
    public static final int n = -11;
    public static final int o = -12;
    public static final int p = 0;
    public static final int q = 1;
    public static final int r = 2;
    public static final int s = 3;
    public static final int t = 4;
    public static final int u = 5;
    public static final int v = 6;
    public static final int w = 7;
    public static final int x = 8;
    public static final int y = 9;
    public static final int z = 22;
    private Object C = new Object();
    private boolean N = false;
    private int O = 1501;
    private u P = null;
    private int Q = 1;
    boolean A = false;
    private boolean S = true;
    private boolean T = false;
    protected BlockingQueue<String> B = new LinkedBlockingQueue();
    private boolean U = false;

    static {
        System.loadLibrary("asrfix");
    }

    private JniAsrFix() {
    }

    public static int a(int i2) {
        return (i2 <= -12 || i2 >= 0) ? i2 : i2 + ErrorCode.ASR_FIXENGINE_TRANS_ERROR;
    }

    public static JniAsrFix a() {
        if (D == null) {
            D = new JniAsrFix();
            E = new ArrayList();
            R = new ArrayList<>();
        }
        return D;
    }

    public static boolean a(String str) {
        return crcCheck(str) == 0;
    }

    private native int cancel();

    private native int check_wav_end();

    public static native int compileDecodeNet(String str, String str2);

    private static native int crcCheck(String str);

    private native int getOptionInt(int i2);

    private native String getOptionString(int i2, String str);

    private native String getResult();

    public static native String getVersion();

    private native int init(String str, String str2);

    private native int isEngineIdle();

    private native int isactive(byte[] bArr, int i2);

    private native int recognize(byte[] bArr, int i2);

    private native void release();

    /* JADX INFO: Access modifiers changed from: private */
    public native int reset(String str, String str2);

    private native String search(String str, String str2);

    private native int setActiveNet(int i2);

    private native int setOptionInt(int i2, int i3);

    private native int setOptionString(int i2, String str);

    private native int start(String str, int i2);

    private native int stop();

    private native void trackInfo(int i2);

    private void x() {
        if (this.U) {
            return;
        }
        a((byte[]) null, 0);
        g();
    }

    public int a(int i2, int i3) {
        if (!this.U) {
            return ErrorCode.ASR_SDK_FIX_RECOGNIZER_NO_INIT;
        }
        int optionInt = setOptionInt(i2, i3);
        return optionInt < 0 ? a(optionInt) : optionInt;
    }

    public int a(int i2, String str) {
        if (!this.U) {
            return ErrorCode.ASR_SDK_FIX_RECOGNIZER_NO_INIT;
        }
        int optionString = setOptionString(i2, str);
        return optionString < 0 ? a(optionString) : optionString;
    }

    public int a(long j2, int i2) {
        if (!this.U) {
            return ErrorCode.ASR_SDK_FIX_RECOGNIZER_NO_INIT;
        }
        int iGrammarCompilerSetOptionInt = grammarCompilerSetOptionInt(j2, 1000, i2);
        return iGrammarCompilerSetOptionInt < 0 ? a(iGrammarCompilerSetOptionInt) : iGrammarCompilerSetOptionInt;
    }

    public int a(long j2, String str, String str2) {
        if (E.contains(str)) {
            y.a("loadGompiledJsgf failed , the jsgf.dat of this grammarTag is already exists! The grammarTag is " + str);
            return -1;
        }
        int iLoadCompiledJsgf = loadCompiledJsgf(j2, str2);
        if (iLoadCompiledJsgf != 0) {
            return iLoadCompiledJsgf;
        }
        E.add(str);
        return iLoadCompiledJsgf;
    }

    public int a(long j2, String str, String str2, String str3) {
        int iCompileDynamicUserData;
        int i2 = 0;
        synchronized (D) {
            this.S = false;
            if (j2 == 0) {
                y.c("compile  compileDynamicUserData fail handle=0");
                iCompileDynamicUserData = ErrorCode.ASR_SDK_FIX_COMPILE_NO_INIT;
            } else {
                if (E.contains(str)) {
                    y.c("compileDynamicUserData : grammarDat is loaded so compile directly!");
                } else {
                    y.c("compileDynamicUserData  loadedGrammar = + ", str, " grammarPath= ", str2);
                    iCompileDynamicUserData = loadCompiledJsgf(j2, str2);
                    if (iCompileDynamicUserData == 0) {
                        E.add(str);
                        y.c("compileDynamicUserData loadCompiledJsgf success!");
                    } else {
                        y.a("compileDynamicUserData loadCompileJsgf error!  loadCompiledJsgfResult = " + iCompileDynamicUserData);
                    }
                }
                while (isEngineIdle() != 1 && i2 < 2000) {
                    try {
                        Thread.sleep(50L);
                        i2 += 50;
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
                iCompileDynamicUserData = compileDynamicUserData(j2, str3, str);
                if (iCompileDynamicUserData == 0) {
                    y.c("compileDynamicUserData : compile success! ", Integer.valueOf(i2));
                } else {
                    y.c("compileDynamicUserData : compile failed!", Integer.valueOf(i2));
                }
                this.S = true;
            }
        }
        return iCompileDynamicUserData;
    }

    public int a(String str, int i2) {
        x();
        if (!this.U) {
            return ErrorCode.ASR_SDK_FIX_RECOGNIZER_NO_INIT;
        }
        y.c("JniAsrFix : start_ -> recognizerStatus = ", Integer.valueOf(this.O));
        if (this.O != 1501) {
            return ErrorCode.ASR_FIXENGINE_UNKNOW_ERROR;
        }
        int iStart = start(str, i2);
        if (iStart == 0) {
            this.N = true;
            this.O = 1502;
            this.P.a(this.O);
        }
        return iStart < 0 ? a(iStart) : iStart;
    }

    public int a(String str, String str2, w wVar, String str3) {
        this.S = false;
        if (this.U) {
            y.c("Recognizer.loadModel queue add ", str, PinyinConverter.PINYIN_SEPARATOR, str2);
            this.B.add(str);
            this.B.add(str2);
            this.B.add(str3);
            if (!this.T) {
                new a(this, wVar).start();
            }
        } else {
            y.a("Recognizer.loadModel not init Error");
        }
        return 0;
    }

    public int a(String str, String str2, String str3, w wVar) {
        i();
        this.A = false;
        if (D != null) {
            D.a(Boolean.valueOf(wVar.aw()));
        }
        int iInit = init(str, str2);
        if (iInit == 0) {
            if (wVar.aN() != -1 && D != null) {
                D.j(wVar.aN());
            }
            this.O = 1501;
            this.U = true;
            if ("init_asr" == str3) {
                this.Q = getOptionInt(100);
                for (int i2 = 0; i2 < this.Q; i2++) {
                    int optionInt = setOptionInt(16, i2);
                    y.c("JniAsrFix ", "modelNum = ", Integer.valueOf(this.Q), ", modelId = ", Integer.valueOf(optionInt));
                    R.add(Integer.valueOf(optionInt));
                }
                wVar.a(R);
                if (this.Q < 2) {
                    int optionInt2 = setOptionInt(16, 0);
                    y.c("JniAsrFix :modelNum = ", Integer.valueOf(this.Q), ", defaulltModelId = ", Integer.valueOf(optionInt2));
                    wVar.E(optionInt2);
                    wVar.D(optionInt2);
                }
                this.P.a(1129, (int) System.currentTimeMillis());
            }
        } else {
            this.P.b(1300, ErrorCode.ASR_SDK_FIX_RECOGNIZER_INIT_ERROR);
        }
        return a(iInit);
    }

    public int a(String str, String str2, String str3, String str4, String str5, String str6) {
        y.c("compile  initUserDataCompiler");
        long jInitUserDataCompiler = initUserDataCompiler(str4);
        if (jInitUserDataCompiler == 0) {
            return ErrorCode.ASR_SDK_FIX_COMPILE_NO_INIT;
        }
        y.c("compile  compileUserData ===handle,inPartialFile, jsgf, szContent, netDat,outPartialFile", " = ", Long.valueOf(jInitUserDataCompiler), " , ", str, " , ", str2, " , ", str3, " , ", str5, " , ", str6);
        int iPartialCompileUserData = partialCompileUserData(jInitUserDataCompiler, str, str2, str3, str5, str6);
        y.c("compile  destroyUserDataCompiler");
        destroyUserDataCompiler(jInitUserDataCompiler);
        if (iPartialCompileUserData == 0) {
            y.c("compile  compileUserData ok");
            return iPartialCompileUserData;
        }
        if (iPartialCompileUserData == -10) {
            y.a("compile compileUserData partialfile error, autofix ok");
            return 0;
        }
        y.a("compile  compileUserData fail code = " + iPartialCompileUserData);
        return a(iPartialCompileUserData);
    }

    public int a(boolean z2) {
        return z2 ? setOptionInt(125, 0) : setOptionInt(125, 1);
    }

    public int a(byte[] bArr, int i2) {
        if (!this.U) {
            return ErrorCode.ASR_SDK_FIX_RECOGNIZER_NO_INIT;
        }
        int iIsactive = isactive(bArr, i2);
        return iIsactive < 0 ? a(iIsactive) : iIsactive;
    }

    public String a(long j2) {
        return this.U ? j2 == 0 ? "handle is 0" : grammarCompilerGetOptionString(j2, 1002) : "";
    }

    public String a(String str, String str2) {
        if (this.U) {
            return search(str, str2);
        }
        return null;
    }

    public void a(u uVar) {
        this.P = uVar;
    }

    public void a(Boolean bool) {
        if (bool.booleanValue()) {
            setOptionInt(12, 1);
        } else {
            setOptionInt(12, 0);
        }
    }

    public int b(int i2) {
        return t() ? setOptionInt(108, i2) : setOptionInt(17, i2);
    }

    public int b(long j2, int i2) {
        if (!this.U) {
            return ErrorCode.ASR_SDK_FIX_RECOGNIZER_NO_INIT;
        }
        int iGrammarCompilerSetOptionInt = grammarCompilerSetOptionInt(j2, 1001, i2);
        return iGrammarCompilerSetOptionInt < 0 ? a(iGrammarCompilerSetOptionInt) : iGrammarCompilerSetOptionInt;
    }

    public int b(boolean z2) {
        return z2 ? setOptionInt(126, 0) : setOptionInt(126, 1);
    }

    public int b(byte[] bArr, int i2) {
        return this.U ? recognize(bArr, i2) : ErrorCode.ASR_SDK_FIX_RECOGNIZER_NO_INIT;
    }

    public void b() {
        int iCancel = cancel();
        if (iCancel != 0) {
            y.c("JniAsrFix : cancel failed , result code = ", Integer.valueOf(iCancel));
        }
    }

    public int c(int i2) {
        return setOptionInt(18, i2 / 10);
    }

    public boolean c() {
        return this.A;
    }

    public native int compileDynamicUserData(long j2, String str, String str2);

    public native int compileUserData(long j2, String str, String str2, String str3);

    public int d(int i2) {
        return setOptionInt(g.g, i2);
    }

    public void d() {
        this.A = true;
    }

    public native void destroyUserDataCompiler(long j2);

    public int e() {
        if (!this.U) {
            return ErrorCode.ASR_SDK_FIX_RECOGNIZER_NO_INIT;
        }
        int iStop = stop();
        if (iStop == 0) {
            this.O = 1501;
            this.P.a(this.O);
            this.N = false;
        }
        return iStop < 0 ? a(iStop) : iStop;
    }

    public int e(int i2) {
        return setOptionInt(200, i2);
    }

    public int f(int i2) {
        return setOptionInt(201, i2);
    }

    public String f() {
        return this.U ? getResult() : "";
    }

    public int g() {
        if (!this.U) {
            return ErrorCode.ASR_SDK_FIX_RECOGNIZER_NO_INIT;
        }
        int iCancel = cancel();
        if (iCancel == 0) {
            this.O = 1501;
            this.P.a(this.O);
            this.N = false;
        }
        return iCancel < 0 ? a(iCancel) : iCancel;
    }

    public int g(int i2) {
        return setOptionInt(202, i2);
    }

    public native String getTagsInfo(long j2);

    public native String grammarCompilerGetOptionString(long j2, int i2);

    public native int grammarCompilerSetOptionInt(long j2, int i2, int i3);

    public int h(int i2) {
        return setActiveNet(i2);
    }

    public boolean h() {
        return this.U;
    }

    public int i(int i2) {
        return setOptionInt(22, i2);
    }

    public void i() {
        if (this.U) {
            y.c("do Release");
            release();
            E.clear();
            this.U = false;
            this.N = false;
            this.S = true;
        }
    }

    public native long initUserDataCompiler(String str);

    public void j(int i2) {
        setOptionInt(500, i2);
        setOptionInt(502, 1);
        y.c("JniAsrFix : setThreadNum num = ", Integer.valueOf(i2));
    }

    public boolean j() {
        return this.N;
    }

    public int k() {
        return this.Q;
    }

    public int l() {
        return getOptionInt(101);
    }

    public native int loadCompiledJsgf(long j2, String str);

    public native int loadGrammarStr(String str);

    public List<Integer> m() {
        return R;
    }

    public int n() {
        int optionInt = getOptionInt(103);
        y.c("getAuthorizedStaus = ", Integer.valueOf(optionInt));
        return optionInt;
    }

    public String o() {
        String optionString = getOptionString(104, "");
        y.c("getExpiryTime = ", optionString);
        return optionString;
    }

    public String p() {
        String optionString = getOptionString(105, "");
        y.c("getLimitPac = ", optionString);
        return optionString;
    }

    public native int partialCompileUserData(long j2, String str, String str2, String str3, String str4, String str5);

    public int q() {
        return getOptionInt(102);
    }

    public int r() {
        return getOptionInt(107);
    }

    public int s() {
        return getOptionInt(106);
    }

    public boolean t() {
        return getVersion().contains("V3.");
    }

    public int u() {
        return check_wav_end();
    }

    public native int unloadGrammar(String str);

    public void v() {
        E.clear();
    }
}
