package eu.xenit.custodian.domain.buildsystem;

import eu.xenit.custodian.domain.entities.buildsystem.BuildSystemsCollection;
import eu.xenit.custodian.ports.spi.buildsystem.Build;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

public class BuildSystemsContainer extends BuildItemContainer<String, Build> implements BuildSystemsCollection {

    public BuildSystemsContainer() {
        super(new LinkedHashMap<>());
    }

    /**
     * Register the specified {@link Build build}.
     * @param build the build to register
     */
    public void add(Build build) {
        add(build.buildSystem().id(), build);
    }

    @Override
    public Stream<Build> builds() {
        return this.items();
    }
}
