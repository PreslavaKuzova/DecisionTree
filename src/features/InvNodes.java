package features;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InvNodes implements Feature<String> {
    @Override
    public Map<String, List<BreastCancerData>> getOccurrenceMap(List<BreastCancerData> data) {
        return data.stream()
                .collect(Collectors.groupingBy(BreastCancerData::getInvNodes));
    }
}
