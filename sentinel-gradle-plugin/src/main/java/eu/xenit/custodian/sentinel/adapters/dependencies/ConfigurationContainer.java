package eu.xenit.custodian.sentinel.adapters.dependencies;

import eu.xenit.custodian.sentinel.domain.AnalysisContentPart;
import eu.xenit.custodian.sentinel.domain.ItemContainer;

public class ConfigurationContainer extends ItemContainer<String, ConfigurationResult> implements AnalysisContentPart {

    public ConfigurationContainer() {
        super();
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
