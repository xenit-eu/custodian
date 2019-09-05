package eu.xenit.custodian.sentinel.asserts;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.function.Consumer;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Condition;
import org.assertj.core.internal.Failures;

public abstract class AbstractJsonNodeAssert<SELF extends AbstractJsonNodeAssert<SELF, ACTUAL>, ACTUAL extends JsonNode>
        extends AbstractAssert<SELF, ACTUAL> {

    protected Failures failures = Failures.instance();

    public AbstractJsonNodeAssert(ACTUAL actual, Class<?> selfType)
    {
        super(actual, selfType);
    }

    /**
     * Asserts that a field with a specified fieldName exists
     *
     * @param fieldName the expected field
     * @return {@code this} assertion object
     */
    public SELF has(String fieldName)
    {
        if (!this.actual.has(fieldName)) {
            this.failWithMessage("Field '%s' was not found.");
        }

        return this.myself;
    }

    /**
     * Asserts that the current node is Object node
     */
    public SELF isObject()
    {
        if (!this.actual.isObject())
        {
            this.failWithMessage("Expected object, but is %s instead", this.actual.getNodeType());
        }

        return this.myself;
    }

    /**
     * Asserts that the current node is an Array node
     */
    public SELF isArray()
    {
        if (!this.actual.isArray())
        {
            this.failWithMessage("Expected array, but is %s instead", this.actual.getNodeType());
        }

        return this.myself;
    }




    /**
     * Assert that the current node is a "virtual" nodes which represent a missing field
     */
    public SELF isMissingNode() {
        if (!this.actual.isMissingNode())
        {
            this.failWithMessage("Expected field to be missing, but is %s instead", this.actual.getNodeType());
        }
        return this.myself;
    }


    /**
     * Fluent helper to run assertions on a specified field. The argument of the callback is the
     * specified field. If the field does not exists, the callback will be invoked with a "missing node"
     * instead.
     *
     * @param fieldName the specified field
     * @param callback the callback that will be invoked
     * @return {@code this} assertion object
     */
    public SELF assertField(String fieldName, Consumer<JsonNodeAssert> callback)
    {
        JsonNode path = this.actual.findPath(fieldName);
        callback.accept(new JsonNodeAssert(path));
        return this.myself;
    }

    public SELF assertField(String fieldName, Condition<JsonNode> condition)
    {
        return this.assertField(fieldName, jsonNodeAssert -> jsonNodeAssert.has(condition));
    }

    /**
     * Fluent helper that asserts the current node is an array and invokes a callback to run assertions on.
     *
     * @param callback the callback that will be invoked
     * @return {@code this} assertion object
     */
    public SELF isArray(Consumer<ArrayNodeAssert> callback)
    {
        this.isArray();

        ArrayNode array = (ArrayNode) this.actual;
        callback.accept(new ArrayNodeAssert(array));

        return this.myself;
    }

    public SELF assertFieldArray(String fieldName, Consumer<ArrayNodeAssert> callback)
    {
        JsonNode path = this.actual.findPath(fieldName);
        callback.accept(new ArrayNodeAssert((ArrayNode) path));
        return this.myself;
    }


}
