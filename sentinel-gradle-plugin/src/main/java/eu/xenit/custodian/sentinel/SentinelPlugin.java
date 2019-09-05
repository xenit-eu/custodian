package eu.xenit.custodian.sentinel;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class SentinelPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {

        target.getTasks().create(SentinelReportTask.TASK_NAME, SentinelReportTask.class);

    }
}
