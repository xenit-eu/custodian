package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import static eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleDependencyMatcher.Util.doesNotMatch;

import eu.xenit.custodian.adapters.metadata.gradle.notation.GradleArtifactStringNotationParser;
import eu.xenit.custodian.asserts.build.buildsystem.Dependency;
import eu.xenit.custodian.asserts.build.buildsystem.DependencyMatcher;
import lombok.RequiredArgsConstructor;

public interface GradleDependencyMatcher extends DependencyMatcher<GradleDependency> {

    static GradleDependencyMatcher from(String configuration, String notation) {
        GradleArtifactSpecification spec = GradleArtifactStringNotationParser.parse(notation);
        return new GradleArtifactSpecMatcher(configuration, spec);
    }

    default GradleDependency castedOrNull(Dependency dep) {
        if (dep instanceof GradleDependency) {
            return (GradleDependency) dep;
        }
        return null;
    }

    @RequiredArgsConstructor
    class GradleArtifactSpecMatcher implements GradleDependencyMatcher {

        private final String configuration;
        private final GradleArtifactSpecification spec;

        @Override
        public boolean test(GradleDependency dependency) {
            if (dependency == null || spec == null) {
                return false;
            }

            if (doesNotMatch(this.configuration, dependency.getTargetConfiguration())) {
                return false;
            }

            if (dependency instanceof GradleModuleDependency) {
                return this.test((GradleModuleDependency) dependency);
            }
            return false;
        }


        private boolean test(GradleModuleDependency dependency) {
            if (dependency == null || spec == null) {
                return false;
            }

            if (spec.getModuleId() != null) {
                if (doesNotMatch(spec.getModuleId().getGroup(), dependency.getGroup())) {
                    return false;
                }

                if (doesNotMatch(spec.getModuleId().getName(), dependency.getName())) {
                    return false;
                }
            }

            if (doesNotMatch(spec.getVersionSpec().getValue(), dependency.getVersionSpec().getValue())) {
                return false;
            }

            if (dependency.getArtifacts().isEmpty()) {
                // the dependency has no artifacts specificed
                // check if the spec has defaults
                String classifier = spec.getClassifier();
                if (classifier != null && !classifier.equals("")) {
                    return false;
                }

                String extension = spec.getExtension();
                if (extension != null && !extension.equals("") && !extension.equalsIgnoreCase("jar")) {
                    return false;
                }
            } else {
                // There are one or more artifacts configured for this dependency
                // if an extension or classifier is specified, ALL artifacts must match
                if (!dependency.getArtifacts().stream().allMatch(this::test)) {
                    return false;
                }
            }

            return true;
        }

        private boolean test(GradleArtifactSpecification artifact) {
            if (doesNotMatch(spec.getClassifier(), artifact.getClassifier())) {
                return false;
            }

            if (doesNotMatch(spec.getExtension(), artifact.getExtension())) {
                return false;
            }

            return true;
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "("
                    + "configuration=" + valueOrWildcard(this.configuration) + ", "
                    + "group=" + valueOrWildcard(this.spec.getModuleId().getGroup()) + ", "
                    + "artifact=" + valueOrWildcard(this.spec.getModuleId().getName()) + ", "
                    + "version=" + valueOrWildcard(this.spec.getVersionSpec()) + ", "
                    + "classifier=" + valueOrWildcard(this.spec.getClassifier()) + ", "
                    + "extension=" + valueOrWildcard(this.spec.getExtension())
                    + ")";
        }

        private static String valueOrWildcard(Object value) {
            if (value == null) {
                return "<any>";
            }

            return value.toString();
        }


    }


    static class Util {

        /**
         * Match strings against a given value
         *
         * @param matcher is the string to match or null
         * @param value the value to match
         * @return true when the matcher is null or the value matches the matcher
         */
        static boolean matchesOrNull(String matcher, String value) {
            if (matcher == null) {
                return true;
            }

            return matcher.equalsIgnoreCase(value);
        }

        static boolean doesNotMatch(String matcher, String value) {
            return !matchesOrNull(matcher, value);
        }
    }

}


