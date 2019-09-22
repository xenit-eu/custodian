package eu.xenit.custodian.adapters.analysis.gradle.asserts;

import eu.xenit.custodian.adapters.analysis.gradle.build.DefaultExternalModuleIdentifier;
import eu.xenit.custodian.adapters.analysis.gradle.build.GradleDependencyContainer;
import java.util.Objects;
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

//        DefaultExternalModuleIdentifier.
//        // check condition
//        if (!Objects.equals(actual.get()getName(), name)) {
//            failWithMessage("Expected character's name to be <%s> but was <%s>", name, actual.getName());
//        }
        return this;
    }
}

