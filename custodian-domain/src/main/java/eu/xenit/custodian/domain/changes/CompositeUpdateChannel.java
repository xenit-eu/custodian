package eu.xenit.custodian.domain.changes;

import eu.xenit.custodian.domain.usecases.changes.LogicalChange;
import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.ports.spi.channel.UpdateChannel;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class CompositeUpdateChannel implements UpdateChannel {

    private final Collection<UpdateChannel> channels;

    public CompositeUpdateChannel(Collection<UpdateChannel> channels) {
        Objects.requireNonNull(channels, "channels must not be null");
        this.channels = channels;
    }

    @Override
    public Collection<LogicalChange> getChanges(ClonedRepositorySourceMetadata metadata) {
        return channels.stream()
                .flatMap(channel -> channel.getChanges(metadata).stream())
                .collect(Collectors.toList());
    }
}
