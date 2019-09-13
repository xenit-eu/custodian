package eu.xenit.custodian.sentinel.adapters.gradle;

import eu.xenit.custodian.sentinel.domain.PartialAnalyzer;
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
                .buildFile(project.getBuildFile().getName())
                .buildDir(this.getRelativeBuildDir(project).toString())
                .build();
    }

    private Path getRelativeBuildDir(Project project) {
        return project.getProjectDir().toPath().relativize(project.getBuildDir().toPath());
    }
}
