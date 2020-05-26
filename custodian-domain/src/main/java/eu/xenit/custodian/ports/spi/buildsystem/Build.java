package eu.xenit.custodian.ports.spi.buildsystem;

public interface Build {

    BuildSystem buildsystem();

    Project getRootProject();

    BuildModifier modify();
    BuildUpdates updates();
}
