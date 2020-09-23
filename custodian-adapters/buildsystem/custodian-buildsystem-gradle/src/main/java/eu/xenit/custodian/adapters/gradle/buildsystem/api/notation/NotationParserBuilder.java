package eu.xenit.custodian.adapters.gradle.buildsystem.api.notation;

public class NotationParserBuilder {

    public <T> StringNotationParser<T> build(NotationParserDelegate delegate,
            DependencyNotationConverter<T> converter) {

        return (String notation) -> {
            //ParsedDependencyNotation parsed = this.parse(notation);
            ParsedDependencyNotation parsed = delegate.apply(notation);
            return converter.from(parsed.getGroup(), parsed.getName(), parsed.getVersion(),
                    parsed.getClassifier(), parsed.getExtension());
        };
    }
}
