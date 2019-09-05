package eu.xenit.custodian.sentinel.asserts.condition;

import com.fasterxml.jackson.databind.JsonNode;
import org.assertj.core.api.Condition;

public class Repository {

    public static Condition<JsonNode> withUrl(String url) {
        return new Condition<>(repo -> repo.path("url").textValue().equalsIgnoreCase(url),
                "Expected repository with url '%s'", url);
    }
}
