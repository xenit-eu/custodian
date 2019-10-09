package eu.xenit.custodian.domain.metadata.buildsystem;

import java.util.LinkedHashMap;
import java.util.function.Function;

public class BuildSystemsContainer extends BuildItemContainer<String, Build> {

    public BuildSystemsContainer(Function<String, Build> buildResolver) {
        super(new LinkedHashMap<>());
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
