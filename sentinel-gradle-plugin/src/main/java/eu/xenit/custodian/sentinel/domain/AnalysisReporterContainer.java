package eu.xenit.custodian.sentinel.domain;

import java.util.LinkedHashMap;
import java.util.function.Function;

public class AnalysisReporterContainer extends ItemContainer<String, PartialReporter> {

    public AnalysisReporterContainer(Function<String, PartialReporter> func) {
        super(new LinkedHashMap<>(), func);
    }
}
