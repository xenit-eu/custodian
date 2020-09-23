package eu.xenit.custodian.adapters.gradle.buildsystem;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.MavenResolverStub;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.adapters.stubs.MinorUpdateResolverStub;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.MavenResolverApi;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.ResolverMavenRepository;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.ResolverArtifactSpecification;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleIdentifier;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.DefaultGradleModuleDependency;
import java.util.stream.Stream;

public interface Fixtures {

    interface Modules {

        static GradleModuleIdentifier apacheHttpClient() {
            return GradleModuleIdentifier.from("org.apache.httpcomponents", "httpclient");
        }

        static GradleModuleIdentifier junit() {
            return GradleModuleIdentifier.from("junit", "junit");
        }
    }

    interface Dependency {

        static GradleModuleDependency apacheHttpClient(String version) {
            return DefaultGradleModuleDependency.implementation(Modules.apacheHttpClient(), version);
        }

        static GradleModuleDependency apacheHttpClient() {
            return apacheHttpClient("4.5.10");
        }

        static GradleModuleDependency junit() {
            return DefaultGradleModuleDependency.implementation(Modules.junit(), "4.12");
        }
    }

    interface MavenResolver {

        static MavenResolverApi emptyRepository() {
            return new MavenResolverStub(ResolverMavenRepository.mavenCentral(), Stream.empty());
        }

        static MavenResolverApi minorBumpFixture() {
            return new eu.xenit.custodian.adapters.buildsystem.maven.resolver.MavenResolver(new MinorUpdateResolverStub());
        }

        static MavenResolverApi mavenCentralStub() {
            return new MavenResolverStub(
                    ResolverMavenRepository.mavenCentral(),
                    Stream.of("4.5.0", "4.5.1", "4.5.2", "4.5.3", "4.5.4", "4.6.0", "4.6.1").map(v ->  artifact(Dependency.apacheHttpClient(v)))
            );
        }

        static ResolverArtifactSpecification artifact(GradleModuleDependency dependency) {
            return dependency

                    // First see if there are (optional) artifacts specified
                    .getArtifacts().stream()
                    .map(spec -> ResolverArtifactSpecification.from(
                            spec.getModuleId().getGroup(),
                            spec.getModuleId().getName(),
                            spec.getVersionSpec().getValue(),
                            spec.getClassifier(),
                            spec.getExtension()))
                    .findFirst()

                    // else use module-id + version + default classifier/extension
                    .orElse(ResolverArtifactSpecification.from(
                            dependency.getGroup(),
                            dependency.getId(),
                            dependency.getVersionSpec().getValue()));

        }
    }

}
