package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.Base64;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: classes.dex */
public final class JSONScanner extends JSONLexerBase {
    protected static final char[] typeFieldName = ("\"" + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();
    public final int ISO8601_LEN_0;
    public final int ISO8601_LEN_1;
    public final int ISO8601_LEN_2;
    private final String text;

    public JSONScanner(String input) {
        this(input, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(String input, int features) {
        this.ISO8601_LEN_0 = "0000-00-00".length();
        this.ISO8601_LEN_1 = "0000-00-00T00:00:00".length();
        this.ISO8601_LEN_2 = "0000-00-00T00:00:00.000".length();
        this.features = features;
        this.text = input;
        this.bp = -1;
        next();
        if (this.ch == 65279) {
            next();
        }
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final char charAt(int index) {
        if (index >= this.text.length()) {
            return (char) 26;
        }
        return this.text.charAt(index);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final char next() {
        int i = this.bp + 1;
        this.bp = i;
        char cCharAt = charAt(i);
        this.ch = cCharAt;
        return cCharAt;
    }

    public JSONScanner(char[] input, int inputLength) {
        this(input, inputLength, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(char[] input, int inputLength, int features) {
        this(new String(input, 0, inputLength), features);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    protected final void copyTo(int offset, int count, char[] dest) {
        this.text.getChars(offset, offset + count, dest, 0);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final int indexOf(char ch, int startIndex) {
        return this.text.indexOf(ch, startIndex);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final String addSymbol(int offset, int len, int hash, SymbolTable symbolTable) {
        return symbolTable.addSymbol(this.text, offset, len, hash);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public byte[] bytesValue() {
        return Base64.decodeFast(this.text, this.np + 1, this.sp);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final String stringVal() {
        return !this.hasSpecial ? subString(this.np + 1, this.sp) : new String(this.sbuf, 0, this.sp);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public final String subString(int offset, int count) {
        char[] chars = new char[count];
        for (int i = offset; i < offset + count; i++) {
            chars[i - offset] = this.text.charAt(i);
        }
        return new String(chars);
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase, com.alibaba.fastjson.parser.JSONLexer
    public final String numberString() {
        char chLocal = charAt((this.np + this.sp) - 1);
        int sp = this.sp;
        if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B' || chLocal == 'F' || chLocal == 'D') {
            sp--;
        }
        return subString(this.np, sp);
    }

    public boolean scanISO8601DateIfMatch() {
        return scanISO8601DateIfMatch(true);
    }

    public boolean scanISO8601DateIfMatch(boolean strict) throws NumberFormatException {
        int hour;
        int minute;
        int seconds;
        int millis;
        int rest = this.text.length() - this.bp;
        if (!strict && rest > 13) {
            char c0 = charAt(this.bp);
            char c1 = charAt(this.bp + 1);
            char c2 = charAt(this.bp + 2);
            char c3 = charAt(this.bp + 3);
            char c4 = charAt(this.bp + 4);
            char c5 = charAt(this.bp + 5);
            char c_r0 = charAt((this.bp + rest) - 1);
            char c_r1 = charAt((this.bp + rest) - 2);
            if (c0 == '/' && c1 == 'D' && c2 == 'a' && c3 == 't' && c4 == 'e' && c5 == '(' && c_r0 == '/' && c_r1 == ')') {
                int plusIndex = -1;
                for (int i = 6; i < rest; i++) {
                    char c = charAt(this.bp + i);
                    if (c == '+') {
                        plusIndex = i;
                    } else if (c < '0' || c > '9') {
                        break;
                    }
                }
                if (plusIndex == -1) {
                    return false;
                }
                int offset = this.bp + 6;
                String numberText = subString(offset, plusIndex - offset);
                long millis2 = Long.parseLong(numberText);
                Locale local = Locale.getDefault();
                this.calendar = Calendar.getInstance(TimeZone.getDefault(), local);
                this.calendar.setTimeInMillis(millis2);
                this.token = 5;
                return true;
            }
        }
        if (rest == 8 || rest == 14 || rest == 17) {
            if (strict) {
                return false;
            }
            char y0 = charAt(this.bp);
            char y1 = charAt(this.bp + 1);
            char y2 = charAt(this.bp + 2);
            char y3 = charAt(this.bp + 3);
            char M0 = charAt(this.bp + 4);
            char M1 = charAt(this.bp + 5);
            char d0 = charAt(this.bp + 6);
            char d1 = charAt(this.bp + 7);
            if (!checkDate(y0, y1, y2, y3, M0, M1, d0, d1)) {
                return false;
            }
            setCalendar(y0, y1, y2, y3, M0, M1, d0, d1);
            if (rest != 8) {
                char h0 = charAt(this.bp + 8);
                char h1 = charAt(this.bp + 9);
                char m0 = charAt(this.bp + 10);
                char m1 = charAt(this.bp + 11);
                char s0 = charAt(this.bp + 12);
                char s1 = charAt(this.bp + 13);
                if (!checkTime(h0, h1, m0, m1, s0, s1)) {
                    return false;
                }
                if (rest == 17) {
                    char S0 = charAt(this.bp + 14);
                    char S1 = charAt(this.bp + 15);
                    char S2 = charAt(this.bp + 16);
                    if (S0 < '0' || S0 > '9' || S1 < '0' || S1 > '9' || S2 < '0' || S2 > '9') {
                        return false;
                    }
                    millis = (digits[S0] * 100) + (digits[S1] * 10) + digits[S2];
                } else {
                    millis = 0;
                }
                hour = (digits[h0] * 10) + digits[h1];
                minute = (digits[m0] * 10) + digits[m1];
                seconds = (digits[s0] * 10) + digits[s1];
            } else {
                hour = 0;
                minute = 0;
                seconds = 0;
                millis = 0;
            }
            this.calendar.set(11, hour);
            this.calendar.set(12, minute);
            this.calendar.set(13, seconds);
            this.calendar.set(14, millis);
            this.token = 5;
            return true;
        }
        if (rest < this.ISO8601_LEN_0 || charAt(this.bp + 4) != '-' || charAt(this.bp + 7) != '-') {
            return false;
        }
        char y02 = charAt(this.bp);
        char y12 = charAt(this.bp + 1);
        char y22 = charAt(this.bp + 2);
        char y32 = charAt(this.bp + 3);
        char M02 = charAt(this.bp + 5);
        char M12 = charAt(this.bp + 6);
        char d02 = charAt(this.bp + 8);
        char d12 = charAt(this.bp + 9);
        if (!checkDate(y02, y12, y22, y32, M02, M12, d02, d12)) {
            return false;
        }
        setCalendar(y02, y12, y22, y32, M02, M12, d02, d12);
        char t = charAt(this.bp + 10);
        if (t == 'T' || (t == ' ' && !strict)) {
            if (rest < this.ISO8601_LEN_1 || charAt(this.bp + 13) != ':' || charAt(this.bp + 16) != ':') {
                return false;
            }
            char h02 = charAt(this.bp + 11);
            char h12 = charAt(this.bp + 12);
            char m02 = charAt(this.bp + 14);
            char m12 = charAt(this.bp + 15);
            char s02 = charAt(this.bp + 17);
            char s12 = charAt(this.bp + 18);
            if (!checkTime(h02, h12, m02, m12, s02, s12)) {
                return false;
            }
            int hour2 = (digits[h02] * 10) + digits[h12];
            int minute2 = (digits[m02] * 10) + digits[m12];
            int seconds2 = (digits[s02] * 10) + digits[s12];
            this.calendar.set(11, hour2);
            this.calendar.set(12, minute2);
            this.calendar.set(13, seconds2);
            char dot = charAt(this.bp + 19);
            if (dot == '.') {
                if (rest < this.ISO8601_LEN_2) {
                    return false;
                }
                char S02 = charAt(this.bp + 20);
                if (S02 < '0' || S02 > '9') {
                    return false;
                }
                int millis3 = digits[S02];
                int millisLen = 1;
                char S12 = charAt(this.bp + 21);
                if (S12 >= '0' && S12 <= '9') {
                    millis3 = (millis3 * 10) + digits[S12];
                    millisLen = 2;
                }
                if (millisLen == 2) {
                    char S22 = charAt(this.bp + 22);
                    if (S22 >= '0' && S22 <= '9') {
                        millis3 = (millis3 * 10) + digits[S22];
                        millisLen = 3;
                    }
                }
                this.calendar.set(14, millis3);
                int timzeZoneLength = 0;
                char timeZoneFlag = charAt(this.bp + 20 + millisLen);
                if (timeZoneFlag == '+' || timeZoneFlag == '-') {
                    char t0 = charAt(this.bp + 20 + millisLen + 1);
                    if (t0 < '0' || t0 > '1') {
                        return false;
                    }
                    char t1 = charAt(this.bp + 20 + millisLen + 2);
                    if (t1 < '0' || t1 > '9') {
                        return false;
                    }
                    char t2 = charAt(this.bp + 20 + millisLen + 3);
                    if (t2 == ':') {
                        char t3 = charAt(this.bp + 20 + millisLen + 4);
                        if (t3 != '0') {
                            return false;
                        }
                        char t4 = charAt(this.bp + 20 + millisLen + 5);
                        if (t4 != '0') {
                            return false;
                        }
                        timzeZoneLength = 6;
                    } else if (t2 == '0') {
                        char t32 = charAt(this.bp + 20 + millisLen + 4);
                        if (t32 != '0') {
                            return false;
                        }
                        timzeZoneLength = 5;
                    } else {
                        timzeZoneLength = 3;
                    }
                    int timeZoneOffset = ((digits[t0] * 10) + digits[t1]) * 3600 * 1000;
                    if (timeZoneFlag == '-') {
                        timeZoneOffset = -timeZoneOffset;
                    }
                    if (this.calendar.getTimeZone().getRawOffset() != timeZoneOffset) {
                        String[] timeZoneIDs = TimeZone.getAvailableIDs(timeZoneOffset);
                        if (timeZoneIDs.length > 0) {
                            TimeZone timeZone = TimeZone.getTimeZone(timeZoneIDs[0]);
                            this.calendar.setTimeZone(timeZone);
                        }
                    }
                }
                char end = charAt(this.bp + millisLen + 20 + timzeZoneLength);
                if (end != 26 && end != '\"') {
                    return false;
                }
                int i2 = this.bp + millisLen + 20 + timzeZoneLength;
                this.bp = i2;
                this.ch = charAt(i2);
                this.token = 5;
                return true;
            }
            this.calendar.set(14, 0);
            int i3 = this.bp + 19;
            this.bp = i3;
            this.ch = charAt(i3);
            this.token = 5;
            return true;
        }
        if (t == '\"' || t == 26) {
            this.calendar.set(11, 0);
            this.calendar.set(12, 0);
            this.calendar.set(13, 0);
            this.calendar.set(14, 0);
            int i4 = this.bp + 10;
            this.bp = i4;
            this.ch = charAt(i4);
            this.token = 5;
            return true;
        }
        return false;
    }

    private boolean checkTime(char h0, char h1, char m0, char m1, char s0, char s1) {
        if (h0 == '0') {
            if (h1 < '0' || h1 > '9') {
                return false;
            }
        } else if (h0 == '1') {
            if (h1 < '0' || h1 > '9') {
                return false;
            }
        } else if (h0 != '2' || h1 < '0' || h1 > '4') {
            return false;
        }
        if (m0 >= '0' && m0 <= '5') {
            if (m1 < '0' || m1 > '9') {
                return false;
            }
        } else if (m0 != '6' || m1 != '0') {
            return false;
        }
        if (s0 >= '0' && s0 <= '5') {
            if (s1 < '0' || s1 > '9') {
                return false;
            }
        } else if (s0 != '6' || s1 != '0') {
            return false;
        }
        return true;
    }

    private void setCalendar(char y0, char y1, char y2, char y3, char M0, char M1, char d0, char d1) {
        Locale local = Locale.getDefault();
        this.calendar = Calendar.getInstance(TimeZone.getDefault(), local);
        int year = (digits[y0] * 1000) + (digits[y1] * 100) + (digits[y2] * 10) + digits[y3];
        int month = ((digits[M0] * 10) + digits[M1]) - 1;
        int day = (digits[d0] * 10) + digits[d1];
        this.calendar.set(1, year);
        this.calendar.set(2, month);
        this.calendar.set(5, day);
    }

    static boolean checkDate(char y0, char y1, char y2, char y3, char M0, char M1, int d0, int d1) {
        if ((y0 != '1' && y0 != '2') || y1 < '0' || y1 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9') {
            return false;
        }
        if (M0 == '0') {
            if (M1 < '1' || M1 > '9') {
                return false;
            }
        } else {
            if (M0 != '1') {
                return false;
            }
            if (M1 != '0' && M1 != '1' && M1 != '2') {
                return false;
            }
        }
        if (d0 == 48) {
            if (d1 < 49 || d1 > 57) {
                return false;
            }
        } else if (d0 == 49 || d0 == 50) {
            if (d1 < 48 || d1 > 57) {
                return false;
            }
        } else {
            if (d0 != 51) {
                return false;
            }
            if (d1 != 48 && d1 != 49) {
                return false;
            }
        }
        return true;
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    public boolean isEOF() {
        return this.bp == this.text.length() || (this.ch == 26 && this.bp + 1 == this.text.length());
    }

    @Override // com.alibaba.fastjson.parser.JSONLexerBase
    protected final void arrayCopy(int srcPos, char[] dest, int destPos, int length) {
        this.text.getChars(srcPos, srcPos + length, dest, destPos);
    }
}
