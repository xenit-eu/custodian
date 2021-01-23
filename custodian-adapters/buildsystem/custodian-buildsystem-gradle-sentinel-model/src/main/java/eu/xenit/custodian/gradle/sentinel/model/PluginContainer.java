package eu.xenit.custodian.gradle.sentinel.model;

import java.util.List;

public interface PluginContainer {

    void add(String plugin, String implClass, String version);

    List<PluginModel> all();
}
