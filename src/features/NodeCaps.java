package features;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum NodeCaps implements Feature {
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
    public double calculateEntropy(List<BreastCancerData> data) {
        Map<NodeCaps, Long> occurrences = data.stream()
                .map(BreastCancerData::getNodeCaps)
                .filter(it -> it != NodeCaps.UNKNOWN)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        
        return calculateEntropy(occurrences, data.size(), NodeCaps.values().length - 1);
    }
}
