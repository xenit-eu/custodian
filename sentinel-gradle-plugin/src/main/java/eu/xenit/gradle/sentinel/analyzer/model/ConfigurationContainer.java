package eu.xenit.gradle.sentinel.analyzer.model;

import java.util.LinkedHashMap;
import java.util.function.Function;

public class ConfigurationContainer extends ItemContainer<String, ConfigurationResult> {

    public ConfigurationContainer() {
        this(ConfigurationResult::getName);
    }

    public ConfigurationContainer(Function<ConfigurationResult, String> keyFunction) {
        super(new LinkedHashMap<>(), keyFunction);
    }
}
