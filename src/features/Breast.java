package features;

public enum Breast {
    LEFT("left"),
    RIGHT("right"),
    UNKNOWN("");

    private final String breast;

    Breast(String breast) {
        this.breast = breast;
    }

    public String getBreast() {
        return breast;
    }

    public static Breast getBreast(String value) {
        for (Breast variable : values())
            if (variable.getBreast().equalsIgnoreCase(value)) return variable;
        return UNKNOWN;
    }
}
