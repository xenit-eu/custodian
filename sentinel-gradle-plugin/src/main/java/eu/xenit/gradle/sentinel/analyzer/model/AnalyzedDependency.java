package eu.xenit.gradle.sentinel.analyzer.model;

import lombok.Setter;
import lombok.Getter;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ModuleIdentifier;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.internal.artifacts.DefaultModuleVersionIdentifier;

public class AnalyzedDependency implements ModuleVersionIdentifier {

    private final ModuleVersionIdentifier dependency;

    @Getter @Setter
    private ComponentType component;

    @Getter @Setter
    private DependencyResolution resolution;

//    private final Dependency dependency;

    public static AnalyzedDependency create(Dependency dependency)
    {
        return new AnalyzedDependency(dependency.getGroup(), dependency.getName(), dependency.getVersion());
    }

    public AnalyzedDependency(String group, String name, String version)
    {
        this(DefaultModuleVersionIdentifier.newId(group, name, version));
    }

    public AnalyzedDependency(ModuleVersionIdentifier dependency) {
        this.dependency = dependency;
    }

    public ModuleIdentifier getId() {
        return this.dependency.getModule();
    }

    @Override
    public String toString() {
        return this.dependency.toString();
    }

    @Override
    public String getVersion() {
        return this.dependency.getVersion();
    }

    @Override
    public String getGroup() {
        return this.dependency.getGroup();
    }

    @Override
    public String getName() {
        return this.dependency.getName();
    }

    @Override
    public ModuleIdentifier getModule() {
        return this.dependency.getModule();
    }
}
