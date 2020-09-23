package eu.xenit.custodian.domain.usecases.buildsystem;

import eu.xenit.custodian.domain.usecases.UseCase;
import eu.xenit.custodian.domain.usecases.buildsystem.GetBuildSystemsUseCase.GetBuildSystemsCommand;
import eu.xenit.custodian.ports.spi.buildsystem.Build;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystem;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemException;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemPort;
import eu.xenit.custodian.util.Arguments;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


public interface GetBuildSystemsUseCase extends UseCase<GetBuildSystemsCommand, Collection<Build>> {

    @Override
    Collection<Build> handle(GetBuildSystemsCommand getBuildSystemsCommand);

    @Data
    class GetBuildSystemsCommand {

        /**
         * Directory to analyze. This should point to the root-directory of the project.
         */
        private final Path location;

        /**
         * Callback notification infrastructure when a {@link BuildSystemPort} encountered an exception.
         */
        private BiConsumer<BuildSystem, BuildSystemException> exceptionCallback = (buildSystem, ex) -> { };
    }
}

