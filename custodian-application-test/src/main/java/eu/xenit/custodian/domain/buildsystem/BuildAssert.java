package eu.xenit.custodian.domain.buildsystem;

import eu.xenit.custodian.ports.spi.buildsystem.Build;
import eu.xenit.custodian.ports.spi.buildsystem.Project;
import org.assertj.core.api.AbstractAssert;

public class BuildAssert<SELF extends BuildAssert<SELF, ACTUAL>, ACTUAL extends Build> extends AbstractAssert<SELF, ACTUAL> {

    public BuildAssert(ACTUAL build) {
        super(build, BuildAssert.class);
    }

    @SuppressWarnings("unchecked")
    public ProjectAssert assertRootProject() {
        return new ProjectAssert(this.actual.getRootProject());
    }
//    public abstract ProjectAssert getRootProject();
}
