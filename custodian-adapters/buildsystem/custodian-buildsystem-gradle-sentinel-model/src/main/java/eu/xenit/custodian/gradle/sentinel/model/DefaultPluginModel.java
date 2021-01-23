package eu.xenit.custodian.gradle.sentinel.model;

import java.io.Serializable;
import java.util.Objects;

public class DefaultPluginModel implements PluginModel, Serializable {

    private final String id;
    private final String className;
    private final String version;
    private final boolean applied;

    public DefaultPluginModel(String id, String className, String version, boolean applied) {
        this.id = Objects.requireNonNull(id, "id is required");
        this.className = Objects.requireNonNull(className, "className is required");;
        this.version = version;
        this.applied = applied;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public boolean isApplied() {
        return applied;
    }
}
