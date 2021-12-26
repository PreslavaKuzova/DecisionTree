package features;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum Breast implements Feature<Breast> {
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
    public Map<Breast, List<BreastCancerData>> getOccurrenceMap(List<BreastCancerData> data) {
        return data.stream()
                .filter(it -> it.getBreast() != Breast.UNKNOWN)
                .collect(Collectors.groupingBy(BreastCancerData::getBreast));
    }
}
