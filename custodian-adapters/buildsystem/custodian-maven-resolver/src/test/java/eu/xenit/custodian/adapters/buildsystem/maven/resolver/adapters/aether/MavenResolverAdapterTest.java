package eu.xenit.custodian.adapters.buildsystem.maven.resolver.adapters.aether;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.adapters.stub.MavenRepositoryStub;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifactVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverMavenRepository;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.VersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.ResolverArtifactSpecification;
import java.util.Collections;
import java.util.stream.Stream;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.resolution.VersionResolutionException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

class MavenResolverAdapterTest {

    /**
     * This actually calls maven-central for live data. This means the tests is not suitable
     * for automated unit-tests, only for a developer to manually run if functionality needs to be
     * verified.
     */
    @Test
    @Disabled("Performs actual lookup in maven central")
    public void realMavenCentralIntegrationTest() {
        var mavenResolver = new MavenResolverAdapter();

        VersionRangeQueryResult rangeQueryResult = mavenResolver.resolveVersionRange(
                Collections.singletonList(ResolverMavenRepository.mavenCentral()),
                ResolverArtifactSpecification.from("org.apache.httpcomponents", "httpclient", "[4.5.1,4.5.max]"));

        // Note: this is a REAL integration test, which means that if the test fails,
        // a new version has probably been released and you need to bump this version ---vvvv---
        assertThat(rangeQueryResult.getHighestVersion()).hasValue(ResolverArtifactVersion.from("4.5.12"));
    }

    @Test
    public void testRepositorySystemInvocations() throws VersionRangeResolutionException, VersionResolutionException {
        RepositorySystem repositorySystem = mock(RepositorySystem.class);
        MavenResolverAdapter mavenResolverAdapter = new MavenResolverAdapter(repositorySystem);

        // set up mock with asserts
        when(repositorySystem.resolveVersionRange(any(), any()))
                .thenAnswer((Answer<VersionRangeResult>) invocation -> {
                    VersionRangeRequest versionRangeRequest = invocation.getArgument(1, VersionRangeRequest.class);
                    return new VersionRangeResult(versionRangeRequest);
                });

        mavenResolverAdapter.resolveVersionRange(
                Collections.singletonList(ResolverMavenRepository.mavenCentral()),
                ResolverArtifactSpecification.from("org.apache.httpcomponents", "httpclient", "[4.5.1,4.6.0)"));

        verify(repositorySystem).resolveVersionRange(any(), any());
    }

    @Test
    public void testStub() {
        var apacheClient = ResolverArtifactSpecification.from("org.apache.httpcomponents", "httpclient", "4.5.0");
        MavenRepositoryStub repoStub = new MavenRepositoryStub(ResolverMavenRepository.mavenCentral(),
                Stream.of("4.5.0", "4.5.1", "4.5.2", "4.5.3", "4.5.4", "4.6.0", "4.6.1")
                        .map(v -> apacheClient.customize(c -> c.setVersion(v)))
        );

        RepositorySystemStub repositorySystem = new RepositorySystemStub(repoStub);
        MavenResolverAdapter mavenResolverAdapter = new MavenResolverAdapter(repositorySystem);
        VersionRangeQueryResult result = mavenResolverAdapter.resolveVersionRange(
                Collections.singletonList(ResolverMavenRepository.mavenCentral()),
                ResolverArtifactSpecification.from("org.apache.httpcomponents", "httpclient", "[4.5.2,4.6.0)"));

        assertThat(result).isNotNull();
        assertThat(result.versions().map(ResolverArtifactVersion::getValue)).containsSequence("4.5.2", "4.5.3", "4.5.4");
        assertThat(result.getHighestVersion()).hasValue(ResolverArtifactVersion.from("4.5.4"));


    }

}