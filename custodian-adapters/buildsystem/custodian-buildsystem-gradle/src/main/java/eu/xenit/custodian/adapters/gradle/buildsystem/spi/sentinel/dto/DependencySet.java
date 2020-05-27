package eu.xenit.custodian.adapters.gradle.buildsystem.spi.sentinel.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class DependencySet extends ArrayList<Dependency> {

    public DependencySet() {

    }

    public DependencySet(Collection<Dependency> dependencies) {
        super(dependencies);
    }

    public DependencySet(Dependency ... dependencies) {
        this(Arrays.asList(dependencies));
    }
//    private final String configuration;
}
