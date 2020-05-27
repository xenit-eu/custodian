package eu.xenit.custodian.adapters.gradle.buildsystem.api;

public interface MavenRepository extends RemoteRepository {

    default MavenRepository mavenCentral() {
        return new MavenRepository() {
            @Override
            public String getId() {
                return "MavenCentral";
            }

            @Override
            public String getUrl() {
                return "https://repo.maven.apache.org/maven2/";
            }
        };
    }
}

