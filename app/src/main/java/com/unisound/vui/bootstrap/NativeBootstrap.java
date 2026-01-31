package com.unisound.vui.bootstrap;

import com.unisound.vui.bootstrap.ANTELocalConfiguration;
import com.unisound.vui.engine.ANTEngine;
import com.unisound.vui.engine.ANTEngineInitializer;
import com.unisound.vui.engine.ANTEngineOption;
import com.unisound.vui.engine.ANTPipeline;
import com.unisound.vui.util.AttributeKey;
import com.unisound.vui.util.LogMgr;
import java.util.Map;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/* loaded from: classes.dex */
public class NativeBootstrap extends AbstractBootstrap<NativeBootstrap> {
    private static final String TAG = "NativeBootstrap";

    private final class a extends ANTEngineInitializer {
        private a() {
        }

        @Override // com.unisound.vui.engine.ANTEngineInitializer
        protected void onEngineInitDone(final ANTEngine engine) {
            engine.unsafe().markModeInitialized();
            int iIntValue = ((Integer) engine.config().getOption(ANTEngineOption.ASR_OPT_WAKEUP_MODEL_ID)).intValue();
            int iIntValue2 = ((Integer) engine.config().getOption(ANTEngineOption.ASR_OPT_RECOGNIZE_MODEL_ID)).intValue();
            engine.config().setOption(ANTEngineOption.ASR_OPT_WAKEUP_MODEL_ID, Integer.valueOf(iIntValue));
            engine.config().setOption(ANTEngineOption.ASR_OPT_RECOGNIZE_MODEL_ID, Integer.valueOf(iIntValue2));
            engine.pipeline().addLast(NativeBootstrap.this.initializationHandler());
            engine.unsafe().setAndroidContext(NativeBootstrap.this.androidContext());
            engine.setWakeupWord(NativeBootstrap.this.wakeupWord(), false);
            Observable.just(new DefaultLocalConfigurationProvider(NativeBootstrap.this.androidContext())).map(new Func1<DefaultLocalConfigurationProvider, ANTELocalConfiguration>() { // from class: com.unisound.vui.bootstrap.NativeBootstrap.a.2
                @Override // rx.functions.Func1
                /* renamed from: a, reason: merged with bridge method [inline-methods] */
                public ANTELocalConfiguration call(DefaultLocalConfigurationProvider defaultLocalConfigurationProvider) {
                    defaultLocalConfigurationProvider.copyJsgfFromAssets();
                    return defaultLocalConfigurationProvider.getConfig();
                }
            }).subscribeOn(Schedulers.io()).subscribe(new Action1<ANTELocalConfiguration>() { // from class: com.unisound.vui.bootstrap.NativeBootstrap.a.1
                @Override // rx.functions.Action1
                /* renamed from: a, reason: merged with bridge method [inline-methods] */
                public void call(ANTELocalConfiguration aNTELocalConfiguration) {
                    engine.config().setLocalConfiguration(aNTELocalConfiguration);
                    String strMainTag = NativeBootstrap.this.mainTag();
                    ANTELocalConfiguration.LocalRecognition localRecognition = aNTELocalConfiguration.getLocalRecognition(strMainTag);
                    localRecognition.getGrammerPath();
                    int iLoadCompiledJsgf = engine.unsafe().loadCompiledJsgf(strMainTag, localRecognition.getCompileJsgfPath());
                    engine.unsafe().insertVocab(NativeBootstrap.this.mainVocab(), strMainTag);
                    if (iLoadCompiledJsgf != 0) {
                        LogMgr.e(NativeBootstrap.TAG, "loadGrammar errro %s", strMainTag);
                    }
                    engine.config().setMainTag(strMainTag);
                    engine.config().setMainVocab(NativeBootstrap.this.mainVocab());
                }
            });
        }
    }

    @Override // com.unisound.vui.bootstrap.AbstractBootstrap
    protected void init0(ANTEngine engine) {
        Map<ANTEngineOption<?>, ?> mapOptions = options();
        synchronized (mapOptions) {
            engine.config().setOptions(mapOptions);
        }
        Map<AttributeKey<?>, Object> mapAttrs = attrs();
        synchronized (mapAttrs) {
            for (Map.Entry<AttributeKey<?>, Object> entry : mapAttrs.entrySet()) {
                engine.attr(entry.getKey()).set(entry.getValue());
            }
        }
        ANTPipeline aNTPipelinePipeline = engine.pipeline();
        if (handler() != null) {
            aNTPipelinePipeline.addLast(handler());
        }
        aNTPipelinePipeline.addLast(new a());
    }

    @Override // com.unisound.vui.bootstrap.AbstractBootstrap
    boolean isUseProxyMode() {
        return false;
    }
}
