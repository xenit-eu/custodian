package eu.xenit.custodian.adapters.buildsystem.maven;

import eu.xenit.custodian.domain.buildsystem.GroupArtifactModuleIdentifier;
import eu.xenit.custodian.domain.buildsystem.ModuleIdentifier;

public interface MavenModuleIdentifier extends ModuleIdentifier {

    /**
     * The group of the module.
     *
     * @return module group
     */
    String getGroup();

    /**
     * The name of the module.
     *
     * @return module name
     */
    String getName();

    static MavenModuleIdentifier from(String group, String name) {
        return new DefaultMavenModuleIdentifier(group, name);
    }

    static MavenModuleIdentifier from(GroupArtifactModuleIdentifier moduleId) {
        return new DefaultMavenModuleIdentifier(moduleId.getGroup(), moduleId.getName());
    }

}
