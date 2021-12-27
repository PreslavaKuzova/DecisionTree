
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        List<BreastCancerData> data = FileDevice.read("breast-cancer.data");

        Node root = calculateDecisionTree(data, null, new ArrayList<>());
        System.out.println(root);
    }

    private static Node calculateDecisionTree(
            List<BreastCancerData> data,
            String answer,
            List<Feature> bannedFeatures
    ) {
        if (data.isEmpty()) {
            //leaf that is empty
            throw new IllegalArgumentException();
        }

        if (doAllElementsHaveSameClass(data)) {
            //leaf that holds class value
            //classification, characteristic that lead us here, no children
            return new Node(
                    data.get(0).getClassification().getClassVariable(),
                    answer,
                    Collections.emptyList()
            );
        }

        if (bannedFeatures.size() == Feature.values().length) {
            return new Node(
                    Classification.UNKNOWN.getClassVariable(),
                    answer,
                    Collections.emptyList()
            );
        }

        //the key is the characteristic value (eg age 20-29, 30-39 etc)
        //the value is subset of the whole list that matches this characteristic
        Feature bestFeature = findHighestInformationGainFeature(data, bannedFeatures);
        Map<String, List<BreastCancerData>> featureGroupedData =
                getOccurrenceMap(data, bestFeature.getPosition());

        Node node = new Node(bestFeature.name(), answer, new ArrayList<>());

        for (Map.Entry<String, List<BreastCancerData>> entry : featureGroupedData.entrySet()) {
            String subsetAnswer = entry.getKey();
            List<BreastCancerData> subsetFromThatAnswer = entry.getValue();

            List<Feature> allBannedFeatures = getAllBannedFeatures(bannedFeatures, bestFeature);

            node.children.add(calculateDecisionTree(subsetFromThatAnswer, subsetAnswer, allBannedFeatures));
        }

        return node;
    }

    private static List<Feature> getAllBannedFeatures(List<Feature> bannedFeatures, Feature bestFeature) {
        List<Feature> allBannedFeatures = new ArrayList<>();
        allBannedFeatures.add(bestFeature);
        allBannedFeatures.addAll(bannedFeatures);
        return allBannedFeatures;
    }

    private static boolean doAllElementsHaveSameClass(List<BreastCancerData> subsetFromThatAnswer) {
        return Collections.frequency(subsetFromThatAnswer.stream().map(BreastCancerData::getClassification).toList(), subsetFromThatAnswer.get(0).getClassification()) == subsetFromThatAnswer.size();
    }

    private static Feature findHighestInformationGainFeature(List<BreastCancerData> data, List<Feature> bannedFeatures) {
        Feature highestInformationGainFeature = null;
        double highestInformationGain = Integer.MIN_VALUE;

        for (Feature feature : removeBannedFeatures(Arrays.stream(Feature.values()).toList(), bannedFeatures)) {
            double currentInformationGain = calculateInformationGain(data, getOccurrenceMap(data, feature.getPosition()));
            if (currentInformationGain > highestInformationGain) {
                highestInformationGain = currentInformationGain;
                highestInformationGainFeature = feature;
            }
        }

        return highestInformationGainFeature;
    }

    public static List<Feature> removeBannedFeatures(List<Feature> allFeatures, List<Feature> bannedFeatures) {
        // Prepare a union
        List<Feature> union = new ArrayList<>(allFeatures);
        union.addAll(bannedFeatures);
        // Prepare an intersection
        List<Feature> intersection = new ArrayList<>(allFeatures);
        intersection.retainAll(bannedFeatures);
        // Subtract the intersection from the union
        union.removeAll(intersection);

        return union;
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
        for (List<BreastCancerData> sublistOfSpitByFeatureData : occurrences.values()) {
            sum += (sublistOfSpitByFeatureData.size() * 1.0 / data.size()) * Classification.calculateEntropy(sublistOfSpitByFeatureData);
        }

        return Classification.calculateEntropy(data) - sum;
    }
}