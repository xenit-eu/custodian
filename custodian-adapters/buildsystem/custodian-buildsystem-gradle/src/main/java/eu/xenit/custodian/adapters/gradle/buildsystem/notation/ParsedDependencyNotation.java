package eu.xenit.custodian.adapters.gradle.buildsystem.notation;

public interface ParsedDependencyNotation {

    String getGroup();

    String getName();

    String getVersion();

    String getClassifier();

    String getExtension();
}
