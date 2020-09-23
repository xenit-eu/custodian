package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.toolingapi;

import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuildSystem;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradleBuilder;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradleProjectBuilder;
import eu.xenit.custodian.gradle.sentinel.model.GradleBuildModel;
import eu.xenit.custodian.gradle.sentinel.model.ProjectModel;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class GradleToolingApiModelConverter {

    static GradleBuild convert(GradleBuildModel buildModel) {

        GradleBuilder builder = new GradleBuildSystem().builder();

        buildModel.getAllProjects()
                .forEach(project -> builder.addProject(convertProject(project)));

        return builder.build();
    }

    private static Consumer<GradleProjectBuilder> convertProject(ProjectModel project) {
        return projectBuilder -> {
            projectBuilder
                    .name(project.getName())
                    .path(project.getPath())
                    .buildFile(project.getBuildFile())
                    .projectDir(project.getProjectDir());

        };
    }
}
