package eu.xenit.custodian.adapters.analysis.gradle;

import eu.xenit.custodian.adapters.analysis.gradle.sentinel.Report;
import java.io.InputStream;
import java.nio.file.Path;

public interface SentinelReportParser {

    Report parse(InputStream inputStream);

    Report parse(Path path);

    Report parse(String json);
}
