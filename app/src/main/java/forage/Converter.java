package forage;

/* loaded from: classes.dex */
interface Converter {

    public interface Provider {
        Converter get();
    }

    String convert(String str);
}
