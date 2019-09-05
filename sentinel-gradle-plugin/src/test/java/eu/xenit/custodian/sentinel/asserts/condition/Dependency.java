package eu.xenit.custodian.sentinel.asserts.condition;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Objects;
import java.util.function.Predicate;
import org.assertj.core.api.Condition;

public class Dependency {

    public static Condition<JsonNode> from(String dependency) {
        return new Condition<>(matches(dependency), "Matches from '%s'", dependency);
    }

    private static Predicate<JsonNode> matches(String dependency) {
        String[] split = dependency.split(":");

        if (split.length == 2) {
            return matches(split[0], split[1], "");
        } else if (split.length == 3) {
            return matches(split[0], split[1], split[2]);
        }

        throw new IllegalArgumentException("expected from in format <group>:<artifact>:<version>");

    }

    private static Predicate<JsonNode> matches(String group, String artifact, String version)
    {
        return jsonNode -> {
            if (jsonNode.isMissingNode()) {
                return false;
            }

            if (!Objects.equals(group, jsonNode.findPath("group").textValue())) {
                return false;
            }

            if (!Objects.equals(artifact, jsonNode.findPath("artifact").textValue())) {
                return false;
            }

            if (!Objects.equals(version, jsonNode.findPath("version").textValue())) {
                return false;
            }

            return true;
        };
    }
}
