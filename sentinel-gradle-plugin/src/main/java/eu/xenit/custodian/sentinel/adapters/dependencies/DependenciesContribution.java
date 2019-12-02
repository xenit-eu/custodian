package eu.xenit.custodian.sentinel.adapters.dependencies;

import eu.xenit.custodian.sentinel.domain.AnalysisContribution;
import eu.xenit.custodian.sentinel.domain.OutputFormat;
import eu.xenit.custodian.sentinel.domain.PartialReporter;

public class DependenciesContribution implements AnalysisContribution<DependenciesContainer> {

    private final DependenciesContainer analysis;

    public DependenciesContribution(DependenciesContainer analysis)  {

        this.analysis = analysis;
    }

    @Override
    public String getName() {
        return DependenciesAnalysisContributor.NAME;
    }

    @Override
    public DependenciesContainer getAnalysis() {
        return this.analysis;
    }

    @Override
    public PartialReporter<DependenciesContainer> getReporter(OutputFormat format) {
        if (format == OutputFormat.JSON) {
            return new JsonDependenciesReporter();
        }

        throw new IllegalArgumentException("Format not supported: " + format);
    }
}
