package okhttp3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import org.eclipse.paho.client.mqttv3.a.b.u;

/* loaded from: classes.dex */
public final class MultipartBody extends RequestBody {
    private final ByteString boundary;
    private long contentLength = -1;
    private final MediaType contentType;
    private final MediaType originalType;
    private final List<Part> parts;
    public static final MediaType MIXED = MediaType.parse("multipart/mixed");
    public static final MediaType ALTERNATIVE = MediaType.parse("multipart/alternative");
    public static final MediaType DIGEST = MediaType.parse("multipart/digest");
    public static final MediaType PARALLEL = MediaType.parse("multipart/parallel");
    public static final MediaType FORM = MediaType.parse("multipart/form-data");
    private static final byte[] COLONSPACE = {58, 32};
    private static final byte[] CRLF = {u.n, 10};
    private static final byte[] DASHDASH = {45, 45};

    MultipartBody(ByteString boundary, MediaType type, List<Part> parts) {
        this.boundary = boundary;
        this.originalType = type;
        this.contentType = MediaType.parse(type + "; boundary=" + boundary.utf8());
        this.parts = Util.immutableList(parts);
    }

    public MediaType type() {
        return this.originalType;
    }

    public String boundary() {
        return this.boundary.utf8();
    }

    public int size() {
        return this.parts.size();
    }

    public List<Part> parts() {
        return this.parts;
    }

    public Part part(int index) {
        return this.parts.get(index);
    }

    @Override // okhttp3.RequestBody
    public MediaType contentType() {
        return this.contentType;
    }

    @Override // okhttp3.RequestBody
    public long contentLength() throws IOException {
        long result = this.contentLength;
        if (result != -1) {
            return result;
        }
        long result2 = writeOrCountBytes(null, true);
        this.contentLength = result2;
        return result2;
    }

    @Override // okhttp3.RequestBody
    public void writeTo(BufferedSink sink) throws IOException {
        writeOrCountBytes(sink, false);
    }

    private long writeOrCountBytes(@Nullable BufferedSink sink, boolean countBytes) throws IOException {
        long byteCount = 0;
        Buffer byteCountBuffer = null;
        if (countBytes) {
            byteCountBuffer = new Buffer();
            sink = byteCountBuffer;
        }
        int partCount = this.parts.size();
        for (int p = 0; p < partCount; p++) {
            Part part = this.parts.get(p);
            Headers headers = part.headers;
            RequestBody body = part.body;
            sink.write(DASHDASH);
            sink.write(this.boundary);
            sink.write(CRLF);
            if (headers != null) {
                int headerCount = headers.size();
                for (int h = 0; h < headerCount; h++) {
                    sink.writeUtf8(headers.name(h)).write(COLONSPACE).writeUtf8(headers.value(h)).write(CRLF);
                }
            }
            MediaType contentType = body.contentType();
            if (contentType != null) {
                sink.writeUtf8("Content-Type: ").writeUtf8(contentType.toString()).write(CRLF);
            }
            long contentLength = body.contentLength();
            if (contentLength != -1) {
                sink.writeUtf8("Content-Length: ").writeDecimalLong(contentLength).write(CRLF);
            } else if (countBytes) {
                byteCountBuffer.clear();
                return -1L;
            }
            sink.write(CRLF);
            if (countBytes) {
                byteCount += contentLength;
            } else {
                body.writeTo(sink);
            }
            sink.write(CRLF);
        }
        sink.write(DASHDASH);
        sink.write(this.boundary);
        sink.write(DASHDASH);
        sink.write(CRLF);
        if (countBytes) {
            byteCount += byteCountBuffer.size();
            byteCountBuffer.clear();
        }
        return byteCount;
    }

    static StringBuilder appendQuotedString(StringBuilder target, String key) {
        target.append('\"');
        int len = key.length();
        for (int i = 0; i < len; i++) {
            char ch = key.charAt(i);
            switch (ch) {
                case '\n':
                    target.append("%0A");
                    break;
                case '\r':
                    target.append("%0D");
                    break;
                case '\"':
                    target.append("%22");
                    break;
                default:
                    target.append(ch);
                    break;
            }
        }
        target.append('\"');
        return target;
    }

    public static final class Part {
        final RequestBody body;

        @Nullable
        final Headers headers;

        public static Part create(RequestBody body) {
            return create(null, body);
        }

        public static Part create(@Nullable Headers headers, RequestBody body) {
            if (body == null) {
                throw new NullPointerException("body == null");
            }
            if (headers != null && headers.get("Content-Type") != null) {
                throw new IllegalArgumentException("Unexpected header: Content-Type");
            }
            if (headers != null && headers.get("Content-Length") != null) {
                throw new IllegalArgumentException("Unexpected header: Content-Length");
            }
            return new Part(headers, body);
        }

        public static Part createFormData(String name, String value) {
            return createFormData(name, null, RequestBody.create((MediaType) null, value));
        }

        public static Part createFormData(String name, @Nullable String filename, RequestBody body) {
            if (name == null) {
                throw new NullPointerException("name == null");
            }
            StringBuilder disposition = new StringBuilder("form-data; name=");
            MultipartBody.appendQuotedString(disposition, name);
            if (filename != null) {
                disposition.append("; filename=");
                MultipartBody.appendQuotedString(disposition, filename);
            }
            return create(Headers.of("Content-Disposition", disposition.toString()), body);
        }

        private Part(@Nullable Headers headers, RequestBody body) {
            this.headers = headers;
            this.body = body;
        }

        @Nullable
        public Headers headers() {
            return this.headers;
        }

        public RequestBody body() {
            return this.body;
        }
    }

    public static final class Builder {
        private final ByteString boundary;
        private final List<Part> parts;
        private MediaType type;

        public Builder() {
            this(UUID.randomUUID().toString());
        }

        public Builder(String boundary) {
            this.type = MultipartBody.MIXED;
            this.parts = new ArrayList();
            this.boundary = ByteString.encodeUtf8(boundary);
        }

        public Builder setType(MediaType type) {
            if (type == null) {
                throw new NullPointerException("type == null");
            }
            if (!type.type().equals("multipart")) {
                throw new IllegalArgumentException("multipart != " + type);
            }
            this.type = type;
            return this;
        }

        public Builder addPart(RequestBody body) {
            return addPart(Part.create(body));
        }

        public Builder addPart(@Nullable Headers headers, RequestBody body) {
            return addPart(Part.create(headers, body));
        }

        public Builder addFormDataPart(String name, String value) {
            return addPart(Part.createFormData(name, value));
        }

        public Builder addFormDataPart(String name, @Nullable String filename, RequestBody body) {
            return addPart(Part.createFormData(name, filename, body));
        }

        public Builder addPart(Part part) {
            if (part == null) {
                throw new NullPointerException("part == null");
            }
            this.parts.add(part);
            return this;
        }

        public MultipartBody build() {
            if (this.parts.isEmpty()) {
                throw new IllegalStateException("Multipart body must have at least one part.");
            }
            return new MultipartBody(this.boundary, this.type, this.parts);
        }
    }
}
