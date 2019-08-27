package eu.xenit.custodian.sentinel;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class SentinelPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        System.out.println("-- applying SentinelPlugin to "+target.getName()+" -> registering task sentinelReport");


        target.getTasks().create("sentinelReport", SentinelReportTask.class);

    }
}
