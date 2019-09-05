package eu.xenit.custodian.sentinel.asserts;

import static eu.xenit.custodian.sentinel.asserts.error.ShouldHaveNoFields.shouldHaveNoFields;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JsonNodeAssert extends AbstractJsonNodeAssert<JsonNodeAssert, JsonNode> {

    public JsonNodeAssert(File file) {
        this(parse(file));
    }

    public JsonNodeAssert(String content) {
        this(parse(content));
    }

    public JsonNodeAssert(JsonNode actual) {
        super(actual, JsonNodeAssert.class);
    }

    public JsonNodeAssert isEmpty() {
        if (this.actual.fields().hasNext()) {
            throw failures.failure(info, shouldHaveNoFields(actual.getClass(), asSet(this.actual.fieldNames())));
        }
        return myself;

    }

    private static JsonNode parse(String content) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonNode parse(File file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <E> Set<E> asSet(Iterator<E> sourceIterator) {
        Iterable<E> iterable = () -> sourceIterator;
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toSet());
    }
}
