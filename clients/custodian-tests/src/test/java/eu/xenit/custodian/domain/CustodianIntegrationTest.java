package eu.xenit.custodian.domain;


import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.client.test.CustodianTestClient;
import eu.xenit.custodian.adapters.client.test.ProjectMetadataAssertionTrait;
import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleBuildSystem;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.Test;

public class CustodianIntegrationTest extends BaseIntegrationTest {



    @Test
    public void basic() throws IOException, MetadataAnalyzerException {
        Path buildDotGradle = this.gradleBuild()
                .withJavaPlugin()
                .withMavenCentral()
                .withDependencies(dependencies -> {
                    dependencies.add("org.apache.httpcomponents", "httpclient", "4.5.1");
                })
                .build()
                .materialize(this.temporaryFolder.newFolder().toPath());

        logFileContent(buildDotGradle);

        CustodianTestClient client = this.createClient();

        ProjectMetadataAssertionTrait projectMetadata = client.extractMetadata(buildDotGradle.getParent());
        projectMetadata.assertThat()
                .hasBuildSystem(GradleBuildSystem.ID, gradle -> {
                    gradle.hasDependency("org.apache.httpcomponents:httpclient:4.5.1");
                });

    }
}