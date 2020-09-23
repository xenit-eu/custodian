package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import org.assertj.core.api.AbstractAssert;

//public class GradleBuildAssert extends BuildAssert<GradleBuildAssert, GradleBuild> {
public class GradleBuildAssert extends AbstractAssert<GradleBuildAssert, GradleBuild> {

    public GradleBuildAssert(GradleBuild build) {
        super(build, GradleBuildAssert.class);
    }


    public static GradleBuildAssert assertThat(GradleBuild build) {
        return new GradleBuildAssert(build);
    }

    public GradleProjectAssert assertRootProject() {
        return new GradleProjectAssert(this.actual.getRootProject());
    }
}
