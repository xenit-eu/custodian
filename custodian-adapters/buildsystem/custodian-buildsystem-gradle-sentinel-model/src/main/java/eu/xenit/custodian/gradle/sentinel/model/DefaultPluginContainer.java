package eu.xenit.custodian.gradle.sentinel.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class DefaultPluginContainer implements PluginContainer, Serializable {

//    private final Map<String, PluginModel> plugins = new LinkedHashMap<>();
    private final List<PluginModel> plugins = new ArrayList<PluginModel>();

    public void addAll(Stream<PluginModel> stream) {
        stream.forEach(plugin -> {
//            this.plugins.add(new DefaultPluginModel(plugin.getClass().getName()));
            this.plugins.add(Objects.requireNonNull(plugin, "plugin in stream cannot be null"));
        });
    }
    public void add(String pluginId, String implClass, String version) {
        var plugin = new DefaultPluginModel(pluginId, implClass, version, true);
        this.plugins.add(plugin);
    }

    public List<PluginModel> all() {
        return this.plugins;
    }
//
//    @Override
//    public Stream<String> stream() {
//        return this.plugins.stream();
//    }
}
