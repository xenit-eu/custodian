package eu.xenit.custodian.adapters.gradle.buildsystem.impl;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.DefaultGradleBuildModifier.GradleBuildModification;
import eu.xenit.custodian.domain.usecases.updates.Patch;
import eu.xenit.custodian.ports.spi.buildsystem.BuildModification;
import eu.xenit.custodian.ports.spi.buildsystem.ProjectModuleDependencyModifier;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class GradleProjectModuleDependencyModifier implements ProjectModuleDependencyModifier {

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

        if (this.tryUpdateInlineDependency(versionSpec, this.project.getBuildFile())) {
            // succesfully updated build.gradle dependency version
        } else {

        }
    }

    boolean tryUpdateInlineDependency(String versionSpec, Path buildDotGradlePath) {


        try {
            String source = Files.readString(buildDotGradlePath);
            String result = updateInlineDependencyVersion(this.dependency, versionSpec, source);

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

    static String updateInlineDependencyVersion(GradleModuleDependency dependency, String newVersion, String source) {
        String matcher = String.format("(%s\\s*\\(?\\s*['\"]%s:%s):%s(.+)",
                dependency.getTargetConfiguration(), dependency.getGroup(),
                dependency.getName(), dependency.getVersionSpec().getValue());
        String replacement = "$1:" + newVersion + "$2";

        return source.replaceFirst(matcher, replacement);
    }
}
