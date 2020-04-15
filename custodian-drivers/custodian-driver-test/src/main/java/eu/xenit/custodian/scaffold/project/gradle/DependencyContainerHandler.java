package eu.xenit.custodian.scaffold.project.gradle;

import io.spring.initializr.generator.buildsystem.DependencyContainer;
import io.spring.initializr.generator.buildsystem.gradle.GradleDependency;
import io.spring.initializr.generator.buildsystem.gradle.GradleDependency.Builder;
import io.spring.initializr.generator.version.VersionReference;
import java.util.function.Consumer;

public class DependencyContainerHandler {

    private final DependencyContainer container;

    DependencyContainerHandler(DependencyContainer container) {
        this.container = container;
    }

    public DependencyContainerHandler add(String group, String artifact) {
        return this.add(group, artifact, dependency -> {
        });
    }

    public DependencyContainerHandler add(String group, String artifact, String version) {
        return this.add(group, artifact, dependency -> {
            dependency.version(VersionReference.ofValue(version));
        });
    }

    public DependencyContainerHandler add(String group, String artifact, Consumer<GradleDependency.Builder> callback) {
        Builder builder = GradleDependency.withCoordinates(group, artifact);
        callback.accept(builder);

        this.container.add(builder.toString(), builder);
        return this;
    }


}
