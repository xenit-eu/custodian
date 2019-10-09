package eu.xenit.custodian.domain;

import eu.xenit.custodian.adapters.client.test.CustodianTestClient;
import eu.xenit.custodian.ports.api.Custodian;
import eu.xenit.custodian.scaffold.project.gradle.GradleProjectBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseIntegrationTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    protected CustodianTestClient createClient() {
        Custodian custodian = CustodianFactory.withDefaultSettings().create();
        return new CustodianTestClient(custodian);
    }

    protected GradleProjectBuilder gradleBuild() {
        return GradleProjectBuilder.create();
    }

    protected void logFileContent(Path buildDotGradle) throws IOException {
        String content = new String(Files.readAllBytes(buildDotGradle));
//        log().info(buildDotGradle.toString() + System.lineSeparator() + content);
        System.out.println(buildDotGradle.toString() + System.lineSeparator() + content);

    }


    private Logger log() {
        return LoggerFactory.getLogger(this.getClass());
    }
}
