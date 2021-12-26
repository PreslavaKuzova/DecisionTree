package features;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Classification implements Feature {
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

    @Override
    public double calculateEntropy(List<BreastCancerData> data) {
        Map<Classification, Long> occurrences = data.stream()
                .map(BreastCancerData::getClassification)
                .filter(it -> it != Classification.UNKNOWN)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return calculateEntropy(occurrences, data.size(), Classification.values().length - 1);
    }
}
