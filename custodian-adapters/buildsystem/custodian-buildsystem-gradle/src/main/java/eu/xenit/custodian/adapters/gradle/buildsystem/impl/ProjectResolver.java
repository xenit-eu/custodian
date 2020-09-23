package eu.xenit.custodian.adapters.gradle.buildsystem.impl;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import java.util.Optional;

public interface ProjectResolver {

    GradleProject getRootProject();
    Optional<GradleProject> getParentProject(GradleProject project);


}
