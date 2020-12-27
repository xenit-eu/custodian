package eu.xenit.custodian.adapters.gradle.updates.usecases;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.gradle.buildsystem.Fixtures.MavenResolver;
import eu.xenit.custodian.adapters.gradle.buildsystem.Fixtures.Modules;
import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuildSystem;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model.GradleBuildAssert;
import eu.xenit.custodian.adapters.gradle.updates.impl.dependencies.DependencyVersionUpdateProposal;
import eu.xenit.custodian.adapters.gradle.updates.impl.dependencies.UpdateGradleDependencies;
import eu.xenit.custodian.adapters.gradle.updates.impl.dependencies.UpdateMavenDependencyVersion;
import eu.xenit.custodian.adapters.gradle.updates.usecases.GetGradleBuildUpdateProposalsUseCase.GetGradleBuildUpdateProposalsCommand;
import eu.xenit.custodian.adapters.gradle.updates.usecases.ports.GradleBuildUpdateProposal;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GetGradleBuildUpdateProposalsUseCaseImplTest {

    @Test
    public void test(@TempDir Path tempFolder) throws IOException {
        var gradleBuildUpdateProposals = new GetGradleBuildUpdateProposalsUseCaseImpl(
                new UpdateGradleDependencies(
                        new UpdateMavenDependencyVersion(MavenResolver.minorBumpFixture())
                )
        );

//        BuildDotGradle buildDotGradle = GradleProjectBuilder.create()
//                .withJavaPlugin()
//                .withMavenCentral()
//                .withDependencies(dependencies -> {
//                    dependencies.add("org.apache.httpcomponents", "httpclient", "4.5.1");
//                })
//                .build()
//                .materialize(tempFolder);
//        buildDotGradle.logBuildScriptContent();

        GradleBuild build = new GradleBuildSystem().builder()
                .addProject(project -> project
                        .name("root")
                        .withDependencies(dependencies -> {
                            dependencies.implementation(Modules.apacheHttpClient(), "4.5.6");
                        })
                )
                .materialize(tempFolder);

        GradleBuildAssert.assertThat(build)
                .assertRootProject(project -> {
                    project.hasDependency("org.apache.httpcomponents:httpclient:4.5.6");
                });


        var command = new GetGradleBuildUpdateProposalsCommand(build);
        var result = gradleBuildUpdateProposals.handle(command);

        assertThat(result).isNotNull();
        assertThat(result.getUpdates()).hasSize(1);
        GradleBuildUpdateProposal proposal = result.getUpdates().stream().findFirst().orElseThrow();

        assertThat(proposal).isInstanceOf(DependencyVersionUpdateProposal.class);

        var versionUpdate = (DependencyVersionUpdateProposal) proposal;
        var dependency = versionUpdate.getDependency();

        assertThat(dependency.getGroup()).isEqualTo("org.apache.httpcomponents");
        assertThat(dependency.getName()).isEqualTo("httpclient");
        assertThat(dependency.getVersionSpec().getValue()).isEqualTo("4.5.6");
        assertThat(versionUpdate.getDescription()).isEqualTo("Update httpclient from 4.5.6 to 4.6.0");

        var modification = versionUpdate.apply();
        assertThat(modification.isEmpty()).isFalse();
    }

}