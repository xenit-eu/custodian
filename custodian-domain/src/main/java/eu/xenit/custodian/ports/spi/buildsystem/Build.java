package eu.xenit.custodian.ports.spi.buildsystem;

public interface Build {

    BuildSystem buildsystem();

    @Deprecated
    String name();

    Project getRootProject();

    BuildModifier modify();
    BuildUpdates updates();
}
