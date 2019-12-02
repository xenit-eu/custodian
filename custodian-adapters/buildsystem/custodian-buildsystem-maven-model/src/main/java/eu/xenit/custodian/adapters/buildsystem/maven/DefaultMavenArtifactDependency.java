//package eu.xenit.custodian.adapters.buildsystem.maven;
//
//import eu.xenit.custodian.domain.buildsystem.Repository;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Objects;
//import java.util.Set;
//import lombok.Getter;
//
//public class DefaultMavenArtifactDependency implements MavenArtifactDependency {
//
//    @Getter
//    private final MavenModuleIdentifier id;
//
//    @Getter
//    private final Set<Repository> repositories;
//
//    protected DefaultMavenArtifactDependency(Customizer builder) {
//        Objects.requireNonNull(builder.groupId, "groupId cannot be null");
//        Objects.requireNonNull(builder.artifactId, "artifactId cannot be null");
//
////        this.id = MavenModuleIdentifier.from(
////                builder.groupId,
////                builder.artifactId,
////                builder.version,
////                builder.classifier,
////                builder.extension
////        );
//
//        this.repositories = Collections.<Repository>unmodifiableSet(builder.repositories);
//
//    }
//
//    public static Customizer withCoordinates(String group, String artifact) {
//        Objects.requireNonNull(group, "group must not be null");
//        Objects.requireNonNull(artifact, "artifact must not be null");
//
//        return new Customizer(group, artifact);
//    }
//
//    public static class Customizer<B extends Customizer> {
//
//        private String groupId;
//        private String artifactId;
//        private String version;
//
//        private String classifier;
//        private String extension;
//
//        private Set<Repository> repositories = new HashSet<>();
//
//        protected Customizer(String groupId, String artifactId) {
//            this.groupId = groupId;
//            this.artifactId = artifactId;
//        }
//
//        public B groupId(String groupId) {
//            this.groupId = groupId;
//            return self();
//        }
//
//        public B artifactId(String artifactId) {
//            this.artifactId = artifactId;
//            return self();
//        }
//
//        @SuppressWarnings("unchecked")
//        protected B self() {
//            return (B) this;
//        }
//
//        public B version(String version) {
//            this.version = version;
//            return self();
//        }
//
//        public B classifier(String classifier)
//        {
//            this.classifier = classifier;
//            return self();
//        }
//
//        public B extension(String extension)
//        {
//            this.extension = extension;
//            return self();
//        }
//
//        public B repository(Repository repository) {
//            Objects.requireNonNull(repository, "repository should not be null");
//            this.repositories.add(repository);
//            return self();
//        }
//
//        public B repositories(Set<Repository> repositories)
//        {
//            Objects.requireNonNull(repositories, "repositories should not be null");
//            this.repositories = new HashSet<>(repositories);
//            return self();
//        }
//
//        public MavenArtifactDependency build() {
//            return new DefaultMavenArtifactDependency(this);
//        }
//    }
//}
