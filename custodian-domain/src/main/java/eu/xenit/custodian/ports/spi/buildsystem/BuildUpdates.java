package eu.xenit.custodian.ports.spi.buildsystem;

import java.util.stream.Stream;

public interface BuildUpdates {

    Stream<BuildUpdateProposal> proposals();
}
