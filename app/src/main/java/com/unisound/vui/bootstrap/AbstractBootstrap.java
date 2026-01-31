package com.unisound.vui.bootstrap;

import android.content.Context;
import android.text.TextUtils;
import com.unisound.common.y;
import com.unisound.vui.bootstrap.AbstractBootstrap;
import com.unisound.vui.engine.ANTEngine;
import com.unisound.vui.engine.ANTEngineOption;
import com.unisound.vui.engine.ANTHandler;
import com.unisound.vui.engine.ANTPipeline;
import com.unisound.vui.util.AttributeKey;
import com.unisound.vui.util.LogMgr;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class AbstractBootstrap<B extends AbstractBootstrap<B>> {
    private static final String TAG = AbstractBootstrap.class.getSimpleName();
    private volatile ANTEFactory<? extends ANTEngine> antEngineFactory;
    private Context context;
    private volatile ANTHandler handler;
    private volatile ANTHandler initializationHandler;
    private ANTELocalConfiguration localConfiguration;
    private volatile String mainTag;
    private Map<String, List<String>> mainVocab;
    private ANTPipeline pipeline;
    private volatile List<String> wakeupWord;
    private final Map<ANTEngineOption<?>, Object> options = new LinkedHashMap();
    private final Map<AttributeKey<?>, Object> attrs = new LinkedHashMap();

    final Context androidContext() {
        return this.context;
    }

    public B androidContext(Context context) {
        if (context == null) {
            throw new NullPointerException("context");
        }
        this.context = context;
        return this;
    }

    public B antEngineFactory(ANTEFactory<ANTEngine> antEngineFactory) {
        if (antEngineFactory == null) {
            throw new NullPointerException("antEngineFactory");
        }
        if (this.antEngineFactory != null) {
            throw new IllegalStateException("antEngineFactory set already");
        }
        this.antEngineFactory = antEngineFactory;
        return this;
    }

    public <T> B attr(AttributeKey<T> key, T value) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        if (value == null) {
            synchronized (this.attrs) {
                this.attrs.remove(key);
            }
        } else {
            synchronized (this.attrs) {
                this.attrs.put(key, value);
            }
        }
        return this;
    }

    final Map<AttributeKey<?>, Object> attrs() {
        return this.attrs;
    }

    public B handler(ANTHandler handler) {
        if (handler == null) {
            throw new NullPointerException("handler");
        }
        this.handler = handler;
        return this;
    }

    final ANTHandler handler() {
        return this.handler;
    }

    public void init() {
        LogMgr.d(TAG, y.y);
        validate();
        ANTEngine aNTEngineNewANTEngine = this.antEngineFactory.newANTEngine();
        try {
            this.pipeline = aNTEngineNewANTEngine.pipeline();
            init0(aNTEngineNewANTEngine);
            if (isUseProxyMode()) {
                aNTEngineNewANTEngine.initializeSdk();
            } else {
                aNTEngineNewANTEngine.initializeMode();
            }
        } catch (Exception e) {
            LogMgr.e(TAG, "init exception:" + e.toString());
            aNTEngineNewANTEngine.unsafe().release(1401, null);
        }
    }

    abstract void init0(ANTEngine aNTEngine) throws Exception;

    public B initializationHandler(ANTHandler initializationHandler) {
        if (initializationHandler == null) {
            throw new NullPointerException("initializationHandler");
        }
        this.initializationHandler = initializationHandler;
        return this;
    }

    final ANTHandler initializationHandler() {
        return this.initializationHandler;
    }

    abstract boolean isUseProxyMode();

    final ANTELocalConfiguration localConfiguration() {
        return this.localConfiguration;
    }

    public B localConfiguration(ANTELocalConfiguration localConfiguration) {
        if (localConfiguration == null) {
            throw new NullPointerException("ant engine localConfiguration is null");
        }
        this.localConfiguration = localConfiguration;
        return this;
    }

    public B mainTag(String mainTag) {
        if (TextUtils.isEmpty(mainTag)) {
            throw new NullPointerException("mainTag");
        }
        this.mainTag = mainTag;
        return this;
    }

    final String mainTag() {
        return this.mainTag;
    }

    public B mainVocab(Map<String, List<String>> vocab) {
        if (vocab == null) {
            throw new NullPointerException("mainVocab");
        }
        this.mainVocab = vocab;
        return this;
    }

    final Map<String, List<String>> mainVocab() {
        return this.mainVocab;
    }

    public <T> B option(ANTEngineOption<T> option, T value) {
        if (option == null) {
            throw new NullPointerException("option");
        }
        if (value == null) {
            synchronized (this.options) {
                this.options.remove(option);
            }
        } else {
            synchronized (this.options) {
                this.options.put(option, value);
            }
        }
        return this;
    }

    public B options(a provider) {
        if (provider == null) {
            throw new NullPointerException("ant engine options provider");
        }
        synchronized (this.options) {
            Map<? extends ANTEngineOption<?>, ? extends Object> mapOptions = provider.options();
            if (mapOptions == null) {
                throw new NullPointerException("The ANTEOptionsProvider.options() method can't return null value");
            }
            this.options.putAll(mapOptions);
        }
        return this;
    }

    public B options(Map<ANTEngineOption<?>, Object> optionObjectMap) {
        synchronized (this.options) {
            if (optionObjectMap == null) {
                throw new NullPointerException("The optionObjectMap value can't be null");
            }
            this.options.putAll(optionObjectMap);
        }
        return this;
    }

    final Map<ANTEngineOption<?>, Object> options() {
        return this.options;
    }

    public ANTPipeline pipeline() {
        return this.pipeline;
    }

    public B validate() {
        if (this.antEngineFactory == null) {
            throw new IllegalStateException("antEngineFactory not set");
        }
        if (this.context == null) {
            throw new IllegalStateException("android context not set");
        }
        if (this.wakeupWord == null) {
            throw new IllegalStateException("wakeup word not set");
        }
        if (this.initializationHandler == null) {
            throw new IllegalStateException("initializationHandler not set");
        }
        return this;
    }

    public B wakeupWord(List<String> wakeupWord) {
        if (wakeupWord == null) {
            throw new NullPointerException("wakeupWord");
        }
        this.wakeupWord = wakeupWord;
        return this;
    }

    final List<String> wakeupWord() {
        return this.wakeupWord;
    }
}
