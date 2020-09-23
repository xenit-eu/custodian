package eu.xenit.custodian.adapters.gradle.buildsystem;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.sentinel.JacksonSentinelReportParser;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.sentinel.SentinelGradleBuildReaderAdapter;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.toolingapi.GradleToolingBuildReaderAdapter;
import eu.xenit.custodian.adapters.gradle.buildsystem.spi.model.GradleBuildReaderPort;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystem;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemException;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemPort;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GradleBuildSystemAdapter implements BuildSystemPort {

    private static final Logger log = LoggerFactory.getLogger(GradleBuildSystemAdapter.class);

    private final GradleBuildReaderPort buildModelPort;

    public GradleBuildSystemAdapter() {
        //this(new SentinelGradleBuildReaderAdapter(new JacksonSentinelReportParser(), GradleRunner::create));
        this(new GradleToolingBuildReaderAdapter());
    }

    public GradleBuildSystemAdapter(GradleBuildReaderPort buildModelPort) {
        Objects.requireNonNull(buildModelPort, "buildModelPort must not be null");
        this.buildModelPort = buildModelPort;
    }

    @Override
    public BuildSystem id() {
        return new GradleBuildSystem();
    }

    @Override
    public Optional<GradleBuild> getBuild(Path location) throws BuildSystemException {
        return this.buildModelPort.getBuild(location);
    }
}
