package eu.xenit.custodian.gradle.sentinel.model;

import java.io.Serializable;

public class DefaultPluginModel implements PluginModel, Serializable {

    private final String className;

    public DefaultPluginModel(String className) {
        this.className = className;
    }

}
