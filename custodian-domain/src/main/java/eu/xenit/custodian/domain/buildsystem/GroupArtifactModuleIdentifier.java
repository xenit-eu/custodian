package eu.xenit.custodian.domain.buildsystem;

@Deprecated
public interface GroupArtifactModuleIdentifier extends ModuleIdentifier {

    /**
     * The group of the module. Possibly null.
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

    static GroupArtifactModuleIdentifier from(String group, String name) {
        return new DefaultGroupArtifactModuleIdentifier(group, name);
    }
}
