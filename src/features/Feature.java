package features;

import java.util.List;
import java.util.Map;

public interface Feature {
    double calculateEntropy(List<BreastCancerData> data);

    default <T> double calculateEntropy(Map<T, Long> occurrences, long dataSize, int options) {
        double sum = 0.0;
        for (Long numberOfNodeCapsOccurrence : occurrences.values()) {
            sum += (numberOfNodeCapsOccurrence * 1.0 / dataSize) *
                    (Math.log(numberOfNodeCapsOccurrence * 1.0 / dataSize) /
                            Math.log(options)); // needed for logb(n) = loge(n) / loge(b)
        }
        return -sum;
    }
}
