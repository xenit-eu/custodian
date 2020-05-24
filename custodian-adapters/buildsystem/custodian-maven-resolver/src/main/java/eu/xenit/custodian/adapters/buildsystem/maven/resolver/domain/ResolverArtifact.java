package eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain;

import lombok.Data;

public interface ResolverArtifact {


    String getClassifier();
    String getExtension();

    static ResolverArtifact from(String classifier, String extension) {
        return new DefaultResolverArtifact(classifier, extension);
    }

    static ResolverArtifact defaultArtifact() {
        return from("", "jar");
    }

    @Data
    class DefaultResolverArtifact implements ResolverArtifact {
        final String classifier;
        final String extension;
    }

}
