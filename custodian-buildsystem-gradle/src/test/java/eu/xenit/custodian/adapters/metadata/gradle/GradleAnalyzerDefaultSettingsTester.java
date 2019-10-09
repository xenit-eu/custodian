package eu.xenit.custodian.adapters.metadata.gradle;

import eu.xenit.custodian.domain.CustodianFactory;
import eu.xenit.custodian.domain.CustodianFactoryAssert;
import org.junit.Test;


public class GradleAnalyzerDefaultSettingsTester {

    @Test
    public void testAnalyzerIsLoadedWithDefaultSettings() {
        CustodianFactory factory = CustodianFactory.withDefaultSettings();

        CustodianFactoryAssert.assertThat(factory)
                .hasMetadataAnalyzer(SentinelGradleProjectAnalyzer.class);
    }

}