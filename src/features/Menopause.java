package features;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Menopause implements Feature {
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

    @Override
    public double calculateEntropy(List<BreastCancerData> data) {
        Map<Menopause, Long> occurrences = data.stream()
                .map(BreastCancerData::getMenopause)
                .filter(it -> it != Menopause.UNKNOWN)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return calculateEntropy(occurrences, data.size(), Menopause.values().length - 1);
    }
}
