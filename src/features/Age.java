package features;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum Age implements Feature<Age> {
    TEEN("10-19"),
    TWENTIES("20-29"),
    THIRTIES("30-39"),
    FORTIES("40-49"),
    FIFTIES("50-59"),
    SIXTIES("60-69"),
    SEVENTIES("70-79"),
    EIGHTIES("80-89"),
    NINETIES("90-99"),
    UNKNOWN("0");

    private final String age;

    Age(String age) {
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    public static Age getAge(String value) {
        for (Age variable : values())
            if (variable.getAge().equalsIgnoreCase(value)) return variable;
        return UNKNOWN;
    }

    @Override
    public Map<Age, List<BreastCancerData>> getOccurrenceMap(List<BreastCancerData> data) {
        return data.stream()
                .filter(it -> it.getAge() != Age.UNKNOWN)
                .collect(Collectors.groupingBy(BreastCancerData::getAge));
    }
}
