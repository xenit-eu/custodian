package eu.xenit.custodian.domain.service.analysis;

import eu.xenit.custodian.domain.repository.scm.ProjectHandle;
import eu.xenit.custodian.domain.repository.analysis.metadata.ProjectMetadata;
import eu.xenit.custodian.domain.repository.analysis.AnalyzerException;

public interface ProjectAnalysisService {

    ProjectMetadata analyze(ProjectHandle project) throws AnalyzerException;


}
