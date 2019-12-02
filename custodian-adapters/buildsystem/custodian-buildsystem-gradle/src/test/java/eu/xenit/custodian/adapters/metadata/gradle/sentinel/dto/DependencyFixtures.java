package eu.xenit.custodian.adapters.metadata.gradle.sentinel.dto;


import eu.xenit.custodian.adapters.metadata.gradle.MavenModuleIdentifierFixtures;

public interface DependencyFixtures {

    static Dependency apacheHttpClient() {
        return Dependency.from(MavenModuleIdentifierFixtures.apacheHttpClient());
    }

    static Dependency junit() {
        return Dependency.from(MavenModuleIdentifierFixtures.junit());
    }

}