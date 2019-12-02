package eu.xenit.custodian.adapters.buildsystem.maven.notation;

public interface ParsedDependencyNotation {

    String getGroup();

    String getName();

    String getVersion();

    String getClassifier();

    String getExtension();
}
