package eu.xenit.custodian.adapters.gradle.buildsystem.impl.builder;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuildModifier;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleDsl;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradleBuilder;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradleProjectBuilder;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.DefaultGradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.DefaultGradleBuildModifier;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.DefaultGradleProject;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.writer.forge.ForgeGradleBuildWriter;
import eu.xenit.custodian.adapters.gradle.buildsystem.spi.writer.GradleBuildWriter;
import eu.xenit.custodian.util.Arguments;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class DefaultGradleBuilder implements GradleBuilder {

    private final GradleProjectTree projectTree = new GradleProjectTree();
    private final BuildRootDirectory rootDirectory = new BuildRootDirectory();

    private GradleDsl dsl = GradleDsl.GROOVY;
    private GradleBuildModifier modifier = new DefaultGradleBuildModifier();
    private GradleBuildWriter writer = new ForgeGradleBuildWriter();

//    /**
//     * The default workingDirectory is a fake in-memory filesystem.
//     *
//     * That is because Gradle model we are building here, does not have to be materialized at all.
//     */
//    private Path workingDirectory = Jimfs.newFileSystem(Configuration.unix()).getPath("/");



    @Override
    public GradleBuild build() {
        return new DefaultGradleBuild(this);
    }

    @Override
    public GradleBuild materialize(Path projectRoot) throws IOException {
        GradleBuild build = this.workingDirectory(projectRoot).build();
        this.writer.writeBuild(build);

        return build;
    }

    @Override
    public GradleBuilder addProject(Consumer<GradleProjectBuilder> callback) {
        Arguments.notNull(callback, "callback");

        DefaultGradleProjectBuilder projectBuilder = new DefaultGradleProjectBuilder(this.projectTree, this.rootDirectory, this.dsl);
        callback.accept(projectBuilder);

        DefaultGradleProject project = projectBuilder.build();
        this.projectTree.addProject(project);

        return this;
    }

    public GradleBuilder workingDirectory(Path directory) {
        this.rootDirectory.setDirectory(directory);
        return this;
    }

}