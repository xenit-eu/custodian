package eu.xenit.custodian.adapters.buildsystem.maven.resolver.api;

import lombok.Value;

public interface ResolverMavenRepository {

    String getId();
    String getUrl();

    static ResolverMavenRepository mavenCentral() {
        return from("maven-central", "https://repo.maven.apache.org/maven2/");
    }

    static ResolverMavenRepository from(String id, String url) {
        return new DefaultResolverMavenRepository(id, url);
    }


    @Value
    class DefaultResolverMavenRepository implements ResolverMavenRepository {
        String id;
        String url;
    }
}
