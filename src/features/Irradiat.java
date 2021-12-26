package features;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum Irradiat implements Feature<Irradiat> {
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
    public Map<Irradiat, List<BreastCancerData>> getOccurrenceMap(List<BreastCancerData> data) {
        return data.stream()
                .filter(it -> it.getIrradiat() != Irradiat.UNKNOWN)
                .collect(Collectors.groupingBy(BreastCancerData::getIrradiat));
    }
}
