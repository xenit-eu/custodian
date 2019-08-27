package eu.xenit.gradle.sentinel.analyzer.model;

import java.util.LinkedHashMap;
import java.util.function.Function;
import org.gradle.api.artifacts.ModuleIdentifier;

public class DependencyContainer extends ItemContainer<ModuleIdentifier, AnalyzedDependency> {

    public DependencyContainer() {
        this(AnalyzedDependency::getId);
    }

    public DependencyContainer(Function<AnalyzedDependency, ModuleIdentifier> keyFunction) {
        super(new LinkedHashMap<>(), keyFunction);
    }
}
