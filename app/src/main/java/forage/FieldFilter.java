package forage;

/* loaded from: classes.dex */
public interface FieldFilter {
    public static final FieldFilter DEFAULT = new Default();

    public static final class Default implements FieldFilter {
        @Override // forage.FieldFilter
        public boolean accept(String fieldName, int modifiers, Class<?> type) {
            return true;
        }
    }

    public static class PSFSignatureFilter implements FieldFilter {
        private static boolean isPsfSignature(int modifiers) {
            return (25 & modifiers) != 0;
        }

        @Override // forage.FieldFilter
        public boolean accept(String fieldName, int modifiers, Class<?> type) {
            return isPsfSignature(modifiers);
        }
    }

    boolean accept(String str, int i, Class<?> cls);
}
