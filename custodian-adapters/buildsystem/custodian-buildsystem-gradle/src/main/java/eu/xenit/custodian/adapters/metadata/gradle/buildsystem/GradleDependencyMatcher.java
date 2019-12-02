package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import static eu.xenit.custodian.adapters.metadata.gradle.buildsystem.Util.doesNotMatch;

import eu.xenit.custodian.adapters.metadata.gradle.notation.GradleStringNotationParser;
import eu.xenit.custodian.domain.buildsystem.DependencyMatcher;
import lombok.Data;

public interface GradleDependencyMatcher extends DependencyMatcher<GradleDependency> {

    static GradleDependencyMatcher from(String notation) {
        GradleArtifactSpecification spec = GradleStringNotationParser.parse(notation);
        return new GradleArtifactSpecMatcher(spec);
    }

//    static GradleDependencyMatcher from(GradleModuleDependency matcher) {
//        return new GradleModuleDependencyMatcher(matcher);
//    }

//    @Data
//    class GradleModuleDependencyMatcher implements GradleDependencyMatcher {
//
//        private final GradleModuleDependency matcher;
//
//        @Override
//        public boolean test(GradleDependency gradleDependency) {
//            return false;
//        }
//
//        static boolean test(GradleModuleDependency matcher, GradleDependency dependency) {
//            if (matcher == null || dependency == null) {
//                return false;
//            }
//
//            if (matcher.getModuleId() != null) {
//                if (doesNotMatch(matcher.getModuleId().getGroup(), dependency.getGroup())) {
//                    return false;
//                }
//
//                if (doesNotMatch(matcher.getModuleId().getName(), dependency.getName())) {
//                    return false;
//                }
//            }
//
//            if (doesNotMatch(matcher.getVersion().getValue(), dependency.getVersion().getValue())) {
//                return false;
//            }
//
//            if (dependency.getArtifacts().isEmpty()) {
//                // the dependency has no artifacts specificed
//                // check if the spec has defaults
//                String classifier = matcher.get.getClassifier();
//                if (classifier != null && !classifier.equals("")) {
//                    return false;
//                }
//
//                String extension = spec.getExtension();
//                if (extension != null && !extension.equals("") && !extension.equalsIgnoreCase("jar")) {
//                    return false;
//                }
//            } else {
//                // There are one or more artifacts configured for this dependency
//                // if an extension or classifier is specified, ALL artifacts must match
//                if (!dependency.getArtifacts().stream().allMatch(this::test)) {
//                    return false;
//                }
//            }
//
//            return true;
//        }
//    }
    @Data
    class GradleArtifactSpecMatcher implements GradleDependencyMatcher {

        private final GradleArtifactSpecification spec;

        @Override
        public boolean test(GradleDependency dependency) {
            if (dependency == null || spec == null) {
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


    }


}

class Util {
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
