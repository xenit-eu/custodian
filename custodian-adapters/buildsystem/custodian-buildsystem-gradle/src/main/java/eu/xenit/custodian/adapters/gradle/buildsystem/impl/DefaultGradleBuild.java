package eu.xenit.custodian.adapters.gradle.buildsystem.impl;

import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuildSystem;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuildModifier;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleDsl;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.builder.DefaultGradleBuilder;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.builder.GradleProjectTree;
import eu.xenit.custodian.adapters.gradle.buildsystem.spi.writer.GradleBuildWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.Getter;
import lombok.experimental.Accessors;

public class DefaultGradleBuild implements GradleBuild {

    private ProjectResolver projects;

    /**
     * Working directory of the {@code GradleBuild} model. Can be null.
     */
//    @Getter
//    private Path workingDirectory;

    private final GradleBuildModifier modifier;
    private final GradleBuildWriter writer;

    @Override
    public GradleProject getRootProject() {
        return this.projects.getRootProject();
    }

    @Getter
    @Accessors(fluent = true)
    public GradleBuildSystem buildSystem;


    public DefaultGradleBuild(DefaultGradleBuilder builder) {

        this.projects = builder.projectTree();
        this.buildSystem = new GradleBuildSystem(builder.dsl().toString());

//        this.workingDirectory = builder.workingDirectory();
        this.modifier = builder.modifier();
        this.writer = builder.writer();
    }

    public GradleBuildModifier modify() {
        return this.modifier;
    }


//    public Path resolve(Path path) {
//        return this.workingDirectory.resolve(path);
//    }
//
//    public Path resolve(String path) {
//        return this.workingDirectory.resolve(path);
//    }
}
