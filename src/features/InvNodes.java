package features;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InvNodes implements Feature {
    private final String OPTIONS = "0-2, 3-5, 6-8, 9-11, 12-14, 15-17, 18-20, 21-23, 24-26, 27-29, 30-32, 33-35, 36-39";

    @Override
    public double calculateEntropy(List<BreastCancerData> data) {
        Map<String, Long> occurrences = data.stream()
                .map(BreastCancerData::getInvNodes)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return calculateEntropy(occurrences, data.size(), OPTIONS.split(",").length);
    }
}
