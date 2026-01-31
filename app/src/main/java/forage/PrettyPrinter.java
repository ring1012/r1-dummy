package forage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/* loaded from: classes.dex */
public final class PrettyPrinter {
    private final Converter converter;
    private boolean originalOuput;

    static class Default implements Converter {
        Default() {
        }

        @Override // forage.Converter
        public String convert(String input) {
            return input;
        }
    }

    static class GsonPrettyPrinter extends Default {
        private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        private final JsonParser jsonParser = new JsonParser();

        GsonPrettyPrinter() {
        }

        @Override // forage.PrettyPrinter.Default, forage.Converter
        public String convert(String input) {
            try {
                return this.gson.toJson(this.jsonParser.parse(input));
            } catch (JsonSyntaxException e) {
                return super.convert(input);
            }
        }
    }

    private PrettyPrinter() {
        if (hasGsonOnClasspath()) {
            this.converter = new GsonPrettyPrinter();
        } else {
            this.converter = new Default();
        }
    }

    public static PrettyPrinter getInstance() {
        return new PrettyPrinter();
    }

    private static boolean hasGsonOnClasspath() throws ClassNotFoundException {
        try {
            Class.forName("com.google.gson.Gson");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public PrettyPrinter setOriginalOutput() {
        this.originalOuput = true;
        return this;
    }

    public String toPrettyString(String input) {
        return this.originalOuput ? input : this.converter.convert(input);
    }
}
