package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import java.util.function.Consumer;
import org.assertj.core.api.AbstractAssert;

public class GradleBuildAssert extends AbstractAssert<GradleBuildAssert, GradleBuild> {

    public GradleBuildAssert(GradleBuild build) {
        super(build, GradleBuildAssert.class);
    }


    public static GradleBuildAssert assertThat(GradleBuild build) {
        return new GradleBuildAssert(build);
    }

    public GradleBuildAssert assertRootProject(Consumer<GradleProjectAssert> callback) {
        GradleProjectAssert project = new GradleProjectAssert(this.actual.getRootProject());
        callback.accept(project);
        return this;
    }
}
