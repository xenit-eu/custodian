package eu.xenit.custodian.domain.usecases.changes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

public class DefaultLogicalChangeSet implements LogicalChangeSet {

    private Collection<LogicalChange> changes = new ArrayList<>();

    public DefaultLogicalChangeSet(Collection<LogicalChange> changeSets) {
        this.changes.addAll(changeSets);
    }

    @Override
    public Stream<LogicalChange> stream() {
        return this.changes.stream();
    }
}
