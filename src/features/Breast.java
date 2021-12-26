package features;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Breast implements Feature {
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

    @Override
    public double calculateEntropy(List<BreastCancerData> data) {
        Map<Breast, Long> occurrences = data.stream()
                .map(BreastCancerData::getBreast)
                .filter(it -> it != Breast.UNKNOWN)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return calculateEntropy(occurrences, data.size(), Breast.values().length - 1);
    }
}
