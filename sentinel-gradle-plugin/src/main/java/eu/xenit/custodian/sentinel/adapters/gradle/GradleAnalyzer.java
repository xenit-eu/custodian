package eu.xenit.custodian.sentinel.adapters.gradle;

import eu.xenit.custodian.sentinel.domain.PartialAnalyzer;
import java.io.File;
import java.nio.file.Path;
import org.gradle.api.Project;

public class GradleAnalyzer implements PartialAnalyzer<GradleInfo> {

//    @Override
//    public String getName() {
//        return "gradle";
//    }

    public GradleInfo analyze(Project project) {

        return GradleInfo.builder()
                .version(project.getGradle().getGradleVersion())
                .buildFile(this.relativeToProjectDir(project, project.getBuildFile()))
                .buildDir(this.relativeToProjectDir(project, project.getBuildDir()))
                .build();
    }

    private String relativeToProjectDir(Project project, File path) {
        return project.getProjectDir()
                .toPath()
                .relativize(path.toPath())
                .toString();
    }
}
