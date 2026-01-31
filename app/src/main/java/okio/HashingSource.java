package okio;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes.dex */
public final class HashingSource extends ForwardingSource {
    private final Mac mac;
    private final MessageDigest messageDigest;

    public static HashingSource md5(Source source) {
        return new HashingSource(source, "MD5");
    }

    public static HashingSource sha1(Source source) {
        return new HashingSource(source, "SHA-1");
    }

    public static HashingSource sha256(Source source) {
        return new HashingSource(source, "SHA-256");
    }

    public static HashingSource hmacSha1(Source source, ByteString key) {
        return new HashingSource(source, key, "HmacSHA1");
    }

    public static HashingSource hmacSha256(Source source, ByteString key) {
        return new HashingSource(source, key, "HmacSHA256");
    }

    private HashingSource(Source source, String algorithm) {
        super(source);
        try {
            this.messageDigest = MessageDigest.getInstance(algorithm);
            this.mac = null;
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError();
        }
    }

    private HashingSource(Source source, ByteString key, String algorithm) throws InvalidKeyException {
        super(source);
        try {
            this.mac = Mac.getInstance(algorithm);
            this.mac.init(new SecretKeySpec(key.toByteArray(), algorithm));
            this.messageDigest = null;
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchAlgorithmException e2) {
            throw new AssertionError();
        }
    }

    @Override // okio.ForwardingSource, okio.Source
    public long read(Buffer sink, long byteCount) throws IllegalStateException, IOException {
        long result = super.read(sink, byteCount);
        if (result != -1) {
            long start = sink.size - result;
            long offset = sink.size;
            Segment s = sink.head;
            while (offset > start) {
                s = s.prev;
                offset -= s.limit - s.pos;
            }
            while (offset < sink.size) {
                int pos = (int) ((s.pos + start) - offset);
                if (this.messageDigest != null) {
                    this.messageDigest.update(s.data, pos, s.limit - pos);
                } else {
                    this.mac.update(s.data, pos, s.limit - pos);
                }
                offset += s.limit - s.pos;
                start = offset;
                s = s.next;
            }
        }
        return result;
    }

    public ByteString hash() {
        byte[] result = this.messageDigest != null ? this.messageDigest.digest() : this.mac.doFinal();
        return ByteString.of(result);
    }
}
