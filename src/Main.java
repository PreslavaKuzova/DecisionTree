
import features.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    static List<Feature> features = List.of(
            Age.UNKNOWN,
            Menopause.UNKNOWN,
            new TumorSize(),
            new InvNodes(),
            NodeCaps.UNKNOWN,
            DegMalig.UNKNOWN,
            Breast.UNKNOWN,
            BreastQuadrant.UNKNOWN,
            Irradiat.UNKNOWN
    );

    public static void main(String[] args) throws FileNotFoundException {
        List<BreastCancerData> data = FileDevice.read("breast-cancer.data");

        System.out.println(calculateInformationGain(data));
    }

    /* Entropy is used as a way to measure how “mixed” a column is.
       Specifically, entropy is used to measure disorder */
    public static double calculateEntropy(List<BreastCancerData> data, Feature feature) {
        return feature.calculateEntropy(data);
    }

    public static double calculateInformationGain(List<BreastCancerData> data) {
        //split by a feature
        //calculate the entropy of the target column for each subset
        Map<Age, List<BreastCancerData>> occurrences = data.stream()
                .filter(it -> it.getAge() != Age.UNKNOWN)
                .collect(Collectors.groupingBy(BreastCancerData::getAge));

        double informationGain = 0;
        for (List<BreastCancerData> list : occurrences.values()) {
            informationGain += (list.size() * 1.0 / data.size()) * calculateEntropy(list, Classification.UNKNOWN);
        }

        return calculateEntropy(data, Classification.UNKNOWN) - informationGain;
    }
}