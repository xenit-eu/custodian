package eu.xenit.custodian.adapters.buildsystem.maven;

import eu.xenit.custodian.asserts.build.buildsystem.ModuleVersionIdentifier;

public interface MavenModuleVersionIdentifier extends ModuleVersionIdentifier {

    MavenModuleIdentifier getModuleId();
    MavenModuleVersion getVersion();

    /**
     * The group of the module.
     *
     * @return module group
     */
    default String getGroup() {
        return this.getModuleId().getGroup();
    }

    /**
     * The name of the module.
     *
     * @return module name
     */
    default String getName() {
        return this.getModuleId().getName();
    }

    /**
     * The maven-coordinates of the module in group:artifact:version notation
     *
     * @return the maven-coordinates of the module
     */
    default String getId() {
        return String.format("%s:%s:%s", this.getGroup(), this.getName(), this.getVersion().getValue());
    }

    static MavenModuleVersionIdentifier from(String group, String name, String version) {
        return new DefaultMavenModuleVersionIdentifier(group, name, version);
    }

    static MavenModuleVersionIdentifier from(MavenModuleIdentifier module, String version) {
        return new DefaultMavenModuleVersionIdentifier(module, MavenModuleVersion.from(version));
    }
}
