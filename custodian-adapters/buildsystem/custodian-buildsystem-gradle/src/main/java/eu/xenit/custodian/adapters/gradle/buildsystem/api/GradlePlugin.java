package eu.xenit.custodian.adapters.gradle.buildsystem.api;

import lombok.Getter;

@Getter
public class GradlePlugin {

    private final String id;

    private final String version;

    private final boolean applied;

    /**
     * Create a new instance.
     * @param id the id of the plugin
     * @param applied whether the plugin should be applied or not
     */
    public GradlePlugin(String id, String version, boolean applied) {
        this.id = id;
        this.version = version;
        this.applied = applied;
    }

}
