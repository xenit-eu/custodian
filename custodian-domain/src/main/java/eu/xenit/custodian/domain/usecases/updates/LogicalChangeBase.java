package eu.xenit.custodian.domain.usecases.updates;

import eu.xenit.custodian.ports.spi.buildsystem.Build;
import eu.xenit.custodian.ports.spi.buildsystem.Project;
import eu.xenit.custodian.ports.spi.updates.LogicalChange;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class LogicalChangeBase implements LogicalChange {

    @Getter(AccessLevel.PROTECTED)
    private final Build build;

    @Getter(AccessLevel.PROTECTED)
    private final Project project;

    protected LogicalChangeBase(Build build, Project project) {
        this.build =  Objects.requireNonNull(build, "Argument 'build' is required");
        this.project = Objects.requireNonNull(project, "Argument 'project' is required");
    }

    @Override
    public abstract ChangeApplicationResult apply();
}
