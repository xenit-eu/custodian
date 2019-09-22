package eu.xenit.custodian.adapters.analysis.gradle.notation;

import java.util.Objects;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class NotationParserBuilder {

    public <T> NotationParser<T> build(DependencyNotationConverter<T> converter) {

        return (String notation) -> {
            ParsedDependencyStringNotation parsed = this.parse(notation);
            return converter.apply(parsed.getGroup(), parsed.getName(), parsed.getVersion(),
                    parsed.getClassifier(), parsed.getType());
        };
    }

    private ParsedDependencyStringNotation parse(String notation) {
        Objects.requireNonNull(notation, "notation can not be null");

        String type = null;
        int typeIndex = notation.lastIndexOf("@");
        if (typeIndex != -1) {
            type = notation.substring(typeIndex + 1);
            notation = notation.substring(0, typeIndex);
        }

        String[] parts = notation.split(":");
        if (parts.length < 2 || parts.length > 4)
        {
            throw new InvalidDependencyNotationException("Notation '%s' is not valid");
        }

        String group = parts[0].equals("") ? null : parts[0];
        String name = parts[1].equals("") ? null : parts[1];

        String version = null;
        if (parts.length > 2) {
            version = parts[2].equals("") ? null : parts[2];
        }

        String classifier = null;
        if (parts.length > 3) {
            classifier = parts[3].equals("") ? null : parts[3];
        }

        return new ParsedDependencyStringNotation(group, name, version, classifier, type);
    }

    @FunctionalInterface
    public interface NotationParser<T> extends Function<String, T> {

    }

    @FunctionalInterface
    public interface DependencyNotationConverter<T> {

        T apply(String group, String name, String version, String classifier, String type);
    }

    @Getter
    @AllArgsConstructor
    class ParsedDependencyStringNotation {

        private String group;
        private String name;
        private String version;
        private String classifier;
        private String type;
    }
}
