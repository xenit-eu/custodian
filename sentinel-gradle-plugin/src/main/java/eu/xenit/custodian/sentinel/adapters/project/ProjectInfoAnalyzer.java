package eu.xenit.custodian.sentinel.adapters.project;

import eu.xenit.custodian.sentinel.domain.PartialAnalyzer;
import java.io.File;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.gradle.api.Project;

public class ProjectInfoAnalyzer implements PartialAnalyzer<ProjectInformation> {

    private final boolean useRelativePaths;

    public ProjectInfoAnalyzer(boolean useRelativePaths) {

        this.useRelativePaths = useRelativePaths;
    }


    public ProjectInformation analyze(Project project) {

        return ProjectInformation.builder()
                .name(project.getName())
                .path(project.getPath())
                .displayName(project.getDisplayName())

                .projectDir(this.getPathString(project.getRootProject(), project.getProjectDir()))
                .buildFile(this.getPathString(project, project.getBuildFile()))
                .buildDir(this.getPathString(project, project.getBuildDir()))

                .subprojects(this.getChildProjects(project))
                .build();
    }

    private String getPathString(Project project, File path) {
        if (this.useRelativePaths) {
            return project.getProjectDir()
                    .toPath()
                    .relativize(path.toPath())
                    .toString();
        } else {
            return path.getAbsolutePath();
        }
    }

    private Map<String, ProjectInformation> getChildProjects(Project project) {
        return project.getChildProjects().values().stream()
                .map(this::analyze)
                .collect(Collectors.toMap(ProjectInformation::getName, Function.identity()));
    }
}
