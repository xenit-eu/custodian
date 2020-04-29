package eu.xenit.custodian.domain.changes;

import eu.xenit.custodian.ports.spi.build.Build;
import eu.xenit.custodian.ports.spi.build.Project;
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
