package eu.xenit.custodian.domain.buildsystem;

import eu.xenit.custodian.adapters.buildsystem.gradle.GradleBuild;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleBuildSystem;
import org.assertj.core.api.Assertions;

public class GradleBuildAssert extends BuildAssert<GradleBuildAssert, GradleBuild> {

    public GradleBuildAssert(GradleBuild build) {
        super(build);
    }

    public static GradleBuildAssert assertThat(GradleBuild build) {
        return new GradleBuildAssert(build);
    }

    @Override
    public GradleProjectAssert getRootProject() {
        return new GradleProjectAssert(this.actual.getProject());
    }

    public GradleBuildAssert isGradleBuild() {
        Assertions.assertThat(this.actual.buildsystem().id()).isEqualTo(GradleBuildSystem.ID);
        return this.myself;
    }

    public GradleBuildAssert hasName(String name) {
        Assertions.assertThat(this.actual.name()).isEqualToIgnoringWhitespace(name);
        return this.myself;
    }
}
