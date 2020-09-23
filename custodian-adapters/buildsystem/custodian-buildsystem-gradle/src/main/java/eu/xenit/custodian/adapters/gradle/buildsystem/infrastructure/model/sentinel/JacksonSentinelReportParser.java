package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.sentinel;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.sentinel.dto.Report;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JacksonSentinelReportParser implements SentinelReportParser {

    private ObjectMapper objectMapper = new ObjectMapper()
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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
