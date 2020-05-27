package eu.xenit.custodian.domain.changes;

import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import eu.xenit.custodian.ports.spi.updates.LogicalChange;
import eu.xenit.custodian.ports.spi.updates.UpdatePort;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class CompositeUpdateChannel implements UpdatePort {

    private final Collection<UpdatePort> channels;

    public CompositeUpdateChannel(Collection<UpdatePort> channels) {
        Objects.requireNonNull(channels, "channels must not be null");
        this.channels = channels;
    }

    @Override
    public Collection<LogicalChange> getUpdateProposals(ProjectModel projectModel) {
        return channels.stream()
                .flatMap(channel -> channel.getUpdateProposals(projectModel).stream())
                .collect(Collectors.toList());
    }
}
