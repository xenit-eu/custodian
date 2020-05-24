package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemPort;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemPortFactory;

public class GradleBuildSystemAdaptorFactory implements BuildSystemPortFactory {

    @Override
    public BuildSystemPort create() {
        return new GradleBuildSystemAdapter();
    }
}
