package eu.xenit.custodian.adapters.gradle.buildsystem;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.application.CustodianFactory;
import eu.xenit.custodian.application.CustodianFactoryAssert;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemPortFactory;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import org.junit.jupiter.api.Test;


public class GradleAnalyzerDefaultSettingsTester {

    @Test
    public void testSpringLoadedFactories() {

        var services = ServiceLoader.load(BuildSystemPortFactory.class).stream().map(Provider::get);
        assertThat(services).hasOnlyOneElementSatisfying(factory ->
                assertThat(factory).isInstanceOf(GradleBuildSystemAdaptorFactory.class));
    }

    @Test
    public void testAnalyzerIsLoadedWithDefaultSettings() {
        CustodianFactory factory = CustodianFactory.withDefaultSettings();

        CustodianFactoryAssert.assertThat(factory)
                .hasBuildSystem(GradleBuildSystemAdapter.class);
    }

}