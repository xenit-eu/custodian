package eu.xenit.custodian.adapters.buildsystem.gradle.notation;

@FunctionalInterface
public interface DependencyNotationConverter<T> {

    T from(String group, String name, String version, String classifier, String type);
}
