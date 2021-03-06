import java.util.*;

public class Node {
    public String value; //feature or class
    public String answer; //characteristic
    public List<Node> children = new ArrayList<Node>();

    public Node(String value, String answer, List<Node> children) {
        this.value = value;
        this.answer = answer;
        this.children = children;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value='" + value + '\'' +
                ", answer='" + answer + '\'' +
                ", children=" + Arrays.toString(children.toArray()) +
                '}';
    }
}
