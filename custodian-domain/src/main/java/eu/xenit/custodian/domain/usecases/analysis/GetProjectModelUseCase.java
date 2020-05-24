package eu.xenit.custodian.domain.usecases.analysis;

import eu.xenit.custodian.domain.buildsystem.BuildSystemsContainer;
import eu.xenit.custodian.domain.entities.buildsystem.BuildSystemsCollection;
import eu.xenit.custodian.domain.usecases.UseCase;
import eu.xenit.custodian.domain.usecases.analysis.GetProjectModelUseCase.GetProjectModelCommand;
import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import eu.xenit.custodian.domain.usecases.buildsystem.GetBuildSystemsUseCase;
import eu.xenit.custodian.domain.usecases.buildsystem.GetBuildSystemsUseCase.GetBuildSystemsCommand;
import eu.xenit.custodian.ports.spi.buildsystem.Build;
import java.nio.file.Path;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

public interface GetProjectModelUseCase extends
        UseCase<GetProjectModelCommand, ProjectModel> {


    ProjectModel handle(GetProjectModelCommand command);

    @Data
    class GetProjectModelCommand {

        private final Path location;
    }
}

@Slf4j
class DefaultGetProjectModelUseCase implements GetProjectModelUseCase {


    private final GetBuildSystemsUseCase buildSystemUseCase;

    public DefaultGetProjectModelUseCase(GetBuildSystemsUseCase buildSystemUseCase) {
        this.buildSystemUseCase = buildSystemUseCase;
    }

    @Override
    public ProjectModel handle(GetProjectModelCommand command) {

        GetBuildSystemsCommand getBuildSystemsCommand = new GetBuildSystemsCommand(command.getLocation());
        getBuildSystemsCommand.setExceptionCallback((buildSystem, exception) -> {
            log.warn("Build system failure '{}': {}", buildSystem, exception);
        });

        DefaultProjectModel projectModel = new DefaultProjectModel();
        this.buildSystemUseCase.handle(getBuildSystemsCommand)
                .forEach(projectModel::addBuild);

        return projectModel;
    }

    class DefaultProjectModel implements ProjectModel {

        private BuildSystemsContainer buildSystems = new BuildSystemsContainer();

        @Override
        public BuildSystemsCollection buildsystems() {
            return this.buildSystems;
        }

        void addBuild(Build build) {
            this.buildSystems.add(build);
        }
    }
}

//    public ClonedRepositorySourceMetadata extractMetadata(
//            ClonedRepositoryHandle clonedRepositoryHandle) throws MetadataAnalyzerException {
//        ClonedRepositorySourceMetadata result = new ClonedRepositorySourceMetadataAnalysisResult(clonedRepositoryHandle);
//
//        return result;
//    }
