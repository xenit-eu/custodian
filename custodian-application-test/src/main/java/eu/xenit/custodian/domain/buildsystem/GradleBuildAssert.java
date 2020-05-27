package eu.xenit.custodian.domain.buildsystem;

import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuildSystem;
import org.assertj.core.api.Assertions;

public class GradleBuildAssert extends BuildAssert<GradleBuildAssert, GradleBuild> {

    public GradleBuildAssert(GradleBuild build) {
        super(build);
    }

    public static GradleBuildAssert assertThat(GradleBuild build) {
        return new GradleBuildAssert(build);
    }

    @Override
    public GradleProjectAssert assertRootProject() {
        return new GradleProjectAssert(this.actual.getRootProject());
    }

    public GradleBuildAssert isGradleBuild() {
        Assertions.assertThat(this.actual.buildsystem().id()).isEqualTo(GradleBuildSystem.ID);
        return this.myself;
    }

}
