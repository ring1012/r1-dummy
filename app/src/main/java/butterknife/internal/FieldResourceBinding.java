package butterknife.internal;

/* loaded from: classes.dex */
final class FieldResourceBinding {
    private final int id;
    private final String method;
    private final String name;

    FieldResourceBinding(int id, String name, String method) {
        this.id = id;
        this.name = name;
        this.method = method;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getMethod() {
        return this.method;
    }
}
