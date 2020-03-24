package eu.xenit.custodian.domain;

import eu.xenit.custodian.ports.spi.channel.UpdateChannel;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzer;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import java.util.Set;

public class CustodianFactoryAccessor {

    private final CustodianFactory actual;

    public CustodianFactoryAccessor(CustodianFactory actual) {
        this.actual = actual;
    }

    public Set<SourceControlHandler> getSourceControlHandlers() {
        return this.actual.getSourceControlHandlers();
    }

    public Set<ProjectMetadataAnalyzer> getMetadataAnalyzers() {
        return this.actual.getMetadataAnalyzers();
    }

    public Set<UpdateChannel> getUpdateChannels() {
        return this.actual.getUpdateChannels();
    }
}
