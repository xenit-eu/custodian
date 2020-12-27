package eu.xenit.custodian.gradle.sentinel.model;

import java.util.List;

public interface PluginContainer {

    void add(String plugin);

    List<String> all();
}
