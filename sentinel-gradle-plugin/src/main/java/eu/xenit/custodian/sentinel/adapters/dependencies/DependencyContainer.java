package eu.xenit.custodian.sentinel.adapters.dependencies;

import eu.xenit.custodian.sentinel.domain.ItemContainer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import org.gradle.api.artifacts.ModuleIdentifier;

public class DependencyContainer extends ItemContainer<ModuleIdentifier, AnalyzedDependency> {

    public DependencyContainer() {
        this(new LinkedHashMap<>());
    }

    private DependencyContainer(Map<ModuleIdentifier, AnalyzedDependency> items) {
        super(items);
    }

    public void add(AnalyzedDependency result) {

        if (result == null) {
            throw new IllegalArgumentException("ConfigurationResult must not be null");
        }

        ModuleIdentifier key = result.getId();
        if (key == null) {
            throw new IllegalArgumentException("No valid key for value: "+result);
        }

        this.add(key, result);
    }

    private static Function<AnalyzedDependency, ModuleIdentifier> defaultKeyFunction() {
        return AnalyzedDependency::getId;
    }
}
