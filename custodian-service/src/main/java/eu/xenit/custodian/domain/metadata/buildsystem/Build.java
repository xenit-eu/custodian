package eu.xenit.custodian.domain.metadata.buildsystem;

import java.util.Collection;

public interface Build<TBuild extends Build> {

    BuildSystem buildsystem();

    String name();
    TBuild parent();
    Collection<TBuild> subprojects();

    DependencyContainer dependencies();
    RepositoryContainer repositories();
}
