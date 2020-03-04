package eu.xenit.custodian.domain.buildsystem;

import java.util.Collection;

public interface Build<TBuild extends Build<TBuild, TDependency>, TDependency extends Dependency> {

    BuildSystem buildsystem();

    String name();
    TBuild parent();
    Collection<TBuild> subprojects();

    DependencyContainer<TDependency> dependencies();
    RepositoryContainer repositories();
}
