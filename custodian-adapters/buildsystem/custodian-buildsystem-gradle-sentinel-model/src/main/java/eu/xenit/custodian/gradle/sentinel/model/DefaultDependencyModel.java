package eu.xenit.custodian.gradle.sentinel.model;

import java.io.Serializable;

public class DefaultDependencyModel implements DependencyModel, Serializable {

    private final String group;
    private final String name;
    private final String version;


    public DefaultDependencyModel(String group, String name, String version) {
        this.group = group;
        this.name = name;
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
