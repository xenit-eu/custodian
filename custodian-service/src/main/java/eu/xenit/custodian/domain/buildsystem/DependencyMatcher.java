package eu.xenit.custodian.domain.buildsystem;

import java.util.function.Predicate;

public interface DependencyMatcher<T extends Dependency> extends Predicate<T> {

}
