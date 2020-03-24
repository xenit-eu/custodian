package eu.xenit.custodian.domain.buildsystem;

import eu.xenit.custodian.ports.spi.build.Project;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

public interface ProjectContainer {

    Stream<? extends Project> stream();
    Iterator<? extends Project> iterator();

    Optional<? extends Project> getProject(String name);

    default int size() {
        return (int) this.stream().count();
    }
}
