package eu.xenit.custodian.domain.buildsystem;

import java.util.function.Predicate;

public interface DependencyMatcher<TDependency extends Dependency> extends Predicate<TDependency> {

    default boolean testUncasted(Dependency dep) {
        TDependency casted = castedOrNull(dep);
        return this.test(casted);
    }

    TDependency castedOrNull(Dependency dep);
}
