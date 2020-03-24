package eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto;


import eu.xenit.custodian.adapters.buildsystem.gradle.MavenModuleIdentifierFixtures;

public interface DependencyFixtures {

    static Dependency apacheHttpClient() {
        return Dependency.from(MavenModuleIdentifierFixtures.apacheHttpClient());
    }

    static Dependency junit() {
        return Dependency.from(MavenModuleIdentifierFixtures.junit());
    }

}