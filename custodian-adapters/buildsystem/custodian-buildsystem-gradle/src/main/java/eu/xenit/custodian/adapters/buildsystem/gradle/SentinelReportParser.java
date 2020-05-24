package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.adapters.buildsystem.gradle.spi.sentinel.dto.Report;
import java.io.InputStream;
import java.nio.file.Path;

public interface SentinelReportParser {

    Report parse(InputStream inputStream);

    Report parse(Path path);

    Report parse(String json);
}
