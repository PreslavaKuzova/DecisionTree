package features;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum Menopause implements Feature<Menopause> {
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
    public Map<Menopause, List<BreastCancerData>> getOccurrenceMap(List<BreastCancerData> data) {
        return data.stream()
                .filter(it -> it.getMenopause() != Menopause.UNKNOWN)
                .collect(Collectors.groupingBy(BreastCancerData::getMenopause));
    }
}
