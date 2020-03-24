package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.domain.buildsystem.ModuleDependency;
import eu.xenit.custodian.ports.spi.build.BuildModifier;
import eu.xenit.custodian.ports.spi.build.Project;
import eu.xenit.custodian.ports.spi.build.ProjectModuleDependencyModifier;
import eu.xenit.custodian.util.Arguments;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GradleBuildModifier implements BuildModifier {

    private final GradleBuild build;

    public GradleBuildModifier(GradleBuild build) {
        this.build = build;
    }

    @Override
    public void updateDependency(Project project, ModuleDependency dependency,
            Consumer<ProjectModuleDependencyModifier> callback) {

        ProjectModuleDependencyModifier modifier = new GradleProjectModuleDependencyModifier(
                build,
                Arguments.isInstanceOf(GradleProject.class, project, "project"),
                Arguments.isInstanceOf(GradleModuleDependency.class, dependency, "dependency"));

        callback.accept(modifier);

        // TODO return change result ?
    }


    class GradleProjectModuleDependencyModifier implements ProjectModuleDependencyModifier {

        private final GradleBuild build;
        private final GradleProject project;
        private final GradleModuleDependency dependency;

        public GradleProjectModuleDependencyModifier(GradleBuild build, GradleProject project, GradleModuleDependency dependency) {
            this.build = build;
            this.project = project;
            this.dependency = dependency;
        }

        @Override
        public void setVersion(String versionSpec) {

            log.warn("Update gradle build dependency {} {} -> {}", dependency.getId(),
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

            this.updateInlineDependencyVersion(versionSpec);

        }

        private boolean updateInlineDependencyVersion(String versionSpec) {
            Path buildDotGradlePath = this.project.getBuildFile();

            try {
                List<String> strings = Files.readAllLines(buildDotGradlePath);
                strings.forEach(log::warn);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
