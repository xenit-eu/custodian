package eu.xenit.custodian.adapters.gradle.buildsystem.notation;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class GradleDependencyNotationParserDelegate implements NotationParserDelegate {

    @Override
    public ParsedDependencyNotation apply(String notation) {
        Objects.requireNonNull(notation, "notation can not be null");

        String extension = null;
        int typeIndex = notation.lastIndexOf("@");
        if (typeIndex != -1) {
            extension = notation.substring(typeIndex + 1);
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

        return new ParsedDependencyStringNotation(group, name, version, classifier, extension);
    }

    @Getter
    @AllArgsConstructor
    private static class ParsedDependencyStringNotation implements ParsedDependencyNotation {

        private String group;
        private String name;
        private String version;
        private String classifier;
        private String extension;
    }
}
