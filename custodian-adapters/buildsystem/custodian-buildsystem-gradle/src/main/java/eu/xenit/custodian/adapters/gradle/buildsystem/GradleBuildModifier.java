package eu.xenit.custodian.adapters.gradle.buildsystem;

import eu.xenit.custodian.domain.usecases.updates.Patch;
import eu.xenit.custodian.ports.spi.buildsystem.BuildModification;
import eu.xenit.custodian.ports.spi.buildsystem.BuildModifier;
import eu.xenit.custodian.ports.spi.buildsystem.Dependency;
import eu.xenit.custodian.ports.spi.buildsystem.Project;
import eu.xenit.custodian.ports.spi.buildsystem.ProjectModuleDependencyModifier;
import eu.xenit.custodian.util.Arguments;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GradleBuildModifier implements BuildModifier {

    public GradleBuildModifier() {
    }

    @Override
    public BuildModification updateDependency(Project project, Dependency dependency,
            Consumer<ProjectModuleDependencyModifier> callback) {

        GradleProjectModuleDependencyModifier modifier = new GradleProjectModuleDependencyModifier(
                Arguments.isInstanceOf(GradleProject.class, project, "project"),
                Arguments.isInstanceOf(GradleModuleDependency.class, dependency, "dependency"));

        callback.accept(modifier);

        return modifier.getModification();


        // so is the build.gradle now modified or not ?
        // which one ? (=which subproject)

        // TODO return change result ?

    }


    static class GradleBuildModification implements BuildModification {

        private final List<Patch> patches = new ArrayList<>();

        @Override
        public Stream<Patch> getPatches() {
            return this.patches.stream();
        }

        @Override
        public boolean isEmpty() {
            return this.patches.isEmpty();
        }

        void add(Patch patch) {
            this.patches.add(Arguments.notNull(patch, "patch"));
        }

        void addAll(Stream<Patch> patches) {
            Arguments.notNull(patches, "patches");
            patches.forEach(this.patches::add);
        }

    }

    static class GradleProjectModuleDependencyModifier implements ProjectModuleDependencyModifier {

        private final GradleProject project;
        private final GradleModuleDependency dependency;

        private final GradleBuildModification modification = new GradleBuildModification();
//        private final List<Patch> patches = new ArrayList<>();

        public GradleProjectModuleDependencyModifier(GradleProject project,
                GradleModuleDependency dependency) {
            this.project = project;
            this.dependency = dependency;
        }

        BuildModification getModification() {
            return this.modification;
        }

        @Override
        public void setVersion(String versionSpec) {

            log.info("Update gradle build dependency {} {} -> {}", dependency.getId(),
                    dependency.getVersionSpec().getValue(), versionSpec);

            // find the correct GradleProject
            // find the build.gradle file
            // try a few update strategies

            // Dependabot strategy:
            // https://github.com/dependabot/dependabot-core/blob/master/gradle/lib/dependabot/gradle/file_updater.rb
            //
            // 1. update_files_for_property_change
            // 2. update_files_for_dep_set_change -> wth is a dependencySet ? isn't that gradle internals ?
            // 3. update_version_in_buildfile

            if (this.updateInlineDependencyVersion(versionSpec)) {
                // succesfully updated build.gradle dependency version
            } else {

            }

        }

        private boolean updateInlineDependencyVersion(String versionSpec) {
            Path buildDotGradlePath = this.project.getBuildFile();

            try {
                String source = Files.readString(buildDotGradlePath);
                String matcher = String.format("(%s\\s+'%s:%s):%s(.+)",
                        dependency.getTargetConfiguration(), dependency.getGroup(),
                        dependency.getName(), dependency.getVersionSpec().getValue());
                String replacement = "$1:" + versionSpec + "$2";

                String result = source.replaceFirst(matcher, replacement);

                log.debug(result);

                if (source.equalsIgnoreCase(result)) {
                    // source and result is unchanged
                    // no success
                    return false;
                }

                // actually write to the file
                Files.writeString(buildDotGradlePath, result);

                Patch patch = Patch.from(buildDotGradlePath, source);
                this.modification.add(patch);

                return true;

            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
