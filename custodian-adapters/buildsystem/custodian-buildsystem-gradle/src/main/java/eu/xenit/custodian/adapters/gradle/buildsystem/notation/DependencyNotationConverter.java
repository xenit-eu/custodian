package eu.xenit.custodian.adapters.gradle.buildsystem.notation;

@FunctionalInterface
public interface DependencyNotationConverter<T> {

    T from(String group, String name, String version, String classifier, String type);
}