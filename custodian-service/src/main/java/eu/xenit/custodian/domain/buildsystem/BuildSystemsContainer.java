package eu.xenit.custodian.domain.buildsystem;

import java.util.LinkedHashMap;

public class BuildSystemsContainer extends BuildItemContainer<String, Build> {

    public BuildSystemsContainer() {
        super(new LinkedHashMap<>());
    }

    /**
     * Register the specified {@link Build build}.
     * @param build the build to register
     */
    public void add(Build build) {
        add(build.buildsystem().id(), build);
    }

}
