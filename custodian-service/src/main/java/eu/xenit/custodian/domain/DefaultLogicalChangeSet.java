package eu.xenit.custodian.domain;

import eu.xenit.custodian.domain.changes.LogicalChange;
import eu.xenit.custodian.domain.changes.LogicalChangeSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

public class DefaultLogicalChangeSet implements LogicalChangeSet {

    private Collection<LogicalChange> changes = new ArrayList<>();

    DefaultLogicalChangeSet(Collection<LogicalChange> changeSets) {
        this.changes.addAll(changeSets);
    }

    @Override
    public Stream<LogicalChange> changes() {
        return this.changes.stream();
    }
}
