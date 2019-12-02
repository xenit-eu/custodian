package eu.xenit.custodian.domain.buildsystem;

import java.util.function.Consumer;
import org.assertj.core.api.AbstractAssert;

public class BuildAssert extends AbstractAssert<BuildAssert, Build<Build, Dependency>> {

    public BuildAssert(Build build) {
        super(build, BuildAssert.class);
    }

    public BuildAssert hasDependency(String notation) {
        this.isNotNull();

        return this.withDependencies(dependencies -> {
            dependencies.hasDependency(notation);
        });
    }

    public BuildAssert withDependencies(Consumer<DependencyContainerAssert<Dependency>> callback) {
        this.isNotNull();
        callback.accept(new DependencyContainerAssert<>(this.actual.dependencies()));
        return this.myself;
    }
}
