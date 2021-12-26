package features;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TumorSize implements Feature {
    private final String OPTIONS = "0-4, 5-9, 10-14, 15-19, 20-24, 25-29, 30-34, 35-39, 40-44, 45-49, 50-54, 55-59";

    @Override
    public double calculateEntropy(List<BreastCancerData> data) {
        Map<String, Long> occurrences = data.stream()
                .map(BreastCancerData::getTumorSize)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return calculateEntropy(occurrences, data.size(), OPTIONS.split(",").length);
    }
}
