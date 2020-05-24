package eu.xenit.custodian.domain.usecases.buildsystem;

import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemPort;
import eu.xenit.custodian.util.Arguments;
import java.util.Collection;
import java.util.List;

public class BuildSystemsUseCasesFactory {

    private final List<BuildSystemPort> buildSystemPorts;

    public BuildSystemsUseCasesFactory(Collection<BuildSystemPort> buildSystemPorts) {
        Arguments.notNull(buildSystemPorts, "buildSystemPorts");
        this.buildSystemPorts = List.copyOf(buildSystemPorts);
    }

    public GetBuildSystemsUseCase createGetBuildSystemsUseCase() {
        return new DefaultGetBuildSystemsUseCase(this.buildSystemPorts);
    }
}
