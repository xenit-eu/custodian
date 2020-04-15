package eu.xenit.custodian.driver.test;

import eu.xenit.custodian.domain.changes.LogicalChange;
import eu.xenit.custodian.domain.changes.LogicalChangeSet;
import eu.xenit.custodian.domain.changes.LogicalChangeSetAssert;
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
    public Stream<LogicalChange> stream() {
        return this.changeSet.stream();
    }

    @Override
    public LogicalChangeSetAssert assertThat() {
        return new LogicalChangeSetAssert(this.changeSet);
    }

}
