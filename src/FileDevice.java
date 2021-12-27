import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileDevice {
    public static final String REGEX = ",";

    public static List<BreastCancerData> read(String inputDirectory) throws FileNotFoundException {
        List<BreastCancerData> data = new ArrayList<>();

        InputStream stream = new FileInputStream(inputDirectory);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {

            String line = "";
            while ((line = reader.readLine()) != null) {
                data.add(new BreastCancerData(line.split(REGEX)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}

