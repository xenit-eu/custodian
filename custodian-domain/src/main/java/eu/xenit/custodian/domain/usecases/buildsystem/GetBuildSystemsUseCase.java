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

@Slf4j
class DefaultGetBuildSystemsUseCase implements GetBuildSystemsUseCase {

    private final List<BuildSystemPort> buildSystemPorts;

    DefaultGetBuildSystemsUseCase(List<BuildSystemPort> buildSystemPorts) {
        Arguments.notNull(buildSystemPorts, "buildSystemPorts");
        this.buildSystemPorts = List.copyOf(buildSystemPorts);
    }

    @Override
    public Collection<Build> handle(GetBuildSystemsCommand command) {
        return this.buildSystemPorts.stream()
                .map(buildSystemPort -> {
                    try {
                        return buildSystemPort.getBuild(command.getLocation());
                    } catch (BuildSystemException e) {
                        BiConsumer<BuildSystem, BuildSystemException> callback = command.getExceptionCallback();
                        if (callback != null) {
                            callback.accept(buildSystemPort.id(), e);
                        }

                        return Optional.ofNullable((Build) null);
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
