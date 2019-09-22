package eu.xenit.custodian.adapters.analysis.gradle;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.xenit.custodian.adapters.analysis.gradle.sentinel.Report;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JacksonSentinelReportParser implements SentinelReportParser {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Report parse(InputStream inputStream) {
        try {
            return this.objectMapper.readValue(inputStream, Report.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Report parse(Path path) {
        try {
            String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            return this.parse(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Report parse(String json) {
        try {
            return this.objectMapper.readValue(json, Report.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
