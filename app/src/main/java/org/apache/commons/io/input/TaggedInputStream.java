package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.UUID;
import org.apache.commons.io.TaggedIOException;

/* loaded from: classes.dex */
public class TaggedInputStream extends ProxyInputStream {
    private final Serializable tag;

    public TaggedInputStream(InputStream proxy) {
        super(proxy);
        this.tag = UUID.randomUUID();
    }

    public boolean isCauseOf(Throwable exception) {
        return TaggedIOException.isTaggedWith(exception, this.tag);
    }

    public void throwIfCauseOf(Throwable throwable) throws IOException {
        TaggedIOException.throwCauseIfTaggedWith(throwable, this.tag);
    }

    @Override // org.apache.commons.io.input.ProxyInputStream
    protected void handleIOException(IOException e) throws IOException {
        throw new TaggedIOException(e, this.tag);
    }
}
