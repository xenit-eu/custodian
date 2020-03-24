package eu.xenit.custodian.adapters.channel.mavenrepo;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.domain.CustodianFactory;
import eu.xenit.custodian.asserts.domain.CustodianFactoryAssert;
import eu.xenit.custodian.ports.spi.channel.UpdateChannelFactory;
import org.junit.Test;
import org.springframework.core.io.support.SpringFactoriesLoader;

public class MavenRepositoriesUpdateChannelFactoryTest {

    @Test
    public void testSpringLoadedFactories() {

        assertThat(SpringFactoriesLoader.loadFactories(
                UpdateChannelFactory.class,
                UpdateChannelFactory.class.getClassLoader())
        )
        .hasAtLeastOneElementOfType(MavenRepositoriesUpdateChannelFactory.class);
    }

    @Test
    public void testAnalyzerIsLoadedWithDefaultSettings() {
        CustodianFactory factory = CustodianFactory.withDefaultSettings();

        CustodianFactoryAssert.assertThat(factory)
                .hasUpdateChannel(MavenRepositoriesUpdateChannel.class);
    }

}