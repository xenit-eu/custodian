package eu.xenit.custodian.gradle.sentinel.model;

public interface ProjectModel {

    String getName();
    String getPath();
    String getProjectDir();
    String getBuildDir();
    String getBuildFile();

    PluginContainer getPlugins();
}
