package features;

import java.util.List;
import java.util.Map;

public interface Feature<T> {
    Map<T, List<BreastCancerData>> getOccurrenceMap(List<BreastCancerData> data);
}
