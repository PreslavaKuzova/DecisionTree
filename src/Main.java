
import features.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        List<BreastCancerData> data = FileDevice.read("breast-cancer.data");

        //If we were to continue we would use recursion to keep splitting
        // each split with a goal to end each branch with an entropy of zero.
        System.out.println(findHighestInformationGainFeature(data));
    }

    private static Feature findHighestInformationGainFeature(List<BreastCancerData> data) {
        Feature highestInformationGainFeature = null;
        double highestInformationGain = Double.MIN_VALUE;
        for (Feature feature : Feature.values()) {
            double currentInformationGain = calculateInformationGain(data, getOccurrenceMap(data, feature.getPosition()));
            if (currentInformationGain > highestInformationGain) {
                highestInformationGain = currentInformationGain;
                highestInformationGainFeature = feature;
            }
        }

        return highestInformationGainFeature;
    }

    public static Map<String, List<BreastCancerData>> getOccurrenceMap(List<BreastCancerData> data, int position) {
        return data.stream()
                .filter(it -> !it.getFeatureAtPosition(position).equals("?"))
                .collect(Collectors.groupingBy(it -> it.getFeatureAtPosition(position)));
    }

    // If the result is positive, we’ve lowered entropy with our split.
    // The higher the result is, the more we’ve lowered entropy.
    public static <T> double calculateInformationGain(List<BreastCancerData> data, Map<T, List<BreastCancerData>> occurrences) {
        //split by a feature
        //calculate the entropy of the target column for each subset
        double sum = 0;
        for (List<BreastCancerData> list : occurrences.values()) {
            sum += (list.size() * 1.0 / data.size()) * Classification.calculateEntropy(list);
        }

        return Classification.calculateEntropy(data) - sum;
    }
}