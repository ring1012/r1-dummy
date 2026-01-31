package okhttp3;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.ByteString;
import okio.Okio;
import okio.Source;

/* loaded from: classes.dex */
public abstract class RequestBody {
    @Nullable
    public abstract MediaType contentType();

    public abstract void writeTo(BufferedSink bufferedSink) throws IOException;

    public long contentLength() throws IOException {
        return -1L;
    }

    public static RequestBody create(@Nullable MediaType contentType, String content) {
        Charset charset = Util.UTF_8;
        if (contentType != null && (charset = contentType.charset()) == null) {
            charset = Util.UTF_8;
            contentType = MediaType.parse(contentType + "; charset=utf-8");
        }
        byte[] bytes = content.getBytes(charset);
        return create(contentType, bytes);
    }

    public static RequestBody create(@Nullable final MediaType contentType, final ByteString content) {
        return new RequestBody() { // from class: okhttp3.RequestBody.1
            @Override // okhttp3.RequestBody
            @Nullable
            public MediaType contentType() {
                return contentType;
            }

            @Override // okhttp3.RequestBody
            public long contentLength() throws IOException {
                return content.size();
            }

            @Override // okhttp3.RequestBody
            public void writeTo(BufferedSink sink) throws IOException {
                sink.write(content);
            }
        };
    }

    public static RequestBody create(@Nullable MediaType contentType, byte[] content) {
        return create(contentType, content, 0, content.length);
    }

    public static RequestBody create(@Nullable final MediaType contentType, final byte[] content, final int offset, final int byteCount) {
        if (content == null) {
            throw new NullPointerException("content == null");
        }
        Util.checkOffsetAndCount(content.length, offset, byteCount);
        return new RequestBody() { // from class: okhttp3.RequestBody.2
            @Override // okhttp3.RequestBody
            @Nullable
            public MediaType contentType() {
                return contentType;
            }

            @Override // okhttp3.RequestBody
            public long contentLength() {
                return byteCount;
            }

            @Override // okhttp3.RequestBody
            public void writeTo(BufferedSink sink) throws IOException {
                sink.write(content, offset, byteCount);
            }
        };
    }

    public static RequestBody create(@Nullable final MediaType contentType, final File file) {
        if (file == null) {
            throw new NullPointerException("content == null");
        }
        return new RequestBody() { // from class: okhttp3.RequestBody.3
            @Override // okhttp3.RequestBody
            @Nullable
            public MediaType contentType() {
                return contentType;
            }

            @Override // okhttp3.RequestBody
            public long contentLength() {
                return file.length();
            }

            @Override // okhttp3.RequestBody
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(file);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }
}
