package butterknife.internal;

/* loaded from: classes.dex */
final class FieldViewBinding implements ViewBinding {
    private final String name;
    private final boolean required;
    private final String type;

    FieldViewBinding(String name, String type, boolean required) {
        this.name = name;
        this.type = type;
        this.required = required;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    @Override // butterknife.internal.ViewBinding
    public String getDescription() {
        return "field '" + this.name + "'";
    }

    public boolean isRequired() {
        return this.required;
    }

    public boolean requiresCast() {
        return !"android.view.View".equals(this.type);
    }
}
