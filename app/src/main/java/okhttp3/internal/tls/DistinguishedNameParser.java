package okhttp3.internal.tls;

import android.support.v4.view.MotionEventCompat;
import cn.yunzhisheng.asr.JniUscClient;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import javax.security.auth.x500.X500Principal;

/* loaded from: classes.dex */
final class DistinguishedNameParser {
    private int beg;
    private char[] chars;
    private int cur;
    private final String dn;
    private int end;
    private final int length;
    private int pos;

    DistinguishedNameParser(X500Principal principal) {
        this.dn = principal.getName("RFC2253");
        this.length = this.dn.length();
    }

    private String nextAT() {
        while (this.pos < this.length && this.chars[this.pos] == ' ') {
            this.pos++;
        }
        if (this.pos == this.length) {
            return null;
        }
        this.beg = this.pos;
        this.pos++;
        while (this.pos < this.length && this.chars[this.pos] != '=' && this.chars[this.pos] != ' ') {
            this.pos++;
        }
        if (this.pos >= this.length) {
            throw new IllegalStateException("Unexpected end of DN: " + this.dn);
        }
        this.end = this.pos;
        if (this.chars[this.pos] == ' ') {
            while (this.pos < this.length && this.chars[this.pos] != '=' && this.chars[this.pos] == ' ') {
                this.pos++;
            }
            if (this.chars[this.pos] != '=' || this.pos == this.length) {
                throw new IllegalStateException("Unexpected end of DN: " + this.dn);
            }
        }
        this.pos++;
        while (this.pos < this.length && this.chars[this.pos] == ' ') {
            this.pos++;
        }
        if (this.end - this.beg > 4 && this.chars[this.beg + 3] == '.' && ((this.chars[this.beg] == 'O' || this.chars[this.beg] == 'o') && ((this.chars[this.beg + 1] == 'I' || this.chars[this.beg + 1] == 'i') && (this.chars[this.beg + 2] == 'D' || this.chars[this.beg + 2] == 'd')))) {
            this.beg += 4;
        }
        return new String(this.chars, this.beg, this.end - this.beg);
    }

    private String quotedAV() {
        this.pos++;
        this.beg = this.pos;
        this.end = this.beg;
        while (this.pos != this.length) {
            if (this.chars[this.pos] == '\"') {
                this.pos++;
                while (this.pos < this.length && this.chars[this.pos] == ' ') {
                    this.pos++;
                }
                return new String(this.chars, this.beg, this.end - this.beg);
            }
            if (this.chars[this.pos] == '\\') {
                this.chars[this.end] = getEscaped();
            } else {
                this.chars[this.end] = this.chars[this.pos];
            }
            this.pos++;
            this.end++;
        }
        throw new IllegalStateException("Unexpected end of DN: " + this.dn);
    }

    private String hexAV() {
        int hexLen;
        if (this.pos + 4 >= this.length) {
            throw new IllegalStateException("Unexpected end of DN: " + this.dn);
        }
        this.beg = this.pos;
        this.pos++;
        while (this.pos != this.length && this.chars[this.pos] != '+' && this.chars[this.pos] != ',' && this.chars[this.pos] != ';') {
            if (this.chars[this.pos] == ' ') {
                this.end = this.pos;
                this.pos++;
                while (this.pos < this.length && this.chars[this.pos] == ' ') {
                    this.pos++;
                }
                hexLen = this.end - this.beg;
                if (hexLen >= 5 || (hexLen & 1) == 0) {
                    throw new IllegalStateException("Unexpected end of DN: " + this.dn);
                }
                byte[] encoded = new byte[hexLen / 2];
                int p = this.beg + 1;
                for (int i = 0; i < encoded.length; i++) {
                    encoded[i] = (byte) getByte(p);
                    p += 2;
                }
                return new String(this.chars, this.beg, hexLen);
            }
            if (this.chars[this.pos] >= 'A' && this.chars[this.pos] <= 'F') {
                char[] cArr = this.chars;
                int i2 = this.pos;
                cArr[i2] = (char) (cArr[i2] + ' ');
            }
            this.pos++;
        }
        this.end = this.pos;
        hexLen = this.end - this.beg;
        if (hexLen >= 5) {
        }
        throw new IllegalStateException("Unexpected end of DN: " + this.dn);
    }

    private String escapedAV() {
        this.beg = this.pos;
        this.end = this.pos;
        while (this.pos < this.length) {
            switch (this.chars[this.pos]) {
                case ' ':
                    this.cur = this.end;
                    this.pos++;
                    char[] cArr = this.chars;
                    int i = this.end;
                    this.end = i + 1;
                    cArr[i] = ' ';
                    while (this.pos < this.length && this.chars[this.pos] == ' ') {
                        char[] cArr2 = this.chars;
                        int i2 = this.end;
                        this.end = i2 + 1;
                        cArr2[i2] = ' ';
                        this.pos++;
                    }
                    if (this.pos != this.length && this.chars[this.pos] != ',' && this.chars[this.pos] != '+' && this.chars[this.pos] != ';') {
                        break;
                    } else {
                        return new String(this.chars, this.beg, this.cur - this.beg);
                    }
                case MotionEventCompat.AXIS_GENERIC_12 /* 43 */:
                case MotionEventCompat.AXIS_GENERIC_13 /* 44 */:
                case ';':
                    return new String(this.chars, this.beg, this.end - this.beg);
                case '\\':
                    char[] cArr3 = this.chars;
                    int i3 = this.end;
                    this.end = i3 + 1;
                    cArr3[i3] = getEscaped();
                    this.pos++;
                    break;
                default:
                    char[] cArr4 = this.chars;
                    int i4 = this.end;
                    this.end = i4 + 1;
                    cArr4[i4] = this.chars[this.pos];
                    this.pos++;
                    break;
            }
        }
        return new String(this.chars, this.beg, this.end - this.beg);
    }

    private char getEscaped() {
        this.pos++;
        if (this.pos == this.length) {
            throw new IllegalStateException("Unexpected end of DN: " + this.dn);
        }
        switch (this.chars[this.pos]) {
            case ' ':
            case '\"':
            case '#':
            case MotionEventCompat.AXIS_GENERIC_6 /* 37 */:
            case MotionEventCompat.AXIS_GENERIC_11 /* 42 */:
            case MotionEventCompat.AXIS_GENERIC_12 /* 43 */:
            case MotionEventCompat.AXIS_GENERIC_13 /* 44 */:
            case ';':
            case '<':
            case JniUscClient.ax /* 61 */:
            case JniUscClient.ay /* 62 */:
            case '\\':
            case '_':
                return this.chars[this.pos];
            default:
                return getUTF8();
        }
    }

    private char getUTF8() {
        int count;
        int res;
        int res2 = getByte(this.pos);
        this.pos++;
        if (res2 < 128) {
            return (char) res2;
        }
        if (res2 < 192 || res2 > 247) {
            return '?';
        }
        if (res2 <= 223) {
            count = 1;
            res = res2 & 31;
        } else if (res2 <= 239) {
            count = 2;
            res = res2 & 15;
        } else {
            count = 3;
            res = res2 & 7;
        }
        for (int i = 0; i < count; i++) {
            this.pos++;
            if (this.pos == this.length || this.chars[this.pos] != '\\') {
                return '?';
            }
            this.pos++;
            int b = getByte(this.pos);
            this.pos++;
            if ((b & PsExtractor.AUDIO_STREAM) != 128) {
                return '?';
            }
            res = (res << 6) + (b & 63);
        }
        return (char) res;
    }

    private int getByte(int position) {
        int b1;
        int b2;
        if (position + 1 >= this.length) {
            throw new IllegalStateException("Malformed DN: " + this.dn);
        }
        char c = this.chars[position];
        if (c >= '0' && c <= '9') {
            b1 = c - '0';
        } else if (c >= 'a' && c <= 'f') {
            b1 = c - 'W';
        } else if (c >= 'A' && c <= 'F') {
            b1 = c - '7';
        } else {
            throw new IllegalStateException("Malformed DN: " + this.dn);
        }
        char c2 = this.chars[position + 1];
        if (c2 >= '0' && c2 <= '9') {
            b2 = c2 - '0';
        } else if (c2 >= 'a' && c2 <= 'f') {
            b2 = c2 - 'W';
        } else if (c2 >= 'A' && c2 <= 'F') {
            b2 = c2 - '7';
        } else {
            throw new IllegalStateException("Malformed DN: " + this.dn);
        }
        return (b1 << 4) + b2;
    }

    public String findMostSpecific(String attributeType) {
        this.pos = 0;
        this.beg = 0;
        this.end = 0;
        this.cur = 0;
        this.chars = this.dn.toCharArray();
        String attType = nextAT();
        if (attType == null) {
            return null;
        }
        do {
            String attValue = "";
            if (this.pos == this.length) {
                return null;
            }
            switch (this.chars[this.pos]) {
                case '\"':
                    attValue = quotedAV();
                    break;
                case '#':
                    attValue = hexAV();
                    break;
                case MotionEventCompat.AXIS_GENERIC_12 /* 43 */:
                case MotionEventCompat.AXIS_GENERIC_13 /* 44 */:
                case ';':
                    break;
                default:
                    attValue = escapedAV();
                    break;
            }
            if (!attributeType.equalsIgnoreCase(attType)) {
                if (this.pos >= this.length) {
                    return null;
                }
                if (this.chars[this.pos] != ',' && this.chars[this.pos] != ';' && this.chars[this.pos] != '+') {
                    throw new IllegalStateException("Malformed DN: " + this.dn);
                }
                this.pos++;
                attType = nextAT();
            } else {
                return attValue;
            }
        } while (attType != null);
        throw new IllegalStateException("Malformed DN: " + this.dn);
    }
}
