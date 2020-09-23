package eu.xenit.custodian.adapters.gradle.buildsystem.spi.writer;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import java.io.IOException;

public interface GradleBuildWriter {
    void writeBuild(GradleBuild build) throws IOException;
}
