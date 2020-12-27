package eu.xenit.custodian.gradle.sentinel.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class DefaultGradleBuildModel implements GradleBuildModel, Serializable {

    private final Map<String, ProjectModel> projects;

    public DefaultGradleBuildModel(Map<String, ProjectModel> projects) {
        this.projects = projects;
    }

    @Override
    public ProjectModel getRootProject() {
        return null;
    }

    @Override
    public Collection<ProjectModel> getAllProjects() {
//        return ImmutableDomainObjectSet.of(this.projects.values());
        return this.projects.values();
    }

}
