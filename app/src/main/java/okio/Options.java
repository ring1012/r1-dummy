package okio;

import java.util.AbstractList;
import java.util.RandomAccess;

/* loaded from: classes.dex */
public final class Options extends AbstractList<ByteString> implements RandomAccess {
    final ByteString[] byteStrings;

    private Options(ByteString[] byteStrings) {
        this.byteStrings = byteStrings;
    }

    public static Options of(ByteString... byteStrings) {
        return new Options((ByteString[]) byteStrings.clone());
    }

    @Override // java.util.AbstractList, java.util.List
    public ByteString get(int i) {
        return this.byteStrings[i];
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.byteStrings.length;
    }
}
