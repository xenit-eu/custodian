package eu.xenit.custodian.application;

import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemPort;
import eu.xenit.custodian.ports.spi.updates.UpdatePort;
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

    public CustodianFactoryAssert hasBuildSystem(Class<? extends BuildSystemPort> buildSystem) {
        Assertions.assertThat(this.actual.getBuildSystems())
                .hasAtLeastOneElementOfType(buildSystem);

        return myself;
    }

    public CustodianFactoryAssert hasNoBuildSystemProjectModelAnalyzer() {
        Assertions.assertThat(this.actual.getBuildSystems()).isEmpty();
        return myself;
    }

    public CustodianFactoryAssert hasUpdateChannel(Class<? extends UpdatePort> updateChannel) {
        Assertions.assertThat(this.actual.getUpdateChannels())
                .hasAtLeastOneElementOfType(updateChannel);

        return myself;
    }

    public CustodianFactoryAssert hasNoUpdateChannels() {
        Assertions.assertThat(this.actual.getUpdateChannels()).isEmpty();
        return myself;
    }
}
