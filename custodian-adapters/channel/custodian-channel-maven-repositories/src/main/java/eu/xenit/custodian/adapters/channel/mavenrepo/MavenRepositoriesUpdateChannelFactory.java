package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.ports.spi.updates.UpdateChannel;
import eu.xenit.custodian.ports.spi.updates.UpdateChannelFactory;

public class MavenRepositoriesUpdateChannelFactory implements UpdateChannelFactory {

    @Override
    public UpdateChannel create() {
        return new MavenRepositoriesUpdateChannel(new DefaultMavenArtifactResolver());
    }
}
