package eu.xenit.custodian.adapters.gradle.buildsystem.api.notation;

public interface ParsedDependencyNotation {

    String getGroup();

    String getName();

    String getVersion();

    String getClassifier();

    String getExtension();
}
