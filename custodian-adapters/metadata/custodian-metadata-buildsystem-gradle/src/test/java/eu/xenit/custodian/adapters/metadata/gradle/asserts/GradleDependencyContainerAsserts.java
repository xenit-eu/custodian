package eu.xenit.custodian.adapters.metadata.gradle.asserts;

import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleDependencyContainer;
import org.assertj.core.api.AbstractAssert;

public class GradleDependencyContainerAsserts extends
        AbstractAssert<GradleDependencyContainerAsserts, GradleDependencyContainer> {

    public GradleDependencyContainerAsserts(GradleDependencyContainer actual) {
        super(actual, GradleDependencyContainerAsserts.class);
    }

    public static GradleDependencyContainerAsserts assertThat(GradleDependencyContainer actual)
    {
        return new GradleDependencyContainerAsserts(actual);
    }

    public GradleDependencyContainerAsserts hasDependency(String notation) {
        isNotNull();

        this.failWithMessage("assert not implemented");

        return this;
    }
}

