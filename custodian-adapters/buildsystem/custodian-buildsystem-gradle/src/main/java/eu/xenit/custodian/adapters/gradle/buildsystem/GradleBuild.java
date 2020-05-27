package eu.xenit.custodian.adapters.gradle.buildsystem;

import eu.xenit.custodian.ports.spi.buildsystem.Build;
import eu.xenit.custodian.ports.spi.buildsystem.BuildModifier;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystem;
import java.nio.file.Path;
import lombok.Getter;

public class GradleBuild implements Build {

    @Getter
    private final GradleProject rootProject;

    @Getter
    private final Path location;

    GradleBuild(Path location, GradleProject rootProject) {
        this.location = location;
        this.rootProject = rootProject;
    }

    @Override
    public BuildSystem buildsystem() {
        return new GradleBuildSystem();
    }
    
    public BuildModifier modify() {
        return new GradleBuildModifier();
    }

}
