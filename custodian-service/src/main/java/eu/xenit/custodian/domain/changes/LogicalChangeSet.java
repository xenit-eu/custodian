package eu.xenit.custodian.domain.changes;

import java.util.stream.Stream;

public interface LogicalChangeSet {

    Stream<LogicalChange> changes();
}
