package eu.xenit.custodian.domain.buildsystem;

import eu.xenit.custodian.adapters.gradle.buildsystem.GradleProject;

public class GradleProjectAssert extends ProjectAssert<GradleProjectAssert, GradleProject> {

    public GradleProjectAssert(GradleProject project) {
        super(project);
    }

    public static GradleProjectAssert assertThat(GradleProject project) {
        return new GradleProjectAssert(project);
    }


}
