
import java.io.FileNotFoundException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        List<BreastCancerData> data = FileDevice.read("breast-cancer.data");
        for (BreastCancerData cancerData: data) {
            System.out.println(cancerData.toString());
        }
    }
}
