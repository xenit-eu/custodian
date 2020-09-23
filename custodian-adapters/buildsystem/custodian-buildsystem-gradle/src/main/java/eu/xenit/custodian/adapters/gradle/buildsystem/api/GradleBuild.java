package eu.xenit.custodian.adapters.gradle.buildsystem.api;

import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuildSystem;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.DefaultGradleBuildModifier;
import eu.xenit.custodian.ports.spi.buildsystem.Build;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystem;
import eu.xenit.custodian.ports.spi.buildsystem.Project;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

public interface GradleBuild extends Build {

    @Override
    GradleProject getRootProject();

    @Override
    GradleBuildSystem buildSystem();

    GradleBuildModifier modify();

//    GradleBuild materialize(Path workingDirectory) throws IOException;
}
