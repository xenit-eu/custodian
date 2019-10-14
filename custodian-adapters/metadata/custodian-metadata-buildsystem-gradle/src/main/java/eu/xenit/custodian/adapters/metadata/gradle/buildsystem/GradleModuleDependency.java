package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import eu.xenit.custodian.domain.metadata.buildsystem.Dependency;
import eu.xenit.custodian.domain.metadata.buildsystem.Repository;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;

public class GradleModuleDependency implements Dependency<ExternalModuleIdentifier> {

    @Getter
    private final ExternalModuleIdentifier id;

    @Getter
    private final Set<String> configurations;

    @Getter
    private final Set<Repository> repositories;

    protected GradleModuleDependency(Builder builder) {
        this.id = new DefaultExternalModuleIdentifier(
                builder.groupId,
                builder.artifactId,
                builder.version,
                builder.classifier,
                builder.extension
        );

        this.configurations = Collections.unmodifiableSet(builder.configurations);
        this.repositories = Collections.unmodifiableSet(builder.repositories);
    }


    @Override
    public String getVersion() {
        return this.getId().getVersion();
    }

    public static Builder withCoordinates(String group, String artifact)
    {
        Objects.requireNonNull(group, "group must not be null");
        Objects.requireNonNull(artifact, "artifact must not be null");

        return new Builder(group, artifact);
    }

    public static Builder withCoordinates(String group, String artifact, String version)
    {
        return withCoordinates(group, artifact).version(version);
    }

    public static Builder from(eu.xenit.custodian.adapters.metadata.gradle.sentinel.dto.Dependency dependency) {
        return withCoordinates(dependency.getGroup(), dependency.getArtifact(), dependency.getVersion());
    }

    private static Builder from(ExternalModuleIdentifier id) {
        return withCoordinates(id.getGroup(), id.getName(), id.getVersion())
                .classifier(id.getClassifier())
                .extension(id.getType());
    }

    public Builder toBuilder() {
        return from(this.getId())
                .configurations(this.getConfigurations())
                .repositories(this.getRepositories());
    }


    public static class Builder {

        private String groupId;
        private String artifactId;
        private String version;

//        private String scope;
        private String classifier;
        private String extension;


        private Set<String> configurations = new HashSet<>();
        private Set<Repository> repositories = new HashSet<>();

        protected Builder(String groupId, String artifactId) {
            this.groupId = groupId;
            this.artifactId = artifactId;
        }



        public Builder groupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder artifactId(String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder classifier(String classifier)
        {
            this.classifier = classifier;
            return this;
        }

        public Builder extension(String extension)
        {
            this.extension = extension;
            return this;
        }

        public Builder configuration(String configuration)
        {
            Objects.requireNonNull(configurations, "configuration should not be null");

            this.configurations.add(configuration);
            return this;
        }

        public Builder configurations(Set<String> configurations)
        {
            Objects.requireNonNull(configurations, "configurations should not be null");
            this.configurations = new HashSet<>(configurations);
            return this;
        }

        public Builder repository(Repository repository) {
            Objects.requireNonNull(configurations, "repository should not be null");
            this.repositories.add(repository);
            return this;
        }

        public Builder repositories(Set<Repository> repositories)
        {
            Objects.requireNonNull(repositories, "repositories should not be null");
            this.repositories = new HashSet<>(repositories);
            return this;
        }

        public GradleModuleDependency build() {
            Objects.requireNonNull(groupId, "groupId cannot be null");
            Objects.requireNonNull(artifactId, "artifactId cannot be null");

            return new GradleModuleDependency(this);
        }
    }

}
