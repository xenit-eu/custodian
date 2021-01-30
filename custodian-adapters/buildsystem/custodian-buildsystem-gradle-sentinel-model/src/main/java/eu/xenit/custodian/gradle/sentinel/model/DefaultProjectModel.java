package eu.xenit.custodian.gradle.sentinel.model;

import java.io.Serializable;

public class DefaultProjectModel implements ProjectModel, Serializable {

    private final String path;

    private String name;
    private String group;
    private String version;

    private String projectDir;
    private String buildDir;
    private String buildFile;

    private final PluginContainer plugins = new DefaultPluginContainer();
    private final DefaultDependenciesContainer dependencies = new DefaultDependenciesContainer();

    public DefaultProjectModel(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getProjectDir() {
        return projectDir;
    }

    public void setProjectDir(String projectDir) {
        this.projectDir = projectDir;
    }

    @Override
    public String getBuildDir() {
        return buildDir;
    }

    public void setBuildDir(String buildDir) {
        this.buildDir = buildDir;
    }

    @Override
    public String getBuildFile() {
        return buildFile;
    }

    @Override
    public PluginContainer getPlugins() {
        return this.plugins;
    }

    @Override
    public DefaultDependenciesContainer getDependencies() {
        return this.dependencies;
    }

    public void setBuildFile(String buildScript) {
        this.buildFile = buildScript;
    }



}
