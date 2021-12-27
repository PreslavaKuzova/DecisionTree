
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        List<BreastCancerData> data = FileDevice.read("breast-cancer.data");

        //If we were to continue we would use recursion to keep splitting
        // each split with a goal to end each branch with an entropy of zero.
//        Feature highest = findHighestInformationGainFeature(data);
//        Map<String, List<BreastCancerData>> featureGroupedData = getOccurrenceMap(data, highest.getPosition());


        Node root = calculateDecisionTree(data, null);

        //calculateDecisionTree(data, featureGroupedData);

        //split by the found feature
        //continue splitting not taking it into account
    }

    private static Node calculateDecisionTree(
            List<BreastCancerData> data,
            String answer
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

        //the key is the characteristic value (eg age 20-29, 30-39 etc)
        //the value is subset of the whole list that matches this characteristic
        Feature bestFeature = findHighestInformationGainFeature(data);
        Map<String, List<BreastCancerData>> featureGroupedData =
                getOccurrenceMap(data, bestFeature.getPosition());

        Node node = new Node(bestFeature.name(), answer, new ArrayList<>());

        for (Map.Entry<String, List<BreastCancerData>> entry : featureGroupedData.entrySet()) {
            String subsetAnswer = entry.getKey();
            List<BreastCancerData> subsetFromThatAnswer = entry.getValue();

            //TODO need to remove the current feature aka bestFeature
            node.children.add(calculateDecisionTree(subsetFromThatAnswer, subsetAnswer));
        }

        return node;
    }

    private static boolean doAllElementsHaveSameClass(List<BreastCancerData> subsetFromThatAnswer) {
        return Collections.frequency(subsetFromThatAnswer.stream().map(BreastCancerData::getClassification).toList(), subsetFromThatAnswer.get(0).getClassification()) == subsetFromThatAnswer.size();
    }

    private static Feature findHighestInformationGainFeature(List<BreastCancerData> data) {
        Feature highestInformationGainFeature = null;
        double highestInformationGain = Integer.MIN_VALUE;
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