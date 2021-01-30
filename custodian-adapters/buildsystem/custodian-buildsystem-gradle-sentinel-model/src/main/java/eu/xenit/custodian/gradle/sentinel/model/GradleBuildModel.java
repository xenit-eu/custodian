package eu.xenit.custodian.gradle.sentinel.model;

import java.nio.file.Path;
import java.util.Collection;

public interface GradleBuildModel {

    String getRootDirectory();
    ProjectModel getRootProject();

    Collection<ProjectModel> getAllProjects();

}
