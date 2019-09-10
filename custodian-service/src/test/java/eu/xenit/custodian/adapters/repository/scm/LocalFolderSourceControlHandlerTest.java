package eu.xenit.custodian.adapters.repository.scm;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.repository.scm.local.LocalFolderSourceControlHandler;
import eu.xenit.custodian.adapters.repository.scm.local.WorkingCopyStrategy;
import eu.xenit.custodian.domain.model.ProjectHandle;
import eu.xenit.custodian.domain.model.ProjectReference;
import eu.xenit.custodian.domain.repository.scm.SourceControlHandler;
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

        assertThat(handler.canHandle(ProjectReference.from(userHomePath()))).isEqualTo(true);
    }

    @Test
    public void canHandleFolder_withoutFileScheme() {
        SourceControlHandler handler = new LocalFolderSourceControlHandler();

        assertThat(handler.canHandle(ProjectReference.from(userHome()))).isEqualTo(true);
    }

    @Test
    public void canHandleFolder_currentWorkingDirectory() {
        SourceControlHandler handler = new LocalFolderSourceControlHandler();

        assertThat(handler.canHandle(ProjectReference.from("."))).isEqualTo(true);
    }

    @Test
    public void checkout_currentWorkingDir_inplace() throws IOException {
        SourceControlHandler handler = new LocalFolderSourceControlHandler(WorkingCopyStrategy.INPLACE);

        ProjectHandle checkout = handler.checkout(ProjectReference.from("."));
        assertThat(checkout.getLocation())
                .isDirectory()
                .isEqualByComparingTo(Paths.get(".").toAbsolutePath().normalize());
    }

    @Test
    public void checkout_currentWorkingDir_tempDirectory() throws IOException {
        SourceControlHandler handler = new LocalFolderSourceControlHandler(WorkingCopyStrategy.TEMPDIR);

        ProjectHandle checkout = handler.checkout(ProjectReference.from("."));
        assertThat(checkout.getLocation())
                .isDirectory()
                .satisfies(location -> location.startsWith(Paths.get("/tmp/")));
    }
}