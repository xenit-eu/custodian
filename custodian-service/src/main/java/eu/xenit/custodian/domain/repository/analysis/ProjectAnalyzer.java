package eu.xenit.custodian.domain.repository.analysis;

import eu.xenit.custodian.domain.model.ProjectHandle;
import eu.xenit.custodian.domain.model.metadata.ProjectMetadata;

public interface ProjectAnalyzer {

    ProjectMetadata analyze(ProjectHandle handle) throws AnalyzerException;
}
