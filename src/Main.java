
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    static final int N_FOLD_CROSS_VALIDATION_VALUE = 15;

    static List<BreastCancerData> allBreastCancerData;
    static List<List<BreastCancerData>> nFoldCrossValidationBreastCancerData = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        allBreastCancerData = FileDevice.read("breast-cancer.data");
        divideIntoNTestGroups();
        calculateAccuracy();
    }

    private static void calculateAccuracy() {
        double modelAccuracy = 0.0;

        for (int i = 0; i < N_FOLD_CROSS_VALIDATION_VALUE; i++) {
            System.out.println("Test No " + (i + 1));

            // Only use N - 1 lists of BreastCancerData out of all N
            // Create a list that holds off the data of these N - 1 lists (train set)
            List<BreastCancerData> currentTestData = extractCurrentTestData(i);

            // For each train set calculate a DecisionTree
            Node currentTestSetRoot = calculateDecisionTree(currentTestData, null, new ArrayList<>());

            int correctPredictions = 0;

            // For each value in the test set find nFoldCrossValidationBreastCancerData.get(i)
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
        //node is the current value
        //go down the decision tree somehow to find the prediction to the given classifier

        //if the node holds feature value, we haven't reached a leaf that holds information about the class yet
        Feature feature;
        try {
            feature = Feature.valueOf(node.value);
        } catch (IllegalArgumentException e) {
            // Feature.valueOf(node.value) throws IllegalArgumentException when the enum doesn't contain such value
            // This means that the node contains not a Feature value but a Classifier value, and we have reached a leaf
            // Bottom of the recursion
            return Classification.getClassification(node.value);
        }

        // We want to extract the answer that will lead us to the next node
        String answer = breastCancerData.getFeatureAtPosition(feature.getPosition());

        Classification classification = Classification.UNKNOWN;
        for (Node child : node.children) {
            // We find the child that holds this answer
            // Each node contains:
            // - Feature (AGE, BREAST, etc.) value
            // - Answer that lead us to that value (for instance 20-29, 30-29, ... for AGE )
            // - Children Nodes
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
            // Leaf that is empty
            throw new IllegalArgumentException();
        }

        if (doAllElementsHaveSameClass(data)) {
            // Leaf that holds class value
            // Classification, characteristic that lead us here, no children
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

       // Find the feature that has the highest information gain
        Feature bestFeature = findHighestInformationGainFeature(data, bannedFeatures);

        // Split the data into subset for each characteristic value that the bestFeature might have
        // The key is the characteristic value (eg age 20-29, 30-39 etc)
        // The value is subset of the whole list that matches this characteristic
        Map<String, List<BreastCancerData>> featureGroupedData =
                getOccurrenceMap(data, bestFeature.getPosition());

        // The node is NOT a leaf - that means it contains Feature value as a characteristic
        // The answer is the answer that lead us here
        // For instance, the parent node is AGE, the value that lead us here is 20-29 so answer will be 20-29
        Node node = new Node(bestFeature.name(), answer, new ArrayList<>());

        // For each value the characteristic has, calculate the following child decision subtree
        for (Map.Entry<String, List<BreastCancerData>> entry : featureGroupedData.entrySet()) {
            String subsetAnswer = entry.getKey();
            List<BreastCancerData> subsetFromThatAnswer = entry.getValue();

            // Banned features are feature we have already split the data by
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
        // Split by a feature
        // Calculate the entropy of the target column for each subset
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