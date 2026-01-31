package com.google.android.exoplayer2;

/* loaded from: classes.dex */
public final class RendererConfiguration {
    public static final RendererConfiguration DEFAULT = new RendererConfiguration(0);
    public final int tunnelingAudioSessionId;

    public RendererConfiguration(int tunnelingAudioSessionId) {
        this.tunnelingAudioSessionId = tunnelingAudioSessionId;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RendererConfiguration other = (RendererConfiguration) obj;
        return this.tunnelingAudioSessionId == other.tunnelingAudioSessionId;
    }

    public int hashCode() {
        return this.tunnelingAudioSessionId;
    }
}
