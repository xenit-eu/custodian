package eu.xenit.custodian.ports.spi.buildsystem;

import java.util.function.Predicate;

public interface DependencyMatcher<TDependency extends Dependency> extends Predicate<TDependency> {

    @Override
    boolean test(TDependency var1);

    default boolean testUncasted(Dependency dep) {
        TDependency casted = castedOrNull(dep);
        return this.test(casted);
    }

    TDependency castedOrNull(Dependency dep);
}
