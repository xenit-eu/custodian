package eu.xenit.custodian.adapters.gradle.buildsystem.asserts;

public interface DependenciesAssert {

    DependenciesAssert hasDependency(String configuration, String notation);

    default DependenciesAssert hasImplementation(String dependency) {
        return this.hasDependency("implementation", dependency);
    }

    default DependenciesAssert hasTestImplementation(String dependency) {
        return this.hasDependency("testImplementation", dependency);
    }
}
