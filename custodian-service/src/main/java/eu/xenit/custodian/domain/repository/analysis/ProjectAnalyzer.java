package eu.xenit.custodian.domain.repository.analysis;

import eu.xenit.custodian.domain.repository.scm.ProjectHandle;
import eu.xenit.custodian.domain.repository.analysis.metadata.ProjectMetadata;

public interface ProjectAnalyzer {

    ProjectMetadata analyze(ProjectHandle handle) throws AnalyzerException;
}
