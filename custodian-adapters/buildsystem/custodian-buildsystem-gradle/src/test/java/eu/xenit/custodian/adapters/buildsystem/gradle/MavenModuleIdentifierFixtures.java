package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleVersionIdentifier;

public interface MavenModuleIdentifierFixtures {

    static MavenModuleVersionIdentifier apacheHttpClient() {
        return MavenModuleVersionIdentifier.from("org.apache.httpcomponents", "httpclient", "4.5.1");
    }

    static MavenModuleVersionIdentifier junit() {
        return MavenModuleVersionIdentifier.from("junit", "junit", "4.12");
    }
}
