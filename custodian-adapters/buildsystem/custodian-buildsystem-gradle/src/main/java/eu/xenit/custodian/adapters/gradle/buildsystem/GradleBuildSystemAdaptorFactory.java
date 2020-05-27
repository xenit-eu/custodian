package eu.xenit.custodian.adapters.gradle.buildsystem;

import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemPort;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemPortFactory;

public class GradleBuildSystemAdaptorFactory implements BuildSystemPortFactory {

    @Override
    public BuildSystemPort create() {
        return new GradleBuildSystemAdapter();
    }
}
