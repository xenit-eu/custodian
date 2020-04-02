package eu.xenit.custodian.domain;


import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.client.test.ChangeSetAssertionTrait;
import eu.xenit.custodian.adapters.client.test.CustodianTestClient;
import eu.xenit.custodian.adapters.client.test.ClonedRepositorySourceMetadataAssertionTrait;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleBuild;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleBuildSystem;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleDependency;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleDependencyMatcher;
import eu.xenit.custodian.domain.buildsystem.GradleBuildAssert;
import eu.xenit.custodian.domain.changes.ChangeApplicationResult;
import eu.xenit.custodian.domain.changes.LogicalChange;
import eu.xenit.custodian.ports.api.ClonedRepositoryHandle;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import eu.xenit.custodian.scaffold.project.gradle.BuildDotGradle;
import java.io.IOException;
import java.util.NoSuchElementException;
import org.junit.Test;

public class CustodianIntegrationTest extends BaseIntegrationTest {


    @Test
    public void basic() throws IOException, MetadataAnalyzerException {
        BuildDotGradle buildDotGradle = this.newGradleBuild()
                .withJavaPlugin()
                .withMavenCentral()
                .withDependencies(dependencies -> {
                    dependencies.add("org.apache.httpcomponents", "httpclient", "4.5.1");
                })
                .build()
                .materialize(this.temporaryFolder.newFolder().toPath());
        buildDotGradle.logBuildGradleContent();

        CustodianTestClient client = this.createClient();
        ClonedRepositoryHandle workingCopy = client.checkout(buildDotGradle.getDirectory());

        ClonedRepositorySourceMetadataAssertionTrait projectMetadata = client.extractMetadata(workingCopy);
        projectMetadata.assertThat()
                .hasBuildSystem(GradleBuildSystem.ID, GradleBuild.class, gradle -> {
                    new GradleBuildAssert(gradle)
                            .getRootProject()
                            .hasDependency("org.apache.httpcomponents:httpclient:4.5.1");
                });

        ChangeSetAssertionTrait changes = client.getChanges(projectMetadata);
        changes.assertThat()
                .isNotNull()
                // only expecting a single maven-dependency-update
                .hasSize(1)
                .hasDependencyUpdate(
                        GradleDependency.class,
                        GradleDependencyMatcher.from("implementation", "org.apache.httpcomponents:httpclient:4.5.1"),
                        "4.5.12");

        LogicalChange dependencyUpdate = changes.stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("No value present"));

        ChangeApplicationResult result = dependencyUpdate.apply();
        assertThat(result.isSuccess()).isTrue();

        // re-running the dependency metadata extraction
        // check the dependency has been updated to 4.5.12
        client.extractMetadata(workingCopy)
                .assertThat()
                .hasBuildSystem(GradleBuildSystem.ID, GradleBuild.class, gradle -> {
                    new GradleBuildAssert(gradle)
                            .getRootProject()
                            .hasDependency("org.apache.httpcomponents:httpclient:4.5.12");
                });
    }
}