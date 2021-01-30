package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.toolingapi;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.spi.model.GradleBuildReaderPort;
import eu.xenit.custodian.gradle.sentinel.model.GradleBuildModel;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemException;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.gradle.tooling.GradleConnectionException;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ModelBuilder;
import org.gradle.tooling.ProjectConnection;

@Slf4j
public class GradleToolingBuildReaderAdapter implements GradleBuildReaderPort {

    @Override
    public Optional<GradleBuild> getBuild(Path location) throws BuildSystemException {
        try {
            return Optional.ofNullable(this.connect(location));
        } catch (IOException ioe) {
            throw new BuildSystemException(ioe);
        }
    }

    /**
     * @param location The working directory.
     * @throws GradleConnectionException On failure to establish a connection with the target Gradle version.
     * @throws IOException On failure to copy the init.gradle script into a temp location.
     */
    public GradleBuild connect(Path location) throws GradleConnectionException, IOException {

        log.warn("GradleConnector forProjectDirectory: "+location.toFile().getAbsoluteFile());
        GradleConnector connector = GradleConnector.newConnector()
                .forProjectDirectory(location.toFile().getAbsoluteFile());

        Path initScriptPath = getInitScriptPath();

        log.warn("initscript path: "+initScriptPath);

        try (ProjectConnection connection = connector.connect();
                Closeable ignored = () -> Files.deleteIfExists(initScriptPath)) {

            ByteArrayOutputStream stdoutBuffer = new ByteArrayOutputStream();
            ModelBuilder<GradleBuildModel> customModelBuilder = connection.model(GradleBuildModel.class);
            customModelBuilder.withArguments("--init-script", initScriptPath.toString());
            customModelBuilder.setStandardOutput(stdoutBuffer);
//            customModelBuilder.addProgressListener(
//                    (ProgressListener) event -> System.out.println("CustomModel -> status changed: " + event.getDescription()));

            GradleBuildModel model = customModelBuilder.get();

            log.warn("TOOLING API:");
            log.warn(new String(stdoutBuffer.toByteArray()));

            return GradleToolingApiModelConverter.convert(model);
        }
    }

    Path getInitScriptPath() throws IOException {
        Path initScriptPath = Files.createTempFile("sentinel-", ".gradle");
        try (var stream = this.getClass().getClassLoader().getResourceAsStream("sentinel/init.gradle")) {
            Objects.requireNonNull(stream, "init.gradle not found");
            Files.copy(stream, initScriptPath, StandardCopyOption.REPLACE_EXISTING);
        }
        return initScriptPath;
    }

}
