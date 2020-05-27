package eu.xenit.custodian.domain.changes;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.ports.spi.buildsystem.Dependency;
import eu.xenit.custodian.ports.spi.buildsystem.DependencyMatcher;
import eu.xenit.custodian.domain.usecases.updates.DependencyVersionUpdate;
import eu.xenit.custodian.domain.usecases.updates.LogicalChangeSet;
import java.util.Optional;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class LogicalChangeSetAssert extends AbstractAssert<LogicalChangeSetAssert, LogicalChangeSet> {

    public LogicalChangeSetAssert(LogicalChangeSet changeSet) {
        super(changeSet, LogicalChangeSetAssert.class);
    }

    public LogicalChangeSetAssert hasSize(int size) {
        Assertions.assertThat(this.actual.stream()).hasSize(size);
        return myself;
    }


    /**
     * Assert {@code LogicalChangeSet} contains a dependency
     */
    public <T extends Dependency> LogicalChangeSetAssert hasDependencyUpdate(
            Class<T> type,
            DependencyMatcher<T> matcher,
            String targetVersion) {

        Optional<DependencyVersionUpdate> dependencyUpdate = this.actual.stream()
                .filter(DependencyVersionUpdate.class::isInstance)
                .map(DependencyVersionUpdate.class::cast)
                .filter(update -> type.isInstance(update.getDependency()))
                .filter(update -> matcher.test(type.cast(update.getDependency())))
                .findAny();

        Assertions.assertThat(dependencyUpdate)
                .as("Could not find dependency update that matches %s", matcher)
                .isPresent();

        assertThat(dependencyUpdate.get().getProposedVersion())
                .isPresent()
                .hasValueSatisfying(updateVersion ->
                        assertThat(updateVersion.getValue())
                                .as("Dependency update %s:%s -> %s",
                                        dependencyUpdate.get().getDependency().getId(),
                                        dependencyUpdate.get().getCurrentVersion(), targetVersion)
                                .isEqualTo(targetVersion));

        return myself;
    }


}
