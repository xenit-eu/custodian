package eu.xenit.custodian.adapters.repository.analysis;

import eu.xenit.custodian.domain.repository.analysis.ProjectAnalyzer;
import java.nio.file.Paths;
import org.junit.Test;

public class SentinelGradlePojectAnalyzerTest {

    @Test
    public void analyze() {
        ProjectAnalyzer sentinel = new SentinelGradlePojectAnalyzer();
        sentinel.analyze(Paths.get("."));

    }
}