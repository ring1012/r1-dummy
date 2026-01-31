package butterknife.internal;

/* loaded from: classes.dex */
final class Parameter {
    static final Parameter[] NONE = new Parameter[0];
    private final int listenerPosition;
    private final String type;

    Parameter(int listenerPosition, String type) {
        this.listenerPosition = listenerPosition;
        this.type = type;
    }

    int getListenerPosition() {
        return this.listenerPosition;
    }

    String getType() {
        return this.type;
    }

    public boolean requiresCast(String toType) {
        return !this.type.equals(toType);
    }
}
