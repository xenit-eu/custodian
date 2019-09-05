package eu.xenit.custodian.sentinel.analyzer.model;

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

    private static Function<AnalyzedDependency, ModuleIdentifier> defaultKeyFunction() {
        return AnalyzedDependency::getId;
    }
}
