package eu.xenit.custodian.domain.usecases.analysis.ports;

import java.nio.file.Path;

public interface ProjectAnalyzerPort {

    ProjectModel analyze(Path path);

}
