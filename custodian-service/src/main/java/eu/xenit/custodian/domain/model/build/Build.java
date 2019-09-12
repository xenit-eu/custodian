package eu.xenit.custodian.domain.model.build;

import eu.xenit.custodian.domain.model.buildsystem.BuildSystem;
import java.util.Collection;

public interface Build<TBuild extends Build> {

    BuildSystem buildsystem();

    String name();
    TBuild parent();
    Collection<TBuild> subprojects();
}
