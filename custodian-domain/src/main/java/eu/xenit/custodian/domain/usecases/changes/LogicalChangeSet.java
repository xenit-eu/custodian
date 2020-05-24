package eu.xenit.custodian.domain.usecases.changes;

import java.util.stream.Stream;

public interface LogicalChangeSet {

    Stream<LogicalChange> stream();

}
