package eu.xenit.custodian.adapters.gradle.buildsystem.asserts;

import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model.GradleRepositoriesModelAssert;

public interface RepositoriesAssert {

    RepositoriesAssert hasMavenCentral();

    RepositoriesAssert hasRepository(String url);
    RepositoriesAssert doesNotHaveRepository(String url);


}
