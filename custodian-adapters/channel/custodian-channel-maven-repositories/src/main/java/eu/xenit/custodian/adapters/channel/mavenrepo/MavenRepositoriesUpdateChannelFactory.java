package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.ports.spi.channel.UpdateChannel;
import eu.xenit.custodian.ports.spi.channel.UpdateChannelFactory;

public class MavenRepositoriesUpdateChannelFactory implements UpdateChannelFactory {

    @Override
    public UpdateChannel create() {
        return new MavenRepositoriesUpdateChannel(new DefaultMavenArtifactResolver());
    }
}
