package eu.xenit.custodian.adapters.gradle.updates.impl.dependencies;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.MavenResolverStub;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.MavenResolverApi;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.DefaultVersionRangeQueryResult;
import eu.xenit.custodian.adapters.gradle.buildsystem.Fixtures;
import eu.xenit.custodian.adapters.gradle.buildsystem.Fixtures.Dependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.GradleProject;
import eu.xenit.custodian.adapters.gradle.buildsystem.GradleRepositoryContainer;
import org.junit.jupiter.api.Test;

class UpdateMavenDependencyVersionTest {

    @Test
    public void testUpdateMavenDependencyVersion() {
        MavenResolverApi resolver = Fixtures.MavenResolver.minorUpgrade();
        var updater = new UpdateMavenDependencyVersion(resolver);

        GradleProject project = mock(GradleProject.class);
        when(project.getRepositories()).thenReturn(mock(GradleRepositoryContainer.class));

        var proposal = updater.apply(null, project, Dependency.apacheHttpClient("4.5.10"));

        assertThat(proposal).isPresent();
    }

}