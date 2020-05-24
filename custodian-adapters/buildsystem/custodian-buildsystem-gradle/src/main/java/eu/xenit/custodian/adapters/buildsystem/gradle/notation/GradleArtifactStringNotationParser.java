package eu.xenit.custodian.adapters.buildsystem.gradle.notation;

import eu.xenit.custodian.adapters.buildsystem.gradle.GradleArtifactSpecification;

public class GradleArtifactStringNotationParser implements StringNotationParser<GradleArtifactSpecification> {

    private static StringNotationParser<GradleArtifactSpecification> delegate = new NotationParserBuilder()
            .build(new GradleDependencyNotationParserDelegate(), new GradleNotationDependencyConverter());

    @Override
    public GradleArtifactSpecification apply(String notation) {
        return delegate.apply(notation);
    }

    public static GradleArtifactSpecification parse(String notation) {
        return delegate.apply(notation);
    }

    private static class GradleNotationDependencyConverter implements DependencyNotationConverter<GradleArtifactSpecification> {

        @Override
        public GradleArtifactSpecification from(String group, String name, String version,
                String classifier, String extension) {
            return GradleArtifactSpecification.from(group, name, version).customize(spec -> {
                spec.setClassifier(classifier);
                spec.setExtension(extension);
            });
        }
    }
}
