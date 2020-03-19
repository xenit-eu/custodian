package eu.xenit.custodian.adapters.client.test;

import eu.xenit.custodian.asserts.build.changes.LogicalChange;
import eu.xenit.custodian.asserts.build.changes.LogicalChangeSet;
import eu.xenit.custodian.asserts.build.changes.LogicalChangeSetAssert;
import java.util.Objects;
import java.util.stream.Stream;
import org.assertj.core.api.AssertProvider;

public class ChangeSetAssertionTrait implements LogicalChangeSet, AssertProvider<LogicalChangeSetAssert> {

    private final LogicalChangeSet changeSet;

    public ChangeSetAssertionTrait(LogicalChangeSet changeSet) {
        Objects.requireNonNull(changeSet, "Argument 'changeSet' is required");
        this.changeSet = changeSet;
    }

    @Override
    public Stream<LogicalChange> changes() {
        return this.changeSet.changes();
    }

    @Override
    public LogicalChangeSetAssert assertThat() {
        return new LogicalChangeSetAssert(this.changeSet);
    }

}
