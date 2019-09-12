package eu.xenit.custodian.sentinel.analyzer.project;

import eu.xenit.custodian.sentinel.analyzer.AspectAnalysis;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.gradle.api.Project;

public class ProjectInfoAnalyzer implements AspectAnalysis {

    public ProjectInformation analyze(Project project) {

        return ProjectInformation.builder()
                .name(project.getName())
                .path(project.getPath())
                .projectDir(this.getRelativeProjectDir(project).toString())
                .displayName(project.getDisplayName())
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
