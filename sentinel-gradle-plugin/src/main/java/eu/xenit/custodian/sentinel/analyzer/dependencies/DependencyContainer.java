package eu.xenit.custodian.sentinel.analyzer.dependencies;

import eu.xenit.custodian.sentinel.analyzer.ItemContainer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import org.gradle.api.artifacts.ModuleIdentifier;

public class DependencyContainer extends ItemContainer<ModuleIdentifier, AnalyzedDependency> {

    public DependencyContainer() {
        this(defaultKeyFunction());
    }

    public DependencyContainer(Function<AnalyzedDependency, ModuleIdentifier> keyFunction) {
        this(new LinkedHashMap<>(), keyFunction);
    }

    private DependencyContainer(Map<ModuleIdentifier, AnalyzedDependency> items,
                               Function<AnalyzedDependency, ModuleIdentifier> keyFunction) {
        super(items, keyFunction);
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
