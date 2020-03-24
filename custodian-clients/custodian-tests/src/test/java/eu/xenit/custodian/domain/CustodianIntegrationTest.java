package eu.xenit.custodian.domain;


import eu.xenit.custodian.adapters.client.test.ChangeSetAssertionTrait;
import eu.xenit.custodian.adapters.client.test.CustodianTestClient;
import eu.xenit.custodian.adapters.client.test.ClonedRepositorySourceMetadataAssertionTrait;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleBuild;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleBuildSystem;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleDependency;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleDependencyMatcher;
import eu.xenit.custodian.domain.buildsystem.GradleBuildAssert;
import eu.xenit.custodian.domain.changes.LogicalChange;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import eu.xenit.custodian.scaffold.project.gradle.BuildDotGradle;
import java.io.IOException;
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
        ClonedRepositorySourceMetadataAssertionTrait projectMetadata = client.extractMetadata(buildDotGradle.getParent());

        projectMetadata.assertThat()
                .hasBuildSystem(GradleBuildSystem.ID, GradleBuild.class, gradle -> {
                    new GradleBuildAssert(gradle).getRootProject().hasDependency("org.apache.httpcomponents:httpclient:4.5.1");
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

        changes.stream().forEach(LogicalChange::apply);


    }
}