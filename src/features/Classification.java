package features;

public enum Classification {
    NO_RECURRENCE("no-recurrence-events"),
    RECURRENCE("recurrence-events"),
    UNKNOWN("unknown");

    private final String classVariable;

    Classification(String answer) {
        this.classVariable = answer;
    }

    public String getClassVariable() {
        return classVariable;
    }

    public static Classification getClassification(String value) {
        for (Classification variable : values())
            if (variable.getClassVariable().equalsIgnoreCase(value)) return variable;
        return UNKNOWN;
    }
}
