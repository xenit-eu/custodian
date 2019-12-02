package eu.xenit.custodian.domain.buildsystem;

import java.util.stream.Stream;

public interface DependencyContainer<TDependency extends Dependency> {

    Stream<TDependency> stream();

    default Stream<TDependency> matches(DependencyMatcher<TDependency> matcher) {
        return this.stream().filter(matcher);
    }

    default Stream<TDependency> matches(String notation) {
        return this.matches(this.matcher(notation));
    }

    DependencyMatcher<TDependency> matcher(String notation);

}
