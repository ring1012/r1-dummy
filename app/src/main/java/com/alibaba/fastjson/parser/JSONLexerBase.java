package com.alibaba.fastjson.parser;

import android.support.v4.view.MotionEventCompat;
import cn.yunzhisheng.asr.JniUscClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.google.android.exoplayer2.C;
import com.kaolafm.sdk.client.BuildConfig;
import com.tencent.bugly.Bugly;
import com.unisound.b.g;
import com.unisound.common.ad;
import java.io.Closeable;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public abstract class JSONLexerBase implements JSONLexer, Closeable {
    private static final Map<String, Integer> DEFAULT_KEYWORDS;
    protected static final int INT_MULTMIN_RADIX_TEN = -214748364;
    protected static final int INT_N_MULTMAX_RADIX_TEN = -214748364;
    protected static final long MULTMIN_RADIX_TEN = -922337203685477580L;
    protected static final long N_MULTMAX_RADIX_TEN = -922337203685477580L;
    private static final ThreadLocal<SoftReference<char[]>> SBUF_REF_LOCAL;
    protected static final int[] digits;
    protected static final char[] typeFieldName;
    protected static boolean[] whitespaceFlags;
    protected int bp;
    protected char ch;
    protected int eofPos;
    protected boolean hasSpecial;
    protected int np;
    protected int pos;
    protected char[] sbuf;
    protected int sp;
    protected int token;
    protected int features = JSON.DEFAULT_PARSER_FEATURE;
    protected Calendar calendar = null;
    public int matchStat = 0;
    protected Map<String, Integer> keywods = DEFAULT_KEYWORDS;

    public abstract String addSymbol(int i, int i2, int i3, SymbolTable symbolTable);

    protected abstract void arrayCopy(int i, char[] cArr, int i2, int i3);

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract byte[] bytesValue();

    public abstract char charAt(int i);

    protected abstract void copyTo(int i, int i2, char[] cArr);

    public abstract int indexOf(char c, int i);

    public abstract boolean isEOF();

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract char next();

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract String numberString();

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public abstract String stringVal();

    public abstract String subString(int i, int i2);

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put(JniUscClient.az, 8);
        map.put("new", 9);
        map.put("true", 6);
        map.put(Bugly.SDK_IS_DEV, 7);
        map.put("undefined", 23);
        DEFAULT_KEYWORDS = map;
        SBUF_REF_LOCAL = new ThreadLocal<>();
        typeFieldName = ("\"" + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();
        whitespaceFlags = new boolean[256];
        whitespaceFlags[32] = true;
        whitespaceFlags[10] = true;
        whitespaceFlags[13] = true;
        whitespaceFlags[9] = true;
        whitespaceFlags[12] = true;
        whitespaceFlags[8] = true;
        digits = new int[103];
        for (int i = 48; i <= 57; i++) {
            digits[i] = i - 48;
        }
        for (int i2 = 97; i2 <= 102; i2++) {
            digits[i2] = (i2 - 97) + 10;
        }
        for (int i3 = 65; i3 <= 70; i3++) {
            digits[i3] = (i3 - 65) + 10;
        }
    }

    protected void lexError(String key, Object... args) {
        this.token = 1;
    }

    public JSONLexerBase() {
        SoftReference<char[]> sbufRef = SBUF_REF_LOCAL.get();
        if (sbufRef != null) {
            this.sbuf = sbufRef.get();
            SBUF_REF_LOCAL.set(null);
        }
        if (this.sbuf == null) {
            this.sbuf = new char[64];
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void nextToken() throws NumberFormatException {
        this.sp = 0;
        while (true) {
            this.pos = this.bp;
            if (this.ch == '\"') {
                scanString();
                return;
            }
            if (this.ch == ',') {
                next();
                this.token = 16;
                return;
            }
            if (this.ch >= '0' && this.ch <= '9') {
                scanNumber();
                return;
            }
            if (this.ch == '-') {
                scanNumber();
                return;
            }
            switch (this.ch) {
                case '\b':
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    next();
                case MotionEventCompat.AXIS_GENERIC_8 /* 39 */:
                    if (!isEnabled(Feature.AllowSingleQuotes)) {
                        throw new JSONException("Feature.AllowSingleQuotes is false");
                    }
                    scanStringSingleQuote();
                    return;
                case MotionEventCompat.AXIS_GENERIC_9 /* 40 */:
                    next();
                    this.token = 10;
                    return;
                case MotionEventCompat.AXIS_GENERIC_10 /* 41 */:
                    next();
                    this.token = 11;
                    return;
                case ':':
                    next();
                    this.token = 17;
                    return;
                case 'S':
                    scanSet();
                    return;
                case 'T':
                    scanTreeSet();
                    return;
                case '[':
                    next();
                    this.token = 14;
                    return;
                case ']':
                    next();
                    this.token = 15;
                    return;
                case 'f':
                    scanFalse();
                    return;
                case 'n':
                    scanNullOrNew();
                    return;
                case 't':
                    scanTrue();
                    return;
                case 'u':
                    scanUndefined();
                    return;
                case BuildConfig.VERSION_CODE /* 123 */:
                    next();
                    this.token = 12;
                    return;
                case '}':
                    next();
                    this.token = 13;
                    return;
                default:
                    if (isEOF()) {
                        if (this.token == 20) {
                            throw new JSONException("EOF error");
                        }
                        this.token = 20;
                        int i = this.eofPos;
                        this.bp = i;
                        this.pos = i;
                        return;
                    }
                    lexError("illegal.char", String.valueOf((int) this.ch));
                    next();
                    return;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:6:0x0016  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0116 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0030 A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    @Override // com.alibaba.fastjson.parser.JSONLexer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void nextToken(int r8) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 336
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.nextToken(int):void");
    }

    public final void nextIdent() throws NumberFormatException {
        while (isWhitespace(this.ch)) {
            next();
        }
        if (this.ch == '_' || Character.isLetter(this.ch)) {
            scanIdent();
        } else {
            nextToken();
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void nextTokenWithColon() throws NumberFormatException {
        nextTokenWithChar(':');
    }

    public final void nextTokenWithChar(char expect) throws NumberFormatException {
        this.sp = 0;
        while (this.ch != expect) {
            if (this.ch == ' ' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\f' || this.ch == '\b') {
                next();
            } else {
                throw new JSONException("not match " + expect + " - " + this.ch);
            }
        }
        next();
        nextToken();
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final int token() {
        return this.token;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String tokenName() {
        return JSONToken.name(this.token);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final int pos() {
        return this.pos;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final int getBufferPosition() {
        return this.bp;
    }

    public final String stringDefaultValue() {
        if (isEnabled(Feature.InitStringFieldAsEmpty)) {
            return "";
        }
        return null;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final Number integerValue() throws NumberFormatException {
        long limit;
        int i;
        long result = 0;
        boolean negative = false;
        if (this.np == -1) {
            this.np = 0;
        }
        int i2 = this.np;
        int max = this.np + this.sp;
        char type = ' ';
        switch (charAt(max - 1)) {
            case 'B':
                max--;
                type = 'B';
                break;
            case 'L':
                max--;
                type = 'L';
                break;
            case 'S':
                max--;
                type = 'S';
                break;
        }
        if (charAt(this.np) == '-') {
            negative = true;
            limit = Long.MIN_VALUE;
            i = i2 + 1;
        } else {
            limit = C.TIME_UNSET;
            i = i2;
        }
        long multmin = negative ? -922337203685477580L : -922337203685477580L;
        if (i < max) {
            result = -digits[charAt(i)];
            i++;
        }
        while (i < max) {
            int i3 = i + 1;
            int digit = digits[charAt(i)];
            if (result < multmin) {
                return new BigInteger(numberString());
            }
            long result2 = result * 10;
            if (result2 < digit + limit) {
                return new BigInteger(numberString());
            }
            result = result2 - digit;
            i = i3;
        }
        if (negative) {
            if (i <= this.np + 1) {
                throw new NumberFormatException(numberString());
            }
            if (result >= -2147483648L && type != 'L') {
                if (type == 'S') {
                    return Short.valueOf((short) result);
                }
                if (type == 'B') {
                    return Byte.valueOf((byte) result);
                }
                return Integer.valueOf((int) result);
            }
            return Long.valueOf(result);
        }
        long result3 = -result;
        if (result3 <= 2147483647L && type != 'L') {
            if (type == 'S') {
                return Short.valueOf((short) result3);
            }
            if (type == 'B') {
                return Byte.valueOf((byte) result3);
            }
            return Integer.valueOf((int) result3);
        }
        return Long.valueOf(result3);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void nextTokenWithColon(int expect) throws NumberFormatException {
        nextTokenWithChar(':');
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public float floatValue() {
        return Float.parseFloat(numberString());
    }

    public double doubleValue() {
        return Double.parseDouble(numberString());
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public void config(Feature feature, boolean state) {
        this.features = Feature.config(this.features, feature, state);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final boolean isEnabled(Feature feature) {
        return Feature.isEnabled(this.features, feature);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final char getCurrent() {
        return this.ch;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String scanSymbol(SymbolTable symbolTable) {
        skipWhitespace();
        if (this.ch == '\"') {
            return scanSymbol(symbolTable, '\"');
        }
        if (this.ch == '\'') {
            if (!isEnabled(Feature.AllowSingleQuotes)) {
                throw new JSONException("syntax error");
            }
            return scanSymbol(symbolTable, '\'');
        }
        if (this.ch == '}') {
            next();
            this.token = 13;
            return null;
        }
        if (this.ch == ',') {
            next();
            this.token = 16;
            return null;
        }
        if (this.ch == 26) {
            this.token = 20;
            return null;
        }
        if (!isEnabled(Feature.AllowUnQuotedFieldNames)) {
            throw new JSONException("syntax error");
        }
        return scanSymbolUnQuoted(symbolTable);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String scanSymbol(SymbolTable symbolTable, char quote) throws NumberFormatException {
        String value;
        int offset;
        int hash = 0;
        this.np = this.bp;
        this.sp = 0;
        boolean hasSpecial = false;
        while (true) {
            char chLocal = next();
            if (chLocal != quote) {
                if (chLocal == 26) {
                    throw new JSONException("unclosed.str");
                }
                if (chLocal == '\\') {
                    if (!hasSpecial) {
                        hasSpecial = true;
                        if (this.sp >= this.sbuf.length) {
                            int newCapcity = this.sbuf.length * 2;
                            if (this.sp > newCapcity) {
                                newCapcity = this.sp;
                            }
                            char[] newsbuf = new char[newCapcity];
                            System.arraycopy(this.sbuf, 0, newsbuf, 0, this.sbuf.length);
                            this.sbuf = newsbuf;
                        }
                        arrayCopy(this.np + 1, this.sbuf, 0, this.sp);
                    }
                    char chLocal2 = next();
                    switch (chLocal2) {
                        case '\"':
                            hash = (hash * 31) + 34;
                            putChar('\"');
                            break;
                        case MotionEventCompat.AXIS_GENERIC_8 /* 39 */:
                            hash = (hash * 31) + 39;
                            putChar('\'');
                            break;
                        case MotionEventCompat.AXIS_GENERIC_16 /* 47 */:
                            hash = (hash * 31) + 47;
                            putChar(IOUtils.DIR_SEPARATOR_UNIX);
                            break;
                        case '0':
                            hash = (hash * 31) + chLocal2;
                            putChar((char) 0);
                            break;
                        case '1':
                            hash = (hash * 31) + chLocal2;
                            putChar((char) 1);
                            break;
                        case '2':
                            hash = (hash * 31) + chLocal2;
                            putChar((char) 2);
                            break;
                        case '3':
                            hash = (hash * 31) + chLocal2;
                            putChar((char) 3);
                            break;
                        case '4':
                            hash = (hash * 31) + chLocal2;
                            putChar((char) 4);
                            break;
                        case '5':
                            hash = (hash * 31) + chLocal2;
                            putChar((char) 5);
                            break;
                        case '6':
                            hash = (hash * 31) + chLocal2;
                            putChar((char) 6);
                            break;
                        case '7':
                            hash = (hash * 31) + chLocal2;
                            putChar((char) 7);
                            break;
                        case 'F':
                        case 'f':
                            hash = (hash * 31) + 12;
                            putChar('\f');
                            break;
                        case '\\':
                            hash = (hash * 31) + 92;
                            putChar(IOUtils.DIR_SEPARATOR_WINDOWS);
                            break;
                        case 'b':
                            hash = (hash * 31) + 8;
                            putChar('\b');
                            break;
                        case 'n':
                            hash = (hash * 31) + 10;
                            putChar('\n');
                            break;
                        case ad.u /* 114 */:
                            hash = (hash * 31) + 13;
                            putChar('\r');
                            break;
                        case 't':
                            hash = (hash * 31) + 9;
                            putChar('\t');
                            break;
                        case 'u':
                            int val = Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16);
                            hash = (hash * 31) + val;
                            putChar((char) val);
                            break;
                        case 'v':
                            hash = (hash * 31) + 11;
                            putChar((char) 11);
                            break;
                        case g.g /* 120 */:
                            char x1 = next();
                            this.ch = x1;
                            char x2 = next();
                            this.ch = x2;
                            int x_val = (digits[x1] * 16) + digits[x2];
                            char x_char = (char) x_val;
                            hash = (hash * 31) + x_char;
                            putChar(x_char);
                            break;
                        default:
                            this.ch = chLocal2;
                            throw new JSONException("unclosed.str.lit");
                    }
                } else {
                    hash = (hash * 31) + chLocal;
                    if (!hasSpecial) {
                        this.sp++;
                    } else if (this.sp == this.sbuf.length) {
                        putChar(chLocal);
                    } else {
                        char[] cArr = this.sbuf;
                        int i = this.sp;
                        this.sp = i + 1;
                        cArr[i] = chLocal;
                    }
                }
            } else {
                this.token = 4;
                if (!hasSpecial) {
                    if (this.np == -1) {
                        offset = 0;
                    } else {
                        offset = this.np + 1;
                    }
                    value = addSymbol(offset, this.sp, hash, symbolTable);
                } else {
                    value = symbolTable.addSymbol(this.sbuf, 0, this.sp, hash);
                }
                this.sp = 0;
                next();
                return value;
            }
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void resetStringPosition() {
        this.sp = 0;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final String scanSymbolUnQuoted(SymbolTable symbolTable) {
        boolean[] firstIdentifierFlags = com.alibaba.fastjson.util.IOUtils.firstIdentifierFlags;
        char first = this.ch;
        boolean firstFlag = this.ch >= firstIdentifierFlags.length || firstIdentifierFlags[first];
        if (!firstFlag) {
            throw new JSONException("illegal identifier : " + this.ch);
        }
        boolean[] identifierFlags = com.alibaba.fastjson.util.IOUtils.identifierFlags;
        int hash = first;
        this.np = this.bp;
        this.sp = 1;
        while (true) {
            char chLocal = next();
            if (chLocal < identifierFlags.length && !identifierFlags[chLocal]) {
                break;
            }
            hash = (hash * 31) + chLocal;
            this.sp++;
        }
        this.ch = charAt(this.bp);
        this.token = 18;
        if (this.sp == 4 && hash == 3392903 && charAt(this.np) == 'n' && charAt(this.np + 1) == 'u' && charAt(this.np + 2) == 'l' && charAt(this.np + 3) == 'l') {
            return null;
        }
        return addSymbol(this.np, this.sp, hash, symbolTable);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void scanString() throws NumberFormatException {
        this.np = this.bp;
        this.hasSpecial = false;
        while (true) {
            char ch = next();
            if (ch != '\"') {
                if (ch == 26) {
                    throw new JSONException("unclosed string : " + ch);
                }
                if (ch == '\\') {
                    if (!this.hasSpecial) {
                        this.hasSpecial = true;
                        if (this.sp >= this.sbuf.length) {
                            int newCapcity = this.sbuf.length * 2;
                            if (this.sp > newCapcity) {
                                newCapcity = this.sp;
                            }
                            char[] newsbuf = new char[newCapcity];
                            System.arraycopy(this.sbuf, 0, newsbuf, 0, this.sbuf.length);
                            this.sbuf = newsbuf;
                        }
                        copyTo(this.np + 1, this.sp, this.sbuf);
                    }
                    char ch2 = next();
                    switch (ch2) {
                        case '\"':
                            putChar('\"');
                            break;
                        case MotionEventCompat.AXIS_GENERIC_8 /* 39 */:
                            putChar('\'');
                            break;
                        case MotionEventCompat.AXIS_GENERIC_16 /* 47 */:
                            putChar(IOUtils.DIR_SEPARATOR_UNIX);
                            break;
                        case '0':
                            putChar((char) 0);
                            break;
                        case '1':
                            putChar((char) 1);
                            break;
                        case '2':
                            putChar((char) 2);
                            break;
                        case '3':
                            putChar((char) 3);
                            break;
                        case '4':
                            putChar((char) 4);
                            break;
                        case '5':
                            putChar((char) 5);
                            break;
                        case '6':
                            putChar((char) 6);
                            break;
                        case '7':
                            putChar((char) 7);
                            break;
                        case 'F':
                        case 'f':
                            putChar('\f');
                            break;
                        case '\\':
                            putChar(IOUtils.DIR_SEPARATOR_WINDOWS);
                            break;
                        case 'b':
                            putChar('\b');
                            break;
                        case 'n':
                            putChar('\n');
                            break;
                        case ad.u /* 114 */:
                            putChar('\r');
                            break;
                        case 't':
                            putChar('\t');
                            break;
                        case 'u':
                            int val = Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16);
                            putChar((char) val);
                            break;
                        case 'v':
                            putChar((char) 11);
                            break;
                        case g.g /* 120 */:
                            int x_val = (digits[next()] * 16) + digits[next()];
                            char x_char = (char) x_val;
                            putChar(x_char);
                            break;
                        default:
                            this.ch = ch2;
                            throw new JSONException("unclosed string : " + ch2);
                    }
                } else if (!this.hasSpecial) {
                    this.sp++;
                } else if (this.sp == this.sbuf.length) {
                    putChar(ch);
                } else {
                    char[] cArr = this.sbuf;
                    int i = this.sp;
                    this.sp = i + 1;
                    cArr[i] = ch;
                }
            } else {
                this.token = 4;
                this.ch = next();
                return;
            }
        }
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final int intValue() {
        int limit;
        int i;
        int i2;
        if (this.np == -1) {
            this.np = 0;
        }
        int result = 0;
        boolean negative = false;
        int i3 = this.np;
        int max = this.np + this.sp;
        if (charAt(this.np) == '-') {
            negative = true;
            limit = Integer.MIN_VALUE;
            i = i3 + 1;
        } else {
            limit = -2147483647;
            i = i3;
        }
        if (negative) {
        }
        if (i < max) {
            result = -digits[charAt(i)];
            i++;
        }
        while (true) {
            if (i >= max) {
                i2 = i;
                break;
            }
            i2 = i + 1;
            char chLocal = charAt(i);
            if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B') {
                break;
            }
            int digit = digits[chLocal];
            if (result < -214748364) {
                throw new NumberFormatException(numberString());
            }
            int result2 = result * 10;
            if (result2 < limit + digit) {
                throw new NumberFormatException(numberString());
            }
            result = result2 - digit;
            i = i2;
        }
        if (negative) {
            if (i2 <= this.np + 1) {
                throw new NumberFormatException(numberString());
            }
            return result;
        }
        return -result;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.sbuf.length <= 8192) {
            SBUF_REF_LOCAL.set(new SoftReference<>(this.sbuf));
        }
        this.sbuf = null;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final boolean isRef() {
        return this.sp == 4 && charAt(this.np + 1) == '$' && charAt(this.np + 2) == 'r' && charAt(this.np + 3) == 'e' && charAt(this.np + 4) == 'f';
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public String scanString(char expectNextChar) {
        this.matchStat = 0;
        int offset = 0 + 1;
        char chLocal = charAt(this.bp + 0);
        if (chLocal == 'n') {
            if (charAt(this.bp + 1) == 'u' && charAt(this.bp + 1 + 1) == 'l' && charAt(this.bp + 1 + 2) == 'l') {
                int i = offset + 3 + 1;
                if (charAt(this.bp + 4) == expectNextChar) {
                    this.bp += 4;
                    next();
                    this.matchStat = 3;
                    return null;
                }
                this.matchStat = -1;
                return null;
            }
            this.matchStat = -1;
            return null;
        }
        if (chLocal != '\"') {
            this.matchStat = -1;
            return stringDefaultValue();
        }
        boolean hasSpecial = false;
        int startIndex = this.bp + 1;
        int endIndex = indexOf('\"', startIndex);
        if (endIndex == -1) {
            throw new JSONException("unclosed str");
        }
        String stringVal = subString(this.bp + 1, endIndex - startIndex);
        int i2 = this.bp + 1;
        while (true) {
            if (i2 >= endIndex) {
                break;
            }
            if (charAt(i2) != '\\') {
                i2++;
            } else {
                hasSpecial = true;
                break;
            }
        }
        if (hasSpecial) {
            this.matchStat = -1;
            return stringDefaultValue();
        }
        int offset2 = (endIndex - (this.bp + 1)) + 1 + 1;
        int offset3 = offset2 + 1;
        if (charAt(this.bp + offset2) == expectNextChar) {
            this.bp += offset3 - 1;
            next();
            this.matchStat = 3;
            return stringVal;
        }
        this.matchStat = -1;
        return stringVal;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public Enum<?> scanEnum(Class<?> enumClass, SymbolTable symbolTable, char serperator) {
        String name = scanSymbolWithSeperator(symbolTable, serperator);
        if (name == null) {
            return null;
        }
        return Enum.valueOf(enumClass, name);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public String scanSymbolWithSeperator(SymbolTable symbolTable, char serperator) {
        String strAddSymbol = null;
        this.matchStat = 0;
        int offset = 0 + 1;
        char chLocal = charAt(this.bp + 0);
        if (chLocal == 'n') {
            if (charAt(this.bp + 1) == 'u' && charAt(this.bp + 1 + 1) == 'l' && charAt(this.bp + 1 + 2) == 'l') {
                int i = offset + 3 + 1;
                if (charAt(this.bp + 4) == serperator) {
                    this.bp += 4;
                    next();
                    this.matchStat = 3;
                } else {
                    this.matchStat = -1;
                }
            } else {
                this.matchStat = -1;
            }
        } else if (chLocal != '\"') {
            this.matchStat = -1;
        } else {
            int hash = 0;
            while (true) {
                int offset2 = offset;
                offset = offset2 + 1;
                char chLocal2 = charAt(this.bp + offset2);
                if (chLocal2 == '\"') {
                    int start = this.bp + 0 + 1;
                    int len = ((this.bp + offset) - start) - 1;
                    strAddSymbol = addSymbol(start, len, hash, symbolTable);
                    int offset3 = offset + 1;
                    if (charAt(this.bp + offset) == serperator) {
                        this.bp += offset3 - 1;
                        next();
                        this.matchStat = 3;
                    } else {
                        this.matchStat = -1;
                    }
                } else {
                    hash = (hash * 31) + chLocal2;
                    if (chLocal2 == '\\') {
                        this.matchStat = -1;
                        break;
                    }
                }
            }
        }
        return strAddSymbol;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public int scanInt(char expectNext) {
        char chLocal;
        this.matchStat = 0;
        int offset = 0 + 1;
        char chLocal2 = charAt(this.bp + 0);
        if (chLocal2 >= '0' && chLocal2 <= '9') {
            int value = digits[chLocal2];
            while (true) {
                int offset2 = offset;
                offset = offset2 + 1;
                chLocal = charAt(this.bp + offset2);
                if (chLocal < '0' || chLocal > '9') {
                    break;
                }
                value = (value * 10) + digits[chLocal];
            }
            if (chLocal == '.') {
                this.matchStat = -1;
                return 0;
            }
            if (value < 0) {
                this.matchStat = -1;
                return 0;
            }
            if (chLocal == expectNext) {
                this.bp += offset - 1;
                next();
                this.matchStat = 3;
                this.token = 16;
                return value;
            }
            this.matchStat = -1;
            return value;
        }
        this.matchStat = -1;
        return 0;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public long scanLong(char expectNextChar) {
        char chLocal;
        this.matchStat = 0;
        int offset = 0 + 1;
        char chLocal2 = charAt(this.bp + 0);
        if (chLocal2 >= '0' && chLocal2 <= '9') {
            long value = digits[chLocal2];
            while (true) {
                int offset2 = offset;
                offset = offset2 + 1;
                chLocal = charAt(this.bp + offset2);
                if (chLocal < '0' || chLocal > '9') {
                    break;
                }
                value = (10 * value) + digits[chLocal];
            }
            if (chLocal == '.') {
                this.matchStat = -1;
                return 0L;
            }
            if (value < 0) {
                this.matchStat = -1;
                return 0L;
            }
            if (chLocal == expectNextChar) {
                this.bp += offset - 1;
                next();
                this.matchStat = 3;
                this.token = 16;
                return value;
            }
            this.matchStat = -1;
            return value;
        }
        this.matchStat = -1;
        return 0L;
    }

    public final void scanTrue() {
        if (this.ch != 't') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'r') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'u') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == 26 || this.ch == '\f' || this.ch == '\b' || this.ch == ':') {
            this.token = 6;
            return;
        }
        throw new JSONException("scan true error");
    }

    public final void scanTreeSet() {
        if (this.ch != 'T') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'r') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'S') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 't') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch == ' ' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\f' || this.ch == '\b' || this.ch == '[' || this.ch == '(') {
            this.token = 22;
            return;
        }
        throw new JSONException("scan set error");
    }

    public final void scanNullOrNew() {
        if (this.ch != 'n') {
            throw new JSONException("error parse null or new");
        }
        next();
        if (this.ch == 'u') {
            next();
            if (this.ch != 'l') {
                throw new JSONException("error parse true");
            }
            next();
            if (this.ch != 'l') {
                throw new JSONException("error parse true");
            }
            next();
            if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == 26 || this.ch == '\f' || this.ch == '\b') {
                this.token = 8;
                return;
            }
            throw new JSONException("scan true error");
        }
        if (this.ch != 'e') {
            throw new JSONException("error parse e");
        }
        next();
        if (this.ch != 'w') {
            throw new JSONException("error parse w");
        }
        next();
        if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == 26 || this.ch == '\f' || this.ch == '\b') {
            this.token = 9;
            return;
        }
        throw new JSONException("scan true error");
    }

    public final void scanUndefined() {
        if (this.ch != 'u') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'n') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'd') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'f') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'i') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'n') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'd') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == 26 || this.ch == '\f' || this.ch == '\b') {
            this.token = 23;
            return;
        }
        throw new JSONException("scan false error");
    }

    public final void scanFalse() {
        if (this.ch != 'f') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'a') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'l') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 's') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == 26 || this.ch == '\f' || this.ch == '\b' || this.ch == ':') {
            this.token = 7;
            return;
        }
        throw new JSONException("scan false error");
    }

    public final void scanIdent() {
        this.np = this.bp - 1;
        this.hasSpecial = false;
        do {
            this.sp++;
            next();
        } while (Character.isLetterOrDigit(this.ch));
        String ident = stringVal();
        Integer tok = getKeyword(ident);
        if (tok != null) {
            this.token = tok.intValue();
        } else {
            this.token = 18;
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final boolean isBlankInput() {
        int i = 0;
        while (true) {
            char chLocal = charAt(i);
            if (chLocal != 26) {
                if (isWhitespace(chLocal)) {
                    i++;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void skipWhitespace() {
        while (this.ch < whitespaceFlags.length && whitespaceFlags[this.ch]) {
            next();
        }
    }

    private final void scanStringSingleQuote() throws NumberFormatException {
        this.np = this.bp;
        this.hasSpecial = false;
        while (true) {
            char chLocal = next();
            if (chLocal != '\'') {
                if (chLocal == 26) {
                    throw new JSONException("unclosed single-quote string");
                }
                if (chLocal == '\\') {
                    if (!this.hasSpecial) {
                        this.hasSpecial = true;
                        if (this.sp > this.sbuf.length) {
                            char[] newsbuf = new char[this.sp * 2];
                            System.arraycopy(this.sbuf, 0, newsbuf, 0, this.sbuf.length);
                            this.sbuf = newsbuf;
                        }
                        copyTo(this.np + 1, this.sp, this.sbuf);
                    }
                    char chLocal2 = next();
                    switch (chLocal2) {
                        case '\"':
                            putChar('\"');
                            break;
                        case MotionEventCompat.AXIS_GENERIC_8 /* 39 */:
                            putChar('\'');
                            break;
                        case MotionEventCompat.AXIS_GENERIC_16 /* 47 */:
                            putChar(IOUtils.DIR_SEPARATOR_UNIX);
                            break;
                        case '0':
                            putChar((char) 0);
                            break;
                        case '1':
                            putChar((char) 1);
                            break;
                        case '2':
                            putChar((char) 2);
                            break;
                        case '3':
                            putChar((char) 3);
                            break;
                        case '4':
                            putChar((char) 4);
                            break;
                        case '5':
                            putChar((char) 5);
                            break;
                        case '6':
                            putChar((char) 6);
                            break;
                        case '7':
                            putChar((char) 7);
                            break;
                        case 'F':
                        case 'f':
                            putChar('\f');
                            break;
                        case '\\':
                            putChar(IOUtils.DIR_SEPARATOR_WINDOWS);
                            break;
                        case 'b':
                            putChar('\b');
                            break;
                        case 'n':
                            putChar('\n');
                            break;
                        case ad.u /* 114 */:
                            putChar('\r');
                            break;
                        case 't':
                            putChar('\t');
                            break;
                        case 'u':
                            int val = Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16);
                            putChar((char) val);
                            break;
                        case 'v':
                            putChar((char) 11);
                            break;
                        case g.g /* 120 */:
                            int x_val = (digits[next()] * 16) + digits[next()];
                            char x_char = (char) x_val;
                            putChar(x_char);
                            break;
                        default:
                            this.ch = chLocal2;
                            throw new JSONException("unclosed single-quote string");
                    }
                } else if (!this.hasSpecial) {
                    this.sp++;
                } else if (this.sp == this.sbuf.length) {
                    putChar(chLocal);
                } else {
                    char[] cArr = this.sbuf;
                    int i = this.sp;
                    this.sp = i + 1;
                    cArr[i] = chLocal;
                }
            } else {
                this.token = 4;
                next();
                return;
            }
        }
    }

    public final void scanSet() {
        if (this.ch != 'S') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 't') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch == ' ' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\f' || this.ch == '\b' || this.ch == '[' || this.ch == '(') {
            this.token = 21;
            return;
        }
        throw new JSONException("scan set error");
    }

    protected final void putChar(char ch) {
        if (this.sp == this.sbuf.length) {
            char[] newsbuf = new char[this.sbuf.length * 2];
            System.arraycopy(this.sbuf, 0, newsbuf, 0, this.sbuf.length);
            this.sbuf = newsbuf;
        }
        char[] cArr = this.sbuf;
        int i = this.sp;
        this.sp = i + 1;
        cArr[i] = ch;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final void scanNumber() {
        this.np = this.bp;
        if (this.ch == '-') {
            this.sp++;
            next();
        }
        while (this.ch >= '0' && this.ch <= '9') {
            this.sp++;
            next();
        }
        boolean isDouble = false;
        if (this.ch == '.') {
            this.sp++;
            next();
            isDouble = true;
            while (this.ch >= '0' && this.ch <= '9') {
                this.sp++;
                next();
            }
        }
        if (this.ch == 'L' || this.ch == 'S' || this.ch == 'B') {
            this.sp++;
            next();
        } else if (this.ch == 'F' || this.ch == 'D') {
            this.sp++;
            next();
            isDouble = true;
        } else if (this.ch == 'e' || this.ch == 'E') {
            this.sp++;
            next();
            if (this.ch == '+' || this.ch == '-') {
                this.sp++;
                next();
            }
            while (this.ch >= '0' && this.ch <= '9') {
                this.sp++;
                next();
            }
            if (this.ch == 'D' || this.ch == 'F') {
                this.sp++;
                next();
            }
            isDouble = true;
        }
        if (isDouble) {
            this.token = 3;
        } else {
            this.token = 2;
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final long longValue() throws NumberFormatException {
        long limit;
        int i;
        int i2;
        long result = 0;
        boolean negative = false;
        int i3 = this.np;
        int max = this.np + this.sp;
        if (charAt(this.np) == '-') {
            negative = true;
            limit = Long.MIN_VALUE;
            i = i3 + 1;
        } else {
            limit = C.TIME_UNSET;
            i = i3;
        }
        if (negative) {
        }
        if (i < max) {
            result = -digits[charAt(i)];
            i++;
        }
        while (true) {
            if (i >= max) {
                i2 = i;
                break;
            }
            i2 = i + 1;
            char chLocal = charAt(i);
            if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B') {
                break;
            }
            int digit = digits[chLocal];
            if (result < -922337203685477580L) {
                throw new NumberFormatException(numberString());
            }
            long result2 = result * 10;
            if (result2 < digit + limit) {
                throw new NumberFormatException(numberString());
            }
            result = result2 - digit;
            i = i2;
        }
        if (negative) {
            if (i2 <= this.np + 1) {
                throw new NumberFormatException(numberString());
            }
            return result;
        }
        return -result;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final Number decimalValue(boolean decimal) {
        char chLocal = charAt((this.np + this.sp) - 1);
        if (chLocal == 'F') {
            return Float.valueOf(Float.parseFloat(numberString()));
        }
        if (chLocal == 'D') {
            return Double.valueOf(Double.parseDouble(numberString()));
        }
        if (decimal) {
            return decimalValue();
        }
        return Double.valueOf(doubleValue());
    }

    @Override // com.alibaba.fastjson.parser.JSONLexer
    public final BigDecimal decimalValue() {
        return new BigDecimal(numberString());
    }

    public static final boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b';
    }

    public Integer getKeyword(String key) {
        return this.keywods.get(key);
    }
}
