package features;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum DegMalig implements Feature {
    ONE(1),
    TWO(2),
    THREE(3),
    UNKNOWN(0);

    private final int value;

    DegMalig(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DegMalig getValue(int value) {
        for (DegMalig variable : values())
            if (variable.getValue() == value) return variable;
        return UNKNOWN;
    }

    @Override
    public double calculateEntropy(List<BreastCancerData> data) {
        Map<DegMalig, Long> occurrences = data.stream()
                .map(BreastCancerData::getDegMalig)
                .filter(it -> it != DegMalig.UNKNOWN)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return calculateEntropy(occurrences, data.size(), DegMalig.values().length - 1);
    }
}
