package eu.xenit.custodian.asserts.domain;

import eu.xenit.custodian.domain.CustodianFactory;
import eu.xenit.custodian.domain.CustodianFactoryAccessor;
import eu.xenit.custodian.ports.spi.channel.UpdateChannel;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzer;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class CustodianFactoryAssert extends AbstractAssert<CustodianFactoryAssert, CustodianFactory> {

    private final CustodianFactoryAccessor accessor;
    public CustodianFactoryAssert(CustodianFactory custodianFactory) {
        super(custodianFactory, CustodianFactoryAssert.class);

        this.accessor = new CustodianFactoryAccessor(this.actual);
    }

    public static CustodianFactoryAssert assertThat(CustodianFactory factory) {
        return new CustodianFactoryAssert(factory);
    }

    public CustodianFactoryAssert hasSourceControlHandler(Class<? extends SourceControlHandler> handler) {
        Assertions.assertThat(this.accessor.getSourceControlHandlers())
                .hasAtLeastOneElementOfType(handler);

        return myself;
    }

    public CustodianFactoryAssert hasNoSourceControlHandlers() {
        Assertions.assertThat(this.accessor.getSourceControlHandlers()).isEmpty();
        return myself;
    }

    public CustodianFactoryAssert hasMetadataAnalyzer(Class<? extends ProjectMetadataAnalyzer> handler) {
        Assertions.assertThat(this.accessor.getMetadataAnalyzers())
                .hasAtLeastOneElementOfType(handler);

        return myself;
    }

    public CustodianFactoryAssert hasNoMetadataAnalyzers() {
        Assertions.assertThat(this.accessor.getMetadataAnalyzers()).isEmpty();
        return myself;
    }

    public CustodianFactoryAssert hasUpdateChannel(Class<? extends UpdateChannel> updateChannel) {
        Assertions.assertThat(this.accessor.getUpdateChannels())
                .hasAtLeastOneElementOfType(updateChannel);

        return myself;
    }

    public CustodianFactoryAssert hasNoUpdateChannels() {
        Assertions.assertThat(this.accessor.getUpdateChannels()).isEmpty();
        return myself;
    }
}
