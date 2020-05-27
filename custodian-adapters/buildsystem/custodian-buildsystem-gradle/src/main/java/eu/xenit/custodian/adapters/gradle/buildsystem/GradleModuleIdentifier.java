package eu.xenit.custodian.adapters.gradle.buildsystem;

public interface GradleModuleIdentifier {

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

    static GradleModuleIdentifier from(String group, String name) {
        return new DefaultGradleModuleIdentifier(group, name);
    }
}