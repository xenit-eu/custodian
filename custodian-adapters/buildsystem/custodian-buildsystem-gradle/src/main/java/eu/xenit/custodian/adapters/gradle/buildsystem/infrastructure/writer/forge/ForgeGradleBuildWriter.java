package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.writer.forge;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProjectContainer;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.writer.forge.io.IndentingWriter;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.writer.forge.io.IndentingWriterFactory;
import eu.xenit.custodian.adapters.gradle.buildsystem.spi.writer.GradleBuildWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ForgeGradleBuildWriter implements GradleBuildWriter {

    private final IndentingWriterFactory indentingWriterFactory;

    public ForgeGradleBuildWriter() {
        this(IndentingWriterFactory.withDefaultSettings());
    }

    public ForgeGradleBuildWriter(IndentingWriterFactory indentingWriterFactory) {
        this.indentingWriterFactory = indentingWriterFactory;
    }

    @Override
    public void writeBuild(GradleBuild build) throws IOException {

        // convert custodian-build-model to initializr-build-model

        // write settings.gradle
        this.writeSettingsGradle(build);

        // traverse project hierarchy
        this.writeProjectRecursively(build.getRootProject());
    }

    void writeSettingsGradle(GradleBuild build) {

    }

    void writeProjectRecursively(GradleProject project) throws IOException {
        // write this build.gradle
        this.writeBuildDotGradle(project);

        // descend down in child projects
        GradleProjectContainer childProjects = project.getChildProjects();
        // TODO recurse ?

//        throw new UnsupportedOperationException("writeProjectRecursively not implemented");
    }

    Path writeBuildDotGradle(GradleProject project) throws IOException {
        Path buildGradlePath = project.getBuildFile();
        this.writeBuild(Files.newBufferedWriter(buildGradlePath), project);

        return buildGradlePath;
    }

    void writeBuild(Writer out, GradleProject project) throws IOException {
        BuildDotGradleWriter buildWriter = BuildDotGradleWriter.createBuildDotGradleWriter(project.getDsl());

        try (IndentingWriter writer = this.indentingWriterFactory.createIndentingWriter("gradle", out)) {
            buildWriter.writeTo(writer, project);
        }
    }
}
