package features;

public enum NodeCaps {
    YES("yes"),
    NO("no"),
    UNKNOWN("");

    private final String nodeCaps;

    NodeCaps(String nodeCaps) {
        this.nodeCaps = nodeCaps;
    }

    public String getNodeCaps() {
        return nodeCaps;
    }

    public static NodeCaps getNodeCaps(String value) {
        for (NodeCaps variable : values())
            if (variable.getNodeCaps().equalsIgnoreCase(value)) return variable;
        return UNKNOWN;
    }
}
