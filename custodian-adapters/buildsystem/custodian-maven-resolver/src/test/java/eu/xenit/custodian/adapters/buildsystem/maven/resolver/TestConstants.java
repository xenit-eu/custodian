package eu.xenit.custodian.adapters.buildsystem.maven.resolver;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.GroupArtifactVersionSpecification;

public interface TestConstants {

    interface Coordinates {

        GroupArtifactVersionSpecification APACHE_HTTPCLIENT = GroupArtifactVersionSpecification
                .from("org.apache.httpcomponents", "httpclient", "4.5.12");
    }
}
