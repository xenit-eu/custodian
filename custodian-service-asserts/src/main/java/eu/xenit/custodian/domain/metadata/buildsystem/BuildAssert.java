package eu.xenit.custodian.domain.metadata.buildsystem;

import java.util.function.Consumer;
import org.assertj.core.api.AbstractAssert;

public class BuildAssert extends AbstractAssert<BuildAssert, Build> {

    public BuildAssert(Build build) {
        super(build, BuildAssert.class);
    }

    public BuildAssert hasDependency(String notation) {
        this.isNotNull();

        return this.withDependencies(dependencies -> {
            dependencies.hasDependency(notation);
        });
    }

    public BuildAssert withDependencies(Consumer<DependencyContainerAssert> callback) {
        this.isNotNull();
        callback.accept(new DependencyContainerAssert(this.actual.dependencies()));
        return this.myself;
    }
}
