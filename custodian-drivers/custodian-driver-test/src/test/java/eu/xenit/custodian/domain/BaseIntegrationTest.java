package eu.xenit.custodian.domain;

import eu.xenit.custodian.driver.test.CustodianTestClient;
import eu.xenit.custodian.ports.api.Custodian;
import eu.xenit.custodian.scaffold.project.gradle.GradleProjectBuilder;
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

    protected GradleProjectBuilder newGradleBuild() {
        return GradleProjectBuilder.create();
    }




    private Logger log() {
        return LoggerFactory.getLogger(this.getClass());
    }
}
