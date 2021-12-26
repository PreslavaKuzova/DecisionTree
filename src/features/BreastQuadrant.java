package features;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum BreastQuadrant implements Feature<BreastQuadrant> {
    LEFT_UP("left_up"),
    LEFT_LOW("left_low"),
    RIGHT_UP("right_up"),
    RIGHT_LOW("right_low"),
    CENTRAL("central"),
    UNKNOWN("");

    private final String quadrant;

    BreastQuadrant(String quadrant) {
        this.quadrant = quadrant;
    }

    public String getQuadrant() {
        return quadrant;
    }

    public static BreastQuadrant getQuadrant(String value) {
        for (BreastQuadrant variable : values())
            if (variable.getQuadrant().equalsIgnoreCase(value)) return variable;
        return UNKNOWN;
    }

    @Override
    public Map<BreastQuadrant, List<BreastCancerData>> getOccurrenceMap(List<BreastCancerData> data) {
        return data.stream()
                .filter(it -> it.getQuadrant() != BreastQuadrant.UNKNOWN)
                .collect(Collectors.groupingBy(BreastCancerData::getQuadrant));
    }
}
