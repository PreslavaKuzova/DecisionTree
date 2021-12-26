
import features.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        List<BreastCancerData> data = FileDevice.read("breast-cancer.data");

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