package eu.xenit.custodian.sentinel.asserts.error;

import java.util.Set;
import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

public class ShouldHaveNoFields extends BasicErrorMessageFactory {

    public static ErrorMessageFactory shouldHaveNoFields(Class<?> actual, Set<String> fields) {
        return new ShouldHaveNoFields(actual, fields);
    }

    private ShouldHaveNoFields(Class<?> actual, Set<String> fields) {
        super("%nExpecting%n" +
                "  <%s>%n" +
                "not to have any fields but it has:%n" +
                "  <%s>", actual, fields);
    }
}
