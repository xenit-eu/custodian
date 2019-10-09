package eu.xenit.custodian.domain.metadata.buildsystem;

public interface Dependency<DI extends DependencyIdentifier> {

    DI getId();
    String getVersion();

}
