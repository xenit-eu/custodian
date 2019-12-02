//package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;
//
//import eu.xenit.custodian.adapters.buildsystem.maven.DefaultMavenArtifactDependency;
//import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactDependency;
//import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleIdentifier;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Objects;
//import java.util.Set;
//import lombok.Getter;
//
//public class DefaultBackupGradleModuleDependency extends DefaultMavenArtifactDependency implements MavenArtifactDependency {
//
//    @Getter
//    private final Set<String> configurations;
//
//    private DefaultBackupGradleModuleDependency(GradleArtifactSpecificationCustomizer builder) {
//        super(builder);
//
//        this.configurations = Collections.unmodifiableSet(builder.configurations);
//
//    }
//
//    public static GradleArtifactSpecificationCustomizer withCoordinates(String group, String artifact) {
//        Objects.requireNonNull(group, "group must not be null");
//        Objects.requireNonNull(artifact, "artifact must not be null");
//
//        return new GradleArtifactSpecificationCustomizer(group, artifact);
//    }
//
//    public static GradleArtifactSpecificationCustomizer withCoordinates(String group, String artifact, String version) {
//        return withCoordinates(group, artifact).version(version);
//    }
//
//    public static GradleArtifactSpecificationCustomizer from(eu.xenit.custodian.adapters.metadata.gradle.sentinel.dto.Dependency dependency) {
//        return withCoordinates(dependency.getGroup(), dependency.getArtifact(), dependency.getVersion());
//    }
//
//    private static GradleArtifactSpecificationCustomizer from(MavenModuleIdentifier id) {
//        return withCoordinates(id.getGroup(), id.getName(), id.getVersion())
//                .classifier(id.getClassifier())
//                .extension(id.getType());
//    }
//
//    public GradleArtifactSpecificationCustomizer customize() {
//        return from(this.getId())
//                .configurations(this.getConfigurations())
//                .repositories(this.getRepositories());
//    }
//
//
//    public static class GradleArtifactSpecificationCustomizer extends DefaultMavenArtifactDependency.GradleArtifactSpecificationCustomizer<GradleArtifactSpecificationCustomizer> {
//
//        private Set<String> configurations = new HashSet<>();
//
//        private GradleArtifactSpecificationCustomizer(String groupId, String artifactId) {
//            super(groupId, artifactId);
//        }
//
//        public GradleArtifactSpecificationCustomizer configuration(String configuration) {
//            Objects.requireNonNull(configurations, "configuration should not be null");
//
//            this.configurations.add(configuration);
//            return this;
//        }
//
//        public GradleArtifactSpecificationCustomizer configurations(Set<String> configurations) {
//            Objects.requireNonNull(configurations, "configurations should not be null");
//            this.configurations = new HashSet<>(configurations);
//            return this;
//        }
//
//        public DefaultBackupGradleModuleDependency build() {
//
//            return new DefaultBackupGradleModuleDependency(this);
//        }
//    }
//
//}
