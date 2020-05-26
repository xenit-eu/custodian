package eu.xenit.custodian.ports.spi.buildsystem;

import java.util.stream.Stream;

public interface DependencyContainer {

    /**
     * The project that defines this set of dependencies.
     *
     * @return the project. Never null;
     */
    Project getProject();

    Stream<? extends Dependency> stream();

    default Stream<? extends Dependency> matches(DependencyMatcher<Dependency> matcher) {
        return this.stream().filter(matcher);
    }

    default Stream<? extends Dependency> matches(String notation) {
        DependencyMatcher<? extends Dependency> matcher = this.matcher(notation);
        return this.stream().filter(matcher::testUncasted);
    }

    DependencyMatcher<? extends Dependency> matcher(String notation);

}
