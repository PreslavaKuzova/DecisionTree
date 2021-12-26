package features;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum NodeCaps implements Feature<NodeCaps> {
    YES("yes"),
    NO("no"),
    UNKNOWN("");

    private final String nodeCaps;

    NodeCaps(String nodeCaps) {
        this.nodeCaps = nodeCaps;
    }

    public String getNodeCaps() {
        return nodeCaps;
    }

    public static NodeCaps getNodeCaps(String value) {
        for (NodeCaps variable : values())
            if (variable.getNodeCaps().equalsIgnoreCase(value)) return variable;
        return UNKNOWN;
    }

    @Override
    public Map<NodeCaps, List<BreastCancerData>> getOccurrenceMap(List<BreastCancerData> data) {
        return data.stream()
                .filter(it -> it.getNodeCaps() != NodeCaps.UNKNOWN)
                .collect(Collectors.groupingBy(BreastCancerData::getNodeCaps));
    }
}
