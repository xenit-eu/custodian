package eu.xenit.custodian.adapters.buildsystem.gradle.notation;

public interface ParsedDependencyNotation {

    String getGroup();

    String getName();

    String getVersion();

    String getClassifier();

    String getExtension();
}
