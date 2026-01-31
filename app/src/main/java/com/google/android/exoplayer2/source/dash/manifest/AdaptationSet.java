package com.google.android.exoplayer2.source.dash.manifest;

import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class AdaptationSet {
    public static final int ID_UNSET = -1;
    public final List<SchemeValuePair> accessibilityDescriptors;
    public final int id;
    public final List<Representation> representations;
    public final int type;

    public AdaptationSet(int id, int type, List<Representation> representations, List<SchemeValuePair> accessibilityDescriptors) {
        List<SchemeValuePair> listUnmodifiableList;
        this.id = id;
        this.type = type;
        this.representations = Collections.unmodifiableList(representations);
        if (accessibilityDescriptors == null) {
            listUnmodifiableList = Collections.emptyList();
        } else {
            listUnmodifiableList = Collections.unmodifiableList(accessibilityDescriptors);
        }
        this.accessibilityDescriptors = listUnmodifiableList;
    }
}
