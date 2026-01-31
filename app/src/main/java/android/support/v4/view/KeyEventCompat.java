package android.support.v4.view;

import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;

/* loaded from: classes.dex */
public final class KeyEventCompat {
    static final KeyEventVersionImpl IMPL;

    interface KeyEventVersionImpl {
        boolean isCtrlPressed(KeyEvent keyEvent);

        boolean metaStateHasModifiers(int i, int i2);

        boolean metaStateHasNoModifiers(int i);

        int normalizeMetaState(int i);
    }

    static class BaseKeyEventVersionImpl implements KeyEventVersionImpl {
        private static final int META_ALL_MASK = 247;
        private static final int META_MODIFIER_MASK = 247;

        BaseKeyEventVersionImpl() {
        }

        private static int metaStateFilterDirectionalModifiers(int metaState, int modifiers, int basic, int left, int right) {
            boolean wantBasic = (modifiers & basic) != 0;
            int directional = left | right;
            boolean wantLeftOrRight = (modifiers & directional) != 0;
            if (wantBasic) {
                if (wantLeftOrRight) {
                    throw new IllegalArgumentException("bad arguments");
                }
                return metaState & (directional ^ (-1));
            }
            if (wantLeftOrRight) {
                return metaState & (basic ^ (-1));
            }
            return metaState;
        }

        @Override // android.support.v4.view.KeyEventCompat.KeyEventVersionImpl
        public int normalizeMetaState(int metaState) {
            if ((metaState & PsExtractor.AUDIO_STREAM) != 0) {
                metaState |= 1;
            }
            if ((metaState & 48) != 0) {
                metaState |= 2;
            }
            return metaState & 247;
        }

        @Override // android.support.v4.view.KeyEventCompat.KeyEventVersionImpl
        public boolean metaStateHasModifiers(int metaState, int modifiers) {
            return metaStateFilterDirectionalModifiers(metaStateFilterDirectionalModifiers(normalizeMetaState(metaState) & 247, modifiers, 1, 64, 128), modifiers, 2, 16, 32) == modifiers;
        }

        @Override // android.support.v4.view.KeyEventCompat.KeyEventVersionImpl
        public boolean metaStateHasNoModifiers(int metaState) {
            return (normalizeMetaState(metaState) & 247) == 0;
        }

        @Override // android.support.v4.view.KeyEventCompat.KeyEventVersionImpl
        public boolean isCtrlPressed(KeyEvent event) {
            return false;
        }
    }

    static class HoneycombKeyEventVersionImpl extends BaseKeyEventVersionImpl {
        HoneycombKeyEventVersionImpl() {
        }

        @Override // android.support.v4.view.KeyEventCompat.BaseKeyEventVersionImpl, android.support.v4.view.KeyEventCompat.KeyEventVersionImpl
        public int normalizeMetaState(int metaState) {
            return KeyEventCompatHoneycomb.normalizeMetaState(metaState);
        }

        @Override // android.support.v4.view.KeyEventCompat.BaseKeyEventVersionImpl, android.support.v4.view.KeyEventCompat.KeyEventVersionImpl
        public boolean metaStateHasModifiers(int metaState, int modifiers) {
            return KeyEventCompatHoneycomb.metaStateHasModifiers(metaState, modifiers);
        }

        @Override // android.support.v4.view.KeyEventCompat.BaseKeyEventVersionImpl, android.support.v4.view.KeyEventCompat.KeyEventVersionImpl
        public boolean metaStateHasNoModifiers(int metaState) {
            return KeyEventCompatHoneycomb.metaStateHasNoModifiers(metaState);
        }

        @Override // android.support.v4.view.KeyEventCompat.BaseKeyEventVersionImpl, android.support.v4.view.KeyEventCompat.KeyEventVersionImpl
        public boolean isCtrlPressed(KeyEvent event) {
            return KeyEventCompatHoneycomb.isCtrlPressed(event);
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 11) {
            IMPL = new HoneycombKeyEventVersionImpl();
        } else {
            IMPL = new BaseKeyEventVersionImpl();
        }
    }

    public static int normalizeMetaState(int metaState) {
        return IMPL.normalizeMetaState(metaState);
    }

    public static boolean metaStateHasModifiers(int metaState, int modifiers) {
        return IMPL.metaStateHasModifiers(metaState, modifiers);
    }

    public static boolean metaStateHasNoModifiers(int metaState) {
        return IMPL.metaStateHasNoModifiers(metaState);
    }

    public static boolean hasModifiers(KeyEvent event, int modifiers) {
        return IMPL.metaStateHasModifiers(event.getMetaState(), modifiers);
    }

    public static boolean hasNoModifiers(KeyEvent event) {
        return IMPL.metaStateHasNoModifiers(event.getMetaState());
    }

    @Deprecated
    public static void startTracking(KeyEvent event) {
        event.startTracking();
    }

    @Deprecated
    public static boolean isTracking(KeyEvent event) {
        return event.isTracking();
    }

    @Deprecated
    public static Object getKeyDispatcherState(View view) {
        return view.getKeyDispatcherState();
    }

    @Deprecated
    public static boolean dispatch(KeyEvent event, KeyEvent.Callback receiver, Object state, Object target) {
        return event.dispatch(receiver, (KeyEvent.DispatcherState) state, target);
    }

    public static boolean isCtrlPressed(KeyEvent event) {
        return IMPL.isCtrlPressed(event);
    }

    private KeyEventCompat() {
    }
}
