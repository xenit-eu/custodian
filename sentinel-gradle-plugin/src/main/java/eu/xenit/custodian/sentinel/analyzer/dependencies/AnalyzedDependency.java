package eu.xenit.custodian.sentinel.analyzer.dependencies;

import lombok.Setter;
import lombok.Getter;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ModuleIdentifier;
import org.gradle.api.internal.artifacts.DefaultModuleIdentifier;

public class AnalyzedDependency implements ModuleIdentifier {

    @Getter
    private final ModuleIdentifier module;

    @Getter
    private String version;

    @Getter @Setter
    private ComponentType component;

    @Getter @Setter
    private DependencyResolution resolution;

//    private final Dependency module;

    public static AnalyzedDependency create(Dependency dependency)
    {
        return new AnalyzedDependency(dependency.getGroup(), dependency.getName(), dependency.getVersion());
    }

    public static AnalyzedDependency create(String group, String name, String version)
    {
        return new AnalyzedDependency(group, name, version);
    }

    public static AnalyzedDependency create(String group, String name)
    {
        return new AnalyzedDependency(group, name, null);
    }

    public static AnalyzedDependency from(String coordinates)
    {
        String[] split = coordinates.split(":");
        if (split.length == 2) {
            return create(split[0], split[1]);
        } else if (split.length == 3) {
            return create(split[0], split[1], split[2]);
        } else {
            throw new IllegalArgumentException("Cannot parse '"+coordinates+"' as dependency coordinates");
        }

    }



    private AnalyzedDependency(String group, String name, String version)
    {
        this(DefaultModuleIdentifier.newId(group, name), version);
    }

    public AnalyzedDependency(ModuleIdentifier module) {
        this(module, null);
    }

    public AnalyzedDependency(ModuleIdentifier module, String version) {
        this.module = module;
        this.version = version;
    }

    public ModuleIdentifier getId() {
        return this.getModule();
    }

    @Override
    public String toString() {
        return this.module.toString();
    }

    @Override
    public String getGroup() {
        return this.module.getGroup();
    }

    @Override
    public String getName() {
        return this.module.getName();
    }

}
