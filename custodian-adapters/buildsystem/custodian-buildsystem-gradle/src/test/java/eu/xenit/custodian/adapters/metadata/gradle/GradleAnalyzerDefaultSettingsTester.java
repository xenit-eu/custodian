package eu.xenit.custodian.adapters.metadata.gradle;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.asserts.build.CustodianFactory;
import eu.xenit.custodian.asserts.build.CustodianFactoryAssert;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzerFactory;
import org.junit.Test;
import org.springframework.core.io.support.SpringFactoriesLoader;


public class GradleAnalyzerDefaultSettingsTester {

    @Test
    public void testSpringLoadedFactories() {

        assertThat(SpringFactoriesLoader.loadFactories(
                ProjectMetadataAnalyzerFactory.class,
                ProjectMetadataAnalyzerFactory.class.getClassLoader())
        ).hasAtLeastOneElementOfType(SentinelGradleProjectAnalayzerFactory.class);
    }

    @Test
    public void testAnalyzerIsLoadedWithDefaultSettings() {
        CustodianFactory factory = CustodianFactory.withDefaultSettings();

        CustodianFactoryAssert.assertThat(factory)
                .hasMetadataAnalyzer(SentinelGradleProjectAnalyzer.class);
    }

}