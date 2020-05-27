package eu.xenit.custodian.adapters.gradle.buildsystem;

import java.nio.file.Path;

public interface GradleBuildFactory {

    GradleBuild createGradleBuild(Path location, GradleProject rootProject);

}

class DefaultGradleBuildFactory implements GradleBuildFactory {

    public DefaultGradleBuildFactory() {

    }



    public GradleBuild createGradleBuild(Path location, GradleProject rootProject) {
        return new GradleBuild(location, rootProject);
    }

}
