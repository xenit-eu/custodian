package eu.xenit.custodian.domain.usecases.updates;

import eu.xenit.custodian.ports.spi.updates.LogicalChange;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

public class DefaultLogicalChangeSet implements LogicalChangeSet {

    private Collection<LogicalChange> changes = new ArrayList<>();

    public DefaultLogicalChangeSet(Collection<LogicalChange> changeSets) {
        this.changes.addAll(changeSets);
    }

    public DefaultLogicalChangeSet(Stream<LogicalChange> stream) {
        stream.forEach(this.changes::add);
    }

    @Override
    public Stream<LogicalChange> stream() {
        return this.changes.stream();
    }
}
