package eu.xenit.custodian.adapters.buildsystem.gradle.sentinel;

import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto.Report;
import java.io.InputStream;
import java.nio.file.Path;

public interface SentinelReportParser {

    Report parse(InputStream inputStream);

    Report parse(Path path);

    Report parse(String json);
}
