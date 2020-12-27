package eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.GroupArtifactVersionSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverGroupArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * A maven artifact specification.
 *
 * Note that while a maven dependency has a type, a maven artifact resolved this to an extension. See <a
 * href="https://maven.apache.org/ref/current/maven-core/artifact-handlers.html">Maven Artifact Handlers</a> for more
 * information.
 *
 * Instances of this class are immutable and the exposed mutator returns a new objects rather than changing the current
 * instance.
 */
public interface ResolverArtifactSpecification {


    String getGroupId();

    String getArtifactId();

    String getVersion();

    String getClassifier();

    String getExtension();

    /**
     * Creates a new artifact specification with the specified coordinates. The artifact's extension defaults to {@code
     * jar} and classifier to an empty string.
     *
     * @param groupId The group identifier of the artifact, may be {@code null}.
     * @param artifactId The artifact identifier of the artifact, may be {@code null}.
     * @param version The version of the artifact, may be {@code null}.
     * @return the artifact specification
     */
    static ResolverArtifactSpecification from(String groupId, String artifactId, String version) {
        return new DefaultResolverArtifactSpecification(groupId, artifactId, version, "", "jar");
    }

    /**
     * Creates a new artifact specification with the specified coordinates, classifier and extension.
     *
     * @param groupId The group identifier of the artifact, may be {@code null}.
     * @param artifactId The artifact identifier of the artifact, may be {@code null}.
     * @param version The version of the artifact, may be {@code null}.
     * @param classifier The classifier of the artifact, may be {@code null}.
     * @param extension The file extension of the artifact, may be {@code null}.
     * @return the artifact specification
     */
    static ResolverArtifactSpecification from(String groupId, String artifactId, String version, String classifier,
            String extension) {
        return new DefaultResolverArtifactSpecification(groupId, artifactId, version, classifier, extension);
    }

    /**
     * Create a new artifact specification with the specified module and artifact coordinates
     *
     * @param coords The maven groupId and artifactId
     * @param versionSpec The version specification
     * @param artifact The artifact details.
     * @return the artifact specification
     */
    static ResolverArtifactSpecification from(ResolverGroupArtifact coords, ResolverVersionSpecification versionSpec,
            ResolverArtifact artifact) {
        return from(
                coords.getGroupId(), coords.getArtifactId(), versionSpec.getValue(),
                artifact.getClassifier(), artifact.getExtension()
        );
    }


    /**
     * Create a new artifact specification with the specified module and artifact coordinates
     *
     * @param coords The maven coordinates of the artifact.
     * @param artifact The artifact details.
     * @return the artifact specification
     */
    static ResolverArtifactSpecification from(GroupArtifactVersionSpecification coords, ResolverArtifact artifact) {
        return from(coords.getGroupId(), coords.getArtifactId(), coords.getVersion().getValue(),
                artifact.getClassifier(),
                artifact.getExtension());
    }


    /**
     * Mutate some fields through a callback interface
     *
     * @param callback that will be called to mutate fields
     * @return a new instance with the mutated fields
     */
    default ResolverArtifactSpecification customize(Consumer<Customizer> callback) {
        Customizer customizer = new Customizer(this);
        callback.accept(customizer);
        return customizer.build();
    }

    @Data
    @AllArgsConstructor
    class Customizer {

        private Customizer(ResolverArtifactSpecification spec) {
            this(spec.getGroupId(), spec.getArtifactId(), spec.getVersion(), spec.getClassifier(), spec.getExtension());
        }

        private String group;
        private String artifact;
        private String version;
        private String classifier;
        private String extension;

        private ResolverArtifactSpecification build() {
            return new DefaultResolverArtifactSpecification(
                    this.group,
                    this.artifact,
                    this.version,
                    this.classifier,
                    this.extension
            );
        }
    }

    @Getter
    @AllArgsConstructor
    class DefaultResolverArtifactSpecification implements ResolverArtifactSpecification {

        String groupId;
        String artifactId;
        String version;
        String classifier;
        String extension;
    }
}
