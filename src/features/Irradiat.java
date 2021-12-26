package features;

public enum Irradiat {
    YES("yes"),
    NO("no"),
    UNKNOWN("");

    private final String irradiat;

    Irradiat(String irradiat) {
        this.irradiat = irradiat;
    }

    public String getIrradiat() {
        return irradiat;
    }

    public static Irradiat getIrradiat(String value) {
        for (Irradiat variable : values())
            if (variable.getIrradiat().equalsIgnoreCase(value)) return variable;
        return UNKNOWN;
    }
}
