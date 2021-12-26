package features;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    // Entropy is used as a way to measure how “mixed” a column is.
    // Specifically, entropy is used to measure disorder.
    // We calculate the entropy of the class variable.
    public static double calculateEntropy(List<BreastCancerData> data) {
        Map<Classification, Long> occurrences = data.stream()
                .map(BreastCancerData::getClassification)
                .filter(it -> it != Classification.UNKNOWN)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        double sum = 0.0;
        for (Long numberOfNodeCapsOccurrence : occurrences.values()) {
            sum += (numberOfNodeCapsOccurrence * 1.0 / data.size()) *
                    (Math.log(numberOfNodeCapsOccurrence * 1.0 / data.size()) /
                            Math.log(Classification.values().length - 1)); // needed for logb(n) = loge(n) / loge(b)
        }
        return -sum;
    }

}
