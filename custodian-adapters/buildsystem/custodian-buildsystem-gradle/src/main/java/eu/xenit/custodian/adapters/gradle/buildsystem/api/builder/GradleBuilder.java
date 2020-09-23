package eu.xenit.custodian.adapters.gradle.buildsystem.api.builder;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleDsl;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface GradleBuilder {

    GradleBuild build();
    GradleBuild materialize(Path projectRoot) throws IOException;

    GradleBuilder dsl(GradleDsl dsl);
    GradleBuilder workingDirectory(Path directory);
    GradleBuilder addProject(Consumer<GradleProjectBuilder> callback);

}
