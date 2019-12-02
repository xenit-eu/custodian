package eu.xenit.custodian.sentinel.adapters.dependencies;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ExternalModuleDependency;
import org.gradle.api.artifacts.ModuleIdentifier;
import org.gradle.api.internal.artifacts.DefaultModuleIdentifier;
import sun.security.pkcs11.Secmod.Module;


@RequiredArgsConstructor
public class DeclaredModuleDependency {

    @Getter
    private final String group;


    @Getter
    private final String name;

    @Getter
    private final String version;

    public ModuleIdentifier getId() {
        return DefaultModuleIdentifier.newId(group, name);
    }


    public static DeclaredModuleDependency create(ExternalModuleDependency dependency) {
        return create(dependency.getGroup(), dependency.getName(), dependency.getVersion());
    }

    public static DeclaredModuleDependency create(String group, String name, String version) {
        return new DeclaredModuleDependency(group, name, version);
    }

    public static DeclaredModuleDependency from(String coordinates)
    {
        String[] split = coordinates.split(":");
        if (split.length == 2) {
            return create(split[0], split[1], null);
        } else if (split.length == 3) {
            return create(split[0], split[1], split[2]);
        } else {
            throw new IllegalArgumentException("Cannot parse '"+coordinates+"' as dependency coordinates");
        }
    }
}
