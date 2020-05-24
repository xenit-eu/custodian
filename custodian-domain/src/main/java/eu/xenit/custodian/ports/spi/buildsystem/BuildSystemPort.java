package eu.xenit.custodian.ports.spi.buildsystem;

import java.nio.file.Path;
import java.util.Optional;

public interface BuildSystemPort {

    BuildSystem id();

    Optional<? extends Build> getBuild(Path location) throws BuildSystemException;
}
