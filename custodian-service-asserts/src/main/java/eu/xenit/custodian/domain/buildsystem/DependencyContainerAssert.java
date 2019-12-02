package eu.xenit.custodian.domain.buildsystem;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.api.AbstractAssert;

public class DependencyContainerAssert<TDependency extends Dependency> extends
        AbstractAssert<DependencyContainerAssert<TDependency>, DependencyContainer<TDependency>> {

    public DependencyContainerAssert(DependencyContainer<TDependency> actual) {
        super(actual, DependencyContainerAssert.class);
    }

    protected DependencyContainerAssert(DependencyContainer<TDependency> actual, Class<? extends DependencyContainerAssert<TDependency>> selfType) {
        super(actual, selfType);
    }


//    public static DependencyContainerAssert assertThat(DependencyContainer actual) {
//        return new DependencyContainerAssert(actual);
//    }

    public DependencyContainerAssert<TDependency> hasDependency(String notation) {
        isNotNull();
//
        Optional<TDependency> match = this.actual.matches(notation).findAny();
        if (!match.isPresent())
        {
            this.failWithMessage("Dependency <%s> not found in %s", notation,
                    this.actual.stream().map(Dependency::toString).collect(Collectors.toList()));
        }

        return myself;
    }

    public DependencyContainerAssert<TDependency> hasDependency(String group, String name, String version) {
        return this.hasDependency(group + ":" + name + ":" + version);
    }
}
