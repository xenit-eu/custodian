package eu.xenit.custodian.domain.changes;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.domain.buildsystem.Dependency;
import eu.xenit.custodian.domain.buildsystem.DependencyMatcher;
import eu.xenit.custodian.domain.buildsystem.ModuleDependency;
import java.util.Optional;
import org.assertj.core.api.AbstractAssert;

public class LogicalChangeSetAssert extends AbstractAssert<LogicalChangeSetAssert, LogicalChangeSet> {

    public LogicalChangeSetAssert(LogicalChangeSet changeSet) {
        super(changeSet, LogicalChangeSetAssert.class);
    }

    /**
     * Assert {@code LogicalChangeSet} contains a dependency
     */
    public <T extends Dependency> LogicalChangeSetAssert hasDependencyUpdate(
            Class<T> type,
            DependencyMatcher<T> matcher,
            String targetVersion) {

        Optional<DependencyUpdate> dependencyUpdate = this.actual.changes()
                .filter(DependencyUpdate.class::isInstance)
                .map(DependencyUpdate.class::cast)
                .filter(update -> type.isInstance(update.getDependency()))
                .filter(update -> matcher.test(type.cast(update.getDependency())))
                .findAny();

        assertThat(dependencyUpdate)
                .as("Could not find dependency update that matches %s", matcher)
                .isPresent();

        assertThat(dependencyUpdate.get().getProposedVersion())
                .isPresent()
                .hasValueSatisfying(updateVersion ->
                        assertThat(updateVersion.getValue())
                                .as("Dependency update %s:%s -> %s",
                                        dependencyUpdate.get().getDependency().getModuleId().getId(),
                                        dependencyUpdate.get().getDependency().getVersionSpec(), targetVersion)
                                .isEqualTo(targetVersion));

        return myself;
    }


}
