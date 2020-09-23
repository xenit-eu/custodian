package eu.xenit.custodian.adapters.gradle.buildsystem.spi.model;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemException;
import java.nio.file.Path;
import java.util.Optional;

public interface GradleBuildReaderPort {

    Optional<GradleBuild> getBuild(Path location) throws BuildSystemException;

}
