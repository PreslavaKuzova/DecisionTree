package features;

public enum Menopause {
    LT40("lt40"),
    GE40("ge40"),
    PREMENOPAUSE("premeno"),
    UNKNOWN("");

    private final String menopause;

    Menopause(String menopause) {
        this.menopause = menopause;
    }

    public String getMenopause() {
        return menopause;
    }

    public static Menopause getMenopause(String value) {
        for (Menopause variable : values())
            if (variable.getMenopause().equalsIgnoreCase(value)) return variable;
        return UNKNOWN;
    }
}
