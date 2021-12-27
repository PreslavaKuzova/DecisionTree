import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DecisionTree {
    private Feature root;
    private List<Node> nodes;

    public DecisionTree(Feature root, List<BreastCancerData> data) {
        this.root = root;

        Map<String, List<BreastCancerData>> subsets = data.stream()
                .filter(it -> !it.getFeatureAtPosition(root.getPosition()).equals("?"))
                .collect(Collectors.groupingBy(it -> it.getFeatureAtPosition(root.getPosition())));

        for (List<BreastCancerData> subset: subsets.values()) {
            //calculate information gain for each attribute
            //decide on the root (one with the best information gain)
            //split
            //repeat
        }

        //we stop when
        //for an attribute all elements are from the same class
        //no elements in the subset

    }
}
