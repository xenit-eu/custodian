package eu.xenit.custodian.sentinel.adapters.dependencies;

import eu.xenit.custodian.sentinel.domain.AnalysisContentPart;
import eu.xenit.custodian.sentinel.domain.ItemContainer;
import java.util.stream.Stream;

public class DependenciesContainer extends ItemContainer<String, ConfigurationDependenciesContainer> implements AnalysisContentPart {

    public DependenciesContainer() {
        super();
    }

    public DependenciesContainer(Stream<ConfigurationDependenciesContainer> configurations) {
        this();
        configurations.forEach(this::add);
    }

    public void add(ConfigurationDependenciesContainer result) {

        if (result == null) {
            throw new IllegalArgumentException("ConfigurationResult must not be null");
        }

        String key = result.getName();
        if (key == null) {
            throw new IllegalArgumentException("No valid key for value: "+result);
        }

        this.add(key, result);
    }

    @Override
    public String getContributionName() {
        return DependenciesAnalysisContributor.NAME;
    }
}
