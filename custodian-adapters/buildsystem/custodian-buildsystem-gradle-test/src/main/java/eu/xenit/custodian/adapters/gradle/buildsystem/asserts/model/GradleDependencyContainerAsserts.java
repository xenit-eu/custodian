package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleDependencyContainer;
import eu.xenit.custodian.ports.spi.buildsystem.Dependency;
import java.util.Optional;
import java.util.stream.Collectors;
import org.assertj.core.api.AbstractAssert;

//public class GradleDependencyContainerAsserts extends DependencyContainerAssert
public class GradleDependencyContainerAsserts extends AbstractAssert<GradleDependencyContainerAsserts, GradleDependencyContainer>
{

    public GradleDependencyContainerAsserts(GradleDependencyContainer actual) {
        super(actual, GradleDependencyContainerAsserts.class);
    }

    public static GradleDependencyContainerAsserts assertThat(GradleDependencyContainer actual)
    {
        return new GradleDependencyContainerAsserts(actual);
    }

    public GradleDependencyContainerAsserts hasDependency(String notation) {
        isNotNull();
//
        Optional<? extends Dependency> match = this.actual.matches(notation).findAny();
        if (match.isEmpty())
        {
            this.failWithMessage("Dependency <%s> not found in %s", notation,
                    this.actual.stream().map(Dependency::toString).collect(Collectors.toList()));
        }

        return myself;
    }

    public GradleDependencyContainerAsserts hasDependency(String group, String name, String version) {
        return this.hasDependency(group + ":" + name + ":" + version);
    }
}

