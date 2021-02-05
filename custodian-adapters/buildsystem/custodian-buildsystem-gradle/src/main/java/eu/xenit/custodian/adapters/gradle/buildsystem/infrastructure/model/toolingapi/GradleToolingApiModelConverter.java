package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.toolingapi;

import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuildSystem;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradleBuilder;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradleProjectBuilder;
import eu.xenit.custodian.gradle.sentinel.model.GradleBuildModel;
import eu.xenit.custodian.gradle.sentinel.model.ProjectModel;
import java.nio.file.Paths;
import java.util.function.Consumer;
import org.gradle.internal.impldep.com.esotericsoftware.minlog.Log;

public class GradleToolingApiModelConverter {

    static GradleBuild convert(GradleBuildModel buildModel) {

        GradleBuilder builder = new GradleBuildSystem().builder();
        builder.workingDirectory(Paths.get(buildModel.getRootDirectory()));

        buildModel.getAllProjects()
                .forEach(project -> builder.addProject(convertProject(project)));

        return builder.build();
    }

    private static Consumer<GradleProjectBuilder> convertProject(ProjectModel project) {
        return projectBuilder -> {
            projectBuilder
                    .name(project.getName())
                    .group(project.getGroup())
                    .version(project.getVersion())

                    .path(project.getPath())
                    .buildFile(project.getBuildFile())
                    .projectDir(project.getProjectDir())

                    .withPlugins(plugins -> {
                        project.getPlugins().all().forEach(plugin -> {
                            plugins.addPlugin(plugin.getId(), plugin.getVersion(), plugin.isApplied());
                        });
                    })

                    .withRepositories(repositories -> {
                        project.getRepositories().all().forEach(repo -> {
                            repositories.addMavenRepository(repo.getName(), repo.getUrl());
                        });
                    })

                    .withDependencies(dependencies -> {
                        project.getDependencies().all().forEach((configuration, dependencySet) -> {
                            dependencySet.forEach(dependency -> {
                                dependencies.add(configuration, dependency.getGroup(), dependency.getName(),
                                        dependency.getVersion());
                            });
                        });
                    });

        };
    }
}
