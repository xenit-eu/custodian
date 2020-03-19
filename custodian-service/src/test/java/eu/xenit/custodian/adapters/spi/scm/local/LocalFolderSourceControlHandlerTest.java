package eu.xenit.custodian.adapters.spi.scm.local;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.service.scm.local.LocalFolderSourceControlHandler;
import eu.xenit.custodian.adapters.service.scm.local.WorkingCopyStrategy;
import eu.xenit.custodian.asserts.build.project.ProjectReferenceParser;
import eu.xenit.custodian.ports.api.SourceRepositoryHandle;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

public class LocalFolderSourceControlHandlerTest {

    private static String userHome() {
        return System.getProperty("user.home");
    }

    private static Path userHomePath() {
        return Paths.get(userHome());
    }

    @Test
    public void canHandleFolder_withFileScheme() {
        SourceControlHandler handler = new LocalFolderSourceControlHandler();

        assertThat(handler.canHandle(ProjectReferenceParser.from(userHomePath()))).isEqualTo(true);
    }

    @Test
    public void canHandleFolder_withoutFileScheme() {
        SourceControlHandler handler = new LocalFolderSourceControlHandler();

        assertThat(handler.canHandle(ProjectReferenceParser.from(userHome()))).isEqualTo(true);
    }

    @Test
    public void canHandleFolder_currentWorkingDirectory() {
        SourceControlHandler handler = new LocalFolderSourceControlHandler();

        assertThat(handler.canHandle(ProjectReferenceParser.from("."))).isEqualTo(true);
    }

    @Test
    public void checkout_currentWorkingDir_inplace() throws IOException {
        SourceControlHandler handler = new LocalFolderSourceControlHandler(WorkingCopyStrategy.INPLACE);

        SourceRepositoryHandle checkout = handler.checkout(ProjectReferenceParser.from("."));
        assertThat(checkout.getLocation())
                .isDirectory()
                .isEqualByComparingTo(Paths.get(".").toAbsolutePath().normalize());
    }

    @Test
    public void checkout_currentWorkingDir_tempDirectory() throws IOException {
        SourceControlHandler handler = new LocalFolderSourceControlHandler(WorkingCopyStrategy.TEMPDIR);

        SourceRepositoryHandle checkout = handler.checkout(ProjectReferenceParser.from("."));
        assertThat(checkout.getLocation())
                .isDirectory()
                .satisfies(location -> location.startsWith(Paths.get("/tmp/")));
    }
}