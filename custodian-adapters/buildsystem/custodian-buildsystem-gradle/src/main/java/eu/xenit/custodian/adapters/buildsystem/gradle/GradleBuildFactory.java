package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.adapters.buildsystem.gradle.adapters.updates.UpdateGradleWrapper;
import eu.xenit.custodian.adapters.buildsystem.gradle.adapters.updates.dependencies.UpdateDependencies;
import eu.xenit.custodian.adapters.buildsystem.gradle.adapters.updates.dependencies.UpdateMavenDependencyVersion;
import eu.xenit.custodian.adapters.buildsystem.gradle.spi.updates.GradleBuildUpdatePort;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.MavenResolver;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface GradleBuildFactory {

    GradleBuild createGradleBuild(Path location, GradleProject rootProject);

}

class DefaultGradleBuildFactory implements GradleBuildFactory {

    private final Collection<GradleBuildUpdatePort> updatePorts;

    public DefaultGradleBuildFactory(Collection<GradleBuildUpdatePort> updatePorts) {

        this.updatePorts = List.copyOf(updatePorts);
    }

    public DefaultGradleBuildFactory() {
        this(getDefaultGradleBuildUpdatePorts());
    }

    private static Collection<GradleBuildUpdatePort> getDefaultGradleBuildUpdatePorts() {
        return Arrays.asList(
                new UpdateGradleWrapper(),
                new UpdateDependencies(Arrays.asList(
                        new UpdateMavenDependencyVersion(new MavenResolver())
                ))
        );
    }

    public GradleBuild createGradleBuild(Path location, GradleProject rootProject) {
        return new GradleBuild(location, rootProject, updatePorts);
    }

}
