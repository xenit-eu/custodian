package eu.xenit.custodian.domain;

import eu.xenit.custodian.domain.changes.ChangeSet;
import eu.xenit.custodian.domain.changes.ChangeSetCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

public class DefaultChangeSetCollection implements ChangeSetCollection {

    private Collection<ChangeSet> changes = new ArrayList<>();

    DefaultChangeSetCollection(Collection<ChangeSet> changeSets) {
        this.changes.addAll(changeSets);
    }

    @Override
    public Stream<ChangeSet> changes() {
        return this.changes.stream();
    }
}
