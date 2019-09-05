package eu.xenit.custodian.sentinel.asserts;

import java.io.File;

public class SentinelReportAssert extends JsonNodeAssert {

    public SentinelReportAssert(File file) {
        super(file);
    }

    public SentinelReportAssert(String content) {
        super(content);
    }

}
