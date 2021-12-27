import java.util.List;

public class BreastCancerData {
    private final Classification classification;
    private List<String> features;

    public BreastCancerData(String[] values) {
        this.classification = Classification.getClassification(values[0]);
        features = List.of(
                values[1],
                values[2],
                values[3],
                values[4],
                values[5],
                values[6],
                values[7],
                values[8],
                values[9]
        );
    }

    public Classification getClassification() {
        return classification;
    }

    public String getFeatureAtPosition(int position) {
        return features.get(position);
    }
}
