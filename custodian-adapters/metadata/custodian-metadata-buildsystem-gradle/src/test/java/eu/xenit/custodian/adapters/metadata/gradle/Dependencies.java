package eu.xenit.custodian.adapters.metadata.gradle;

import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.ExternalModuleIdentifier;
import eu.xenit.custodian.adapters.metadata.gradle.sentinel.dto.Dependency;

public class Dependencies {

    public static ExternalModuleIdentifier apacheHttpClient() {
        return ExternalModuleIdentifier.from("org.apache.httpcomponents:httpclient:4.5.1");
    }

    public static Dependency junitDeprecated() {
        return Dependency.withCoordinates("junit", "junit", "4.12");
    }

    public static ExternalModuleIdentifier junit() {
        return ExternalModuleIdentifier.from("junit:junit:4.12");
    }
}