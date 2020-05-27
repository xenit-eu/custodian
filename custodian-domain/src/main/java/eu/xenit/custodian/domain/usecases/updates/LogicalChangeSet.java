package eu.xenit.custodian.domain.usecases.updates;

import eu.xenit.custodian.ports.spi.updates.LogicalChange;
import java.util.stream.Stream;

public interface LogicalChangeSet {

    Stream<LogicalChange> stream();

}
