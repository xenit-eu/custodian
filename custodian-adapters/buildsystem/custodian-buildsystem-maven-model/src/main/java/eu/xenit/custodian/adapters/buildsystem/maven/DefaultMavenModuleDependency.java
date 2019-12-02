package eu.xenit.custodian.adapters.buildsystem.maven;

import java.util.ArrayList  ;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.Getter;

public class DefaultMavenModuleDependency implements MavenModuleDependency {

    @Getter
    private final MavenModuleIdentifier moduleId;

    @Getter
    private final MavenVersionSpecification versionSpec;

    @Getter
    private final String type;

    @Getter
    private final String classifier;

    @Getter
    private final String scope;

    @Getter
    private final List<MavenModuleIdentifier> exclusions;

    @Getter
    private final String optional;

    DefaultMavenModuleDependency(MavenModuleIdentifier moduleId, MavenVersionSpecification versionSpec, String type,
            String classifier, String scope, List<MavenModuleIdentifier> exclusions, String optional) {

        this.moduleId = moduleId;
        this.versionSpec = versionSpec;
        this.type = type;
        this.classifier = classifier;
        this.scope = scope;
        this.exclusions = Collections.unmodifiableList(new ArrayList<>(exclusions));
        this.optional = optional;
    }

    @Override
    public MavenModuleDependency getDependency() {
        return this;
    }

    @Override
    public Set<MavenArtifactSpecification> getArtifactSpecs() {
        return Collections.singleton(new DefaultMavenArtifactSpecification(
                this.getModuleId(), this.getVersionSpec(), this.getExtension(),
                this.getClassifier(), this.getType()
        ));
    }

    private String getExtension() {
        switch (this.getType()) {
            case "pom":
                return "pom";
            case "jar":
            case "test-jar":
            case "javadoc":
            case "java-source":
                return "jar";
            default:
                throw new UnsupportedOperationException("TODO map artifact type '" + this.getType() + "' to extension");
        }
    }
}
