package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.adapters.buildsystem.gradle.spi.updates.GradleBuildUpdatePort;
import eu.xenit.custodian.ports.spi.buildsystem.*;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystem;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import lombok.Getter;

public class GradleBuild implements Build {

    @Getter
    private final GradleProject rootProject;

    @Getter
    private final Path location;

    private final Collection<GradleBuildUpdatePort> updatePorts;

    GradleBuild(Path location, GradleProject rootProject, Collection<GradleBuildUpdatePort> updatePorts) {
        this.location = location;
        this.rootProject = rootProject;
        this.updatePorts = List.copyOf(updatePorts);
    }

    @Override
    public BuildSystem buildsystem() {
        return new GradleBuildSystem();
    }

    @Override
    public String name() {
        return this.getRootProject().getName();
    }

    @Override
    public BuildModifier modify() {
        return new GradleBuildModifier();
    }

    @Override
    public BuildUpdates updates() {
        return new GradleBuildUpdates(this, updatePorts);
    }
}
