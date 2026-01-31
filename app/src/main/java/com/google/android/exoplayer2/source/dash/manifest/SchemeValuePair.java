package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
public class SchemeValuePair {
    public final String schemeIdUri;
    public final String value;

    public SchemeValuePair(String schemeIdUri, String value) {
        this.schemeIdUri = schemeIdUri;
        this.value = value;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SchemeValuePair other = (SchemeValuePair) obj;
        return Util.areEqual(this.schemeIdUri, other.schemeIdUri) && Util.areEqual(this.value, other.value);
    }

    public int hashCode() {
        return ((this.schemeIdUri != null ? this.schemeIdUri.hashCode() : 0) * 31) + (this.value != null ? this.value.hashCode() : 0);
    }
}
