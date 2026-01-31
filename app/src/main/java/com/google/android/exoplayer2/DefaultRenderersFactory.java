package com.google.android.exoplayer2;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.metadata.MetadataRenderer;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class DefaultRenderersFactory implements RenderersFactory {
    public static final long DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS = 5000;
    public static final int EXTENSION_RENDERER_MODE_OFF = 0;
    public static final int EXTENSION_RENDERER_MODE_ON = 1;
    public static final int EXTENSION_RENDERER_MODE_PREFER = 2;
    protected static final int MAX_DROPPED_VIDEO_FRAME_COUNT_TO_NOTIFY = 50;
    private static final String TAG = "DefaultRenderersFactory";
    private final long allowedVideoJoiningTimeMs;
    private final Context context;
    private final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager;
    private final int extensionRendererMode;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ExtensionRendererMode {
    }

    public DefaultRenderersFactory(Context context) {
        this(context, null);
    }

    public DefaultRenderersFactory(Context context, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager) {
        this(context, drmSessionManager, 0);
    }

    public DefaultRenderersFactory(Context context, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, int extensionRendererMode) {
        this(context, drmSessionManager, extensionRendererMode, DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
    }

    public DefaultRenderersFactory(Context context, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, int extensionRendererMode, long allowedVideoJoiningTimeMs) {
        this.context = context;
        this.drmSessionManager = drmSessionManager;
        this.extensionRendererMode = extensionRendererMode;
        this.allowedVideoJoiningTimeMs = allowedVideoJoiningTimeMs;
    }

    @Override // com.google.android.exoplayer2.RenderersFactory
    public Renderer[] createRenderers(Handler eventHandler, VideoRendererEventListener videoRendererEventListener, AudioRendererEventListener audioRendererEventListener, TextRenderer.Output textRendererOutput, MetadataRenderer.Output metadataRendererOutput) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
        ArrayList<Renderer> renderersList = new ArrayList<>();
        buildVideoRenderers(this.context, this.drmSessionManager, this.allowedVideoJoiningTimeMs, eventHandler, videoRendererEventListener, this.extensionRendererMode, renderersList);
        buildAudioRenderers(this.context, this.drmSessionManager, buildAudioProcessors(), eventHandler, audioRendererEventListener, this.extensionRendererMode, renderersList);
        buildTextRenderers(this.context, textRendererOutput, eventHandler.getLooper(), this.extensionRendererMode, renderersList);
        buildMetadataRenderers(this.context, metadataRendererOutput, eventHandler.getLooper(), this.extensionRendererMode, renderersList);
        buildMiscellaneousRenderers(this.context, eventHandler, this.extensionRendererMode, renderersList);
        return (Renderer[]) renderersList.toArray(new Renderer[renderersList.size()]);
    }

    protected void buildVideoRenderers(Context context, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, long allowedVideoJoiningTimeMs, Handler eventHandler, VideoRendererEventListener eventListener, int extensionRendererMode, ArrayList<Renderer> out) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
        out.add(new MediaCodecVideoRenderer(context, MediaCodecSelector.DEFAULT, allowedVideoJoiningTimeMs, drmSessionManager, false, eventHandler, eventListener, 50));
        if (extensionRendererMode != 0) {
            int extensionRendererIndex = out.size();
            int extensionRendererIndex2 = extensionRendererMode == 2 ? extensionRendererIndex - 1 : extensionRendererIndex;
            try {
                Class<?> clazz = Class.forName("com.google.android.exoplayer2.ext.vp9.LibvpxVideoRenderer");
                Constructor<?> constructor = clazz.getConstructor(Boolean.TYPE, Long.TYPE, Handler.class, VideoRendererEventListener.class, Integer.TYPE);
                Renderer renderer = (Renderer) constructor.newInstance(true, Long.valueOf(allowedVideoJoiningTimeMs), eventHandler, eventListener, 50);
                int i = extensionRendererIndex2 + 1;
                try {
                    out.add(extensionRendererIndex2, renderer);
                    Log.i(TAG, "Loaded LibvpxVideoRenderer.");
                } catch (ClassNotFoundException e) {
                } catch (Exception e2) {
                    e = e2;
                    throw new RuntimeException(e);
                }
            } catch (ClassNotFoundException e3) {
            } catch (Exception e4) {
                e = e4;
            }
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:5|(1:7)(1:52)|(3:57|8|9)|(3:55|10|11)|63|12|13|59|14|15|(6:53|16|17|61|18|66)) */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00f1, code lost:
    
        r12 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00f2, code lost:
    
        r13 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00f4, code lost:
    
        r11 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00fb, code lost:
    
        throw new java.lang.RuntimeException(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x010a, code lost:
    
        r11 = e;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void buildAudioRenderers(android.content.Context r16, com.google.android.exoplayer2.drm.DrmSessionManager<com.google.android.exoplayer2.drm.FrameworkMediaCrypto> r17, com.google.android.exoplayer2.audio.AudioProcessor[] r18, android.os.Handler r19, com.google.android.exoplayer2.audio.AudioRendererEventListener r20, int r21, java.util.ArrayList<com.google.android.exoplayer2.Renderer> r22) throws java.lang.NoSuchMethodException, java.lang.ClassNotFoundException, java.lang.SecurityException {
        /*
            Method dump skipped, instructions count: 277
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.DefaultRenderersFactory.buildAudioRenderers(android.content.Context, com.google.android.exoplayer2.drm.DrmSessionManager, com.google.android.exoplayer2.audio.AudioProcessor[], android.os.Handler, com.google.android.exoplayer2.audio.AudioRendererEventListener, int, java.util.ArrayList):void");
    }

    protected void buildTextRenderers(Context context, TextRenderer.Output output, Looper outputLooper, int extensionRendererMode, ArrayList<Renderer> out) {
        out.add(new TextRenderer(output, outputLooper));
    }

    protected void buildMetadataRenderers(Context context, MetadataRenderer.Output output, Looper outputLooper, int extensionRendererMode, ArrayList<Renderer> out) {
        out.add(new MetadataRenderer(output, outputLooper));
    }

    protected void buildMiscellaneousRenderers(Context context, Handler eventHandler, int extensionRendererMode, ArrayList<Renderer> out) {
    }

    protected AudioProcessor[] buildAudioProcessors() {
        return new AudioProcessor[0];
    }
}
