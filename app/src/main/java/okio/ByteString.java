package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.annotation.Nullable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes.dex */
public class ByteString implements Serializable, Comparable<ByteString> {
    private static final long serialVersionUID = 1;
    final byte[] data;
    transient int hashCode;
    transient String utf8;
    static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static final ByteString EMPTY = of(new byte[0]);

    ByteString(byte[] data) {
        this.data = data;
    }

    public static ByteString of(byte... data) {
        if (data == null) {
            throw new IllegalArgumentException("data == null");
        }
        return new ByteString((byte[]) data.clone());
    }

    public static ByteString of(byte[] data, int offset, int byteCount) {
        if (data == null) {
            throw new IllegalArgumentException("data == null");
        }
        Util.checkOffsetAndCount(data.length, offset, byteCount);
        byte[] copy = new byte[byteCount];
        System.arraycopy(data, offset, copy, 0, byteCount);
        return new ByteString(copy);
    }

    public static ByteString of(ByteBuffer data) {
        if (data == null) {
            throw new IllegalArgumentException("data == null");
        }
        byte[] copy = new byte[data.remaining()];
        data.get(copy);
        return new ByteString(copy);
    }

    public static ByteString encodeUtf8(String s) {
        if (s == null) {
            throw new IllegalArgumentException("s == null");
        }
        ByteString byteString = new ByteString(s.getBytes(Util.UTF_8));
        byteString.utf8 = s;
        return byteString;
    }

    public static ByteString encodeString(String s, Charset charset) {
        if (s == null) {
            throw new IllegalArgumentException("s == null");
        }
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        return new ByteString(s.getBytes(charset));
    }

    public String utf8() {
        String result = this.utf8;
        if (result != null) {
            return result;
        }
        String result2 = new String(this.data, Util.UTF_8);
        this.utf8 = result2;
        return result2;
    }

    public String string(Charset charset) {
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        return new String(this.data, charset);
    }

    public String base64() {
        return Base64.encode(this.data);
    }

    public ByteString md5() {
        return digest("MD5");
    }

    public ByteString sha1() {
        return digest("SHA-1");
    }

    public ByteString sha256() {
        return digest("SHA-256");
    }

    public ByteString sha512() {
        return digest("SHA-512");
    }

    private ByteString digest(String algorithm) {
        try {
            return of(MessageDigest.getInstance(algorithm).digest(this.data));
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    public ByteString hmacSha1(ByteString key) {
        return hmac("HmacSHA1", key);
    }

    public ByteString hmacSha256(ByteString key) {
        return hmac("HmacSHA256", key);
    }

    public ByteString hmacSha512(ByteString key) {
        return hmac("HmacSHA512", key);
    }

    private ByteString hmac(String algorithm, ByteString key) throws NoSuchAlgorithmException, InvalidKeyException {
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(key.toByteArray(), algorithm));
            return of(mac.doFinal(this.data));
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchAlgorithmException e2) {
            throw new AssertionError(e2);
        }
    }

    public String base64Url() {
        return Base64.encodeUrl(this.data);
    }

    @Nullable
    public static ByteString decodeBase64(String base64) {
        if (base64 == null) {
            throw new IllegalArgumentException("base64 == null");
        }
        byte[] decoded = Base64.decode(base64);
        if (decoded != null) {
            return new ByteString(decoded);
        }
        return null;
    }

    public String hex() {
        char[] result = new char[this.data.length * 2];
        int c = 0;
        for (byte b : this.data) {
            int c2 = c + 1;
            result[c] = HEX_DIGITS[(b >> 4) & 15];
            c = c2 + 1;
            result[c2] = HEX_DIGITS[b & 15];
        }
        return new String(result);
    }

    public static ByteString decodeHex(String hex) {
        if (hex == null) {
            throw new IllegalArgumentException("hex == null");
        }
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Unexpected hex string: " + hex);
        }
        byte[] result = new byte[hex.length() / 2];
        for (int i = 0; i < result.length; i++) {
            int d1 = decodeHexDigit(hex.charAt(i * 2)) << 4;
            int d2 = decodeHexDigit(hex.charAt((i * 2) + 1));
            result[i] = (byte) (d1 + d2);
        }
        return of(result);
    }

    private static int decodeHexDigit(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'a' && c <= 'f') {
            return (c - 'a') + 10;
        }
        if (c < 'A' || c > 'F') {
            throw new IllegalArgumentException("Unexpected hex digit: " + c);
        }
        return (c - 'A') + 10;
    }

    public static ByteString read(InputStream in, int byteCount) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("in == null");
        }
        if (byteCount < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + byteCount);
        }
        byte[] result = new byte[byteCount];
        int offset = 0;
        while (offset < byteCount) {
            int read = in.read(result, offset, byteCount - offset);
            if (read == -1) {
                throw new EOFException();
            }
            offset += read;
        }
        return new ByteString(result);
    }

    public ByteString toAsciiLowercase() {
        for (int i = 0; i < this.data.length; i++) {
            byte c = this.data[i];
            if (c >= 65 && c <= 90) {
                byte[] lowercase = (byte[]) this.data.clone();
                lowercase[i] = (byte) (c + 32);
                for (int i2 = i + 1; i2 < lowercase.length; i2++) {
                    byte c2 = lowercase[i2];
                    if (c2 >= 65 && c2 <= 90) {
                        lowercase[i2] = (byte) (c2 + 32);
                    }
                }
                return new ByteString(lowercase);
            }
        }
        return this;
    }

    public ByteString toAsciiUppercase() {
        for (int i = 0; i < this.data.length; i++) {
            byte c = this.data[i];
            if (c >= 97 && c <= 122) {
                byte[] lowercase = (byte[]) this.data.clone();
                lowercase[i] = (byte) (c - 32);
                for (int i2 = i + 1; i2 < lowercase.length; i2++) {
                    byte c2 = lowercase[i2];
                    if (c2 >= 97 && c2 <= 122) {
                        lowercase[i2] = (byte) (c2 - 32);
                    }
                }
                return new ByteString(lowercase);
            }
        }
        return this;
    }

    public ByteString substring(int beginIndex) {
        return substring(beginIndex, this.data.length);
    }

    public ByteString substring(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new IllegalArgumentException("beginIndex < 0");
        }
        if (endIndex > this.data.length) {
            throw new IllegalArgumentException("endIndex > length(" + this.data.length + ")");
        }
        int subLen = endIndex - beginIndex;
        if (subLen < 0) {
            throw new IllegalArgumentException("endIndex < beginIndex");
        }
        if (beginIndex != 0 || endIndex != this.data.length) {
            byte[] copy = new byte[subLen];
            System.arraycopy(this.data, beginIndex, copy, 0, subLen);
            return new ByteString(copy);
        }
        return this;
    }

    public byte getByte(int pos) {
        return this.data[pos];
    }

    public int size() {
        return this.data.length;
    }

    public byte[] toByteArray() {
        return (byte[]) this.data.clone();
    }

    byte[] internalArray() {
        return this.data;
    }

    public ByteBuffer asByteBuffer() {
        return ByteBuffer.wrap(this.data).asReadOnlyBuffer();
    }

    public void write(OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("out == null");
        }
        out.write(this.data);
    }

    void write(Buffer buffer) {
        buffer.write(this.data, 0, this.data.length);
    }

    public boolean rangeEquals(int offset, ByteString other, int otherOffset, int byteCount) {
        return other.rangeEquals(otherOffset, this.data, offset, byteCount);
    }

    public boolean rangeEquals(int offset, byte[] other, int otherOffset, int byteCount) {
        return offset >= 0 && offset <= this.data.length - byteCount && otherOffset >= 0 && otherOffset <= other.length - byteCount && Util.arrayRangeEquals(this.data, offset, other, otherOffset, byteCount);
    }

    public final boolean startsWith(ByteString prefix) {
        return rangeEquals(0, prefix, 0, prefix.size());
    }

    public final boolean startsWith(byte[] prefix) {
        return rangeEquals(0, prefix, 0, prefix.length);
    }

    public final boolean endsWith(ByteString suffix) {
        return rangeEquals(size() - suffix.size(), suffix, 0, suffix.size());
    }

    public final boolean endsWith(byte[] suffix) {
        return rangeEquals(size() - suffix.length, suffix, 0, suffix.length);
    }

    public final int indexOf(ByteString other) {
        return indexOf(other.internalArray(), 0);
    }

    public final int indexOf(ByteString other, int fromIndex) {
        return indexOf(other.internalArray(), fromIndex);
    }

    public final int indexOf(byte[] other) {
        return indexOf(other, 0);
    }

    public int indexOf(byte[] other, int fromIndex) {
        int limit = this.data.length - other.length;
        for (int i = Math.max(fromIndex, 0); i <= limit; i++) {
            if (Util.arrayRangeEquals(this.data, i, other, 0, other.length)) {
                return i;
            }
        }
        return -1;
    }

    public final int lastIndexOf(ByteString other) {
        return lastIndexOf(other.internalArray(), size());
    }

    public final int lastIndexOf(ByteString other, int fromIndex) {
        return lastIndexOf(other.internalArray(), fromIndex);
    }

    public final int lastIndexOf(byte[] other) {
        return lastIndexOf(other, size());
    }

    public int lastIndexOf(byte[] other, int fromIndex) {
        for (int i = Math.min(fromIndex, this.data.length - other.length); i >= 0; i--) {
            if (Util.arrayRangeEquals(this.data, i, other, 0, other.length)) {
                return i;
            }
        }
        return -1;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return (o instanceof ByteString) && ((ByteString) o).size() == this.data.length && ((ByteString) o).rangeEquals(0, this.data, 0, this.data.length);
    }

    public int hashCode() {
        int result = this.hashCode;
        if (result != 0) {
            return result;
        }
        int result2 = Arrays.hashCode(this.data);
        this.hashCode = result2;
        return result2;
    }

    @Override // java.lang.Comparable
    public int compareTo(ByteString byteString) {
        int sizeA = size();
        int sizeB = byteString.size();
        int size = Math.min(sizeA, sizeB);
        for (int i = 0; i < size; i++) {
            int byteA = getByte(i) & 255;
            int byteB = byteString.getByte(i) & 255;
            if (byteA != byteB) {
                return byteA < byteB ? -1 : 1;
            }
        }
        if (sizeA == sizeB) {
            return 0;
        }
        return sizeA >= sizeB ? 1 : -1;
    }

    public String toString() {
        if (this.data.length == 0) {
            return "[size=0]";
        }
        String text = utf8();
        int i = codePointIndexToCharIndex(text, 64);
        if (i == -1) {
            if (this.data.length <= 64) {
                return "[hex=" + hex() + "]";
            }
            return "[size=" + this.data.length + " hex=" + substring(0, 64).hex() + "…]";
        }
        String safeText = text.substring(0, i).replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r");
        if (i < text.length()) {
            return "[size=" + this.data.length + " text=" + safeText + "…]";
        }
        return "[text=" + safeText + "]";
    }

    static int codePointIndexToCharIndex(String s, int codePointCount) {
        int i = 0;
        int j = 0;
        int length = s.length();
        while (i < length) {
            if (j != codePointCount) {
                int c = s.codePointAt(i);
                if ((Character.isISOControl(c) && c != 10 && c != 13) || c == 65533) {
                    return -1;
                }
                j++;
                i += Character.charCount(c);
            } else {
                return i;
            }
        }
        int i2 = s.length();
        return i2;
    }

    private void readObject(ObjectInputStream in) throws IllegalAccessException, NoSuchFieldException, IOException, IllegalArgumentException {
        int dataLength = in.readInt();
        ByteString byteString = read(in, dataLength);
        try {
            Field field = ByteString.class.getDeclaredField("data");
            field.setAccessible(true);
            field.set(this, byteString.data);
        } catch (IllegalAccessException e) {
            throw new AssertionError();
        } catch (NoSuchFieldException e2) {
            throw new AssertionError();
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(this.data.length);
        out.write(this.data);
    }
}
