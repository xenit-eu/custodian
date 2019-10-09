package eu.xenit.custodian.sentinel.asserts;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.Optional;
import java.util.function.Consumer;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Condition;
import org.assertj.core.internal.Failures;
import org.gradle.internal.impldep.aQute.bnd.osgi.resource.FilterParser.Op;

public abstract class JsonAssert<SELF extends JsonAssert<SELF, ACTUAL>, ACTUAL extends JsonNode>
        extends AbstractAssert<SELF, ACTUAL> {

    private final Optional<String> name;

    Failures failures = Failures.instance();

    protected JsonAssert(ACTUAL actual, Class<?> selfType) {
        this(actual, selfType, null);
    }

    protected JsonAssert(ACTUAL actual, Class<?> selfType, String name) {
        super(actual, selfType);

        this.name = Optional.ofNullable(name);
    }

    /**
     * Asserts that a field with a specified fieldName exists
     *
     * @param fieldName the expected field
     * @return {@code this} assertion object
     */
    public SELF has(String fieldName) {
        if (!this.actual.has(fieldName)) {
            this.failWithMessage("Field '%s' was not found.", fieldName);
        }

        return this.myself;
    }

    public SELF assertField(String fieldName, String value) {
        this.assertField(fieldName, field -> field.hasTextValue(value));
        return this.myself;
    }

    public SELF hasTextValue(String value) {
        this.isTextual();
        assertThat(this.actual.textValue()).isEqualTo(value);
        return this.myself;
    }

    public SELF doesNotHaveTextValue(String value) {
        this.isTextual();
        assertThat(this.actual.textValue()).isNotEqualTo(value);
        return this.myself;
    }

//    public SELF TextValue(String value) {
//        this.isTextual();
//        assertThat(this.actual.textValue()).isEqualTo(value);
//        return this.myself;
//    }

    /**
     * Asserts that the current node is Object node
     */
    public SELF isObject() {
        if (!this.actual.isObject()) {
            if (this.name.isPresent()) {
                this.failWithMessage("Expected field '%s' to be an object, but is %s instead", this.name.get(),
                        this.actual.getNodeType());
            } else {
                this.failWithMessage("Expected object, but is %s instead", this.actual.getNodeType());
            }
        }

        return this.myself;
    }

    /**
     * Asserts that the current node is an Array node
     */
    public SELF isArray() {
        if (!this.actual.isArray()) {
            if (this.name.isPresent()) {
                this.failWithMessage("Expected field '%s' to be an array, but is %s instead", this.name.get(),
                        this.actual.getNodeType());
            } else {
                this.failWithMessage("Expected array, but is %s instead", this.actual.getNodeType());
            }
        }

        return this.myself;
    }

    /**
     * Asserts that the current node reprents the null-value
     */
    public SELF isNullValue() {
        if (!this.actual.isNull()) {
            this.failWithMessage("Expected null, but is %s instead", this.actual.getNodeType());
        }

        return this.myself;
    }

    @Override
    public SELF isNotNull() {

        if (this.actual.isNull()) {
            if (this.name.isPresent()) {
                this.failWithMessage("Field '%s' is %s unexpectedly", this.name.get(),
                        this.actual.getNodeType());
            } else {
                this.failWithMessage("Field is %s unexpectedly", this.actual.getNodeType());
            }
        }
        return super.isNotNull();
    }

    public SELF isTextual() {
        if (!this.actual.isTextual()) {
            this.failWithMessage("Expected type text, but is %s instead", this.actual.getNodeType());
        }

        return this.myself;
    }


    /**
     * Assert that the current node is a "virtual" nodes which represent a missing field
     */
    public SELF isMissingNode() {
        if (!this.actual.isMissingNode()) {
            this.failWithMessage("Expected field to be missing, but is %s instead", this.actual.getNodeType());
        }
        return this.myself;
    }


    /**
     * Fluent helper to run assertions on a specified field. The argument of the callback is the specified field. If the
     * field does not exists, the callback will be invoked with a "missing node" instead.
     *
     * @param fieldName the specified field
     * @param callback the callback that will be invoked
     * @return {@code this} assertion object
     */
    public SELF assertField(String fieldName, Consumer<JsonNodeAssert> callback) {
        JsonNode path = this.actual.findPath(fieldName);
        callback.accept(new JsonNodeAssert(path, fieldName));
        return this.myself;
    }

    public SELF assertField(String fieldName, Condition<JsonNode> condition) {
        return this.assertField(fieldName, jsonNodeAssert -> jsonNodeAssert.has(condition));
    }

    /**
     * Fluent helper that asserts the current node is an array and invokes a callback to run assertions on.
     *
     * @param callback the callback that will be invoked
     * @return {@code this} assertion object
     */
    public SELF isArray(Consumer<ArrayNodeAssert> callback) {
        this.isArray();

        ArrayNode array = (ArrayNode) this.actual;
        callback.accept(new ArrayNodeAssert(array));

        return this.myself;
    }

    public SELF assertFieldArray(String fieldName, Consumer<ArrayNodeAssert> callback) {
        JsonNode path = this.actual.findPath(fieldName);
        callback.accept(new ArrayNodeAssert((ArrayNode) path));
        return this.myself;
    }


}
