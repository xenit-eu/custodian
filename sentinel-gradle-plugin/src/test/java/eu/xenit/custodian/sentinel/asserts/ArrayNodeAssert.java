package eu.xenit.custodian.sentinel.asserts;

import static eu.xenit.custodian.sentinel.asserts.JsonNodeAssert.assertThat;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.function.Consumer;
import org.assertj.core.api.Condition;
import org.assertj.core.api.IterableAssert;

public class ArrayNodeAssert extends JsonAssert<ArrayNodeAssert, ArrayNode>
{

    private final IterableAssert<JsonNode> iterableAssert;

    public ArrayNodeAssert(ArrayNode actual) {
        super(actual, ArrayNodeAssert.class);

        this.iterableAssert = new IterableAssert<>(actual);
    }

    public ArrayNodeAssert isEmpty() {
        return this.hasSize(0);
    }

    public ArrayNodeAssert hasSize(int expected) {
        if (this.actual.size() != expected) {
            throw failures.failure(info, shouldHaveSize(actual, this.actual.size(), expected));
        }
        return myself;
    }

    public ArrayNodeAssert haveExactlyOne(Condition<? super JsonNode> condition) {
        return this.haveExactly(1, condition);
    }

    public ArrayNodeAssert haveExactly(int times, Condition<? super JsonNode> condition) {
        this.iterableAssert.haveExactly(times, condition);
        return myself;
    }

    public ArrayNodeAssert hasOnlyOneElementSatisfying(Consumer<JsonNode> assertions) {
        this.iterableAssert.hasOnlyOneElementSatisfying(assertions);
        return myself;
    }

    public ArrayNodeAssert assertOnlyOneElement(Consumer<JsonNodeAssert> callback) {
        this.iterableAssert.hasOnlyOneElementSatisfying(element -> {
            callback.accept(assertThat(element));
        });
        return myself;
    }

    public ArrayNodeAssert doNotHave(Condition<? super JsonNode> condition) {
        this.iterableAssert.doNotHave(condition);
        return myself;
    }

}
