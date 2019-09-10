package eu.xenit.custodian.domain.repository.analysis;

import eu.xenit.custodian.domain.model.metadata.ProjectMetadata;
import java.nio.file.Path;

public interface ProjectAnalyzer {

    ProjectMetadata analyze(Path location);
}
