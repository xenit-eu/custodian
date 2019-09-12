package eu.xenit.custodian.sentinel.reporting;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.xenit.custodian.sentinel.analyzer.SentinelAnalysisResult;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;
import java.io.IOException;


public class SentinelJacksonReporter implements SentinelReporter {

    private final ObjectMapper mapper;

    public SentinelJacksonReporter() {
        this.mapper = new ObjectMapper()
                // pretty print
                .enable(SerializationFeature.INDENT_OUTPUT)

                // empty objects are ok
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)

                // skip fields with have 'null' value
                .setSerializationInclusion(Include.NON_NULL);
    }


    @Override
    public void write(IndentingWriter writer, SentinelAnalysisResult result) throws IOException {

        mapper.writeValue(writer, result);
    }

}
