package eu.xenit.custodian.sentinel.analyzer.dependencies;

import eu.xenit.custodian.sentinel.analyzer.ItemContainer;
import java.util.LinkedHashMap;
import java.util.function.Function;

public class ConfigurationContainer extends ItemContainer<String, ConfigurationResult> {

    public ConfigurationContainer() {
        this(ConfigurationResult::getName);
    }

    public ConfigurationContainer(Function<ConfigurationResult, String> keyFunction) {
        super(new LinkedHashMap<>(), keyFunction);
    }

    public void add(ConfigurationResult result) {

        if (result == null) {
            throw new IllegalArgumentException("ConfigurationResult must not be null");
        }

        String key = result.getName();
        if (key == null) {
            throw new IllegalArgumentException("No valid key for value: "+result);
        }

        this.add(key, result);
    }
}
