package eu.xenit.custodian.adapters.gradle.buildsystem.asserts;

public interface DependenciesAssert {

    DependenciesAssert hasDependency(String configuration, String notation);
}
