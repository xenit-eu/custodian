package eu.xenit.custodian.sentinel;

import eu.xenit.custodian.sentinel.domain.Sentinel;
import eu.xenit.custodian.sentinel.domain.SentinelAnalysisReport;
import eu.xenit.custodian.sentinel.reporting.SentinelJsonReporter;
import eu.xenit.custodian.sentinel.reporting.SentinelReporter;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

public class SentinelReportTask extends DefaultTask {

    public static final String TASK_NAME = "sentinelReport";

    public SentinelReportTask() {
        this.setDescription("Create a structured report on project metadata and dependencies");
        this.setGroup("Help");

        // this.getOutputs().upToDateWhen(element -> false);
    }


    private File output = this.getProject().file(this.getProject().getBuildDir().getPath() + "/sentinel/report.json");

    @OutputFile
    public File getOutput() {
        return this.output;
    }

    public SentinelReportTask setOutput(File output) {
        this.output = output;
        return this;
    }

    @TaskAction
    void report() throws IOException {
        SentinelAnalysisReport result = new Sentinel().analyze(this.getProject());

        SentinelReporter reporter = new SentinelJsonReporter();
//        SentinelReporter reporter = new SentinelJacksonReporter();

//        Writer out = new StringWriter();
        try (IndentingWriter writer = new IndentingWriter(new BufferedWriter(new FileWriter(getOutput())))) {
            reporter.write(writer, result);
        }

//        System.out.println(out.toString());
    }

}
