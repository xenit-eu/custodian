package eu.xenit.custodian.adapters.buildsystem.maven;

import eu.xenit.custodian.domain.buildsystem.Repository;

public interface MavenRepository extends Repository {

    MavenRepository MAVEN_CENTRAL = new MavenRepository() {
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
