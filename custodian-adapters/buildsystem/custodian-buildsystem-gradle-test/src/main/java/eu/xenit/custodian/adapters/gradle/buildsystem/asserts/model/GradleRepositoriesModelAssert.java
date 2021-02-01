package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradlePlugin;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradlePluginContainer;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleRepositoryContainer;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.PluginsAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.RepositoriesAssert;
import org.assertj.core.api.AbstractAssert;

import java.util.Objects;
import java.util.stream.Collectors;

public class GradleRepositoriesModelAssert
        extends AbstractAssert<GradleRepositoriesModelAssert, GradleRepositoryContainer>
        implements RepositoriesAssert {

    public GradleRepositoriesModelAssert(GradleRepositoryContainer actual) {
        super(actual, GradleRepositoriesModelAssert.class);
    }

    public GradleRepositoriesModelAssert hasRepositoryUrl(String url) {
        this.actual.has()
        return this.myself;
    }


    @Override
    public PluginsAssert doesNotHavePlugin(String plugin) {
        return null;
    }

    @Override
    public RepositoriesAssert hasMavenCentral() {
        return null;
    }
}
