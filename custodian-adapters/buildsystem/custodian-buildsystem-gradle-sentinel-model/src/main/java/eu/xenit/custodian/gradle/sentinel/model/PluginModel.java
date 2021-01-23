package eu.xenit.custodian.gradle.sentinel.model;

public interface PluginModel {

    String getId();

    String getClassName();

    String getVersion();

    boolean isApplied();
}
