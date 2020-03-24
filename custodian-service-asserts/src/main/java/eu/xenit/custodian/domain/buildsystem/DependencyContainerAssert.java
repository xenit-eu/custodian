package eu.xenit.custodian.domain.buildsystem;

import java.util.Optional;
import java.util.stream.Collectors;
import org.assertj.core.api.AbstractAssert;

public class DependencyContainerAssert extends
        AbstractAssert<DependencyContainerAssert, DependencyContainer> {

    public DependencyContainerAssert(DependencyContainer actual) {
        super(actual, DependencyContainerAssert.class);
    }

    protected DependencyContainerAssert(DependencyContainer actual, Class<? extends DependencyContainerAssert> selfType) {
        super(actual, selfType);
    }


//    public static DependencyContainerAssert assertThat(DependencyContainer actual) {
//        return new DependencyContainerAssert(actual);
//    }

    public DependencyContainerAssert hasDependency(String notation) {
        isNotNull();
//
        Optional<? extends Dependency> match = this.actual.matches(notation).findAny();
        if (!match.isPresent())
        {
            this.failWithMessage("Dependency <%s> not found in %s", notation,
                    this.actual.stream().map(Dependency::toString).collect(Collectors.toList()));
        }

        return myself;
    }

    public DependencyContainerAssert hasDependency(String group, String name, String version) {
        return this.hasDependency(group + ":" + name + ":" + version);
    }
}
