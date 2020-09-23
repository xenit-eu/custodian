package eu.xenit.custodian.adapters.gradle.buildsystem.api;


import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

public class MavenRepository implements GradleArtifactRepository {

    private static final MavenRepository MAVEN_CENTRAL = MavenRepository
            .withIdAndUrl("maven-central", "https://repo.maven.apache.org/maven2")
            .name("Maven Central")
            .build();

    public static MavenRepository mavenCentral() {
        return MAVEN_CENTRAL;
    }

    @Getter
    private final String id;

    @Getter
    private final String name;

    @Getter
    private final String url;

    @Getter
    private final boolean snapshotsEnabled;

    protected MavenRepository(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.url = builder.url;
        this.snapshotsEnabled = builder.snapshotsEnabled;
    }

    /**
     * Initialize a new repository {@link Builder} with the specified id and url. The name of the repository is
     * initialized with the id.
     *
     * @param id the identifier of the repository
     * @param url the url of the repository
     * @return a new builder
     */
    public static Builder withIdAndUrl(String id, String url) {
        return new Builder(id, url);
    }

    @Override
    public GradleRepositoryType getType() {
        return GradleRepositoryType.MAVEN;
    }

    @Data
    @Accessors(chain = true, fluent = true)
    public static class Builder {

        private String id;

        private String name;

        private String url;

        private boolean snapshotsEnabled;

        public Builder(String id, String url) {
            this.id = id;
            this.name = id;
            this.url = url;
        }

        /**
         * Build a {@link MavenRepository} with the current state of this builder.
         *
         * @return a {@link MavenRepository}
         */
        public MavenRepository build() {
            return new MavenRepository(this);
        }

    }


}
