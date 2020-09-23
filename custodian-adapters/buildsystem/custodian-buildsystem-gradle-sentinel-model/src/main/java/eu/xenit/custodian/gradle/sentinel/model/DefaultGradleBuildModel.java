package eu.xenit.custodian.gradle.sentinel.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.gradle.tooling.model.DomainObjectSet;
import org.gradle.tooling.model.internal.ImmutableDomainObjectSet;

public class DefaultGradleBuildModel implements GradleBuildModel, Serializable {

    private final Map<String, ProjectModel> projects;

    public DefaultGradleBuildModel(Map<String, ProjectModel> projects) {
        this.projects = projects;
    }

//    @Override
//    public ProjectModel getRootProject() {
//        return null;
//    }

    @Override
    public ProjectModel getRootProject() {
        return null;
    }

    @Override
    public Collection<ProjectModel> getAllProjects() {
//        return ImmutableDomainObjectSet.of(this.projects.values());
        return this.projects.values();
    }

//    @Override
//    public <T> boolean hasPlugin(Class<T> type) {
//        return this.pluginClassNames.contains(type.getName());
//    }
}
