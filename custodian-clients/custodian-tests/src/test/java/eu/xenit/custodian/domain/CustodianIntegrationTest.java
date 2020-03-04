package eu.xenit.custodian.domain;


import eu.xenit.custodian.adapters.client.test.ChangeSetAssertionTrait;
import eu.xenit.custodian.adapters.client.test.CustodianTestClient;
import eu.xenit.custodian.adapters.client.test.ProjectMetadataAssertionTrait;
import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleBuildSystem;
import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleDependency;
import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleDependencyMatcher;
import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleModuleDependency;
import eu.xenit.custodian.domain.changes.LogicalChangeSet;
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
        ProjectMetadataAssertionTrait projectMetadata = client.extractMetadata(buildDotGradle.getParent());

        projectMetadata.assertThat()
                .hasBuildSystem(GradleBuildSystem.ID, gradle -> {
                    gradle.hasDependency("org.apache.httpcomponents:httpclient:4.5.1");
                });

        ChangeSetAssertionTrait changes = client.getChanges(projectMetadata);
        changes.assertThat()
                .isNotNull()
                .hasDependencyUpdate(
                        GradleDependency.class,
                        GradleDependencyMatcher.from("implementation", "org.apache.httpcomponents:httpclient:4.5.1"),
                        "4.5.11");

    }
}