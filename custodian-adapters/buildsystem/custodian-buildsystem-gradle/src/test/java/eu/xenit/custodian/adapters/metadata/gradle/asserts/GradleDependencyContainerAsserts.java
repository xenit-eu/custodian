package eu.xenit.custodian.adapters.metadata.gradle.asserts;

import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleDependency;
import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleDependencyContainer;
import eu.xenit.custodian.domain.buildsystem.DependencyContainerAssert;
import org.assertj.core.api.AbstractAssert;

public class GradleDependencyContainerAsserts extends
//        AbstractAssert<GradleDependencyContainerAsserts, GradleDependencyContainer> {
        DependencyContainerAssert<GradleDependency> {

    public GradleDependencyContainerAsserts(GradleDependencyContainer actual) {
        super(actual, GradleDependencyContainerAsserts.class);
    }

    public static GradleDependencyContainerAsserts assertThat(GradleDependencyContainer actual)
    {
        return new GradleDependencyContainerAsserts(actual);
    }

//    public GradleDependencyContainerAsserts hasDependency(String notation) {
//        isNotNull();
//
//        super.hasDependency(notation);
//
//
//        return this;
//    }
}

