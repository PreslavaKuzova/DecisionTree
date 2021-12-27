
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    static final int N_FOLD_CROSS_VALIDATION_VALUE = 10;

    static List<BreastCancerData> allBreastCancerData;
    static List<List<BreastCancerData>> nFoldCrossValidationBreastCancerData = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        allBreastCancerData = FileDevice.read("breast-cancer.data");
        divideIntoNTestGroups();

        double modelAccuracy = 0.0;

        for (int i = 0; i < N_FOLD_CROSS_VALIDATION_VALUE; i++) {
            System.out.println("Test No " + (i + 1));

            List<BreastCancerData> currentTestData = extractCurrentTestData(i);
            Node currentTestSetRoot = calculateDecisionTree(currentTestData, null, new ArrayList<>());

            int correctPredictions = 0;
            for (BreastCancerData breastCancerData : nFoldCrossValidationBreastCancerData.get(i)) {
                Classification prediction = assignToClassifier(currentTestSetRoot, breastCancerData);
                if (prediction == breastCancerData.getClassification()) {
                    correctPredictions++;
                }
            }

            double currentIterationAccuracy = correctPredictions * 1.0 / nFoldCrossValidationBreastCancerData.get(i).size();
            modelAccuracy += currentIterationAccuracy;
            System.out.println("Current test accuracy: " + currentIterationAccuracy);
        }

        System.out.println("Model accuracy: " + modelAccuracy / N_FOLD_CROSS_VALIDATION_VALUE);
    }

    private static Classification assignToClassifier(Node node, BreastCancerData breastCancerData) {
        //current test root
        //go down the decision tree somehow to find the prediction to the given classifier
        Feature feature;
        try {
            feature = Feature.valueOf(node.value);
        } catch (IllegalArgumentException e) {
            return Classification.getClassification(node.value);
        }

        String answer = breastCancerData.getFeatureAtPosition(feature.getPosition());

        Classification classification = Classification.UNKNOWN;
        for (Node child : node.children) {
            if (child.answer.equals(answer)) {
                classification = assignToClassifier(child, breastCancerData);
            }
        }

        return classification;
    }

    private static List<BreastCancerData> extractCurrentTestData(int position) {
        List<BreastCancerData> currentTestData = new ArrayList<>();

        for (int i = 0; i < nFoldCrossValidationBreastCancerData.size(); i++) {
            if (i != position) {
                currentTestData.addAll(nFoldCrossValidationBreastCancerData.get(i));
            }
        }

        return currentTestData;
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

    //divide into n test groups
    private static void divideIntoNTestGroups() {
        int chunk = allBreastCancerData.size() / N_FOLD_CROSS_VALIDATION_VALUE;

        for (int i = 0; i < allBreastCancerData.size(); i += chunk) {
            nFoldCrossValidationBreastCancerData
                    .add(allBreastCancerData.subList(i, Math.min(i + chunk, allBreastCancerData.size())));
        }
    }
}