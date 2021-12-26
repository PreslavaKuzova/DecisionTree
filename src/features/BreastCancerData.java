package features;

public class BreastCancerData {
    private final Classification classification;
    private final Age age;
    private final Menopause menopause;
    private final String tumorSize;
    private final String invNodes;
    private final NodeCaps nodeCaps;
    private final DegMalig degMalig;
    private final Breast breast;
    private final BreastQuadrant quadrant;
    private final Irradiat irradiat;

    public BreastCancerData(String[] values) {
        this.classification = Classification.getClassification(values[0]);
        this.age = Age.getAge(values[1]);
        this.menopause = Menopause.getMenopause(values[2]);
        this.tumorSize = values[3];
        this.invNodes = values[4];
        this.nodeCaps = NodeCaps.getNodeCaps(values[5]);
        this.degMalig = DegMalig.getValue(Integer.parseInt(values[6]));
        this.breast = Breast.getBreast(values[7]);
        this.quadrant = BreastQuadrant.getQuadrant(values[8]);
        this.irradiat = Irradiat.getIrradiat(values[9]);
    }

    public Classification getClassification() {
        return classification;
    }

    public Age getAge() {
        return age;
    }

    public Menopause getMenopause() {
        return menopause;
    }

    public String getTumorSize() {
        return tumorSize;
    }

    public String getInvNodes() {
        return invNodes;
    }

    public NodeCaps getNodeCaps() {
        return nodeCaps;
    }

    public DegMalig getDegMalig() {
        return degMalig;
    }

    public Breast getBreast() {
        return breast;
    }

    public BreastQuadrant getQuadrant() {
        return quadrant;
    }

    public Irradiat getIrradiat() {
        return irradiat;
    }

    @Override
    public String toString() {
        return "features.BreastCancerData{" +
                "classification=" + classification +
                ", age=" + age +
                ", menopause=" + menopause +
                ", tumorSize='" + tumorSize + '\'' +
                ", invNodes='" + invNodes + '\'' +
                ", nodeCaps=" + nodeCaps +
                ", degMalig=" + degMalig +
                ", breast=" + breast +
                ", quadrant=" + quadrant +
                ", irradiat=" + irradiat +
                '}';
    }
}
