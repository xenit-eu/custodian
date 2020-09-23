package eu.xenit.custodian.ports.spi.buildsystem;

import java.nio.file.Path;

public interface Build {

    BuildSystem buildSystem();
    Project getRootProject();
//    Path getLocation();

}
