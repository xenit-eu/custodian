package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.ports.spi.build.*;
import eu.xenit.custodian.ports.spi.build.BuildSystem;
import java.nio.file.Path;
import lombok.Getter;

public class GradleBuild implements Build {

    @Getter
    private final GradleProject project;

    @Getter
    private final Path location;

    public GradleBuild(Path location, GradleProject rootProject) {
        this.location = location;
        project = rootProject;
    }

    @Override
    public BuildSystem buildsystem() {
        return new GradleBuildSystem();
    }

    @Override
    public String name() {
        return this.getProject().getName();
    }

    @Override
    public BuildModifier modify() {
        return new GradleBuildModifier(this);
    }
}
