package eu.xenit.custodian.gradle.sentinel.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DefaultPluginContainer implements PluginContainer, Serializable {

//    private final Map<String, PluginModel> plugins = new LinkedHashMap<>();
    private final List<String> plugins = new ArrayList<String>();

    public void addAll(Stream<PluginModel> stream) {
        stream.forEach(plugin -> {
//            this.plugins.add(new DefaultPluginModel(plugin.getClass().getName()));
            this.plugins.add(plugin.getClass().getName());
        });
    }
    public void add(String plugin) {
        this.plugins.add(plugin);
    }

    public List<String> all() {
        return this.plugins;
    }
//
//    @Override
//    public Stream<String> stream() {
//        return this.plugins.stream();
//    }
}
