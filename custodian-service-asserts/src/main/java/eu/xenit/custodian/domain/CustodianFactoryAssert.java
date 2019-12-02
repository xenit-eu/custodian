package eu.xenit.custodian.domain;

import eu.xenit.custodian.ports.spi.channel.UpdateChannel;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzer;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class CustodianFactoryAssert extends AbstractAssert<CustodianFactoryAssert, CustodianFactory> {

    public CustodianFactoryAssert(CustodianFactory custodianFactory) {
        super(custodianFactory, CustodianFactoryAssert.class);
    }

    public static CustodianFactoryAssert assertThat(CustodianFactory factory) {
        return new CustodianFactoryAssert(factory);
    }

    public CustodianFactoryAssert hasSourceControlHandler(Class<? extends SourceControlHandler> handler) {
        Assertions.assertThat(this.actual.getSourceControlHandlers())
                .hasAtLeastOneElementOfType(handler);

        return myself;
    }

    public CustodianFactoryAssert hasNoSourceControlHandlers() {
        Assertions.assertThat(this.actual.getSourceControlHandlers()).isEmpty();
        return myself;
    }

    public CustodianFactoryAssert hasMetadataAnalyzer(Class<? extends ProjectMetadataAnalyzer> handler) {
        Assertions.assertThat(this.actual.getMetadataAnalyzers())
                .hasAtLeastOneElementOfType(handler);

        return myself;
    }

    public CustodianFactoryAssert hasNoMetadataAnalyzers() {
        Assertions.assertThat(this.actual.getMetadataAnalyzers()).isEmpty();
        return myself;
    }

    public CustodianFactoryAssert hasUpdateChannel(Class<? extends UpdateChannel> updateChannel) {
        Assertions.assertThat(this.actual.getUpdateChannels())
                .hasAtLeastOneElementOfType(updateChannel);

        return myself;
    }

    public CustodianFactoryAssert hasNoUpdateChannels() {
        Assertions.assertThat(this.actual.getUpdateChannels()).isEmpty();
        return myself;
    }
}
