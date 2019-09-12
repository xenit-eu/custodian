package eu.xenit.custodian.domain.model.buildsystem;

import eu.xenit.custodian.domain.model.build.Build;
import java.util.LinkedHashMap;
import java.util.function.Function;

public class BuildSystemsContainer extends ItemContainer<String, Build> {

    BuildSystemsContainer(Function<String, Build> buildResolver) {
        super(new LinkedHashMap<>(), buildResolver);
    }

    public BuildSystemsContainer() {
        this(s -> null);
    }

    /**
     * Register the specified {@link Build build}.
     * @param build the build to register
     */
    public void add(Build build) {
        add(build.buildsystem().id(), build);
    }

}
