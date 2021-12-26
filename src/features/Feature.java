package features;

public enum Feature {
    AGE(0),
    MENOPAUSE(1),
    TUMOR_SIZE(2),
    INV_NODES(3),
    NODE_CAPS(4),
    DEG_MALIG(5),
    BREAST(6),
    BREAST_QUADRANT(7),
    IRRADIATE(8);

    private final int position;

    Feature(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
