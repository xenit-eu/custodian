package eu.xenit.custodian.domain.buildsystem;

import eu.xenit.custodian.adapters.gradle.buildsystem.GradleDependencyContainer;

public class GradleDependencyContainerAsserts extends DependencyContainerAssert {

    public GradleDependencyContainerAsserts(GradleDependencyContainer actual) {
        super(actual, GradleDependencyContainerAsserts.class);
    }

    public static GradleDependencyContainerAsserts assertThat(GradleDependencyContainer actual)
    {
        return new GradleDependencyContainerAsserts(actual);
    }
}

