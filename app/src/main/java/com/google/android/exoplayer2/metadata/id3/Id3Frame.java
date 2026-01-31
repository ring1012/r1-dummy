package com.google.android.exoplayer2.metadata.id3;

import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.Assertions;

/* loaded from: classes.dex */
public abstract class Id3Frame implements Metadata.Entry {
    public final String id;

    public Id3Frame(String id) {
        this.id = (String) Assertions.checkNotNull(id);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
