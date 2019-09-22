package eu.xenit.custodian.sentinel.adapters.project;

import eu.xenit.custodian.sentinel.domain.PartialAnalyzer;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.gradle.api.Project;

public class ProjectInfoAnalyzer implements PartialAnalyzer<ProjectInformation> {

    public ProjectInformation analyze(Project project) {

        return ProjectInformation.builder()
                .name(project.getName())
                .path(project.getPath())
                .displayName(project.getDisplayName())
                .parent(project.getParent() != null ? project.getParent().getPath() : null)
                .projectDir(this.getRelativeProjectDir(project).toString())

                .subprojects(this.getChildProjects(project))
                .build();
    }

    private Path getRelativeProjectDir(Project project) {
        return project.getRootDir().toPath().relativize(project.getProjectDir().toPath());
    }

    private Map<String, ProjectInformation> getChildProjects(Project project) {
        return project.getChildProjects().values().stream()
                .map(this::analyze)
                .collect(Collectors.toMap(ProjectInformation::getName, Function.identity()));
    }
}
