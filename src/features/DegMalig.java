package features;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum DegMalig implements Feature<DegMalig> {
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
    public Map<DegMalig, List<BreastCancerData>> getOccurrenceMap(List<BreastCancerData> data) {
        return data.stream()
                .filter(it -> it.getDegMalig() != DegMalig.UNKNOWN)
                .collect(Collectors.groupingBy(BreastCancerData::getDegMalig));
    }

}
