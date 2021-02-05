package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleRepositoryContainer;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.MavenRepository;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.RepositoriesAssert;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class GradleRepositoriesModelAssert
        extends AbstractAssert<GradleRepositoriesModelAssert, GradleRepositoryContainer>
        implements RepositoriesAssert {

    public GradleRepositoriesModelAssert(GradleRepositoryContainer actual) {
        super(actual, GradleRepositoriesModelAssert.class);
    }

    public GradleRepositoriesModelAssert hasRepository(String url) {
        Assertions.assertThat(this.actual.urls()).contains(url);
        return this.myself;
    }

    @Override
    public GradleRepositoriesModelAssert doesNotHaveRepository(String url) {
        Assertions.assertThat(this.actual.urls()).doesNotContain(url);
        return this.myself;
    }

    @Override
    public GradleRepositoriesModelAssert hasMavenCentral() {
        return this.hasRepository(MavenRepository.mavenCentral().getUrl());
    }
}
