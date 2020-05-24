package eu.xenit.custodian.domain.buildsystem;

import eu.xenit.custodian.ports.spi.buildsystem.Build;
import org.assertj.core.api.AbstractAssert;

public class BuildAssert<SELF extends BuildAssert<SELF, ACTUAL>, ACTUAL extends Build> extends AbstractAssert<SELF, ACTUAL> {

    public BuildAssert(ACTUAL build) {
        super(build, BuildAssert.class);
    }

    public ProjectAssert getRootProject() {
        return new ProjectAssert(this.actual.getRootProject());
    }
}
