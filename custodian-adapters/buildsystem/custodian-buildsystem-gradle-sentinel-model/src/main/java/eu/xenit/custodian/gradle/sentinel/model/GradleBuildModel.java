package eu.xenit.custodian.gradle.sentinel.model;

import java.util.Collection;

public interface GradleBuildModel {

    ProjectModel getRootProject();

    Collection<ProjectModel> getAllProjects();

//    <T> boolean hasPlugin(Class<T> type);

}
