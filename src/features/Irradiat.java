package features;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Irradiat implements Feature {
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

    @Override
    public double calculateEntropy(List<BreastCancerData> data) {
        Map<Irradiat, Long> occurrences = data.stream()
                .map(BreastCancerData::getIrradiat)
                .filter(it -> it != Irradiat.UNKNOWN)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return calculateEntropy(occurrences, data.size(), Irradiat.values().length - 1);
    }
}
