package android.support.v4.content.res;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;

/* loaded from: classes.dex */
public final class ConfigurationHelper {
    private static final ConfigurationHelperImpl IMPL;

    private interface ConfigurationHelperImpl {
        int getDensityDpi(@NonNull Resources resources);

        int getScreenHeightDp(@NonNull Resources resources);

        int getScreenWidthDp(@NonNull Resources resources);

        int getSmallestScreenWidthDp(@NonNull Resources resources);
    }

    static {
        int sdk = Build.VERSION.SDK_INT;
        if (sdk >= 17) {
            IMPL = new JellybeanMr1Impl();
        } else if (sdk >= 13) {
            IMPL = new HoneycombMr2Impl();
        } else {
            IMPL = new GingerbreadImpl();
        }
    }

    private ConfigurationHelper() {
    }

    private static class GingerbreadImpl implements ConfigurationHelperImpl {
        GingerbreadImpl() {
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getScreenHeightDp(@NonNull Resources resources) {
            return ConfigurationHelperGingerbread.getScreenHeightDp(resources);
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getScreenWidthDp(@NonNull Resources resources) {
            return ConfigurationHelperGingerbread.getScreenWidthDp(resources);
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getSmallestScreenWidthDp(@NonNull Resources resources) {
            return ConfigurationHelperGingerbread.getSmallestScreenWidthDp(resources);
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getDensityDpi(@NonNull Resources resources) {
            return ConfigurationHelperGingerbread.getDensityDpi(resources);
        }
    }

    private static class HoneycombMr2Impl extends GingerbreadImpl {
        HoneycombMr2Impl() {
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.GingerbreadImpl, android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getScreenHeightDp(@NonNull Resources resources) {
            return ConfigurationHelperHoneycombMr2.getScreenHeightDp(resources);
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.GingerbreadImpl, android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getScreenWidthDp(@NonNull Resources resources) {
            return ConfigurationHelperHoneycombMr2.getScreenWidthDp(resources);
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.GingerbreadImpl, android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getSmallestScreenWidthDp(@NonNull Resources resources) {
            return ConfigurationHelperHoneycombMr2.getSmallestScreenWidthDp(resources);
        }
    }

    private static class JellybeanMr1Impl extends HoneycombMr2Impl {
        JellybeanMr1Impl() {
        }

        @Override // android.support.v4.content.res.ConfigurationHelper.GingerbreadImpl, android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl
        public int getDensityDpi(@NonNull Resources resources) {
            return ConfigurationHelperJellybeanMr1.getDensityDpi(resources);
        }
    }

    public static int getScreenHeightDp(@NonNull Resources resources) {
        return IMPL.getScreenHeightDp(resources);
    }

    public static int getScreenWidthDp(@NonNull Resources resources) {
        return IMPL.getScreenWidthDp(resources);
    }

    public static int getSmallestScreenWidthDp(@NonNull Resources resources) {
        return IMPL.getSmallestScreenWidthDp(resources);
    }

    public static int getDensityDpi(@NonNull Resources resources) {
        return IMPL.getDensityDpi(resources);
    }
}
