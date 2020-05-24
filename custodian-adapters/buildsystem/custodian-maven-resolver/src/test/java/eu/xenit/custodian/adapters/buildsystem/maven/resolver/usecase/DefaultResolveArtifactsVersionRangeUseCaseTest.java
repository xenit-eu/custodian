package eu.xenit.custodian.adapters.buildsystem.maven.resolver.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.adapters.aether.MavenResolverAdapter;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.GroupArtifactVersionSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverGroupArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverMavenRepository;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.usecase.ResolveArtifactsVersionRangeUseCase.ResolveArtifactsCommand;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class DefaultResolveArtifactsVersionRangeUseCaseTest {


    private UseCaseFactory useCaseFactory = new UseCaseFactory(new MavenResolverAdapter());

    @Test
    void handle() {
        var useCase = this.useCaseFactory.createResolveArtifactsVersionRangeUseCase();

        var coords = ResolverGroupArtifact.from("org.apache.httpcomponents", "httpclient");
        var versionSpec = ResolverVersionSpecification.from("[4.5.1,4.6.0)");
        ResolveArtifactsCommand command = new ResolveArtifactsCommand(
                coords, versionSpec, Collections.emptySet(), Arrays.asList(ResolverMavenRepository.mavenCentral()));
        var versionRangeQueryResult = useCase.handle(command);

        assertThat(versionRangeQueryResult).isNotNull();
    }
}