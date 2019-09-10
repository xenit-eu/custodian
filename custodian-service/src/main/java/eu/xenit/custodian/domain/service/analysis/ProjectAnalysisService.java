package eu.xenit.custodian.domain.service.analysis;

import eu.xenit.custodian.domain.model.ProjectHandle;
import eu.xenit.custodian.domain.model.metadata.ProjectMetadata;

public interface ProjectAnalysisService {

    ProjectMetadata analyze(ProjectHandle project);


}
