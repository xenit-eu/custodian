package eu.xenit.custodian.domain.entities.buildsystem;

import eu.xenit.custodian.ports.spi.buildsystem.Build;
import java.util.stream.Stream;

public interface BuildSystemsCollection {

    Build get(String id);

    Stream<String> ids();
    Stream<Build> builds();
}
