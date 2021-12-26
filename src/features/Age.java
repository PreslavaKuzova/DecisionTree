package features;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Age implements Feature {
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
    public double calculateEntropy(List<BreastCancerData> data) {
        Map<Age, Long> occurrences = data.stream()
                .map(BreastCancerData::getAge)
                .filter(it -> it != Age.UNKNOWN)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return calculateEntropy(occurrences, data.size(), Age.values().length - 1);
    }
}
