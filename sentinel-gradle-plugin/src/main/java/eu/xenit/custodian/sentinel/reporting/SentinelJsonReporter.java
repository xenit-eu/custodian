package eu.xenit.custodian.sentinel.reporting;

import eu.xenit.custodian.sentinel.domain.AnalysisContentPart;
import eu.xenit.custodian.sentinel.domain.AnalysisContribution;
import eu.xenit.custodian.sentinel.domain.OutputFormat;
import eu.xenit.custodian.sentinel.domain.SentinelAnalysisReport;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;
import eu.xenit.custodian.sentinel.reporting.io.JsonWriter;

public class SentinelJsonReporter implements SentinelReporter {

    public static final OutputFormat JSON = OutputFormat.JSON;

    @Override
    public void write(IndentingWriter writer, SentinelAnalysisReport result) {
        JsonWriter json = new JsonWriter(writer);
        json.object(
                result.ids().map(id -> {
                    AnalysisContribution<? extends AnalysisContentPart> contribution = result.get(id);
                    return json.property(id, contribution.report(writer, JSON));
                })
        ).run();
    }
}
