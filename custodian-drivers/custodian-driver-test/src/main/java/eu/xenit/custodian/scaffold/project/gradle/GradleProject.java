package eu.xenit.custodian.scaffold.project.gradle;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildWriter;
import io.spring.initializr.generator.buildsystem.gradle.GroovyDslGradleBuildWriter;
import io.spring.initializr.generator.io.IndentingWriter;
import io.spring.initializr.generator.io.IndentingWriterFactory;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class GradleProject {

    private final IndentingWriterFactory indentingWriterFactory;
    private final GradleBuildWriter buildWriter;
    private final GradleBuild gradleBuild;

    GradleProject(GradleBuild gradleBuild) {
        Objects.requireNonNull(gradleBuild, "gradleBuild can not be null");
        this.gradleBuild = gradleBuild;

        this.indentingWriterFactory = IndentingWriterFactory.withDefaultSettings();
        this.buildWriter = new GroovyDslGradleBuildWriter();
    }

    public BuildDotGradle materialize(Path projectRoot) throws IOException {
        Path buildGradlePath = Files.createFile(projectRoot.resolve("build.gradle"));
        writeBuild(Files.newBufferedWriter(buildGradlePath));
        return new BuildDotGradle(buildGradlePath);
    }

    public void writeBuild(Writer out) throws IOException {
        try (IndentingWriter writer = this.indentingWriterFactory.createIndentingWriter("gradle", out)) {
            this.buildWriter.writeTo(writer, this.gradleBuild);
        }
    }

}
