package eu.xenit.custodian.domain.metadata.buildsystem;

import java.util.stream.Collectors;
import org.assertj.core.api.AbstractAssert;

public class DependencyContainerAssert extends
        AbstractAssert<DependencyContainerAssert, DependencyContainer<DependencyIdentifier, Dependency<DependencyIdentifier>>> {

    public DependencyContainerAssert(DependencyContainer actual) {
        super(actual, DependencyContainerAssert.class);
    }

    public static DependencyContainerAssert assertThat(DependencyContainer actual) {
        return new DependencyContainerAssert(actual);
    }

    public DependencyContainerAssert hasDependency(String notation) {
        isNotNull();

        Dependency dependency = this.actual.get(notation);
        if (dependency == null) {
            this.failWithMessage("Dependency <%s> not found in %s", notation,
                    this.actual.ids().map(DependencyIdentifier::getId).collect(Collectors.toList()));
        }

        return this;
    }
}
