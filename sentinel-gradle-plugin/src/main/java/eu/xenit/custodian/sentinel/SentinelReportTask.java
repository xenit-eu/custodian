package eu.xenit.custodian.sentinel;

import eu.xenit.custodian.sentinel.adapters.dependencies.DependenciesAnalysisContributor;
import eu.xenit.custodian.sentinel.adapters.gradle.GradleAnalysisContributor;
import eu.xenit.custodian.sentinel.adapters.project.ProjectInfoAnalysisContributor;
import eu.xenit.custodian.sentinel.adapters.repositories.RepositoriesAnalysisContributor;
import eu.xenit.custodian.sentinel.domain.AnalysisContentPart;
import eu.xenit.custodian.sentinel.domain.Sentinel;
import eu.xenit.custodian.sentinel.domain.SentinelAnalysisContributor;
import eu.xenit.custodian.sentinel.domain.SentinelAnalysisReport;
import eu.xenit.custodian.sentinel.reporting.SentinelJsonReporter;
import eu.xenit.custodian.sentinel.reporting.SentinelReporter;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

public class SentinelReportTask extends DefaultTask {

    public static final String TASK_NAME = "sentinelReport";

    public SentinelReportTask() {
        this.setDescription("Create a structured report on project metadata and dependencies");
        this.setGroup("Help");

        this.getOutputs().upToDateWhen(element -> false);
    }


    private File output = this.getProject().file(this.getProject().getBuildDir().getPath() + "/sentinel/report.json");

    @OutputFile
    public File getOutput() {
        return this.output;
    }

    public SentinelReportTask setOutput(Object output) {
        this.output = this.getProject().file(output);
        return this;
    }

//    @InputFile
//    public File getInput() {
//        return this.getProject().getBuildFile();
//    }

    @TaskAction
    void report() throws IOException {

        SentinelAnalysisReport result = new Sentinel(Arrays.asList(
                new GradleAnalysisContributor(),
                new ProjectInfoAnalysisContributor(),
                new RepositoriesAnalysisContributor(),
                new DependenciesAnalysisContributor()
        )).analyze(this.getProject());

        SentinelReporter reporter = new SentinelJsonReporter();

        try (IndentingWriter writer = new IndentingWriter(new BufferedWriter(new FileWriter(getOutput())))) {
            reporter.write(writer, result);
        }
    }

}
